package com.nick.wood.space_game.game.game_objects;

import com.nick.wood.graphics_library_3d.objects.scene_graph_objects.SceneGraph;
import com.nick.wood.physics.rigid_body_dynamics_verbose.RigidBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class GameObjectFactory {

	private final ArrayList<RigidBody> rigidBodies = new ArrayList<>();
	private final ArrayList<UUID> toRender = new ArrayList<>();
	private final HashMap<UUID, SceneGraph> rootGameObjectHashMap = new HashMap<>();

	public GameObjectBuilder createGameObjectBuilder() {
		return new GameObjectBuilder(this);
	}

	public ArrayList<RigidBody> getRigidBodies() {
		return rigidBodies;
	}

	public ArrayList<UUID> getToRender() {
		return toRender;
	}

	public HashMap<UUID, SceneGraph> getRootGameObjectHashMap() {
		return rootGameObjectHashMap;
	}
}
