package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class TubeCoralFan extends Block {

	/*   --- CONSTRUCTORS ---   */
	public TubeCoralFan(short id) {
		super(id, "TUBE_CORAL_FAN", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
	}

	public TubeCoralFan(int id) {
		this((short) id);
	}

	private TubeCoralFan(TubeCoralFan old) {
		this(old.id);
	}

}