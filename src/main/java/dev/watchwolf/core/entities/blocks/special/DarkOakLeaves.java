package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class DarkOakLeaves extends Block {

	/*   --- CONSTRUCTORS ---   */
	public DarkOakLeaves(short id) {
		super(id, "DARK_OAK_LEAVES", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
	}

	public DarkOakLeaves(int id) {
		this((short) id);
	}

	private DarkOakLeaves(DarkOakLeaves old) {
		this(old.id);
	}

}