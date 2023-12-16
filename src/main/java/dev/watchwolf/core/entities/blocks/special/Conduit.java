package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class Conduit extends Block {

	/*   --- CONSTRUCTORS ---   */
	public Conduit(short id) {
		super(id, "CONDUIT", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
	}

	public Conduit(int id) {
		this((short) id);
	}

	private Conduit(Conduit old) {
		this(old.id);
	}

}