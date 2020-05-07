package com.nick.wood.space_game.game.controls;

import com.nick.wood.game_control.input.Control;
import com.nick.wood.maths.objects.vector.Vec3d;
import com.nick.wood.physics.rigid_body_dynamics_verbose.RigidBody;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RigidBodyControl implements Control {

	private final double linearForce;
	private final double angularForce;
	private final UUID controlledRigidBodyUUID;
	private Vec3d force = Vec3d.ZERO;
	private Vec3d torque = Vec3d.ZERO;
	private final HashMap<RigidBodyActionEnum, Boolean> actions = new HashMap<>();

	public RigidBodyControl(double linearForce, double angularForce, UUID controlledRigidBodyUUID) {
		this.linearForce = linearForce;
		this.angularForce = angularForce;
		this.controlledRigidBodyUUID = controlledRigidBodyUUID;
		this.actions.put(RigidBodyActionEnum.SLOW_DOWN, false);
	}

	public void reset() {
		force = Vec3d.ZERO;
		torque = Vec3d.ZERO;
		this.actions.put(RigidBodyActionEnum.SLOW_DOWN, false);
	}

	public void mouseMove(double dx, double dy, boolean shiftPressed) {
	}

	public Vec3d getForce() {
		return force;
	}

	public Vec3d getTorque() {
		return torque;
	}

	public void leftLinear() {
		force = force.add(new Vec3d(0.0, linearForce, 0.0));
	}
	public void rightLinear() {
		force = force.add(new Vec3d(0.0, -linearForce, 0.0));
	}
	public void forwardLinear() {
		force = force.add(new Vec3d(linearForce, 0.0, 0.0));
	}
	public void backLinear() {
		force = force.add(new Vec3d(-linearForce, 0.0, 0.0));
	}
	public void upLinear() {
		force = force.add(new Vec3d(0.0, 0.0, linearForce));
	}
	public void downLinear() {
		force = force.add(new Vec3d(0.0, 0.0, -linearForce));
	}

	public void leftRoll() {
		torque = torque.add(new Vec3d(-angularForce, 0.0, 0.0));
	}
	public void rightRoll() {
		torque = torque.add(new Vec3d(angularForce, 0.0, 0.0));
	}
	public void upPitch() {
		torque = torque.add(new Vec3d(0.0, angularForce, 0.0));
	}
	public void downPitch() {
		torque = torque.add(new Vec3d(0.0, -angularForce, 0.0));
	}
	public void leftYaw() {
		torque = torque.add(new Vec3d(0.0, 0.0, angularForce));
	}
	public void rightYaw() {
		torque = torque.add(new Vec3d(0.0, 0.0, -angularForce));
	}

	public void action() {
		this.actions.put(RigidBodyActionEnum.SLOW_DOWN, true);
	}

	@Override
	public void setObjectBeingControlled(Object objectBeingControlled) {

	}

	public UUID getUuid() {
		return controlledRigidBodyUUID;
	}

	public HashMap<RigidBodyActionEnum, Boolean> getActions() {
		return actions;
	}

	public void apply(RigidBody rigidBody) {
		rigidBody.addForce(rigidBody.getRotation().toMatrix().rotate(force));
		rigidBody.addTorque(rigidBody.getRotation().toMatrix().rotate(torque));
	}

	public void preformAction(RigidBody rigidBody, RigidBodyActionEnum rigidBodyActionEnum) {
		switch (rigidBodyActionEnum) {
			case SLOW_DOWN -> rigidBody.slowDown();
		}
	}

	public void preformActions(RigidBody rigidBody) {
		for (Map.Entry<RigidBodyActionEnum, Boolean> rigidBodyActionEnumBooleanEntry : actions.entrySet()) {
			if (rigidBodyActionEnumBooleanEntry.getValue()) {
				preformAction(rigidBody, rigidBodyActionEnumBooleanEntry.getKey());
			}
		}
	}
}
