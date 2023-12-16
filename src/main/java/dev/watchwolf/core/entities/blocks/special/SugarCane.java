package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class SugarCane extends Block {

	/*   --- CONSTRUCTORS ---   */
	public SugarCane(short id) {
		super(id, "SUGAR_CANE", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
	}

	public SugarCane(int id) {
		this((short) id);
	}

	private SugarCane(SugarCane old) {
		this(old.id);
	}

}