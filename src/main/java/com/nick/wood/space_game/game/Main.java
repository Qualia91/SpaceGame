package com.nick.wood.space_game.game;

import java.util.concurrent.ExecutionException;

public class Main {

	public static void main(String[] args) {

		SimulationTest simulationTest = new SimulationTest();

		try {
			simulationTest.randomBox();
		} catch (ExecutionException | InterruptedException e) {
			e.printStackTrace();
		}

	}
}
