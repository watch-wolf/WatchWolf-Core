package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class RedstoneLamp extends Block implements Ignitable {
	/*   --- IGNITABLE INTERFACE ---   */
	@RelevantBlockData
	private boolean ignited;
	@Override
	public boolean isIgnited() {
		return this.ignited;
	}
	@Override

	public Ignitable setIgnited(boolean value) {
		RedstoneLamp current = new RedstoneLamp(this);
		current.ignited = value;
		return current;
	}

	/*   --- CONSTRUCTORS ---   */
	public RedstoneLamp(short id) {
		super(id, "REDSTONE_LAMP", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
		this.ignited = true;
	}

	public RedstoneLamp(int id) {
		this((short) id);
	}

	private RedstoneLamp(RedstoneLamp old) {
		this(old.id);
		this.ignited = old.ignited;
	}

}