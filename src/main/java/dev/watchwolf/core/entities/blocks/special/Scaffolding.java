package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class Scaffolding extends Block {

	/*   --- CONSTRUCTORS ---   */
	public Scaffolding(short id) {
		super(id, "SCAFFOLDING", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
	}

	public Scaffolding(int id) {
		this((short) id);
	}

	private Scaffolding(Scaffolding old) {
		this(old.id);
	}

}