package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class HornCoralFan extends Block {

	/*   --- CONSTRUCTORS ---   */
	public HornCoralFan(short id) {
		super(id, "HORN_CORAL_FAN", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
	}

	public HornCoralFan(int id) {
		this((short) id);
	}

	private HornCoralFan(HornCoralFan old) {
		this(old.id);
	}

}