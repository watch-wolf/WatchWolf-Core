package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class BubbleColumn extends Block {

	/*   --- CONSTRUCTORS ---   */
	public BubbleColumn(short id) {
		super(id, "BUBBLE_COLUMN", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
	}

	public BubbleColumn(int id) {
		this((short) id);
	}

	private BubbleColumn(BubbleColumn old) {
		this(old.id);
	}

}