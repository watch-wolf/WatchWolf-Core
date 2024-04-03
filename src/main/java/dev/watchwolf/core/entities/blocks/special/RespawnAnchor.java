package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class RespawnAnchor extends Block implements Staggered {
	/*   --- STAGGERED INTERFACE ---   */
	@RelevantBlockData
	private int stage;
	private final int maxStage, minStage;
	@Override

	public Staggered setStage(int stage) throws IllegalArgumentException {
		if (stage < this.getMinStage() || stage > this.getMaxStage()) throw new IllegalArgumentException("RespawnAnchor block only allows stages from " + this.getMinStage() + " to " + this.getMaxStage());
		RespawnAnchor current = new RespawnAnchor(this);
		current.stage = stage;
		return current;
	}
	@Override

	public int getStage() {
		return this.stage;
	}
	@Override

	public int getMaxStage() {
		return this.maxStage;
	}
	@Override

	public int getMinStage() {
		return this.minStage;
	}

	/*   --- CONSTRUCTORS ---   */
	public RespawnAnchor(short id) {
		super(id, "RESPAWN_ANCHOR", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
		this.stage = 0;
		this.maxStage = 4;
		this.minStage = 0;
	}

	public RespawnAnchor(int id) {
		this((short) id);
	}

	private RespawnAnchor(RespawnAnchor old) {
		this(old.id);
		this.stage = old.stage;
	}

}