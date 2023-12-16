package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class DeadBrainCoralFan extends Block {

	/*   --- CONSTRUCTORS ---   */
	public DeadBrainCoralFan(short id) {
		super(id, "DEAD_BRAIN_CORAL_FAN", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
	}

	public DeadBrainCoralFan(int id) {
		this((short) id);
	}

	private DeadBrainCoralFan(DeadBrainCoralFan old) {
		this(old.id);
	}

}