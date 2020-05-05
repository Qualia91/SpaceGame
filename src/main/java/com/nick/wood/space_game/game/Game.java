package com.nick.wood.space_game.game;

import com.nick.wood.graphics_library_3d.Window;
import com.nick.wood.graphics_library_3d.input.ActionEnum;
import com.nick.wood.graphics_library_3d.input.Control;
import com.nick.wood.graphics_library_3d.input.GameControlsManager;
import com.nick.wood.graphics_library_3d.input.Inputs;
import com.nick.wood.graphics_library_3d.objects.Camera;
import com.nick.wood.graphics_library_3d.objects.scene_graph_objects.*;
import com.nick.wood.maths.objects.matrix.Matrix4f;
import com.nick.wood.maths.objects.vector.Vec3d;
import com.nick.wood.maths.objects.vector.Vec3f;
import com.nick.wood.physics.Body;
import com.nick.wood.physics.SimulationInterface;
import com.nick.wood.physics.rigid_body_dynamics_verbose.RigidBody;
import com.nick.wood.space_game.game.components.HudController;
import com.nick.wood.space_game.game.controls.RigidBodyActionEnum;
import com.nick.wood.space_game.game.controls.RigidBodyControl;

import java.text.DecimalFormat;
import java.util.*;

public class Game implements Runnable {

	private final SimulationInterface simulation;
	private double simHerts = 60;
	private final Window window;
	private final HashMap<UUID, SceneGraph> rootGameObjects;
	private final Inputs inputs;
	private GameControlsManager gameControlsManager = null;
	private HudController hudController;
	private UUID cameraUUID;

	public Game(int width,
	            int height,
	            SimulationInterface simulation,
	            boolean enableCameraView,
	            boolean enableCameraMove,
	            HashMap<UUID, SceneGraph> rootGameObjects,
	            Inputs inputs) {

		this.rootGameObjects = rootGameObjects;
		this.inputs = inputs;
		this.simulation = simulation;
		this.window = new Window(
				width,
				height,
				"",
				inputs,
				enableCameraView,
				enableCameraMove);

	}


	private Camera getPrimaryCamera(SceneGraphNode sceneGraphNode, Camera camera) {
		for (SceneGraphNode child : sceneGraphNode.getSceneGraphNodeData().getChildren()) {
			if (child instanceof CameraSceneGraph) {
				CameraSceneGraph cameraGameObject = (CameraSceneGraph) child;
				if (cameraGameObject.getCameraType() == CameraType.PRIMARY) {
					cameraUUID = cameraGameObject.getSceneGraphNodeData().getUuid();
					return cameraGameObject.getCamera();
				}
			} else {
				Camera newCamera = getPrimaryCamera(child, camera);
				if (newCamera != null) {
					return newCamera;
				}
			}
		}
		return camera;
	}

	@Override
	public void run() {

		window.init();

		// find primary camera
		for (Map.Entry<UUID, SceneGraph> uuidRootGameObjectEntry : rootGameObjects.entrySet()) {
			Camera camera = getPrimaryCamera(uuidRootGameObjectEntry.getValue(), null);
			if (camera != null) {
				break;
			}
		}

		long lastTime = System.nanoTime();

		double deltaSeconds = 0.0;

		if (hudController != null) {
			hudController.buildMiniMapMesh();
			hudController.getHud().getInformationTextItem().changeText("");
		}

		while (!window.shouldClose()) {

			long now = System.nanoTime();

			deltaSeconds += (now - lastTime) / 1000000000.0;

			while (deltaSeconds >= 1 / simHerts) {

				simulation.iterate(deltaSeconds);

				if (gameControlsManager != null) {

					gameControlsManager.getControl().reset();

					for (Body body : simulation.getBodies()) {
						if (gameControlsManager.getControl().getUuid().equals(body.getUuid())) {

							RigidBody rigidBody = (RigidBody) body;
							gameControlsManager.getControl().setObjectBeingControlled(rigidBody);
							gameControlsManager.checkInputs();

							RigidBodyControl rigidBodyControl = (RigidBodyControl) gameControlsManager.getControl();

							if (hudController != null) {
								hudController.getHud().getHudTransformObject().setPosition((Vec3f) rigidBody.getOrigin().toVecf());
								hudController.getHud().getHudTransformObject().setRotation(rigidBody.getRotation().toMatrix().toMatrix4f());
							}

							rigidBodyControl.apply(rigidBody);

							if (rigidBodyControl.getActions().get(RigidBodyActionEnum.SLOW_DOWN)) {
								slowRigidBodyDown(rigidBody);
								if (hudController != null) {
									hudController.getHud().getInformationTextItem().changeText("SLOWING DOWN");
								}
							} else {
								if (hudController != null) {
									hudController.getHud().getInformationTextItem().changeText("");
								}
							}
						}
					}
				}

				window.setTitle("Iteration time: " + deltaSeconds);

				if (hudController != null) {
					for (Body body : simulation.getBodies()) {
						if (body.getUuid().equals(hudController.getCenterMiniMapOn())) {
							RigidBody rigidBody = (RigidBody) body;
							hudController.getHud().getLinearTextItem().changeText("Linear: " + formatVector(rigidBody.getVelocity()));
							hudController.getHud().getAngularTextItem().changeText("Angular: " + formatVector(rigidBody.getAngularVelocity()));
						}
					}
				}

				deltaSeconds = 0.0;

			}

			mapToGameObjects(rootGameObjects, simulation.getBodies());

			HashMap<UUID, SceneGraph> map = new HashMap<>();
			if (hudController != null) {
				hudController.createMiniMap(simulation.getBodies());
				map = hudController.getHud().getRootGameObjectHashMap();
			}


			window.loop(rootGameObjects, map, cameraUUID);

			lastTime = now;

		}

		window.destroy();

	}

	private static DecimalFormat df2 = new DecimalFormat("#.##");

	private String formatVector(Vec3d vec3d) {
		return df2.format(vec3d.getX()) + ", " + df2.format(vec3d.getY()) + ", " + df2.format(vec3d.getZ());
	}

	private void mapToGameObjects(HashMap<UUID, SceneGraph> gameObjects, ArrayList<? extends Body> rigidBodies) {

		for (Body body : rigidBodies) {

			TransformSceneGraph transformGameObject = (TransformSceneGraph) gameObjects.get(body.getUuid()).getSceneGraphNodeData().getChildren().get(0);
			transformGameObject.setPosition((Vec3f) body.getOrigin().toVecf());
			transformGameObject.setRotation(body.getRotation().toMatrix().toMatrix4f());

		}

	}

	private void slowRigidBodyDown(RigidBody rigidBody) {
		Vec3d linearMomentum = rigidBody.getLinearMomentum();
		double mass = rigidBody.getMass();
		Vec3d linearForce = linearMomentum.scale(mass);
		rigidBody.addForce(linearForce.scale(-5));

		Vec3d angularMomentum = rigidBody.getAngularMomentum();

		if (angularMomentum.length2() < 5) {
			rigidBody.addTorque(angularMomentum.scale(-20));
		} else {
			rigidBody.addTorque(angularMomentum.scale(-10));
		}
	}

	public void setController(Control control) {

		this.gameControlsManager = new GameControlsManager(inputs, control);
	}

	public void addHudController(HudController hudController) {
		this.hudController = hudController;
	}
}
