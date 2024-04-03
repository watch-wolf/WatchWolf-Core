package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class DeadBubbleCoral extends Block {

	/*   --- CONSTRUCTORS ---   */
	public DeadBubbleCoral(short id) {
		super(id, "DEAD_BUBBLE_CORAL", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
	}

	public DeadBubbleCoral(int id) {
		this((short) id);
	}

	private DeadBubbleCoral(DeadBubbleCoral old) {
		this(old.id);
	}

}