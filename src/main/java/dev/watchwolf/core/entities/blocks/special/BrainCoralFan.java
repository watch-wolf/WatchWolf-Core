package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class BrainCoralFan extends Block {

	/*   --- CONSTRUCTORS ---   */
	public BrainCoralFan(short id) {
		super(id, "BRAIN_CORAL_FAN", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
	}

	public BrainCoralFan(int id) {
		this((short) id);
	}

	private BrainCoralFan(BrainCoralFan old) {
		this(old.id);
	}

}