package com.nick.wood.space_game.game.controls;

import com.nick.wood.game_control.input.Control;
import com.nick.wood.game_control.input.ControlManager;

public class HlaGameControlManager implements ControlManager<ExternalInput> {

	private ExternalInput input;
	private Control control;
	private double oldMouseX = 0.0;
	private double oldMouseY = 0.0;

	public HlaGameControlManager(ExternalInput input, Control control) {
		this.input = input;
		this.control = control;
	}

	public ExternalInput getInput() {
		return input;
	}

	public Control getControl() {
		return control;
	}

	public void checkInputs() {

		for (Command command : input.getCommands()) {
			control.forwardLinear();
		}

	}

	public void setInputs(ExternalInput input) {
		this.input = input;
	}

	public void setControl(Control control) {
		this.control = control;
	}
}
