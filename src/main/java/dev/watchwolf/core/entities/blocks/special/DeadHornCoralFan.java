package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class DeadHornCoralFan extends Block {

	/*   --- CONSTRUCTORS ---   */
	public DeadHornCoralFan(short id) {
		super(id, "DEAD_HORN_CORAL_FAN", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
	}

	public DeadHornCoralFan(int id) {
		this((short) id);
	}

	private DeadHornCoralFan(DeadHornCoralFan old) {
		this(old.id);
	}

}