package com.nick.wood.space_game.game;

import com.nick.wood.graphics_library_3d.Material;
import com.nick.wood.graphics_library_3d.Window;
import com.nick.wood.graphics_library_3d.input.Control;
import com.nick.wood.graphics_library_3d.input.Game3DInputs;
import com.nick.wood.graphics_library_3d.input.Inputs;
import com.nick.wood.graphics_library_3d.objects.game_objects.*;
import com.nick.wood.graphics_library_3d.objects.mesh_objects.MeshObject;
import com.nick.wood.graphics_library_3d.objects.mesh_objects.SphereMesh;
import com.nick.wood.graphics_library_3d.objects.mesh_objects.TextItem;
import com.nick.wood.maths.objects.matrix.Matrix4f;
import com.nick.wood.maths.objects.vector.Vec3d;
import com.nick.wood.maths.objects.vector.Vec3f;
import com.nick.wood.physics.Body;
import com.nick.wood.physics.SimulationInterface;
import com.nick.wood.physics.rigid_body_dynamics_verbose.RigidBody;
import com.nick.wood.space_game.game.components.HudController;
import com.nick.wood.space_game.game.controls.ActionEnum;
import com.nick.wood.space_game.game.controls.RigidBodyControl;

import java.text.DecimalFormat;
import java.util.*;

import static com.nick.wood.graphics_library_3d.objects.game_objects.GameObjectType.TRANSFORM;

public class Game implements Runnable {

	private final SimulationInterface simulation;
	private double simHerts = 60;
	private final Window window;
	private final HashMap<UUID, RootGameObject> rootGameObjects;
	private final Inputs inputs;
	private Game3DInputs game3DInputs = null;
	private HudController hudController;

	public Game(int width,
	            int height,
	            SimulationInterface simulation,
	            boolean enableCameraView,
	            boolean enableCameraMove,
	            HashMap<UUID, RootGameObject> rootGameObjects,
	            Inputs inputs) {

		this.rootGameObjects = rootGameObjects;
		this.inputs = inputs;
		this.simulation = simulation;
		this.window = new Window(
				width,
				height,
				"",
				rootGameObjects,
				inputs,
				enableCameraView,
				enableCameraMove);

	}

	@Override
	public void run() {

		window.init();

		long lastTime = System.nanoTime();

		double deltaSeconds = 0.0;

		if (hudController != null) {
			hudController.buildMiniMapMesh();
			hudController.getSlowDownTextItem().changeText("");
		}

		while (!window.shouldClose()) {

			long now = System.nanoTime();

			deltaSeconds += (now - lastTime) / 1000000000.0;

			while (deltaSeconds >= 1 / simHerts) {

				if (game3DInputs != null) {

					game3DInputs.getControl().reset();

					for (Body body : simulation.getBodies()) {
						if (game3DInputs.getControl().getUuid().equals(body.getUuid())) {

							game3DInputs.checkInputs();

							RigidBody rigidBody = (RigidBody) body;

							rigidBody.addForce(rigidBody.getRotation().toMatrix().rotate((Vec3d) game3DInputs.getControl().getForce()));
							rigidBody.addTorque(rigidBody.getRotation().toMatrix().rotate((Vec3d) game3DInputs.getControl().getTorque()));

							RigidBodyControl rigidBodyControl = (RigidBodyControl) game3DInputs.getControl();
							if (rigidBodyControl.getActions().get(ActionEnum.SLOW_DOWN)) {
								slowRigidBodyDown(rigidBody);
								if (hudController != null) {
									hudController.getSlowDownTextItem().changeText("SLOWING DOWN");
								}
							} else {
								if (hudController != null) {
									hudController.getSlowDownTextItem().changeText("");
								}
							}
						}
					}
				}

				simulation.iterate(deltaSeconds);

				window.setTitle("Iteration time: " + deltaSeconds);

				if (hudController != null) {
					for (Body body : simulation.getBodies()) {
						if (body.getUuid().equals(hudController.getRigidBodyUUID())) {
							RigidBody rigidBody = (RigidBody) body;
							hudController.getLinearVelocityTextItem().changeText("Linear: " + formatVector(rigidBody.getVelocity()));
							hudController.getAngularVelocityTextItem().changeText("Angular: " + formatVector(rigidBody.getAngularVelocity()));
							hudController.passPlayerInverseTransform(Matrix4f.InverseTransformation((Vec3f) rigidBody.getOrigin().toVecf(), rigidBody.getRotation().toMatrix().toMatrix4f(), Vec3f.ONE));
						}
					}
				}

				deltaSeconds = 0.0;

			}

			mapToGameObjects(rootGameObjects, simulation.getBodies());

			if (hudController != null) {
				hudController.createMiniMap(simulation.getBodies());
			}

			window.loop(hudController.getMiniMapMeshes(), hudController.getHudLights());

			lastTime = now;

		}

		window.destroy();

	}

	private static DecimalFormat df2 = new DecimalFormat("#.##");

	private String formatVector(Vec3d vec3d) {
		return df2.format(vec3d.getX()) + ", " + df2.format(vec3d.getY()) + ", " + df2.format(vec3d.getZ());
	}

	private void mapToGameObjects(HashMap<UUID, RootGameObject> gameObjects, ArrayList<? extends Body> rigidBodies) {

		for (Body body : rigidBodies) {

			TransformGameObject transformGameObject = (TransformGameObject) gameObjects.get(body.getUuid()).getGameObjectNodeData().getChildren().get(0);
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

		this.game3DInputs = new Game3DInputs(inputs, control);
	}

	public void addHudController(HudController hudController) {
		this.hudController = hudController;
	}
}
