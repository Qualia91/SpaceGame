package com.nick.wood.space_game.game;

import com.nick.wood.graphics_library_3d.objects.Transform;
import com.nick.wood.graphics_library_3d.objects.mesh_objects.MeshBuilder;
import com.nick.wood.graphics_library_3d.objects.mesh_objects.TextItem;
import com.nick.wood.graphics_library_3d.objects.scene_graph_objects.MeshSceneGraph;
import com.nick.wood.graphics_library_3d.objects.scene_graph_objects.SceneGraph;
import com.nick.wood.graphics_library_3d.objects.scene_graph_objects.SceneGraphNode;
import com.nick.wood.graphics_library_3d.objects.scene_graph_objects.TransformSceneGraph;
import com.nick.wood.maths.objects.matrix.Matrix4f;
import com.nick.wood.maths.objects.vector.Vec3f;

import java.util.HashMap;
import java.util.UUID;

import static com.nick.wood.graphics_library_3d.objects.mesh_objects.MeshType.TEXT;

public class Hud {

	private final TransformSceneGraph hudTransformObject;
	private final TransformSceneGraph hudTransformGameObjectLeft;
	private final TransformSceneGraph hudTransformGameObjectTopMiddle;
	private final TransformSceneGraph hudTransformGameObjectRight;
	private final TextItem linearTextItem;
	private final TextItem angularTextItem;
	private final TextItem informationTextItem;
	private final HashMap<UUID, SceneGraph>  rootGameObjectHashMap;

	public Hud() {

		SceneGraph sceneGraph = new SceneGraph();

		Transform hudTransform = new Transform(
				new Vec3f(0, 0,  0),
				new Vec3f(1, 1, 1),
				Matrix4f.Identity
		);

		this.hudTransformObject = new TransformSceneGraph(sceneGraph, hudTransform);

		Transform hudTransformLeft = new Transform(
				new Vec3f(-2, 2.5f, -1),
				new Vec3f(1, 2, 1),
				Matrix4f.Rotation(-90, Vec3f.X).multiply(Matrix4f.Rotation(-20, Vec3f.Z))
		);
		this.hudTransformGameObjectLeft = new TransformSceneGraph(hudTransformObject, hudTransformLeft);

		this.linearTextItem = createHudItem(hudTransformGameObjectLeft, Vec3f.Y.scale(0.1f));
		this.angularTextItem = createHudItem(hudTransformGameObjectLeft, Vec3f.ZERO);

		Transform hudTransformTopMiddle = new Transform(
				new Vec3f(-2, 0.8f, 2.8f),
				new Vec3f(2, 4, 2),
				Matrix4f.Rotation(-90, Vec3f.X)
		);
		this.hudTransformGameObjectTopMiddle = new TransformSceneGraph(hudTransformObject, hudTransformTopMiddle);
		this.informationTextItem = createHudItem(hudTransformGameObjectTopMiddle, Vec3f.ZERO);

		Transform hudTransformRight = new Transform(
				new Vec3f(-3, -1.2f, 0),
				new Vec3f(0.05f, 0.05f, 0.05f),
				Matrix4f.Rotation(20, Vec3f.Z)
		);
		this.hudTransformGameObjectRight = new TransformSceneGraph(hudTransformObject, hudTransformRight);

		this.rootGameObjectHashMap = new HashMap<>();
		rootGameObjectHashMap.put(UUID.randomUUID(), sceneGraph);
	}

	public HashMap<UUID, SceneGraph>  getRootGameObjectHashMap() {
		return rootGameObjectHashMap;
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

	public TransformSceneGraph getHudTransformObject() {
		return hudTransformObject;
	}

	public TransformSceneGraph getHudTransformGameObjectLeft() {
		return hudTransformGameObjectLeft;
	}

	public TransformSceneGraph getHudTransformGameObjectTopMiddle() {
		return hudTransformGameObjectTopMiddle;
	}

	public TransformSceneGraph getHudTransformGameObjectRight() {
		return hudTransformGameObjectRight;
	}

	public TextItem getLinearTextItem() {
		return linearTextItem;
	}

	public TextItem getAngularTextItem() {
		return angularTextItem;
	}

	public TextItem getInformationTextItem() {
		return informationTextItem;
	}
}
