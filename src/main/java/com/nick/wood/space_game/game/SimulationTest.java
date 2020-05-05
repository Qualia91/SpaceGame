package com.nick.wood.space_game.game;

import com.nick.wood.graphics_library_3d.input.Control;
import com.nick.wood.graphics_library_3d.input.Inputs;
import com.nick.wood.graphics_library_3d.lighting.PointLight;
import com.nick.wood.graphics_library_3d.lighting.SpotLight;
import com.nick.wood.graphics_library_3d.objects.Camera;
import com.nick.wood.graphics_library_3d.objects.Transform;
import com.nick.wood.graphics_library_3d.objects.scene_graph_objects.*;
import com.nick.wood.graphics_library_3d.objects.mesh_objects.*;
import com.nick.wood.maths.objects.Quaternion;
import com.nick.wood.maths.objects.matrix.Matrix4f;
import com.nick.wood.maths.objects.vector.Vec3d;
import com.nick.wood.maths.objects.vector.Vec3f;
import com.nick.wood.physics.SimulationInterface;
import com.nick.wood.physics.rigid_body_dynamics_verbose.RigidBody;
import com.nick.wood.physics.rigid_body_dynamics_verbose.RigidBodyType;
import com.nick.wood.space_game.game.components.HudController;
import com.nick.wood.space_game.game.controls.RigidBodyControl;
import com.nick.wood.physics.rigid_body_dynamics_verbose.forces.Drag;
import com.nick.wood.physics.rigid_body_dynamics_verbose.forces.Force;
import com.nick.wood.space_game.game.game_objects.GameObjectBuilder;
import com.nick.wood.space_game.game.game_objects.GameObjectFactory;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.nick.wood.graphics_library_3d.objects.mesh_objects.MeshType.*;

