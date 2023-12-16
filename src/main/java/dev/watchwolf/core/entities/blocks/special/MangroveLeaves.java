package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class MangroveLeaves extends Block {

	/*   --- CONSTRUCTORS ---   */
	public MangroveLeaves(short id) {
		super(id, "MANGROVE_LEAVES", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
	}

	public MangroveLeaves(int id) {
		this((short) id);
	}

	private MangroveLeaves(MangroveLeaves old) {
		this(old.id);
	}

}