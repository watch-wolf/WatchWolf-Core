package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class AzaleaLeaves extends Block {

	/*   --- CONSTRUCTORS ---   */
	public AzaleaLeaves(short id) {
		super(id, "AZALEA_LEAVES", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
	}

	public AzaleaLeaves(int id) {
		this((short) id);
	}

	private AzaleaLeaves(AzaleaLeaves old) {
		this(old.id);
	}

}