package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class Composter extends Block implements Staggered {
	/*   --- STAGGERED INTERFACE ---   */
	@RelevantBlockData
	private int stage;
	private final int maxStage, minStage;
	@Override

	public Staggered setStage(int stage) throws IllegalArgumentException {
		if (stage < this.getMinStage() || stage > this.getMaxStage()) throw new IllegalArgumentException("Composter block only allows stages from " + this.getMinStage() + " to " + this.getMaxStage());
		Composter current = new Composter(this);
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
	public Composter(short id) {
		super(id, "COMPOSTER", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
		this.stage = 0;
		this.maxStage = 8;
		this.minStage = 0;
	}

	public Composter(int id) {
		this((short) id);
	}

	private Composter(Composter old) {
		this(old.id);
		this.stage = old.stage;
	}

}