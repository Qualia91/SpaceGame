package com.nick.wood.space_game.game.game_objects;

import java.util.UUID;

public class GameObject {

	private final UUID uuid = UUID.randomUUID();

	public UUID getUuid() {
		return uuid;
	}

}
