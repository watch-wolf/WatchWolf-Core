package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class ChainCommandBlock extends Block implements Orientable, Conditionable {
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
		ChainCommandBlock current = new ChainCommandBlock(this);
		current.orientation.put(o, value);
		return current;
	}
	@Override

	public Set<Orientable.Orientation> getValidOrientations() {
		return this.orientation.keySet();
	}
	/*   --- CONDITIONABLE INTERFACE ---   */
	@RelevantBlockData
	private boolean conditionable;
	@Override
	public boolean isConditional() {
		return this.conditionable;
	}
	@Override

	public Conditionable setConditional(boolean val) {
		ChainCommandBlock current = new ChainCommandBlock(this);
		current.conditionable = val;
		return current;
	}

	/*   --- CONSTRUCTORS ---   */
	public ChainCommandBlock(short id) {
		super(id, "CHAIN_COMMAND_BLOCK", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
		this.orientation.put(Orientable.Orientation.U, false);
		this.orientation.put(Orientable.Orientation.S, false);
		this.orientation.put(Orientable.Orientation.N, false);
		this.orientation.put(Orientable.Orientation.D, false);
		this.orientation.put(Orientable.Orientation.W, false);
		this.orientation.put(Orientable.Orientation.E, false);
		this.conditionable = false;
	}

	public ChainCommandBlock(int id) {
		this((short) id);
	}

	private ChainCommandBlock(ChainCommandBlock old) {
		this(old.id);
		this.orientation.putAll(old.orientation);
		this.conditionable = old.conditionable;
	}

}