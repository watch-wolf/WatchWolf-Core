package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class Kelp extends Block {

	/*   --- CONSTRUCTORS ---   */
	public Kelp(short id) {
		super(id, "KELP", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
	}

	public Kelp(int id) {
		this((short) id);
	}

	private Kelp(Kelp old) {
		this(old.id);
	}

}