class SimulationTest {
	/*
		void simBasicTest() throws ExecutionException, InterruptedException {

			ArrayList<RigidBody> rigidBodies = new ArrayList<>();

			ArrayList<Force> forces = new ArrayList<>();

			UUID uuid = UUID.randomUUID();
			Quaternion quaternion = Quaternion.RotationX(0.0);
			RigidBody rigidBody = new RigidBody(uuid, 1, new Vec3d(1.0, 1.0, 1.0), new Vec3d(0.0, 0.0, 5.0), quaternion, Vec3d.Z.scale(-1), Vec3d.X.scale(0.1), RigidBodyType.SPHERE,forces);
			rigidBodies.add(rigidBody);

			UUID uuid2 = UUID.randomUUID();
			RigidBody rigidBody2 = new RigidBody(uuid2, 1, new Vec3d(1.0, 1.0, 1.0), new Vec3d(0.0, 0.0, -5.0), quaternion, Vec3d.Z.scale(1), Vec3d.X.scale(0.1), RigidBodyType.SPHERE,forces);
			rigidBodies.add(rigidBody2);

			RootSceneGraph cameraRootObject = new RootSceneGraph();
			Camera camera = new Camera(new Vec3f(-5.0f, 0.0f, 0.0f), new Vec3f(0.0f, 0.0f, 0.0f), 0.5f, 0.1f);
			CameraSceneGraph cameraGameObject = new CameraSceneGraph(cameraRootObject, camera, CameraType.PRIMARY);

			RootSceneGraph lightRootObject = new RootSceneGraph();
			createLights(lightRootObject);

			Inputs inputs = new Inputs();

			HashMap<UUID, RootSceneGraph> rootGameObjectHashMap = new HashMap<>();

			for (RigidBody rigidBodyIte : rigidBodies) {
				rootGameObjectHashMap.put(rigidBodyIte.getUuid(), convertToGameObject(rigidBodyIte, "/textures/texture.png"));
			}

			rootGameObjectHashMap.put(UUID.randomUUID(), cameraRootObject);
			rootGameObjectHashMap.put(UUID.randomUUID(), lightRootObject);

			SimulationInterface simulation = new com.nick.wood.physics.rigid_body_dynamics_verbose.Simulation(rigidBodies);

			Game game = new Game(1000, 800, simulation, true, true, rootGameObjectHashMap, inputs);

			ExecutorService executor = Executors.newFixedThreadPool(8);

			Future<?> submit = executor.submit(game);

			// waits for game to finish
			submit.get();

			// closes executor service
			executor.shutdown();
		}

		void attachedBodiesAtATopLevelTransform() throws ExecutionException, InterruptedException {

			ArrayList<RigidBody> rigidBodies = new ArrayList<>();

			ArrayList<Force> forces = new ArrayList<>();
			//forces.add(new GravityBasic());
			//forces.add(new Drag());

			//ArrayList<Force> forces2 = new ArrayList<>();
			//UUID uuid = UUID.randomUUID();
			//RigidBody rigidBody = new RigidBody(uuid, 1, new Vec3d(1.0, 1.0, 1.0), new Vec3d(2.0, 0.0, 0.0), new Quaternion(1.0, 0.0, 0.0, 0.0), Vec3d.X.scale(10), Vec3d.Y.scale(0.1), RigidBodyType.SPHERE,forces);
			//rigidBodies.add(rigidBody);
			//uuidGameObjectHashMap.put(uuid, convertToGameObject(rigidBody, 10));


			// tests
			//for (int i = 0; i < 2; i++) {
			//	Vec3d mom = Vec3d.X.scale(i * 2);
			//	Vec3d angMom = Vec3d.Z.scale(i*0.01);
			//	if (i == 1) {
			//		mom = mom.neg();
			//		angMom = angMom.neg();
			//	}
			//	UUID uuid = UUID.randomUUID();
			//	RigidBody rigidBody = new RigidBody(uuid, 1, new Vec3d(1.0, 1.0, 1.0), new Vec3d(i*4.0, i/2.0, 0.0), new Quaternion(1.0, 0.0, 0.0, 0.0), mom, angMom, RigidBodyType.SPHERE,forces);
			//	rigidBodies.add(rigidBody);
			//	uuidGameObjectHashMap.put(uuid, convertToGameObject(rigidBody, 10));
			//}
			UUID uuid = UUID.randomUUID();
			Quaternion quaternion = Quaternion.RotationX(0.0);
			RigidBody rigidBody = new RigidBody(uuid, 1, new Vec3d(1.0, 1.0, 1.0), new Vec3d(10.0, 0.0, 0.0), quaternion, Vec3d.ZERO, Vec3d.ZERO, RigidBodyType.SPHERE,forces);
			rigidBodies.add(rigidBody);


			UUID uuid2 = UUID.randomUUID();
			RigidBody rigidBody2 = new RigidBody(uuid2, 1, new Vec3d(1.0, 1.0, 1.0), new Vec3d(0.0, 0.0, 0.0), quaternion, Vec3d.X.scale(10), Vec3d.ZERO, RigidBodyType.SPHERE,forces);
			rigidBodies.add(rigidBody2);

			// create player
			//UUID playerRigidBodyUUID = UUID.randomUUID();
			//RigidBody playerRigidBody = new RigidBody(playerRigidBodyUUID, 0.1, new Vec3d(1.0, 1.0, 1.0), new Vec3d(0.0, 0.0, 0), new Quaternion(1.0, 0.0, 0.0, 0.0), Vec3d.ZERO, Vec3d.Z.scale(0.0), RigidBodyType.SPHERE, forces);
			//rigidBodies.add(playerRigidBody);

			RootSceneGraph cameraRootObject = new RootSceneGraph();
			Camera camera = new Camera(new Vec3f(-10.0f, 0.0f, 0.0f), new Vec3f(0.0f, 0.0f, 0.0f), 0.5f, 0.1f);
			CameraSceneGraph cameraGameObject = new CameraSceneGraph(cameraRootObject, camera, CameraType.PRIMARY);

			//RootGameObject lightRootObject = new RootGameObject();
			//createLights(lightRootObject);
			//rootGameObjectHashMap.put(UUID.randomUUID(), lightRootObject);

			Inputs inputs = new Inputs();

			HashMap<UUID, RootSceneGraph> rootGameObjectHashMap = new HashMap<>();

			//for (RigidBody rigidBodyIte : rigidBodies) {
			//	rootGameObjectHashMap.put(rigidBodyIte.getUuid(), convertToGameObjectAndAttachAnotherMesh(rigidBodyIte));
			//}

			for (RigidBody rigidBodyIte : rigidBodies) {
				rootGameObjectHashMap.put(rigidBodyIte.getUuid(), convertToGameObjectAndAttachAnotherMesh(rigidBodyIte));
			}

			rootGameObjectHashMap.put(UUID.randomUUID(), cameraRootObject);

			SimulationInterface simulation = new com.nick.wood.physics.rigid_body_dynamics_verbose.Simulation(rigidBodies);

			Game game = new Game(1200, 1000, simulation, true, true, rootGameObjectHashMap, inputs);

			ExecutorService executor = Executors.newFixedThreadPool(8);

			Future<?> submit = executor.submit(game);

			// waits for game to finish
			submit.get();

			// closes executor service
			executor.shutdown();
		}

		void twoLinesInteracting() throws ExecutionException, InterruptedException {

			ArrayList<RigidBody> rigidBodies = new ArrayList<>();

			ArrayList<Force> forces = new ArrayList<>();
			Quaternion quaternion = Quaternion.RotationX(0.0);

			// demo 1: 2 lines interacting
			for (int j = 0; j < 10; j++) {
				for (int i = 0; i < 3; i++) {
					Vec3d mom = Vec3d.Z.scale(i + j/10.0);// * (j/10.0));
					Vec3d ang = Vec3d.X.scale(0.001).scale(j);
					//Vec3d ang = Vec3d.ZERO;
					if (i > 0) {
						mom = mom.neg();
						//ang = ang.neg();
						//ang = Vec3d.X.scale(0.01).scale(j);
						ang = Vec3d.ZERO;
					}
					UUID uuidR = UUID.randomUUID();
					RigidBody rigidBodyR = new RigidBody(uuidR, 1, new Vec3d(1.0, 1.0, 1.0), new Vec3d(5.0, j*3.0 - 2*i/3.0, i * 8), quaternion, mom, ang, RigidBodyType.SPHERE,forces);
					rigidBodies.add(rigidBodyR);
				}
			}

			RootSceneGraph cameraRootObject = new RootSceneGraph();
			Camera camera = new Camera(new Vec3f(0.0f, 0.0f, 10.0f), new Vec3f(0.0f, 0.0f, 0.0f), 0.5f, 0.1f);
			CameraSceneGraph cameraGameObject = new CameraSceneGraph(cameraRootObject, camera, CameraType.PRIMARY);

			Inputs inputs = new Inputs();

			HashMap<UUID, RootSceneGraph> rootGameObjectHashMap = new HashMap<>();

			for (RigidBody rigidBody : rigidBodies) {
				rootGameObjectHashMap.put(rigidBody.getUuid(), convertToGameObject(rigidBody, "/textures/white.png"));
			}

			rootGameObjectHashMap.put(UUID.randomUUID(), cameraRootObject);


			RootSceneGraph lightRootObject = new RootSceneGraph();
			createLights(lightRootObject);
			rootGameObjectHashMap.put(UUID.randomUUID(), lightRootObject);

			SimulationInterface simulation = new com.nick.wood.physics.rigid_body_dynamics_verbose.Simulation(rigidBodies);

			Game game = new Game(1000, 800, simulation, true, true, rootGameObjectHashMap, inputs);

			ExecutorService executor = Executors.newFixedThreadPool(4);

			Future<?> submit = executor.submit(game);

			// waits for game to finish
			submit.get();

			// closes executor service
			executor.shutdown();
		}

		void randomBox() throws ExecutionException, InterruptedException {

			ArrayList<RigidBody> rigidBodies = new ArrayList<>();

			ArrayList<Force> forces = new ArrayList<>();
			//forces.add(new Drag());

			// demo 2: random box
			Random random = new Random();
			for (int k = -3; k < 3; k++) {
				for (int j = -3; j < 3; j++) {
					for (int i = -3; i < 3; i++) {
						if (!(i == 0 && j == 0 && k == 0)) {
							UUID uuid = UUID.randomUUID();
							Vec3d mom = Vec3d.X.scale(random.nextInt(20) - 10).add(Vec3d.Y.scale(random.nextInt(20) - 10)).add(Vec3d.Z.scale(random.nextInt(20) - 10));
							Vec3d angMom = Vec3d.X.scale(random.nextInt(20) - 10).add(Vec3d.Y.scale(random.nextInt(20) - 10)).add(Vec3d.Z.scale(random.nextInt(20) - 10));
							RigidBody rigidBody = new RigidBody(uuid, 1, new Vec3d(1.0, 1.0, 1.0), new Vec3d(j * 3, i * 3, k * 3), new Quaternion(1.0, 0.0, 0.0, 0.0), mom, angMom.scale(0.02), RigidBodyType.SPHERE, forces);
							rigidBodies.add(rigidBody);
						}
					}
				}
			}

			UUID uuid = UUID.randomUUID();
			RigidBody rigidBodyCube = new RigidBody(uuid, 1, new Vec3d(1.0, 1.0, 1.0), new Vec3d(0.0, 0.0, 25.0), new Quaternion(1.0, 0.0, 0.0, 0.0), Vec3d.Z.scale(-10), Vec3d.X.scale(0.1), RigidBodyType.CUBOID, forces);
			rigidBodies.add(rigidBodyCube);
			UUID uuid2 = UUID.randomUUID();
			RigidBody rigidBodyCube2 = new RigidBody(uuid2, 1, new Vec3d(1.0, 1.0, 1.0), new Vec3d(0.0, 0.0, -25.0), new Quaternion(1.0, 0.0, 0.0, 0.0), Vec3d.Z, Vec3d.ZERO, RigidBodyType.SPHERE, forces);
			rigidBodies.add(rigidBodyCube2);

			RootSceneGraph cameraRootObject = new RootSceneGraph();
			Camera camera = new Camera(new Vec3f(-20.0f, 0.0f, 0.0f), new Vec3f(0.0f, 0.0f, 0.0f), 0.5f, 0.1f);
			CameraSceneGraph cameraGameObject = new CameraSceneGraph(cameraRootObject, camera, CameraType.PRIMARY);

			HashMap<UUID, RootSceneGraph> rootGameObjectHashMap = new HashMap<>();

			for (RigidBody rigidBody : rigidBodies) {
				RootSceneGraph rootGameObject = convertToGameObject(rigidBody, "/textures/white.png");
				TransformSceneGraph transformGameObjectLaser = (TransformSceneGraph) rootGameObject.getSceneGraphNodeData().getChildren().get(0);
				createLaserUnderTransform(new Vec3f(1.0f, 1.0f, 1.0f), Vec3f.ZERO, transformGameObjectLaser);
				rootGameObjectHashMap.put(rigidBody.getUuid(), rootGameObject);
			}

			rootGameObjectHashMap.put(UUID.randomUUID(), cameraRootObject);

			//// Arena
			//UUID uuidArena = UUID.randomUUID();
			//RigidBody rigidBodyArena = new RigidBody(uuidArena, 10, new Vec3d(100, 100, 100), new Vec3d(0.0, 0.0, 0.0), new Quaternion(1.0, 0.0, 0.0, 0.0), Vec3d.ZERO, Vec3d.ZERO, RigidBodyType.SPHERE_INNER, forces);
			//rigidBodies.add(rigidBodyArena);
			//RootGameObject rootGameObject = convertToGameObject(rigidBodyArena, "/textures/white.png");
			//TransformGameObject transformGameObject = (TransformGameObject) rootGameObject.getGameObjectNodeData().getChildren().get(0);
			//MeshObject meshGroupLight = new SphereMesh(10, new Material("/textures/white.png"), true);
			//createLightUnderTransform(new Vec3f(1.0f, 0.0f, 0.0f), Vec3f.Z.scale(0.30f), meshGroupLight, transformGameObject);
			//createLightUnderTransform(new Vec3f(0.0f, 1.0f, 0.0f), Vec3f.Z.scale(-0.30f), meshGroupLight, transformGameObject);
			//createLightUnderTransform(new Vec3f(0.0f, 0.0f, 1.0f), Vec3f.X.scale(0.30f), meshGroupLight, transformGameObject);
			//createLightUnderTransform(new Vec3f(1.0f, 1.0f, 0.0f), Vec3f.X.scale(-0.30f), meshGroupLight, transformGameObject);
			//createLightUnderTransform(new Vec3f(1.0f, 0.0f, 1.0f), Vec3f.Y.scale(0.30f), meshGroupLight, transformGameObject);
			//createLightUnderTransform(new Vec3f(0.0f, 1.0f, 1.0f), Vec3f.Y.scale(-0.30f), meshGroupLight, transformGameObject);
			//rootGameObjectHashMap.put(uuidArena, rootGameObject);

			// Arena
			createArena(rigidBodies, rootGameObjectHashMap, forces, 500);

			//// rigid body with light attached
			//UUID uuidLaser = UUID.randomUUID();
			//RigidBody rigidBodyLaser = new RigidBody(uuidLaser, 10, new Vec3d(1, 1, 1), new Vec3d(0.0, 0.0, 0.0), new Quaternion(1.0, 0.0, 0.0, 0.0), Vec3d.ZERO, Vec3d.ZERO, RigidBodyType.SPHERE, forces);
			//rigidBodies.add(rigidBodyLaser);
			//RootGameObject rootGameObjectLaser = convertToGameObject(rigidBodyLaser);
			//TransformGameObject transformGameObjectLaser = (TransformGameObject) rootGameObjectLaser.getGameObjectNodeData().getChildren().get(0);
			//MeshObject meshGroupLaser = new SphereMesh(10, new Material("/textures/texture.png"), false);
			//createLaserUnderTransform(new Vec3f(1.0f, 0.0f, 0.0f), Vec3f.ZERO, meshGroupLaser, transformGameObjectLaser);
			//rootGameObjectHashMap.put(uuidLaser, rootGameObjectLaser);



			//RootGameObject lightRootObject = new RootGameObject();
			//createLights(lightRootObject);
			//rootGameObjectHashMap.put(UUID.randomUUID(), lightRootObject);

			Inputs inputs = new Inputs();

			SimulationInterface simulation = new com.nick.wood.physics.rigid_body_dynamics_verbose.Simulation(rigidBodies);

			Game game = new Game(1000, 800, simulation, true, true, rootGameObjectHashMap, inputs);

			ExecutorService executor = Executors.newFixedThreadPool(4);

			Future<?> submit = executor.submit(game);

			// waits for game to finish
			submit.get();

			// closes executor service
			executor.shutdown();
		}

		void cameraAttachedToObject() throws ExecutionException, InterruptedException {

			ArrayList<RigidBody> rigidBodies = new ArrayList<>();

			ArrayList<Force> forces = new ArrayList<>();
			forces.add(new Drag(-0.1));

			UUID uuid = UUID.randomUUID();
			//Quaternion quaternion = Quaternion.RotationX(90);
			Quaternion quaternion = new Quaternion(1.0, 0.0, 0.0, 0.0);
			RigidBody rigidBodyCube = new RigidBody(uuid, 1, new Vec3d(1.0, 1.0, 1.0), new Vec3d(0.0, 0.0, 25.0), quaternion, Vec3d.ZERO, Vec3d.ZERO, RigidBodyType.SPHERE, forces);
			rigidBodies.add(rigidBodyCube);


			HashMap<UUID, RootSceneGraph> rootGameObjectHashMap = new HashMap<>();


			RigidBodyControl control = null;
			for (RigidBody rigidBody : rigidBodies) {
				RootSceneGraph rootGameObject = convertToGameObject(rigidBody, "/textures/white.png");
				TransformSceneGraph transformGameObjectLaser = (TransformSceneGraph) rootGameObject.getSceneGraphNodeData().getChildren().get(0);
				createLaserUnderTransform(new Vec3f(1.0f, 1.0f, 1.0f), Vec3f.ZERO, transformGameObjectLaser);

				control = new RigidBodyControl(100 * rigidBody.getMass(), 1 * Math.sqrt(rigidBody.getMass()), rigidBody.getUuid());

				Camera camera = new Camera(new Vec3f(-5.0f, 0.0f, 1.0f), new Vec3f(0.0f, 0.0f, 0.0f), 0.5f, 0.1f);
				CameraSceneGraph cameraGameObject = new CameraSceneGraph(transformGameObjectLaser, camera, CameraType.PRIMARY);

				rootGameObjectHashMap.put(rigidBody.getUuid(), rootGameObject);
			}

			//// Arena
			UUID uuidArena = UUID.randomUUID();
			RigidBody rigidBodyArena = new RigidBody(uuidArena, 10, new Vec3d(100, 100, 100), new Vec3d(0.0, 0.0, 0.0), new Quaternion(1.0, 0.0, 0.0, 0.0), Vec3d.ZERO, Vec3d.ZERO, RigidBodyType.SPHERE_INNER, forces);
			rigidBodies.add(rigidBodyArena);
			RootSceneGraph rootGameObject = convertToGameObject(rigidBodyArena, "/textures/white.png");
			TransformSceneGraph transformGameObject = (TransformSceneGraph) rootGameObject.getSceneGraphNodeData().getChildren().get(0);
			MeshObject meshGroupLight = new SphereMesh(10, new Material("/textures/white.png"), true, transformation);
			createLightUnderTransform(new Vec3f(1.0f, 0.0f, 0.0f), Vec3f.Z.scale(0.30f), meshGroupLight, transformGameObject, 10);
			createLightUnderTransform(new Vec3f(0.0f, 1.0f, 0.0f), Vec3f.Z.scale(-0.30f), meshGroupLight, transformGameObject, 10);
			createLightUnderTransform(new Vec3f(0.0f, 0.0f, 1.0f), Vec3f.X.scale(0.30f), meshGroupLight, transformGameObject, 10);
			createLightUnderTransform(new Vec3f(1.0f, 1.0f, 0.0f), Vec3f.X.scale(-0.30f), meshGroupLight, transformGameObject, 10);
			createLightUnderTransform(new Vec3f(1.0f, 0.0f, 1.0f), Vec3f.Y.scale(0.30f), meshGroupLight, transformGameObject, 10);
			createLightUnderTransform(new Vec3f(0.0f, 1.0f, 1.0f), Vec3f.Y.scale(-0.30f), meshGroupLight, transformGameObject, 10);
			rootGameObjectHashMap.put(uuidArena, rootGameObject);

			Inputs inputs = new Inputs();

			SimulationInterface simulation = new com.nick.wood.physics.rigid_body_dynamics_verbose.Simulation(rigidBodies);

			Game game = new Game(1000, 800, simulation, false, false, rootGameObjectHashMap, inputs);

			ExecutorService executor = Executors.newFixedThreadPool(4);

			Future<?> submit = executor.submit(game);

			// waits for game to finish
			submit.get();

			// closes executor service
			executor.shutdown();
		}

		void bigBang() throws ExecutionException, InterruptedException {

			ArrayList<RigidBody> rigidBodies = new ArrayList<>();

			ArrayList<Force> forces = new ArrayList<>();

			// demo 3: big bang
			Random random = new Random();
			for (int k = -2; k < 2; k++) {
				for (int j = -2; j < 2; j++) {
					for (int i = -2; i < 2; i++) {
						Vec3d mom = Vec3d.X.scale(-i*15).add(Vec3d.Y.scale(-j*15)).add(Vec3d.Z.scale(-k*15));
						Vec3d angMom = Vec3d.X.scale(random.nextInt(10) - 4).add(Vec3d.Y.scale(random.nextInt(10) - 4)).add(Vec3d.Z.scale(random.nextInt(10) - 4));
						UUID uuid = UUID.randomUUID();
						RigidBody rigidBody = new RigidBody(uuid, 1, new Vec3d(1.0, 1.0, 1.0), new Vec3d(i * 10, j * 10, k*10), new Quaternion(1.0, 0.0, 0.0, 0.0), mom, angMom.scale(0.02), RigidBodyType.SPHERE, forces);
						rigidBodies.add(rigidBody);
					}
				}
			}

			RootSceneGraph cameraRootObject = new RootSceneGraph();
			Camera camera = new Camera(new Vec3f(0.0f, 0.0f, 10.0f), new Vec3f(0.0f, 0.0f, 0.0f), 0.5f, 0.1f);
			CameraSceneGraph cameraGameObject = new CameraSceneGraph(cameraRootObject, camera, CameraType.PRIMARY);

			RootSceneGraph lightRootObject = new RootSceneGraph();
			createLights(lightRootObject);

			Inputs inputs = new Inputs();

			HashMap<UUID, RootSceneGraph> rootGameObjectHashMap = new HashMap<>();


			for (RigidBody rigidBody : rigidBodies) {
				rootGameObjectHashMap.put(rigidBody.getUuid(), convertToGameObject(rigidBody, "/textures/white.png"));
			}


			// Arena
			createArena(rigidBodies, rootGameObjectHashMap, forces, 500);

			rootGameObjectHashMap.put(UUID.randomUUID(), cameraRootObject);
			rootGameObjectHashMap.put(UUID.randomUUID(), lightRootObject);

			SimulationInterface simulation = new com.nick.wood.physics.rigid_body_dynamics_verbose.Simulation(rigidBodies);

			Game game = new Game(1000, 800, simulation, true, true, rootGameObjectHashMap, inputs);

			ExecutorService executor = Executors.newFixedThreadPool(4);

			Future<?> submit = executor.submit(game);

			// waits for game to finish
			submit.get();

			// closes executor service
			executor.shutdown();
		}

		void bigBangWithPlayer() throws ExecutionException, InterruptedException {

			ArrayList<RigidBody> rigidBodies = new ArrayList<>();
			ArrayList<UUID> toRender = new ArrayList<>();

			ArrayList<Force> forces = new ArrayList<>();
			ArrayList<Force> forces2 = new ArrayList<>();
			forces.add(new Drag(-0.1));

			// demo 3: big bang
			Random random = new Random();
			for (int k = -2; k < 2; k++) {
				for (int j = -2; j < 2; j++) {
					for (int i = -2; i < 2; i++) {
						Vec3d mom = Vec3d.X.scale(-i*10).add(Vec3d.Y.scale(-j*10)).add(Vec3d.Z.scale(-k*10));
						Vec3d angMom = Vec3d.X.scale(random.nextInt(10) - 4).add(Vec3d.Y.scale(random.nextInt(10) - 4)).add(Vec3d.Z.scale(random.nextInt(10) - 4));
						UUID uuid = UUID.randomUUID();
						toRender.add(uuid);
						RigidBody rigidBody = new RigidBody(uuid, 1, new Vec3d(1.0, 1.0, 1.0), new Vec3d(i * 10, j * 10, k*10), new Quaternion(1.0, 0.0, 0.0, 0.0), mom, angMom.scale(0.02), RigidBodyType.SPHERE, forces2);
						rigidBodies.add(rigidBody);
					}
				}
			}

			UUID playerUUID = UUID.randomUUID();
			RigidBody rigidBody = new RigidBody(playerUUID, 1, new Vec3d(1.0, 1.0, 1.0), new Vec3d(-50, 0, 0), new Quaternion(1.0, 0.0, 0.0, 0.0), Vec3d.ZERO, Vec3d.ZERO, RigidBodyType.SPHERE, forces);
			RootSceneGraph rootGameObject = convertToGameObject(rigidBody, "/textures/white.png");
			TransformSceneGraph transformGameObjectLaser = (TransformSceneGraph) rootGameObject.getSceneGraphNodeData().getChildren().get(0);
			createLaserUnderTransform(new Vec3f(1.0f, 1.0f, 1.0f), Vec3f.ZERO, transformGameObjectLaser);
			rigidBodies.add(rigidBody);
			Control control = new RigidBodyControl(100 * rigidBody.getMass(), 1 * Math.sqrt(rigidBody.getMass()), rigidBody.getUuid());


			Camera camera = new Camera(new Vec3f(-5.0f, 0.0f, 1.0f), new Vec3f(0.0f, 0.0f, 0.0f), 0.5f, 0.1f);
			CameraSceneGraph cameraGameObject = new CameraSceneGraph(transformGameObjectLaser, camera, CameraType.PRIMARY);


			RootSceneGraph lightRootObject = new RootSceneGraph();
			createLights(lightRootObject);

			Inputs inputs = new Inputs();

			HashMap<UUID, RootSceneGraph> rootGameObjectHashMap = new HashMap<>();

			for (RigidBody rigidBodyInLoop : rigidBodies) {
				rootGameObjectHashMap.put(rigidBodyInLoop.getUuid(), convertToGameObject(rigidBodyInLoop, "/textures/white.png"));
			}

			createArena(rigidBodies, rootGameObjectHashMap, forces, 500);

			HudController hudController = createHud(cameraGameObject, playerUUID, toRender);

			rootGameObjectHashMap.put(playerUUID, rootGameObject);

			rootGameObjectHashMap.put(UUID.randomUUID(), lightRootObject);

			SimulationInterface simulation = new com.nick.wood.physics.rigid_body_dynamics_verbose.Simulation(rigidBodies);

			Game game = new Game(1200, 1000, simulation, false, false, rootGameObjectHashMap, inputs);
			game.setController(control);
			game.addHudController(hudController);

			ExecutorService executor = Executors.newFixedThreadPool(4);

			Future<?> submit = executor.submit(game);

			// waits for game to finish
			submit.get();

			// closes executor service
			executor.shutdown();
		}

		void cubeSphereInteraction() throws ExecutionException, InterruptedException {

			ArrayList<RigidBody> rigidBodies = new ArrayList<>();

			ArrayList<Force> forces = new ArrayList<>();
			ArrayList<Force> forces2 = new ArrayList<>();
			forces.add(new GravityBasic());


			UUID uuid2 = UUID.randomUUID();
			RigidBody rigidBody2 = new RigidBody(uuid2, 1, new Vec3d(1.0, 1.0, 1.0), new Vec3d(0.0, -10.0, 10), new Quaternion(1.0, 0.0, 0.0, 0.0), Vec3d.Z.neg(), Vec3d.ZERO, RigidBodyType.SPHERE, forces);
			rigidBodies.add(rigidBody2);

			UUID uuid3 = UUID.randomUUID();
			RigidBody rigidBody3 = new RigidBody(uuid3, 1, new Vec3d(1.0, 1.0, 1.0), new Vec3d(0.0, -12.0, 15), new Quaternion(1.0, 0.0, 0.0, 0.0), Vec3d.Z.neg(), Vec3d.ZERO, RigidBodyType.SPHERE, forces);
			rigidBodies.add(rigidBody3);

			UUID floorUUID = UUID.randomUUID();
			RigidBody floorRigidBody = new RigidBody(floorUUID, 100, new Vec3d(50.0, 50.0, 1.0), new Vec3d(0.0, 0.0, -1.0), Quaternion.RotationY(0.0), Vec3d.ZERO, Vec3d.ZERO, RigidBodyType.CUBOID, forces2);
			rigidBodies.add(floorRigidBody);

			UUID floorUUID1 = UUID.randomUUID();
			RigidBody floorRigidBody1 = new RigidBody(floorUUID1, 100, new Vec3d(50.0, 50.0, 1.0), new Vec3d(0.0, -20.0, -10.0), Quaternion.RotationY(0.0), Vec3d.ZERO, Vec3d.ZERO, RigidBodyType.CUBOID, forces2);
			rigidBodies.add(floorRigidBody1);

			Inputs inputs = new Inputs();

			RootSceneGraph cameraRootObject = new RootSceneGraph();
			Camera camera = new Camera(new Vec3f(-10.0f, 0.0f, 10.0f), new Vec3f(0.0f, 0.0f, 0.0f), 0.5f, 0.1f);
			CameraSceneGraph cameraGameObject = new CameraSceneGraph(cameraRootObject, camera, CameraType.PRIMARY);

			RootSceneGraph lightRootObject = new RootSceneGraph();
			createLights(lightRootObject);

			HashMap<UUID, RootSceneGraph> rootGameObjectHashMap = new HashMap<>();

			for (RigidBody rigidBody : rigidBodies) {
				rootGameObjectHashMap.put(rigidBody.getUuid(), convertToGameObject(rigidBody, "/textures/white.png"));
			}

			rootGameObjectHashMap.put(UUID.randomUUID(), cameraRootObject);
			rootGameObjectHashMap.put(UUID.randomUUID(), lightRootObject);

			SimulationInterface simulation = new com.nick.wood.physics.rigid_body_dynamics_verbose.Simulation(rigidBodies);

			Game game = new Game(1000, 800, simulation, true, true, rootGameObjectHashMap, inputs);

			ExecutorService executor = Executors.newFixedThreadPool(4);

			Future<?> submit = executor.submit(game);

			// waits for game to finish
			submit.get();

			// closes executor service
			executor.shutdown();
		}

	 */
	void game() throws ExecutionException, InterruptedException {

		ArrayList<RigidBody> rigidBodies = new ArrayList<>();
		ArrayList<UUID> toRender = new ArrayList<>();
		HashMap<UUID, SceneGraph> rootGameObjectHashMap = new HashMap<>();
		ArrayList<Force> forces = new ArrayList<>();
		forces.add(new Drag(-0.1));

		// Arena
		createArena(rootGameObjectHashMap, rigidBodies, 1000, forces);

		// ball
		UUID uuid = UUID.randomUUID();
		toRender.add(uuid);
		Vec3d mom = Vec3d.ZERO;
		Vec3d angMom = Vec3d.Z;
		RigidBody rigidBodyBall = new RigidBody(uuid, 1, new Vec3d(5, 5, 5), new Vec3d(0, 0, 0), new Quaternion(1.0, 0.0, 0.0, 0.0), mom, angMom, RigidBodyType.SPHERE, forces);
		rigidBodies.add(rigidBodyBall);
		rootGameObjectHashMap.put(uuid, convertToGameObject(rigidBodyBall, "/textures/mars.jpg"));

		// player
		UUID playerUUID = UUID.randomUUID();
		double playerMass = 10;
		RigidBody playerRigidBody = new RigidBody(playerUUID, playerMass, new Vec3d(1.0, 1.0, 1.0), new Vec3d(-40, 0, 0), new Quaternion(1.0, 0.0, 0.0, 0.0), Vec3d.ZERO, Vec3d.ZERO, RigidBodyType.SPHERE, forces);
		SceneGraph rootGameObject = convertToGameObjectDragon(playerRigidBody, "/textures/white.png");
		TransformSceneGraph transformGameObjectLaser = (TransformSceneGraph) rootGameObject.getSceneGraphNodeData().getChildren().get(0);
		createLaserUnderTransform(new Vec3f(1.0f, 1.0f, 1.0f), Vec3f.ZERO, transformGameObjectLaser);
		rigidBodies.add(playerRigidBody);
		Camera camera = new Camera(new Vec3f(-5.0f, 0.0f, 1.0f), new Vec3f(0.0f, 0.0f, 0.0f), 0.5f, 0.1f);
		CameraSceneGraph cameraGameObject = new CameraSceneGraph(transformGameObjectLaser, camera, CameraType.PRIMARY);

		HudController hudController = createHud(cameraGameObject, playerUUID, toRender);

		rootGameObjectHashMap.put(playerUUID, rootGameObject);

		// controls
		Inputs inputs = new Inputs();
		Control control = new RigidBodyControl(100 * playerMass, 1 * Math.sqrt(playerMass), playerUUID);

		// sim
		SimulationInterface simulation = new com.nick.wood.physics.rigid_body_dynamics_verbose.Simulation(rigidBodies);

		Game game = new Game(1000, 800, simulation, true, false, rootGameObjectHashMap, inputs);
		game.setController(control);
		game.addHudController(hudController);

		ExecutorService executor = Executors.newFixedThreadPool(4);

		Future<?> submit = executor.submit(game);

		// waits for game to finish
		submit.get();

		// closes executor service
		executor.shutdown();
	}


