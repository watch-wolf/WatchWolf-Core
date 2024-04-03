package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class BubbleCoral extends Block {

	/*   --- CONSTRUCTORS ---   */
	public BubbleCoral(short id) {
		super(id, "BUBBLE_CORAL", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
	}

	public BubbleCoral(int id) {
		this((short) id);
	}

	private BubbleCoral(BubbleCoral old) {
		this(old.id);
	}

}