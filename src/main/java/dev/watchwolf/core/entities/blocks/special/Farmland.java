package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class Farmland extends Block {

	/*   --- CONSTRUCTORS ---   */
	public Farmland(short id) {
		super(id, "FARMLAND", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
	}

	public Farmland(int id) {
		this((short) id);
	}

	private Farmland(Farmland old) {
		this(old.id);
	}

}