	private HudController createHud(SceneGraphNode cameraGameObject, UUID playerUUID, ArrayList<UUID> toRender) {
		Transform hudTransformLeft = new Transform(
				new Vec3f(-2, 2.5f, -1),
				new Vec3f(1, 2, 1),
				Matrix4f.Rotation(-90, Vec3f.X).multiply(Matrix4f.Rotation(-20, Vec3f.Z))
		);
		TransformSceneGraph hudTransformGameObjectLeft = new TransformSceneGraph(cameraGameObject, hudTransformLeft);

		TextItem linearTextItem = createHudItem(hudTransformGameObjectLeft, Vec3f.Y.scale(0.1f));
		TextItem angularTextItem = createHudItem(hudTransformGameObjectLeft, Vec3f.ZERO);

		Transform hudTransformTopMiddle = new Transform(
				new Vec3f(-2, 0.8f, 2.8f),
				new Vec3f(2, 4, 2),
				Matrix4f.Rotation(-90, Vec3f.X)
		);
		TransformSceneGraph hudTransformGameObjectTopMiddle = new TransformSceneGraph(cameraGameObject, hudTransformTopMiddle);
		TextItem slowDownTextItem = createHudItem(hudTransformGameObjectTopMiddle, Vec3f.ZERO);

		Transform hudTransformRight = new Transform(
				new Vec3f(-3, -1.2f, 0),
				new Vec3f(0.05f, 0.05f, 0.05f),
				Matrix4f.Rotation(20, Vec3f.Z)
		);
		TransformSceneGraph hudTransformGameObjectRight = new TransformSceneGraph(cameraGameObject, hudTransformRight);

		return new HudController(linearTextItem, angularTextItem, slowDownTextItem, hudTransformGameObjectRight, playerUUID, toRender);
	}

