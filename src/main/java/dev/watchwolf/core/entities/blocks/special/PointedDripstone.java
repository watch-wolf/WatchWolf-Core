package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class PointedDripstone extends Block {

	/*   --- CONSTRUCTORS ---   */
	public PointedDripstone(short id) {
		super(id, "POINTED_DRIPSTONE", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
	}

	public PointedDripstone(int id) {
		this((short) id);
	}

	private PointedDripstone(PointedDripstone old) {
		this(old.id);
	}

}