package com.nick.wood.space_game.game.controls;

import com.nick.wood.game_control.input.Control;
import com.nick.wood.game_control.input.ControlManager;
import com.nick.wood.hla_game_controller.Command;
import com.nick.wood.hla_game_controller.HlaInput;

public class HlaGameControlManager implements ControlManager<HlaInput> {

	private HlaInput input;
	private Control control;

	public HlaGameControlManager(HlaInput input, Control control) {
		this.input = input;
		this.control = control;
	}

	public HlaInput getInput() {
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

	public void setInputs(HlaInput input) {
		this.input = input;
	}

	public void setControl(Control control) {
		this.control = control;
	}
}
