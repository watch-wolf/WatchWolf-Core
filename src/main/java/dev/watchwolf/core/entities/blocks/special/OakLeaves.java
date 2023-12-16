package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class OakLeaves extends Block {

	/*   --- CONSTRUCTORS ---   */
	public OakLeaves(short id) {
		super(id, "OAK_LEAVES", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
	}

	public OakLeaves(int id) {
		this((short) id);
	}

	private OakLeaves(OakLeaves old) {
		this(old.id);
	}

}