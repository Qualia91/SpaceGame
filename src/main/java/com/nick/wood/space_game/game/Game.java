package com.nick.wood.space_game.game;

import com.nick.wood.game_control.input.Control;
import com.nick.wood.game_control.input.ControlManager;
import com.nick.wood.game_control.input.Input;
import com.nick.wood.graphics_library.Window;
import com.nick.wood.graphics_library.input.DirectCameraController;
import com.nick.wood.graphics_library.input.LWJGLGameControlManager;
import com.nick.wood.graphics_library.input.GraphicsLibraryInput;
import com.nick.wood.graphics_library.objects.Camera;
import com.nick.wood.graphics_library.objects.scene_graph_objects.*;
import com.nick.wood.maths.objects.vector.Vec3d;
import com.nick.wood.maths.objects.vector.Vec3f;
import com.nick.wood.physics.Body;
import com.nick.wood.physics.SimulationInterface;
import com.nick.wood.physics.rigid_body_dynamics_verbose.RigidBody;
import com.nick.wood.space_game.game.components.HudController;
import com.nick.wood.space_game.game.controls.RigidBodyControl;

import java.text.DecimalFormat;
import java.util.*;

import static com.nick.wood.space_game.game.controls.RigidBodyActionEnum.SLOW_DOWN;

public class Game implements Runnable {

	private final SimulationInterface simulation;
	private double simHerts = 60;
	private final Window window;
	private final HashMap<UUID, SceneGraph> rootGameObjects;
	private ArrayList<ControlManager<? extends Input>> controlManagers = new ArrayList<>();
	private HudController hudController;
	private UUID cameraUUID;

	public Game(int width,
	            int height,
	            SimulationInterface simulation,
	            HashMap<UUID, SceneGraph> rootGameObjects) {

		this.rootGameObjects = rootGameObjects;
		this.simulation = simulation;
		this.window = new Window(
				width,
				height,
				"");

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

			while (deltaSeconds >= 1.0 / simHerts) {

				simulation.iterate(deltaSeconds);

				for (ControlManager<? extends Input> controlManager : controlManagers) {

					if (controlManager.getControl() instanceof DirectCameraController) {
						controlManager.checkInputs();
					}

					if (controlManager.getControl() instanceof RigidBodyControl) {

						controlManager.getControl().reset();

						for (Body body : simulation.getBodies()) {
							if (controlManager.getControl().getUuid().equals(body.getUuid())) {

								RigidBody rigidBody = (RigidBody) body;
								controlManager.getControl().setObjectBeingControlled(rigidBody);
								controlManager.checkInputs();

								RigidBodyControl rigidBodyControl = (RigidBodyControl) controlManager.getControl();

								if (body.getUuid().equals(hudController.getCenterMiniMapOn())) {
									if (hudController != null) {
										hudController.getHud().getHudTransformObject().setPosition((Vec3f) rigidBody.getOrigin().toVecf());
										hudController.getHud().getHudTransformObject().setRotation(rigidBody.getRotation().toMatrix().toMatrix4f());
									}
								}

								rigidBodyControl.apply(rigidBody);

								if (body.getUuid().equals(hudController.getCenterMiniMapOn())) {
									if (rigidBodyControl.getActions().get(SLOW_DOWN)) {
										if (hudController != null) {
											hudController.getHud().getInformationTextItem().changeText("SLOWING DOWN");
										}
									} else {
										if (hudController != null) {
											hudController.getHud().getInformationTextItem().changeText("");
										}
									}
								}

								rigidBodyControl.preformActions(rigidBody);
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

		try {
			window.close();
		} catch (Exception exception) {
			exception.printStackTrace();
		}

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

	public void addController(ControlManager<? extends Input> control) {
		this.controlManagers.add(control);
	}

	public void addHudController(HudController hudController) {
		this.hudController = hudController;
	}

	public Window getWindow() {
		return window;
	}
}
