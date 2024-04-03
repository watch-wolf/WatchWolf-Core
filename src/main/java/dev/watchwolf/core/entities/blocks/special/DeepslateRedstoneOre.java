package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class DeepslateRedstoneOre extends Block implements Ignitable {
	/*   --- IGNITABLE INTERFACE ---   */
	@RelevantBlockData
	private boolean ignited;
	@Override
	public boolean isIgnited() {
		return this.ignited;
	}
	@Override

	public Ignitable setIgnited(boolean value) {
		DeepslateRedstoneOre current = new DeepslateRedstoneOre(this);
		current.ignited = value;
		return current;
	}

	/*   --- CONSTRUCTORS ---   */
	public DeepslateRedstoneOre(short id) {
		super(id, "DEEPSLATE_REDSTONE_ORE", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
		this.ignited = true;
	}

	public DeepslateRedstoneOre(int id) {
		this((short) id);
	}

	private DeepslateRedstoneOre(DeepslateRedstoneOre old) {
		this(old.id);
		this.ignited = old.ignited;
	}

}