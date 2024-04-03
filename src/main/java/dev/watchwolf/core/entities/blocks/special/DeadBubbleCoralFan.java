package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class DeadBubbleCoralFan extends Block {

	/*   --- CONSTRUCTORS ---   */
	public DeadBubbleCoralFan(short id) {
		super(id, "DEAD_BUBBLE_CORAL_FAN", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
	}

	public DeadBubbleCoralFan(int id) {
		this((short) id);
	}

	private DeadBubbleCoralFan(DeadBubbleCoralFan old) {
		this(old.id);
	}

}