package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class FireCoralFan extends Block {

	/*   --- CONSTRUCTORS ---   */
	public FireCoralFan(short id) {
		super(id, "FIRE_CORAL_FAN", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
	}

	public FireCoralFan(int id) {
		this((short) id);
	}

	private FireCoralFan(FireCoralFan old) {
		this(old.id);
	}

}