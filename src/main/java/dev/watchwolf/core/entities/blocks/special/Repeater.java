package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class Repeater extends Block implements Orientable, Delayable, Lockable, Powerable {
	/*   --- ORIENTABLE INTERFACE ---   */
	@RelevantBlockData
	private final Map<Orientable.Orientation,Boolean> orientation = new HashMap<>();
	@Override
	public boolean isOrientationSet(Orientable.Orientation o) throws IllegalArgumentException {
		Boolean result = this.orientation.get(o);
		return result;
	}
	@Override

	public Orientable setOrientation(Orientable.Orientation o, boolean value) throws IllegalArgumentException {
		Repeater current = new Repeater(this);
		current.orientation.put(o, value);
		return current;
	}
	@Override

	public Set<Orientable.Orientation> getValidOrientations() {
		return this.orientation.keySet();
	}
	/*   --- DELAYABLE INTERFACE ---   */
	private int delayMinusOne;
	@Override

	public Delayable setDelay(int delay) throws IllegalArgumentException {
		if (delay < 1 || delay > 4) throw new IllegalArgumentException("Repeater block only allows delay from 1 to 4");
		Repeater current = new Repeater(this);
		current.delayMinusOne = delay - 1;
		return current;
	}
	@RelevantBlockData
	@Override

	public int getDelay() {
		return this.delayMinusOne + 1;
	}
	/*   --- IGNITABLE INTERFACE ---   */
	@RelevantBlockData
	private boolean locked;
	@Override
	public boolean isLocked() {
		return this.locked;
	}
	@Override

	public Lockable setLocked(boolean val) {
		Repeater current = new Repeater(this);
		current.locked = val;
		return current;
	}
	/*   --- POWERABLE INTERFACE ---   */
	@RelevantBlockData
	private boolean powered;
	@Override
	public boolean isPowered() {
		return this.powered;
	}
	@Override

	public Powerable setPowered(boolean val) {
		Repeater current = new Repeater(this);
		current.powered = val;
		return current;
	}

	/*   --- CONSTRUCTORS ---   */
	public Repeater(short id) {
		super(id, "REPEATER", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
		this.orientation.put(Orientable.Orientation.U, false);
		this.orientation.put(Orientable.Orientation.S, false);
		this.orientation.put(Orientable.Orientation.N, false);
		this.orientation.put(Orientable.Orientation.D, false);
		this.orientation.put(Orientable.Orientation.W, false);
		this.orientation.put(Orientable.Orientation.E, false);
		this.delayMinusOne = 0; // 0 is 1
		this.locked = false;
		this.powered = false;
	}

	public Repeater(int id) {
		this((short) id);
	}

	private Repeater(Repeater old) {
		this(old.id);
		this.orientation.putAll(old.orientation);
		this.delayMinusOne = old.delayMinusOne;
		this.locked = old.locked;
		this.powered = old.powered;
	}

}