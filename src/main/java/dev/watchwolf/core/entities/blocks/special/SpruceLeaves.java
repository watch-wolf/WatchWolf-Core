package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class SpruceLeaves extends Block {

	/*   --- CONSTRUCTORS ---   */
	public SpruceLeaves(short id) {
		super(id, "SPRUCE_LEAVES", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
	}

	public SpruceLeaves(int id) {
		this((short) id);
	}

	private SpruceLeaves(SpruceLeaves old) {
		this(old.id);
	}

}