package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class DeadFireCoralFan extends Block {

	/*   --- CONSTRUCTORS ---   */
	public DeadFireCoralFan(short id) {
		super(id, "DEAD_FIRE_CORAL_FAN", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
	}

	public DeadFireCoralFan(int id) {
		this((short) id);
	}

	private DeadFireCoralFan(DeadFireCoralFan old) {
		this(old.id);
	}

}