package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class Cactus extends Block {

	/*   --- CONSTRUCTORS ---   */
	public Cactus(short id) {
		super(id, "CACTUS", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
	}

	public Cactus(int id) {
		this((short) id);
	}

	private Cactus(Cactus old) {
		this(old.id);
	}

}