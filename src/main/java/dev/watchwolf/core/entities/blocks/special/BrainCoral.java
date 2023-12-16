package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class BrainCoral extends Block {

	/*   --- CONSTRUCTORS ---   */
	public BrainCoral(short id) {
		super(id, "BRAIN_CORAL", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
	}

	public BrainCoral(int id) {
		this((short) id);
	}

	private BrainCoral(BrainCoral old) {
		this(old.id);
	}

}