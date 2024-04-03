package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class SmoothRedSandstoneSlab extends Block implements Orientable {
	/*   --- ORIENTABLE INTERFACE ---   */
	@RelevantBlockData
	private final Map<Orientable.Orientation,Boolean> orientation = new HashMap<>();
	@Override
	public boolean isOrientationSet(Orientable.Orientation o) throws IllegalArgumentException {
		Boolean result = this.orientation.get(o);
		if (result == null) throw new IllegalArgumentException("SmoothRedSandstoneSlab block doesn't contain orientation " + o.name());
		return result;
	}
	@Override

	public Orientable setOrientation(Orientable.Orientation o, boolean value) throws IllegalArgumentException {
		if (!this.orientation.containsKey(o)) throw new IllegalArgumentException("SmoothRedSandstoneSlab block doesn't contain orientation " + o.name());
		SmoothRedSandstoneSlab current = new SmoothRedSandstoneSlab(this);
		current.orientation.put(o, value);
		return current;
	}
	@Override

	public Set<Orientable.Orientation> getValidOrientations() {
		return this.orientation.keySet();
	}

	/*   --- CONSTRUCTORS ---   */
	public SmoothRedSandstoneSlab(short id) {
		super(id, "SMOOTH_RED_SANDSTONE_SLAB", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
		this.orientation.put(Orientable.Orientation.U, false);
		this.orientation.put(Orientable.Orientation.D, false);
	}

	public SmoothRedSandstoneSlab(int id) {
		this((short) id);
	}

	private SmoothRedSandstoneSlab(SmoothRedSandstoneSlab old) {
		this(old.id);
		this.orientation.putAll(old.orientation);
	}

}