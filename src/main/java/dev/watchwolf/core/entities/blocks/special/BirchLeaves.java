package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class BirchLeaves extends Block {

	/*   --- CONSTRUCTORS ---   */
	public BirchLeaves(short id) {
		super(id, "BIRCH_LEAVES", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
	}

	public BirchLeaves(int id) {
		this((short) id);
	}

	private BirchLeaves(BirchLeaves old) {
		this(old.id);
	}

}