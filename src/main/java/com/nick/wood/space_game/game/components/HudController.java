package com.nick.wood.space_game.game.components;

import com.nick.wood.graphics_library_3d.lighting.PointLight;
import com.nick.wood.graphics_library_3d.objects.Transform;
import com.nick.wood.graphics_library_3d.objects.mesh_objects.MeshBuilder;
import com.nick.wood.graphics_library_3d.objects.scene_graph_objects.*;
import com.nick.wood.graphics_library_3d.objects.mesh_objects.MeshObject;
import com.nick.wood.maths.objects.matrix.Matrix4f;
import com.nick.wood.maths.objects.vector.Vec3d;
import com.nick.wood.maths.objects.vector.Vec3f;
import com.nick.wood.physics.Body;
import com.nick.wood.space_game.game.Hud;

import java.util.ArrayList;
import java.util.UUID;

import static com.nick.wood.graphics_library_3d.objects.scene_graph_objects.RenderObjectType.TRANSFORM;

public class HudController {
	private static final float MAX_DIST  = 1000.0f;

	private final UUID miniMapUUID = UUID.randomUUID();
	private final UUID centerMiniMapOn;
	private final Hud hud;

	private MeshObject miniMapItemMesh;
	private MeshObject miniMapPlayerMesh;
	private MeshObject background;
	private PointLight pointLight;

	private final ArrayList<UUID> toRender;
	private SceneGraphNode parent;

	public HudController(Hud hud,
	                     UUID centerMiniMapOn,
	                     ArrayList<UUID> toRender) {
		this.toRender = toRender;
		this.centerMiniMapOn = centerMiniMapOn;
		this.hud = hud;
	}

	public UUID getMiniMapUUID() {
		return miniMapUUID;
	}

	public void buildMiniMapMesh() {
		this.miniMapItemMesh = createMeshObject(1, false, "/textures/white.png");
		this.miniMapPlayerMesh = createMeshObject(10, false, "/textures/white.png");
		this.background = createMeshObject(10, true, "/textures/2k_neptune.jpg");
		this.pointLight = new PointLight(
				new Vec3f(253.0f/255.0f, 208.0f/255.0f, 35.0f/255.0f),
				0.5);
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

	public void createMiniMap(ArrayList<? extends Body> rootGameObjects) {

		// todo pretty sure im about to cause a memory leak
		hud.getHudTransformGameObjectRight().getSceneGraphNodeData().getChildren().clear();
		// ye that one. the children will still have a ref toi the parent. dont know if GC will still get rid of it

		// find rigid body to center on first
		Vec3d centerOnPosition = Vec3d.ZERO;
		Matrix4f centerOnOrientation = Matrix4f.Identity;
		for (Body rootGameObject : rootGameObjects) {
			if (rootGameObject.getUuid().equals(centerMiniMapOn)) {
				centerOnPosition = (Vec3d) rootGameObject.getOrigin();
				centerOnOrientation = rootGameObject.getRotation().inverse().toMatrix().toMatrix4f();
				MeshSceneGraph meshSceneGraph = new MeshSceneGraph(hud.getHudTransformGameObjectRight(), miniMapPlayerMesh);

				LightSceneGraph lightSceneGraph = new LightSceneGraph(
						hud.getHudTransformGameObjectRight(),
						pointLight
				);

			}
		}

		// now create minimap centered on that position
		for (Body rootGameObject : rootGameObjects) {

			if (!rootGameObject.getUuid().equals(centerMiniMapOn)) {

				if (toRender.contains(rootGameObject.getUuid())) {

					Vec3f vectorToEnemy = (Vec3f) rootGameObject.getOrigin().subtract(centerOnPosition).scale(0.1).toVecf();
					float distanceAway = vectorToEnemy.length2();

					if (distanceAway < MAX_DIST) {

						Transform transform = new Transform(
								centerOnOrientation.multiply(vectorToEnemy),
								Vec3f.ONE.scale((MAX_DIST - distanceAway) / MAX_DIST),
								rootGameObject.getRotation().toMatrix().toMatrix4f()
						);

						TransformSceneGraph transformSceneGraph = new TransformSceneGraph(
								hud.getHudTransformGameObjectRight(),
								transform
						);

						MeshSceneGraph meshSceneGraph = new MeshSceneGraph(transformSceneGraph, miniMapItemMesh);

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

	public UUID getCenterMiniMapOn() {
		return centerMiniMapOn;
	}

	public Hud getHud() {
		return hud;
	}
}
