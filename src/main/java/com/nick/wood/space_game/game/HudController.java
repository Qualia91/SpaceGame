package com.nick.wood.space_game.game;

import com.nick.wood.graphics_library_3d.objects.mesh_objects.TextItem;

import java.util.UUID;

public class HudController {

	private final TextItem linearVelocityTextItem;
	private final TextItem angularVelocityTextItem;
	private final UUID rigidBodyUUID;

	public HudController(TextItem linearVelocityTextItem, TextItem angularVelocityTextItem, UUID rigidBodyUUID) {
		this.linearVelocityTextItem = linearVelocityTextItem;
		this.angularVelocityTextItem = angularVelocityTextItem;
		this.rigidBodyUUID = rigidBodyUUID;
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
}
