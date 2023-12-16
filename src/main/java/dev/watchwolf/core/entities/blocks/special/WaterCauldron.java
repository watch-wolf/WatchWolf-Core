package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class WaterCauldron extends Block implements Staggered {
	/*   --- STAGGERED INTERFACE ---   */
	@RelevantBlockData
	private int stage;
	private final int maxStage, minStage;
	@Override

	public Staggered setStage(int stage) throws IllegalArgumentException {
		if (stage < this.getMinStage() || stage > this.getMaxStage()) throw new IllegalArgumentException("WaterCauldron block only allows stages from " + this.getMinStage() + " to " + this.getMaxStage());
		WaterCauldron current = new WaterCauldron(this);
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
	public WaterCauldron(short id) {
		super(id, "WATER_CAULDRON", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
		this.stage = 1;
		this.maxStage = 3;
		this.minStage = 1;
	}

	public WaterCauldron(int id) {
		this((short) id);
	}

	private WaterCauldron(WaterCauldron old) {
		this(old.id);
		this.stage = old.stage;
	}

}