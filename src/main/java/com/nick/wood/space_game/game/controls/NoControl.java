package com.nick.wood.space_game.game.controls;

import com.nick.wood.graphics_library_3d.input.Control;
import com.nick.wood.maths.objects.matrix.Matrix4d;
import com.nick.wood.maths.objects.vector.Vec;
import com.nick.wood.maths.objects.vector.Vec3d;

import java.util.UUID;

public class NoControl implements Control {

	public NoControl() {
	}

	public void reset() {
	}

	public void mouseMove(double dx, double dy, boolean shiftPressed) {
	}

	@Override
	public void leftLinear() {

	}

	@Override
	public void rightLinear() {

	}

	@Override
	public void forwardLinear() {

	}

	@Override
	public void backLinear() {

	}

	@Override
	public void upLinear() {

	}

	@Override
	public void downLinear() {

	}

	@Override
	public void leftRoll() {

	}

	@Override
	public void rightRoll() {

	}

	@Override
	public void upPitch() {

	}

	@Override
	public void downPitch() {

	}

	@Override
	public void leftYaw() {

	}

	@Override
	public void rightYaw() {

	}


	public void action() {
	}

	@Override
	public void setObjectBeingControlled(Object objectBeingControlled) {

	}

	public UUID getUuid() {
		return null;
	}
}
