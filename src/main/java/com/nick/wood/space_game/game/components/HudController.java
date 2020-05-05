package com.nick.wood.space_game.game.components;

import com.nick.wood.graphics_library_3d.lighting.Light;
import com.nick.wood.graphics_library_3d.lighting.PointLight;
import com.nick.wood.graphics_library_3d.objects.mesh_objects.MeshBuilder;
import com.nick.wood.graphics_library_3d.objects.scene_graph_objects.*;
import com.nick.wood.graphics_library_3d.objects.mesh_objects.MeshObject;
import com.nick.wood.graphics_library_3d.objects.mesh_objects.SphereMesh;
import com.nick.wood.graphics_library_3d.objects.mesh_objects.TextItem;
import com.nick.wood.maths.objects.matrix.Matrix4f;
import com.nick.wood.maths.objects.vector.Vec3d;
import com.nick.wood.maths.objects.vector.Vec3f;
import com.nick.wood.physics.Body;
import com.nick.wood.physics.rigid_body_dynamics_verbose.RigidBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import static com.nick.wood.graphics_library_3d.objects.scene_graph_objects.RenderObjectType.TRANSFORM;

public class HudController {

	private final TextItem linearVelocityTextItem;
	private final TextItem angularVelocityTextItem;
	private final TextItem slowDownTextItem;
	private final UUID rigidBodyUUID;
	private final TransformSceneGraph miniMapContainer;
	private final UUID miniMapUUID = UUID.randomUUID();
	private Matrix4f inverseTransformation = Matrix4f.Identity;

	private MeshObject miniMapItemMesh;
	private MeshObject miniMapPlayerMesh;
	private MeshObject background;

	private final ArrayList<UUID> toRender;
	private HashMap<UUID, RenderObject<MeshObject>> miniMapMeshes = new HashMap<>();
	private HashMap<UUID, RenderObject<Light>> hudLights = new HashMap<>();
	private PointLight pointLight;

	public HudController(TextItem linearVelocityTextItem,
	                     TextItem angularVelocityTextItem,
	                     TextItem slowDownTextItem,
	                     TransformSceneGraph miniMapContainer,
	                     UUID rigidBodyUUID,
	                     ArrayList<UUID> toRender) {
		this.linearVelocityTextItem = linearVelocityTextItem;
		this.angularVelocityTextItem = angularVelocityTextItem;
		this.slowDownTextItem = slowDownTextItem;
		this.rigidBodyUUID = rigidBodyUUID;
		this.miniMapContainer = miniMapContainer;
		this.toRender = toRender;
	}

	public UUID getMiniMapUUID() {
		return miniMapUUID;
	}

	public TextItem getSlowDownTextItem() {
		return slowDownTextItem;
	}

	public UUID getRigidBodyUUID() {
		return rigidBodyUUID;
	}

	public TextItem getLinearVelocityTextItem() {
		return linearVelocityTextItem;
	}

	public TextItem getAngularVelocityTextItem() {
		return angularVelocityTextItem;
	}

	public TransformSceneGraph getMiniMapContainer() {
		return miniMapContainer;
	}

	public void passPlayerInverseTransform(Matrix4f inverseTransformation) {
		this.inverseTransformation = inverseTransformation;
	}

	public Matrix4f getInverseTransformation() {
		return inverseTransformation;
	}

	public void buildMiniMapMesh() {
		this.miniMapItemMesh = createMeshObject(1, false, "/textures/white.png");
		this.miniMapPlayerMesh = createMeshObject(10, true, "/textures/white.png");
		this.background = createMeshObject(10, true, "/textures/2k_neptune.jpg");
		this.pointLight = new PointLight(
				new Vec3f(253.0f/255.0f, 208.0f/255.0f, 35.0f/255.0f),
				1);
	}

