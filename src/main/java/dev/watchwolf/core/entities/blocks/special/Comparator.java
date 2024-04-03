package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class Comparator extends Block implements Orientable, Stateful, Powerable {
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
		Comparator current = new Comparator(this);
		current.orientation.put(o, value);
		return current;
	}
	@Override

	public Set<Orientable.Orientation> getValidOrientations() {
		return this.orientation.keySet();
	}
	/*   --- STATEFUL INTERFACE ---   */
	@RelevantBlockData
	private Stateful.Mode state;
	private final HashSet<Stateful.Mode> allowedStates = new HashSet<>();
	@Override
	public Stateful.Mode getMode() {
		return this.state;
	}
	@Override

	public Stateful setMode(Stateful.Mode mode) throws IllegalArgumentException {
		if (!this.allowedStates.contains(mode)) throw new IllegalArgumentException("Comparator block doesn't allow the state " + mode.name());
		Comparator current = new Comparator(this);
		current.state = mode;
		return current;
	}
	@Override
	public Set<Stateful.Mode> getValidModes() {
		return (HashSet)this.allowedStates.clone();
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
		Comparator current = new Comparator(this);
		current.powered = val;
		return current;
	}

	/*   --- CONSTRUCTORS ---   */
	public Comparator(short id) {
		super(id, "COMPARATOR", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
		this.orientation.put(Orientable.Orientation.U, false);
		this.orientation.put(Orientable.Orientation.S, false);
		this.orientation.put(Orientable.Orientation.N, false);
		this.orientation.put(Orientable.Orientation.D, false);
		this.orientation.put(Orientable.Orientation.W, false);
		this.orientation.put(Orientable.Orientation.E, false);
		this.allowedStates.add(Stateful.Mode.SUBTRACT);
		this.allowedStates.add(Stateful.Mode.COMPARE);
		this.state = Stateful.Mode.SUBTRACT;
		this.powered = false;
	}

	public Comparator(int id) {
		this((short) id);
	}

	private Comparator(Comparator old) {
		this(old.id);
		this.orientation.putAll(old.orientation);
		this.state = old.state;
		this.powered = old.powered;
	}

}