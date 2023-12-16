package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class MangroveRoots extends Block {

	/*   --- CONSTRUCTORS ---   */
	public MangroveRoots(short id) {
		super(id, "MANGROVE_ROOTS", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
	}

	public MangroveRoots(int id) {
		this((short) id);
	}

	private MangroveRoots(MangroveRoots old) {
		this(old.id);
	}

}