package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class PolishedDioriteSlab extends Block implements Orientable {
	/*   --- ORIENTABLE INTERFACE ---   */
	@RelevantBlockData
	private final Map<Orientable.Orientation,Boolean> orientation = new HashMap<>();
	@Override
	public boolean isOrientationSet(Orientable.Orientation o) throws IllegalArgumentException {
		Boolean result = this.orientation.get(o);
		if (result == null) throw new IllegalArgumentException("PolishedDioriteSlab block doesn't contain orientation " + o.name());
		return result;
	}
	@Override

	public Orientable setOrientation(Orientable.Orientation o, boolean value) throws IllegalArgumentException {
		if (!this.orientation.containsKey(o)) throw new IllegalArgumentException("PolishedDioriteSlab block doesn't contain orientation " + o.name());
		PolishedDioriteSlab current = new PolishedDioriteSlab(this);
		current.orientation.put(o, value);
		return current;
	}
	@Override

	public Set<Orientable.Orientation> getValidOrientations() {
		return this.orientation.keySet();
	}

	/*   --- CONSTRUCTORS ---   */
	public PolishedDioriteSlab(short id) {
		super(id, "POLISHED_DIORITE_SLAB", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
		this.orientation.put(Orientable.Orientation.U, false);
		this.orientation.put(Orientable.Orientation.D, false);
	}

	public PolishedDioriteSlab(int id) {
		this((short) id);
	}

	private PolishedDioriteSlab(PolishedDioriteSlab old) {
		this(old.id);
		this.orientation.putAll(old.orientation);
	}

}