	public TextItem createHudItem(SceneGraphNode sceneGraphNode, Vec3f pos) {
		Transform hudTransform = new Transform(
				pos,
				Vec3f.ONE,
				Matrix4f.Identity
		);
		TransformSceneGraph hudTransformGameObject = new TransformSceneGraph(sceneGraphNode, hudTransform);
		TextItem textItem = (TextItem) new MeshBuilder().setMeshType(TEXT).build();
		MeshSceneGraph textMeshObject = new MeshSceneGraph(hudTransformGameObject, textItem);
		return textItem;
	}

	public void createArenaGameObject(GameObjectFactory gameObjectFactory, ArrayList<Force> forces, float width) {

		GameObjectBuilder arenaBuilder = gameObjectFactory.createGameObjectBuilder();
		arenaBuilder.setRigidBodyType(RigidBodyType.SPHERE_INNER)
				.setForces(forces)
				.setMass(100)
				.setDimensions(new Vec3d(width, width, width))
				.setMeshObject(new MeshBuilder().setInvertedNormals(true).setTexture("/textures/2k_neptune.jpg").build()).build();
	}

	private void createArena(HashMap<UUID, SceneGraph> rootGameObject, ArrayList<RigidBody> rigidBodies, int width, ArrayList<Force> forces) {

		//GameObjectBuilder arenaBuilder = gameObjectFactory.createGameObjectBuilder();
		//arenaBuilder.setRigidBodyType(RigidBodyType.SPHERE_INNER)
		//		.setForces(forces)
		//		.setMass(100)
		//		.setDimensions(new Vec3d(width, width, width))
		//		.setMeshObject(new MeshBuilder().setInvertedNormals(true).setTexture("/textures/2k_neptune.jpg").build()).build();

		//// Arena
		SceneGraph sceneGraph = new SceneGraph();
		UUID uuid = UUID.randomUUID();
		RigidBody rigidBody = new RigidBody(
				uuid,
				1000,
				new Vec3d(width, width, width),
				Vec3d.ZERO,
				new Quaternion(1.0, 0.0, 0.0, 0.0),
				Vec3d.ZERO,
				Vec3d.Z,
				RigidBodyType.SPHERE_INNER,
				forces
		);
		rigidBodies.add(rigidBody);
		MeshObject arenaMesh = new MeshBuilder().setInvertedNormals(true).setTexture("/textures/2k_neptune.jpg").build();
		Transform transform = new Transform(Vec3f.ZERO, new Vec3f(width, width, width), Matrix4f.Identity);

		TransformSceneGraph transformSceneGraph = new TransformSceneGraph(sceneGraph, transform);
		MeshSceneGraph meshSceneGraph = new MeshSceneGraph(transformSceneGraph, arenaMesh);

		MeshBuilder meshBuilder = new MeshBuilder()
				.setTriangleNumber(5)
				.setInvertedNormals(true);
		MeshObject meshGroupLight = meshBuilder.build();
		createLightUnderTransform(new Vec3f(1.0f, 0.0f, 0.0f), Vec3f.Z.scale(0.30f), meshGroupLight, transformSceneGraph, width);
		createLightUnderTransform(new Vec3f(0.0f, 1.0f, 0.0f), Vec3f.Z.scale(-0.30f), meshGroupLight, transformSceneGraph, width);
		createLightUnderTransform(new Vec3f(0.0f, 0.0f, 1.0f), Vec3f.X.scale(0.30f), meshGroupLight, transformSceneGraph, width);
		createLightUnderTransform(new Vec3f(1.0f, 1.0f, 0.0f), Vec3f.X.scale(-0.30f), meshGroupLight, transformSceneGraph, width);
		createLightUnderTransform(new Vec3f(1.0f, 0.0f, 1.0f), Vec3f.Y.scale(0.30f), meshGroupLight, transformSceneGraph, width);
		createLightUnderTransform(new Vec3f(0.0f, 1.0f, 1.0f), Vec3f.Y.scale(-0.30f), meshGroupLight, transformSceneGraph, width);
		rootGameObject.put(uuid, sceneGraph);

	}

