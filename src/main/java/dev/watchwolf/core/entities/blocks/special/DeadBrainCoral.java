package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class DeadBrainCoral extends Block {

	/*   --- CONSTRUCTORS ---   */
	public DeadBrainCoral(short id) {
		super(id, "DEAD_BRAIN_CORAL", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
	}

	public DeadBrainCoral(int id) {
		this((short) id);
	}

	private DeadBrainCoral(DeadBrainCoral old) {
		this(old.id);
	}

}