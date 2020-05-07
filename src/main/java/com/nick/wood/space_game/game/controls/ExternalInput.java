package com.nick.wood.space_game.game.controls;


import com.nick.wood.game_control.input.Input;

import java.util.ArrayList;

public class ExternalInput implements Input {
	ArrayList<Command> commands = new ArrayList<>();

	public ExternalInput() {
		this.commands.add(new Command());
	}

	public ArrayList<Command> getCommands() {
		return commands;
	}
}