	public SceneGraph convertToGameObjectDragon(RigidBody rigidBody, String texture) {

		SceneGraph rootObject = new SceneGraph();

		Transform transform = new Transform(
				(Vec3f) rigidBody.getOrigin().toVecf(),
				(Vec3f) rigidBody.getDimensions().toVecf(),
				rigidBody.getRotation().toMatrix().toMatrix4f()
		);

		TransformSceneGraph transformGameObject = new TransformSceneGraph(rootObject, transform);

		Matrix4f matrix4f = Matrix4f.Transform(Vec3f.ZERO, Matrix4f.Rotation(-90, Vec3f.X).multiply(Matrix4f.Rotation(180, Vec3f.Z)), Vec3f.ONE.scale(0.1f));

		MeshBuilder meshBuilder = new MeshBuilder()
				.setMeshType(MODEL)
				.setTriangleNumber(5)
				.setTexture(texture)
				.setTransform(matrix4f);
		MeshObject meshObject = meshBuilder.build();

		MeshSceneGraph meshGameObject = new MeshSceneGraph(
				transformGameObject,
				meshObject
		);

		return rootObject;

	}

	public SceneGraph convertToGameObject(RigidBody rigidBody, String texture) {

		SceneGraph rootObject = new SceneGraph();

		Transform transform = new Transform(
				(Vec3f) rigidBody.getOrigin().toVecf(),
				(Vec3f) rigidBody.getDimensions().toVecf(),
				rigidBody.getRotation().toMatrix().toMatrix4f()
		);

		TransformSceneGraph transformGameObject = new TransformSceneGraph(rootObject, transform);

		MeshBuilder meshBuilder = new MeshBuilder();
		meshBuilder.setTriangleNumber(10)
				.setTexture(texture);

		switch (rigidBody.getType()) {
			case SPHERE_INNER:
				meshBuilder.setInvertedNormals(true);
			case SPHERE:
				meshBuilder.setMeshType(MeshType.SPHERE);
				break;
			case CUBOID:
				meshBuilder.setMeshType(MeshType.CUBOID);
				break;
			default:
				meshBuilder.setMeshType(MeshType.SPHERE);
				break;
		}
		;

		MeshSceneGraph meshGameObject = new MeshSceneGraph(
				transformGameObject,
				meshBuilder.build()
		);

		return rootObject;

	}

