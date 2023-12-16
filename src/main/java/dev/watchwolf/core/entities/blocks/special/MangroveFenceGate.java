package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class MangroveFenceGate extends Block implements Orientable, Openable, Powerable {
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
		MangroveFenceGate current = new MangroveFenceGate(this);
		current.orientation.put(o, value);
		return current;
	}
	@Override

	public Set<Orientable.Orientation> getValidOrientations() {
		return this.orientation.keySet();
	}
	/*   --- OPENABLE INTERFACE ---   */
	@RelevantBlockData
	private boolean isOpen;
	@Override

	public Openable setOpened(boolean opened) {
		MangroveFenceGate current = new MangroveFenceGate(this);
		current.isOpen = opened;
		return current;
	}
	@Override

	public boolean isOpened() {
		return this.isOpen;
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
		MangroveFenceGate current = new MangroveFenceGate(this);
		current.powered = val;
		return current;
	}

	/*   --- CONSTRUCTORS ---   */
	public MangroveFenceGate(short id) {
		super(id, "MANGROVE_FENCE_GATE", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
		this.orientation.put(Orientable.Orientation.U, false);
		this.orientation.put(Orientable.Orientation.S, false);
		this.orientation.put(Orientable.Orientation.N, false);
		this.orientation.put(Orientable.Orientation.D, false);
		this.orientation.put(Orientable.Orientation.W, false);
		this.orientation.put(Orientable.Orientation.E, false);
		this.isOpen = false;
		this.powered = false;
	}

	public MangroveFenceGate(int id) {
		this((short) id);
	}

	private MangroveFenceGate(MangroveFenceGate old) {
		this(old.id);
		this.orientation.putAll(old.orientation);
		this.isOpen = old.isOpen;
		this.powered = old.powered;
	}

}