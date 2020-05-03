package com.nick.wood.space_game.game;

import com.nick.wood.graphics_library_3d.Material;
import com.nick.wood.graphics_library_3d.input.Control;
import com.nick.wood.graphics_library_3d.input.Inputs;
import com.nick.wood.graphics_library_3d.lighting.PointLight;
import com.nick.wood.graphics_library_3d.lighting.SpotLight;
import com.nick.wood.graphics_library_3d.objects.Camera;
import com.nick.wood.graphics_library_3d.objects.Transform;
import com.nick.wood.graphics_library_3d.objects.game_objects.*;
import com.nick.wood.graphics_library_3d.objects.mesh_objects.*;
import com.nick.wood.maths.objects.Quaternion;
import com.nick.wood.maths.objects.matrix.Matrix4f;
import com.nick.wood.maths.objects.vector.Vec3d;
import com.nick.wood.maths.objects.vector.Vec3f;
import com.nick.wood.physics.SimulationInterface;
import com.nick.wood.physics.rigid_body_dynamics_verbose.RigidBody;
import com.nick.wood.physics.rigid_body_dynamics_verbose.RigidBodyType;
import com.nick.wood.physics.rigid_body_dynamics_verbose.forces.GravityBasic;
import com.nick.wood.space_game.game.components.HudController;
import com.nick.wood.space_game.game.controls.RigidBodyControl;
import com.nick.wood.physics.rigid_body_dynamics_verbose.forces.Drag;
import com.nick.wood.physics.rigid_body_dynamics_verbose.forces.Force;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class SimulationTest {

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

		RootGameObject cameraRootObject = new RootGameObject();
		Camera camera = new Camera(new Vec3f(-5.0f, 0.0f, 0.0f), new Vec3f(0.0f, 0.0f, 0.0f), 0.5f, 0.1f);
		CameraGameObject cameraGameObject = new CameraGameObject(cameraRootObject, camera, CameraType.PRIMARY);

		RootGameObject lightRootObject = new RootGameObject();
		createLights(lightRootObject);

		Inputs inputs = new Inputs();

		HashMap<UUID, RootGameObject> rootGameObjectHashMap = new HashMap<>();

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

		RootGameObject cameraRootObject = new RootGameObject();
		Camera camera = new Camera(new Vec3f(-10.0f, 0.0f, 0.0f), new Vec3f(0.0f, 0.0f, 0.0f), 0.5f, 0.1f);
		CameraGameObject cameraGameObject = new CameraGameObject(cameraRootObject, camera, CameraType.PRIMARY);

		//RootGameObject lightRootObject = new RootGameObject();
		//createLights(lightRootObject);
		//rootGameObjectHashMap.put(UUID.randomUUID(), lightRootObject);

		Inputs inputs = new Inputs();

		HashMap<UUID, RootGameObject> rootGameObjectHashMap = new HashMap<>();

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

		RootGameObject cameraRootObject = new RootGameObject();
		Camera camera = new Camera(new Vec3f(0.0f, 0.0f, 10.0f), new Vec3f(0.0f, 0.0f, 0.0f), 0.5f, 0.1f);
		CameraGameObject cameraGameObject = new CameraGameObject(cameraRootObject, camera, CameraType.PRIMARY);

		Inputs inputs = new Inputs();

		HashMap<UUID, RootGameObject> rootGameObjectHashMap = new HashMap<>();

		for (RigidBody rigidBody : rigidBodies) {
			rootGameObjectHashMap.put(rigidBody.getUuid(), convertToGameObject(rigidBody, "/textures/white.png"));
		}

		rootGameObjectHashMap.put(UUID.randomUUID(), cameraRootObject);


		RootGameObject lightRootObject = new RootGameObject();
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

		RootGameObject cameraRootObject = new RootGameObject();
		Camera camera = new Camera(new Vec3f(-20.0f, 0.0f, 0.0f), new Vec3f(0.0f, 0.0f, 0.0f), 0.5f, 0.1f);
		CameraGameObject cameraGameObject = new CameraGameObject(cameraRootObject, camera, CameraType.PRIMARY);

		HashMap<UUID, RootGameObject> rootGameObjectHashMap = new HashMap<>();

		for (RigidBody rigidBody : rigidBodies) {
			RootGameObject rootGameObject = convertToGameObject(rigidBody, "/textures/white.png");
			TransformGameObject transformGameObjectLaser = (TransformGameObject) rootGameObject.getGameObjectNodeData().getChildren().get(0);
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


		HashMap<UUID, RootGameObject> rootGameObjectHashMap = new HashMap<>();


		RigidBodyControl control = null;
		for (RigidBody rigidBody : rigidBodies) {
			RootGameObject rootGameObject = convertToGameObject(rigidBody, "/textures/white.png");
			TransformGameObject transformGameObjectLaser = (TransformGameObject) rootGameObject.getGameObjectNodeData().getChildren().get(0);
			createLaserUnderTransform(new Vec3f(1.0f, 1.0f, 1.0f), Vec3f.ZERO, transformGameObjectLaser);

			control = new RigidBodyControl(100 * rigidBody.getMass(), 1 * Math.sqrt(rigidBody.getMass()), rigidBody.getUuid());

			Camera camera = new Camera(new Vec3f(-5.0f, 0.0f, 1.0f), new Vec3f(0.0f, 0.0f, 0.0f), 0.5f, 0.1f);
			CameraGameObject cameraGameObject = new CameraGameObject(transformGameObjectLaser, camera, CameraType.PRIMARY);

			rootGameObjectHashMap.put(rigidBody.getUuid(), rootGameObject);
		}

		//// Arena
		UUID uuidArena = UUID.randomUUID();
		RigidBody rigidBodyArena = new RigidBody(uuidArena, 10, new Vec3d(100, 100, 100), new Vec3d(0.0, 0.0, 0.0), new Quaternion(1.0, 0.0, 0.0, 0.0), Vec3d.ZERO, Vec3d.ZERO, RigidBodyType.SPHERE_INNER, forces);
		rigidBodies.add(rigidBodyArena);
		RootGameObject rootGameObject = convertToGameObject(rigidBodyArena, "/textures/white.png");
		TransformGameObject transformGameObject = (TransformGameObject) rootGameObject.getGameObjectNodeData().getChildren().get(0);
		MeshObject meshGroupLight = new SphereMesh(10, new Material("/textures/white.png"), true);
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

		RootGameObject cameraRootObject = new RootGameObject();
		Camera camera = new Camera(new Vec3f(0.0f, 0.0f, 10.0f), new Vec3f(0.0f, 0.0f, 0.0f), 0.5f, 0.1f);
		CameraGameObject cameraGameObject = new CameraGameObject(cameraRootObject, camera, CameraType.PRIMARY);

		RootGameObject lightRootObject = new RootGameObject();
		createLights(lightRootObject);

		Inputs inputs = new Inputs();

		HashMap<UUID, RootGameObject> rootGameObjectHashMap = new HashMap<>();


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
		RootGameObject rootGameObject = convertToGameObject(rigidBody, "/textures/white.png");
		TransformGameObject transformGameObjectLaser = (TransformGameObject) rootGameObject.getGameObjectNodeData().getChildren().get(0);
		createLaserUnderTransform(new Vec3f(1.0f, 1.0f, 1.0f), Vec3f.ZERO, transformGameObjectLaser);
		rigidBodies.add(rigidBody);
		Control control = new RigidBodyControl(100 * rigidBody.getMass(), 1 * Math.sqrt(rigidBody.getMass()), rigidBody.getUuid());


		Camera camera = new Camera(new Vec3f(-5.0f, 0.0f, 1.0f), new Vec3f(0.0f, 0.0f, 0.0f), 0.5f, 0.1f);
		CameraGameObject cameraGameObject = new CameraGameObject(transformGameObjectLaser, camera, CameraType.PRIMARY);


		RootGameObject lightRootObject = new RootGameObject();
		createLights(lightRootObject);

		Inputs inputs = new Inputs();

		HashMap<UUID, RootGameObject> rootGameObjectHashMap = new HashMap<>();

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

		RootGameObject cameraRootObject = new RootGameObject();
		Camera camera = new Camera(new Vec3f(-10.0f, 0.0f, 10.0f), new Vec3f(0.0f, 0.0f, 0.0f), 0.5f, 0.1f);
		CameraGameObject cameraGameObject = new CameraGameObject(cameraRootObject, camera, CameraType.PRIMARY);

		RootGameObject lightRootObject = new RootGameObject();
		createLights(lightRootObject);

		HashMap<UUID, RootGameObject> rootGameObjectHashMap = new HashMap<>();

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


	void game() throws ExecutionException, InterruptedException {

		ArrayList<RigidBody> rigidBodies = new ArrayList<>();
		ArrayList<UUID> toRender = new ArrayList<>();
		HashMap<UUID, RootGameObject> rootGameObjectHashMap = new HashMap<>();
		ArrayList<Force> forces = new ArrayList<>();
		forces.add(new Drag(-0.1));

		// Arena
		createArena(rigidBodies, rootGameObjectHashMap, forces, 1000);

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
		RootGameObject rootGameObject = convertToGameObject(playerRigidBody, "/textures/white.png");
		TransformGameObject transformGameObjectLaser = (TransformGameObject) rootGameObject.getGameObjectNodeData().getChildren().get(0);
		createLaserUnderTransform(new Vec3f(1.0f, 1.0f, 1.0f), Vec3f.ZERO, transformGameObjectLaser);
		rigidBodies.add(playerRigidBody);
		Camera camera = new Camera(new Vec3f(-5.0f, 0.0f, 1.0f), new Vec3f(0.0f, 0.0f, 0.0f), 0.5f, 0.1f);
		CameraGameObject cameraGameObject = new CameraGameObject(transformGameObjectLaser, camera, CameraType.PRIMARY);

		HudController hudController = createHud(cameraGameObject, playerUUID, toRender);

		rootGameObjectHashMap.put(playerUUID, rootGameObject);

		// controls
		Inputs inputs = new Inputs();
		Control control = new RigidBodyControl(100 * playerMass, 1 * Math.sqrt(playerMass), playerUUID);

		// sim
		SimulationInterface simulation = new com.nick.wood.physics.rigid_body_dynamics_verbose.Simulation(rigidBodies);

		Game game = new Game(1000, 800, simulation, false, false, rootGameObjectHashMap, inputs);
		game.setController(control);
		game.addHudController(hudController);

		ExecutorService executor = Executors.newFixedThreadPool(4);

		Future<?> submit = executor.submit(game);

		// waits for game to finish
		submit.get();

		// closes executor service
		executor.shutdown();
	}

	private HudController createHud(GameObjectNode cameraGameObject, UUID playerUUID, ArrayList<UUID> toRender) {
		Transform hudTransformLeft = new Transform(
				new Vec3f(-2, 2.5f, -1),
				new Vec3f(1, 2, 1),
				Matrix4f.Rotation(-90, Vec3f.X).multiply(Matrix4f.Rotation(-20, Vec3f.Z))
		);
		TransformGameObject hudTransformGameObjectLeft = new TransformGameObject(cameraGameObject, hudTransformLeft);

		TextItem linearTextItem = createHudItem(hudTransformGameObjectLeft, Vec3f.Y.scale(0.1f));
		TextItem angularTextItem = createHudItem(hudTransformGameObjectLeft, Vec3f.ZERO);

		Transform hudTransformTopMiddle = new Transform(
				new Vec3f(-2, 0.8f, 2.8f),
				new Vec3f(2, 4, 2),
				Matrix4f.Rotation(-90, Vec3f.X)
		);
		TransformGameObject hudTransformGameObjectTopMiddle = new TransformGameObject(cameraGameObject, hudTransformTopMiddle);
		TextItem slowDownTextItem = createHudItem(hudTransformGameObjectTopMiddle, Vec3f.ZERO);

		Transform hudTransformRight = new Transform(
				new Vec3f(-3,-1.2f,0),
				new Vec3f(0.05f,0.05f,0.05f),
				Matrix4f.Rotation(20, Vec3f.Z)
		);
		TransformGameObject hudTransformGameObjectRight = new TransformGameObject(cameraGameObject, hudTransformRight);

		return new HudController(linearTextItem, angularTextItem, slowDownTextItem, hudTransformGameObjectRight, playerUUID, toRender);
	}

	public TextItem createHudItem(GameObjectNode gameObjectNode, Vec3f pos) {
		Transform hudTransform = new Transform(
				pos,
				Vec3f.ONE,
				Matrix4f.Identity
		);
		TransformGameObject hudTransformGameObject = new TransformGameObject(gameObjectNode, hudTransform);
		TextItem textItem = new TextItem("Test", "/font/gothic.png", 16, 16);
		MeshGameObject textMeshObject = new MeshGameObject(hudTransformGameObject, textItem);
		return textItem;
	}

	public void createArena(ArrayList<RigidBody> rigidBodies, HashMap<UUID, RootGameObject> rootGameObjectHashMap, ArrayList<Force> forces, float width) {
		//// Arena
		UUID uuidArena = UUID.randomUUID();
		RigidBody rigidBodyArena = new RigidBody(uuidArena, 100, new Vec3d(width, width, width), new Vec3d(0.0, 0.0, 0.0), new Quaternion(1.0, 0.0, 0.0, 0.0), Vec3d.ZERO, Vec3d.ZERO, RigidBodyType.SPHERE_INNER, forces);
		rigidBodies.add(rigidBodyArena);
		RootGameObject rootGameObject = convertToGameObject(rigidBodyArena, "/textures/2k_neptune.jpg");
		TransformGameObject transformGameObject = (TransformGameObject) rootGameObject.getGameObjectNodeData().getChildren().get(0);
		MeshObject meshGroupLight = new SphereMesh(5, new Material("/textures/white.png"), true);
		createLightUnderTransform(new Vec3f(1.0f, 0.0f, 0.0f), Vec3f.Z.scale(0.30f), meshGroupLight, transformGameObject, width);
		createLightUnderTransform(new Vec3f(0.0f, 1.0f, 0.0f), Vec3f.Z.scale(-0.30f), meshGroupLight, transformGameObject, width);
		createLightUnderTransform(new Vec3f(0.0f, 0.0f, 1.0f), Vec3f.X.scale(0.30f), meshGroupLight, transformGameObject, width);
		createLightUnderTransform(new Vec3f(1.0f, 1.0f, 0.0f), Vec3f.X.scale(-0.30f), meshGroupLight, transformGameObject, width);
		createLightUnderTransform(new Vec3f(1.0f, 0.0f, 1.0f), Vec3f.Y.scale(0.30f), meshGroupLight, transformGameObject, width);
		createLightUnderTransform(new Vec3f(0.0f, 1.0f, 1.0f), Vec3f.Y.scale(-0.30f), meshGroupLight, transformGameObject, width);
		rootGameObjectHashMap.put(uuidArena, rootGameObject);

	}

	public RootGameObject convertToGameObject(RigidBody rigidBody, String texture) {

		RootGameObject rootObject = new RootGameObject();

		Transform transform = new Transform(
				(Vec3f) rigidBody.getOrigin().toVecf(),
				(Vec3f) rigidBody.getDimensions().toVecf(),
				rigidBody.getRotation().toMatrix().toMatrix4f()
		);

		TransformGameObject transformGameObject = new TransformGameObject(rootObject, transform);

		MeshObject meshObject;

		switch (rigidBody.getType()){
			case SPHERE_INNER:
				meshObject = new SphereMesh(10, new Material(texture), true);
				break;
			case SPHERE:
				meshObject = new SphereMesh(10, new Material(texture), false);
				break;
			case CUBOID:
				meshObject = new CubeMesh(false, new Material(texture));
				break;
			default:
				meshObject = new SphereMesh(10, new Material(texture), false);
				break;
		}

		MeshGameObject meshGameObject = new MeshGameObject(
				transformGameObject,
				meshObject
		);

		return rootObject;

	}

	public RootGameObject convertToGameObjectAndAttachAnotherMesh(RigidBody rigidBody) {

		RootGameObject rootObject = new RootGameObject();

		Transform transform = new Transform(
				(Vec3f) rigidBody.getOrigin().toVecf(),
				(Vec3f) rigidBody.getDimensions().toVecf(),
				rigidBody.getRotation().toMatrix().toMatrix4f()
		);

		TransformGameObject transformGameObject = new TransformGameObject(rootObject, transform);

		MeshObject meshObject;

		switch (rigidBody.getType()){
			case SPHERE_INNER:
				meshObject = new SphereMesh(10, new Material("/textures/white.png"), true);
				break;
			case SPHERE:
				meshObject = new SphereMesh(10, new Material("/textures/white.png"), false);
				break;
			case CUBOID:
				meshObject = new CubeMesh(false, new Material("/textures/white.png"));
				break;
			default:
				meshObject = new SphereMesh(10, new Material("/textures/white.png"), false);
				break;
		}

		MeshGameObject meshGameObject = new MeshGameObject(
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

		TransformGameObject transformGameObjectLight = new TransformGameObject(meshGameObject, lightGameObjectTransform);
		LightGameObject lightGameObject = new LightGameObject(transformGameObjectLight, light);
		MeshGameObject meshGameObjectLight = new MeshGameObject(
				transformGameObjectLight,
				meshObject
		);

		return rootObject;

	}

	private RootGameObject convertToPlayerObject(RigidBody rigidBody) {
		RootGameObject rootObject = new RootGameObject();

		Transform transform = new Transform(
				(Vec3f) rigidBody.getOrigin().toVecf(),
				(Vec3f) rigidBody.getDimensions().toVecf(),
				rigidBody.getRotation().toMatrix().toMatrix4f()
		);

		TransformGameObject transformGameObject = new TransformGameObject(rootObject, transform);

		PlayerGameObject playerGameObject = new PlayerGameObject(transformGameObject);

		Camera camera = new Camera(new Vec3f(0.0f, 0.0f, 0.0f), new Vec3f(0.0f, 0.0f, 0.0f), 0.5f, 0.1f);

		CameraGameObject cameraGameObject = new CameraGameObject(transformGameObject, camera, CameraType.PRIMARY);

		MeshObject meshObject;

		switch (rigidBody.getType()){
			case SPHERE_INNER:
				meshObject = new SphereMesh(10, new Material("/textures/white.png"), true);
				break;
			case SPHERE:
				meshObject = new SphereMesh(10, new Material("/textures/white.png"), false);
				break;
			case CUBOID:
				meshObject = new CubeMesh(false, new Material("/textures/white.png")) ;
				break;
			default:
				meshObject = new SphereMesh(10, new Material("/textures/white.png"), false);
				break;
		}

		MeshGameObject meshGameObject = new MeshGameObject(
				transformGameObject,
				meshObject
		);

		return rootObject;
	}

	private void createLights(RootGameObject rootGameObject) {
		MeshObject meshGroupLight = new SphereMesh(10, new Material("/textures/white.png"), true);

		createLight(new Vec3f(0.0f, 1.0f, 0.0f), Vec3f.Z.scale(10), rootGameObject, meshGroupLight);
		createLight(new Vec3f(1.0f, 0.0f, 0.0f), Vec3f.Z.scale(-10), rootGameObject, meshGroupLight);
	}

	private void createLight(Vec3f colour, Vec3f pos, RootGameObject rootGameObject, MeshObject meshGroupLight) {
		PointLight light = new PointLight(
				colour,
				100);
		Transform lightGameObjectTransform = new Transform(
				pos,
				Vec3f.ONE,
				Matrix4f.Identity
		);

		TransformGameObject transformGameObject = new TransformGameObject(rootGameObject, lightGameObjectTransform);
		LightGameObject lightGameObject = new LightGameObject(transformGameObject, light);
		MeshGameObject meshGameObject = new MeshGameObject(
				transformGameObject,
				meshGroupLight
		);
	}

	private void createLaserUnderTransform(Vec3f colour, Vec3f pos, TransformGameObject transformGameObject) {
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

		TransformGameObject transformGameObject1 = new TransformGameObject(transformGameObject, lightGameObjectTransform);
		LightGameObject lightGameObject = new LightGameObject(transformGameObject1, light);
	}

	private void createLightUnderTransform(Vec3f colour, Vec3f pos, MeshObject meshGroupLight, TransformGameObject transformGameObject, float intensity) {
		PointLight light = new PointLight(
				colour,
				intensity);
		Transform lightGameObjectTransform = new Transform(
				pos,
				Vec3f.ONE.scale(0.001f),
				Matrix4f.Identity
		);

		TransformGameObject transformGameObject1 = new TransformGameObject(transformGameObject, lightGameObjectTransform);
		LightGameObject lightGameObject = new LightGameObject(transformGameObject1, light);
		MeshGameObject meshGameObject = new MeshGameObject(
				transformGameObject1,
				meshGroupLight
		);
	}
}