	private MeshObject createMeshObject(int triangleNumber, boolean invertedNormals, String texture) {

		MeshObject meshObject = new MeshBuilder()
				.setTriangleNumber(triangleNumber)
				.setTexture(texture)
				.setInvertedNormals(invertedNormals)
				.build();

		meshObject.getMesh().create();

		return meshObject;
	}
/*
	public void createMiniMap(ArrayList<? extends Body> rootGameObjects) {
		Matrix4f transformationSoFar = findMiniMapTransform(getMiniMapContainer(), Matrix4f.Identity);

		this.miniMapMeshes.clear();
		this.hudLights.clear();

		// first find player rigid body


		//Light underLight = new PointLight(
		//		new Vec3f(1, 0, 0),
		//		10);
		//UUID underLightUUID = UUID.randomUUID();
		//RenderObject<Light> underLightRenderObject = new RenderObject<>(
		//		underLight,
		//		Matrix4f.Translation(new Vec3f(0, 0, -1)).add(transformationSoFar),
		//		underLightUUID);
		//hudLights.put(underLightUUID, underLightRenderObject);

		Matrix4f worldToPlayerSpace = Matrix4f.Identity;
		Vec3d playerPosition = Vec3d.ZERO;
		Matrix4f playerRotation = Matrix4f.Identity;
		for (Body rootGameObject : rootGameObjects) {
			if (rootGameObject.getUuid().equals(rigidBodyUUID)) {
				playerPosition = (Vec3d) rootGameObject.getOrigin();
				playerRotation = rootGameObject.getRotation().toMatrix().toMatrix4f();
				worldToPlayerSpace = Matrix4f.InverseTransformation(
						(Vec3f) rootGameObject.getOrigin().toVecf(),
						rootGameObject.getRotation().toMatrix().toMatrix4f(),
						Vec3f.ONE);

				UUID uuid = UUID.randomUUID();
				RenderObject<MeshObject> meshGroupRenderObject = new RenderObject<>(
						miniMapPlayerMesh,
						transformationSoFar,
						uuid);
				miniMapMeshes.put(uuid, meshGroupRenderObject);

				UUID lightUUID = UUID.randomUUID();
				RenderObject<Light> lightRenderObject = new RenderObject<>(
						pointLight,
						transformationSoFar,
						lightUUID);
				hudLights.put(lightUUID, lightRenderObject);
			}
		}

		UUID miniMapBackgroundUUID = UUID.randomUUID();
		RenderObject<MeshObject> backgroundRenderObject = new RenderObject<>(
				background,
				Matrix4f.Transform(Vec3f.ZERO, Matrix4f.Identity, Vec3f.ONE.scale(20)).multiply(playerRotation.invert()).multiply(transformationSoFar),
				miniMapBackgroundUUID);
		miniMapMeshes.put(miniMapBackgroundUUID, backgroundRenderObject);

		// now create minimap with
		for (Body body : rootGameObjects) {

			if (!body.getUuid().equals(rigidBodyUUID)) {

				if (toRender.contains(body.getUuid())) {

					RigidBody rigidBody = (RigidBody) body;

					double length2 = playerPosition.subtract(rigidBody.getOrigin()).length2();

					if (length2 < 10000) {

						Vec3f newPosition = worldToPlayerSpace.multiply((Vec3f) rigidBody.getOrigin().toVecf()).scale(0.1f);

						float scale = 1.0f;
						if (newPosition.length2() != 0) {
							scale = 1.0f / (float)Math.sqrt(newPosition.length());
						}
						if (scale > 1) {
							scale = 1;
						}

						Matrix4f rigidBodyTransform = Matrix4f.Transform(
								newPosition,
								rigidBody.getRotation().toMatrix().toMatrix4f(),
								Vec3f.ONE.scale(scale)
						).multiply(transformationSoFar);

						UUID uuid = UUID.randomUUID();
						RenderObject<MeshObject> meshGroupRenderObject = new RenderObject<>(
								miniMapItemMesh,
								rigidBodyTransform,
								uuid);
						miniMapMeshes.put(uuid, meshGroupRenderObject);

					}
				}
			}
		}

	}
*/
	public void createMiniMap(ArrayList<? extends Body> rootGameObjects) {
		Matrix4f transformationSoFar = findMiniMapTransform(getMiniMapContainer(), Matrix4f.Identity);

		Matrix4f worldToPlayerSpace = Matrix4f.Identity;
		Vec3d playerPosition = Vec3d.ZERO;
		Matrix4f playerRotation = Matrix4f.Identity;
		for (Body rootGameObject : rootGameObjects) {
			if (rootGameObject.getUuid().equals(rigidBodyUUID)) {
				playerPosition = (Vec3d) rootGameObject.getOrigin();
				playerRotation = rootGameObject.getRotation().toMatrix().toMatrix4f();
				worldToPlayerSpace = Matrix4f.InverseTransformation(
						(Vec3f) rootGameObject.getOrigin().toVecf(),
						rootGameObject.getRotation().toMatrix().toMatrix4f(),
						Vec3f.ONE);

				UUID uuid = UUID.randomUUID();
				RenderObject<MeshObject> meshGroupRenderObject = new RenderObject<>(
						miniMapPlayerMesh,
						transformationSoFar,
						uuid);
				miniMapMeshes.put(uuid, meshGroupRenderObject);

				UUID lightUUID = UUID.randomUUID();
				RenderObject<Light> lightRenderObject = new RenderObject<>(
						pointLight,
						transformationSoFar,
						lightUUID);
				hudLights.put(lightUUID, lightRenderObject);
			}
		}

		UUID miniMapBackgroundUUID = UUID.randomUUID();
		RenderObject<MeshObject> backgroundRenderObject = new RenderObject<>(
				background,
				Matrix4f.Transform(Vec3f.ZERO, Matrix4f.Identity, Vec3f.ONE.scale(20)).multiply(playerRotation.invert()).multiply(transformationSoFar),
				miniMapBackgroundUUID);
		miniMapMeshes.put(miniMapBackgroundUUID, backgroundRenderObject);

		// now create minimap with
		for (Body body : rootGameObjects) {

			if (!body.getUuid().equals(rigidBodyUUID)) {

				if (toRender.contains(body.getUuid())) {

					RigidBody rigidBody = (RigidBody) body;

					double length2 = playerPosition.subtract(rigidBody.getOrigin()).length2();

					if (length2 < 10000) {

						Vec3f newPosition = worldToPlayerSpace.multiply((Vec3f) rigidBody.getOrigin().toVecf()).scale(0.1f);

						float scale = 1.0f;
						if (newPosition.length2() != 0) {
							scale = 1.0f / (float)Math.sqrt(newPosition.length());
						}
						if (scale > 1) {
							scale = 1;
						}

						Matrix4f rigidBodyTransform = Matrix4f.Transform(
								newPosition,
								rigidBody.getRotation().toMatrix().toMatrix4f(),
								Vec3f.ONE.scale(scale)
						).multiply(transformationSoFar);

						UUID uuid = UUID.randomUUID();
						RenderObject<MeshObject> meshGroupRenderObject = new RenderObject<>(
								miniMapItemMesh,
								rigidBodyTransform,
								uuid);
						miniMapMeshes.put(uuid, meshGroupRenderObject);

					}
				}
			}
		}

	}

	private Matrix4f findMiniMapTransform(SceneGraphNode sceneGraphNode, Matrix4f transformSoFar) {

		if (sceneGraphNode.getSceneGraphNodeData().getType() == TRANSFORM) {
			TransformSceneGraph transformGameObject = (TransformSceneGraph) sceneGraphNode;
			transformSoFar = transformSoFar.multiply(transformGameObject.getTransformForRender());
		}

		if (sceneGraphNode.getSceneGraphNodeData().getParent() != null) {
			return findMiniMapTransform(sceneGraphNode.getSceneGraphNodeData().getParent(), transformSoFar);
		} else {
			return transformSoFar;
		}
	}

	private boolean isAvailableRenderData(SceneGraphNodeData sceneGraphNodeData) {
		return sceneGraphNodeData.containsMeshes() || sceneGraphNodeData.containsCameras() || sceneGraphNodeData.containsLights();
	}

	public HashMap<UUID, RenderObject<Light>> getHudLights() {
		return hudLights;
	}

	public HashMap<UUID, RenderObject<MeshObject>> getMiniMapMeshes() {
		return miniMapMeshes;
	}

	public HashMap<UUID, SceneGraph> getHud() {
		return new HashMap<>();
	}
}
