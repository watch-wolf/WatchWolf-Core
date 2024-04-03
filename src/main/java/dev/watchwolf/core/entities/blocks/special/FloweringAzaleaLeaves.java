package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class FloweringAzaleaLeaves extends Block {

	/*   --- CONSTRUCTORS ---   */
	public FloweringAzaleaLeaves(short id) {
		super(id, "FLOWERING_AZALEA_LEAVES", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
	}

	public FloweringAzaleaLeaves(int id) {
		this((short) id);
	}

	private FloweringAzaleaLeaves(FloweringAzaleaLeaves old) {
		this(old.id);
	}

}