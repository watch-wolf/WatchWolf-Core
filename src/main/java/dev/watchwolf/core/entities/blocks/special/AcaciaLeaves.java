package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class AcaciaLeaves extends Block {

	/*   --- CONSTRUCTORS ---   */
	public AcaciaLeaves(short id) {
		super(id, "ACACIA_LEAVES", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
	}

	public AcaciaLeaves(int id) {
		this((short) id);
	}

	private AcaciaLeaves(AcaciaLeaves old) {
		this(old.id);
	}

}