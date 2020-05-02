package com.nick.wood.space_game.game.controls;

public class Action {

	private boolean active = false;
	private final ActionEnum actionEnum;

	public Action(ActionEnum actionEnum) {
		this.actionEnum = actionEnum;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isActive() {
		return active;
	}

	public ActionEnum getActionEnum() {
		return actionEnum;
	}
}
