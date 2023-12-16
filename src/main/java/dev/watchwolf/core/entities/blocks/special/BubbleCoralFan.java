package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class BubbleCoralFan extends Block {

	/*   --- CONSTRUCTORS ---   */
	public BubbleCoralFan(short id) {
		super(id, "BUBBLE_CORAL_FAN", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
	}

	public BubbleCoralFan(int id) {
		this((short) id);
	}

	private BubbleCoralFan(BubbleCoralFan old) {
		this(old.id);
	}

}