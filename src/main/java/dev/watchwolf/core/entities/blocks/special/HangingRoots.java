package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class HangingRoots extends Block {

	/*   --- CONSTRUCTORS ---   */
	public HangingRoots(short id) {
		super(id, "HANGING_ROOTS", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
	}

	public HangingRoots(int id) {
		this((short) id);
	}

	private HangingRoots(HangingRoots old) {
		this(old.id);
	}

}