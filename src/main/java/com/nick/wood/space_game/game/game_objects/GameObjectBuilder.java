package com.nick.wood.space_game.game.game_objects;

import com.nick.wood.graphics_library.lighting.Light;
import com.nick.wood.graphics_library.objects.Camera;
import com.nick.wood.graphics_library.objects.mesh_objects.MeshObject;
import com.nick.wood.graphics_library.objects.scene_graph_objects.*;
import com.nick.wood.maths.objects.Quaternion;
import com.nick.wood.maths.objects.vector.Vec3d;
import com.nick.wood.physics.rigid_body_dynamics_verbose.RigidBody;
import com.nick.wood.physics.rigid_body_dynamics_verbose.RigidBodyType;
import com.nick.wood.physics.rigid_body_dynamics_verbose.forces.Force;

import java.util.ArrayList;
import java.util.UUID;

public class GameObjectBuilder {

	private final GameObjectFactory gameObjectFactory;

	private final UUID uuid = UUID.randomUUID();
	private double mass = 1;
	private Vec3d dimensions = Vec3d.ONE;
	private Vec3d position = Vec3d.ZERO;
	private Vec3d linearMomentum = Vec3d.ZERO;
	private Vec3d angularMomentum = Vec3d.ZERO;
	private RigidBodyType rigidBodyType = RigidBodyType.NONE;
	private Quaternion orientation = new Quaternion(1, 0, 0, 0);
	private MeshObject meshObject;
	private boolean visible = false;
	private ArrayList<Force> forces = new ArrayList<>();
	private Camera camera = null;
	private CameraType cameraType;
	private ArrayList<Light> lights = new ArrayList<>();
	private ArrayList<GameObject> childComponents = new ArrayList<>();

	public GameObject build() {
		if (!rigidBodyType.equals(RigidBodyType.NONE)) {
			gameObjectFactory.getRigidBodies().add(new RigidBody(
					uuid,
					mass,
					dimensions,
					position,
					orientation,
					linearMomentum,
					angularMomentum,
					rigidBodyType,
					forces
			));
		}
		if (visible) {
			gameObjectFactory.getToRender().add(uuid);
		}
		return new GameObject();
	}

	public GameObjectBuilder(GameObjectFactory gameObjectFactory) {
		this.gameObjectFactory = gameObjectFactory;
	}

	public GameObjectBuilder setRigidBody(RigidBodyType rigidBodyType) {
		this.rigidBodyType = rigidBodyType;
		return this;
	}

	public GameObjectBuilder setMass(double mass) {
		this.mass = mass;
		return this;
	}

	public GameObjectBuilder setDimensions(Vec3d dimensions) {
		this.dimensions = dimensions;
		return this;
	}

	public GameObjectBuilder setPosition(Vec3d position) {
		this.position = position;
		return this;
	}

	public GameObjectBuilder setLinearMomentum(Vec3d linearMomentum) {
		this.linearMomentum = linearMomentum;
		return this;
	}

	public GameObjectBuilder setAngularMomentum(Vec3d angularMomentum) {
		this.angularMomentum = angularMomentum;
		return this;
	}

	public GameObjectBuilder setRigidBodyType(RigidBodyType rigidBodyType) {
		this.rigidBodyType = rigidBodyType;
		return this;
	}

	public GameObjectBuilder setOrientation(Quaternion orientation) {
		this.orientation = orientation;
		return this;
	}

	public GameObjectBuilder setMeshObject(MeshObject meshObject) {
		this.meshObject = meshObject;
		return this;
	}

	public GameObjectBuilder setChildComponents(ArrayList<GameObject> childComponents) {
		this.childComponents = childComponents;
		return this;
	}

	public GameObjectBuilder setVisible(boolean visible) {
		this.visible = visible;
		return this;
	}

	public GameObjectBuilder setForces(ArrayList<Force> forces) {
		this.forces = forces;
		return this;
	}

	public GameObjectBuilder addForce(Force force) {
		this.forces.add(force);
		return this;
	}

	public GameObjectBuilder setCamera(Camera camera, CameraType cameraType) {
		this.camera = camera;
		this.cameraType = cameraType;
		return this;
	}

	public GameObjectBuilder addLight(Light light) {
		lights.add(light);
		return this;
	}

	public GameObjectFactory getGameObjectFactory() {
		return gameObjectFactory;
	}

	public UUID getUuid() {
		return uuid;
	}

	public double getMass() {
		return mass;
	}

	public Vec3d getDimensions() {
		return dimensions;
	}

	public Vec3d getPosition() {
		return position;
	}

	public Vec3d getLinearMomentum() {
		return linearMomentum;
	}

	public Vec3d getAngularMomentum() {
		return angularMomentum;
	}

	public RigidBodyType getRigidBodyType() {
		return rigidBodyType;
	}

	public Quaternion getOrientation() {
		return orientation;
	}

	public MeshObject getMeshObject() {
		return meshObject;
	}

	public boolean isVisible() {
		return visible;
	}

	public ArrayList<Force> getForces() {
		return forces;
	}

	public Camera getCamera() {
		return camera;
	}

	public CameraType getCameraType() {
		return cameraType;
	}

	public ArrayList<Light> getLights() {
		return lights;
	}

	public ArrayList<GameObject> getChildComponents() {
		return childComponents;
	}
}
