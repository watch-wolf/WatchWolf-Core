package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class DeadTubeCoralFan extends Block {

	/*   --- CONSTRUCTORS ---   */
	public DeadTubeCoralFan(short id) {
		super(id, "DEAD_TUBE_CORAL_FAN", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
	}

	public DeadTubeCoralFan(int id) {
		this((short) id);
	}

	private DeadTubeCoralFan(DeadTubeCoralFan old) {
		this(old.id);
	}

}