	/*
		public RootSceneGraph convertToGameObjectAndAttachAnotherMesh(RigidBody rigidBody) {

			RootSceneGraph rootObject = new RootSceneGraph();

			Transform transform = new Transform(
					(Vec3f) rigidBody.getOrigin().toVecf(),
					(Vec3f) rigidBody.getDimensions().toVecf(),
					rigidBody.getRotation().toMatrix().toMatrix4f()
			);

			TransformSceneGraph transformGameObject = new TransformSceneGraph(rootObject, transform);

			MeshObject meshObject;

			switch (rigidBody.getType()){
				case SPHERE_INNER:
					meshObject = new SphereMesh(10, new Material("/textures/white.png"), true, transformation);
					break;
				case SPHERE:
					meshObject = new SphereMesh(10, new Material("/textures/white.png"), false, transformation);
					break;
				case CUBOID:
					meshObject = new CubeMesh(false, new Material("/textures/white.png"));
					break;
				default:
					meshObject = new SphereMesh(10, new Material("/textures/white.png"), false, transformation);
					break;
			}

			MeshSceneGraph meshGameObject = new MeshSceneGraph(
					transformGameObject,
					meshObject
			);

			PointLight light = new PointLight(
					Vec3f.X,
					100);
			Transform lightGameObjectTransform = new Transform(
					Vec3f.Z,
					Vec3f.ONE,
					Matrix4f.Identity
			);

			TransformSceneGraph transformGameObjectLight = new TransformSceneGraph(meshGameObject, lightGameObjectTransform);
			LightSceneGraph lightGameObject = new LightSceneGraph(transformGameObjectLight, light);
			MeshSceneGraph meshGameObjectLight = new MeshSceneGraph(
					transformGameObjectLight,
					meshObject
			);

			return rootObject;

		}

		private RootSceneGraph convertToPlayerObject(RigidBody rigidBody) {
			RootSceneGraph rootObject = new RootSceneGraph();

			Transform transform = new Transform(
					(Vec3f) rigidBody.getOrigin().toVecf(),
					(Vec3f) rigidBody.getDimensions().toVecf(),
					rigidBody.getRotation().toMatrix().toMatrix4f()
			);

			TransformSceneGraph transformGameObject = new TransformSceneGraph(rootObject, transform);

			PlayerSceneGraph playerGameObject = new PlayerSceneGraph(transformGameObject);

			Camera camera = new Camera(new Vec3f(0.0f, 0.0f, 0.0f), new Vec3f(0.0f, 0.0f, 0.0f), 0.5f, 0.1f);

			CameraSceneGraph cameraGameObject = new CameraSceneGraph(transformGameObject, camera, CameraType.PRIMARY);

			MeshObject meshObject;

			switch (rigidBody.getType()){
				case SPHERE_INNER:
					meshObject = new SphereMesh(10, new Material("/textures/white.png"), true, transformation);
					break;
				case SPHERE:
					meshObject = new SphereMesh(10, new Material("/textures/white.png"), false, transformation);
					break;
				case CUBOID:
					meshObject = new CubeMesh(false, new Material("/textures/white.png")) ;
					break;
				default:
					meshObject = new SphereMesh(10, new Material("/textures/white.png"), false, transformation);
					break;
			}

			MeshSceneGraph meshGameObject = new MeshSceneGraph(
					transformGameObject,
					meshObject
			);

			return rootObject;
		}

		private void createLights(RootSceneGraph rootGameObject) {
			MeshObject meshGroupLight = new SphereMesh(10, new Material("/textures/white.png"), true, transformation);

			createLight(new Vec3f(0.0f, 1.0f, 0.0f), Vec3f.Z.scale(10), rootGameObject, meshGroupLight);
			createLight(new Vec3f(1.0f, 0.0f, 0.0f), Vec3f.Z.scale(-10), rootGameObject, meshGroupLight);
		}
	*/
	private void createLight(Vec3f colour, Vec3f pos, SceneGraph rootGameObject, MeshObject meshGroupLight) {
		PointLight light = new PointLight(
				colour,
				100);
		Transform lightGameObjectTransform = new Transform(
				pos,
				Vec3f.ONE,
				Matrix4f.Identity
		);

		TransformSceneGraph transformGameObject = new TransformSceneGraph(rootGameObject, lightGameObjectTransform);
		LightSceneGraph lightGameObject = new LightSceneGraph(transformGameObject, light);
		MeshSceneGraph meshGameObject = new MeshSceneGraph(
				transformGameObject,
				meshGroupLight
		);
	}

