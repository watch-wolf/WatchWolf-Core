package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class StrippedSpruceLog extends Block implements Directionable {
	/*   --- DIRECTIONABLE INTERFACE ---   */
	@RelevantBlockData
	private Directionable.Direction direction;
	private final HashSet<Directionable.Direction> allowedDirections = new HashSet<>();
	@Override
	public Directionable.Direction getFacingDirection() {
		return this.direction;
	}
	@Override

	public Directionable setDirection(Directionable.Direction d) throws IllegalArgumentException {
		if (!this.allowedDirections.contains(d)) throw new IllegalArgumentException("StrippedSpruceLog block doesn't allow the direction " + d.name());
		StrippedSpruceLog current = new StrippedSpruceLog(this);
		current.direction = d;
		return current;
	}
	@Override
	public Set<Directionable.Direction> getValidDirections() {
		return (HashSet)this.allowedDirections.clone();
	}

	/*   --- CONSTRUCTORS ---   */
	public StrippedSpruceLog(short id) {
		super(id, "STRIPPED_SPRUCE_LOG", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
		this.allowedDirections.add(Directionable.Direction.Y);
		this.allowedDirections.add(Directionable.Direction.X);
		this.allowedDirections.add(Directionable.Direction.Z);
		this.direction = Directionable.Direction.Y;
	}

	public StrippedSpruceLog(int id) {
		this((short) id);
	}

	private StrippedSpruceLog(StrippedSpruceLog old) {
		this(old.id);
		this.direction = old.direction;
	}

}