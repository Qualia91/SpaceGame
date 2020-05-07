package com.nick.wood.space_game.game.controls;

import com.nick.wood.game_control.input.Action;

public class RigidBodyActionAction implements Action {

	private boolean active = false;
	private final RigidBodyActionEnum rigidBodyActionEnum;

	public RigidBodyActionAction(RigidBodyActionEnum rigidBodyActionEnum) {
		this.rigidBodyActionEnum = rigidBodyActionEnum;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isActive() {
		return active;
	}

	public RigidBodyActionEnum getRigidBodyActionEnum() {
		return rigidBodyActionEnum;
	}
}