	private void createLaserUnderTransform(Vec3f colour, Vec3f pos, TransformSceneGraph transformGameObject) {
		PointLight pointLight = new PointLight(
				colour,
				500);
		SpotLight light = new SpotLight(
				pointLight,
				Vec3f.X,
				0.1f);
		Transform lightGameObjectTransform = new Transform(
				pos,
				Vec3f.ONE,
				Matrix4f.Identity
		);

		TransformSceneGraph transformGameObject1 = new TransformSceneGraph(transformGameObject, lightGameObjectTransform);
		LightSceneGraph lightGameObject = new LightSceneGraph(transformGameObject1, light);
	}

	private void createLightUnderTransform(Vec3f colour, Vec3f pos, MeshObject meshGroupLight, TransformSceneGraph transformGameObject, float intensity) {
		PointLight light = new PointLight(
				colour,
				intensity);
		Transform lightGameObjectTransform = new Transform(
				pos,
				Vec3f.ONE.scale(0.001f),
				Matrix4f.Identity
		);

		TransformSceneGraph transformGameObject1 = new TransformSceneGraph(transformGameObject, lightGameObjectTransform);
		LightSceneGraph lightGameObject = new LightSceneGraph(transformGameObject1, light);
		MeshSceneGraph meshGameObject = new MeshSceneGraph(
				transformGameObject1,
				meshGroupLight
		);
	}
}