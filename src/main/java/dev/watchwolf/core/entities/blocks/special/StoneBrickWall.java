package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class StoneBrickWall extends Block implements Orientable {
	/*   --- ORIENTABLE INTERFACE ---   */
	@RelevantBlockData
	private final Map<Orientable.Orientation,Boolean> orientation = new HashMap<>();
	@Override
	public boolean isOrientationSet(Orientable.Orientation o) throws IllegalArgumentException {
		Boolean result = this.orientation.get(o);
		if (result == null) throw new IllegalArgumentException("StoneBrickWall block doesn't contain orientation " + o.name());
		return result;
	}
	@Override

	public Orientable setOrientation(Orientable.Orientation o, boolean value) throws IllegalArgumentException {
		if (!this.orientation.containsKey(o)) throw new IllegalArgumentException("StoneBrickWall block doesn't contain orientation " + o.name());
		StoneBrickWall current = new StoneBrickWall(this);
		current.orientation.put(o, value);
		return current;
	}
	@Override

	public Set<Orientable.Orientation> getValidOrientations() {
		return this.orientation.keySet();
	}

	/*   --- CONSTRUCTORS ---   */
	public StoneBrickWall(short id) {
		super(id, "STONE_BRICK_WALL", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
		this.orientation.put(Orientable.Orientation.U, false);
		this.orientation.put(Orientable.Orientation.S, false);
		this.orientation.put(Orientable.Orientation.N, false);
		this.orientation.put(Orientable.Orientation.W, false);
		this.orientation.put(Orientable.Orientation.E, false);
	}

	public StoneBrickWall(int id) {
		this((short) id);
	}

	private StoneBrickWall(StoneBrickWall old) {
		this(old.id);
		this.orientation.putAll(old.orientation);
	}

}