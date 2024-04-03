package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class DeadHornCoral extends Block {

	/*   --- CONSTRUCTORS ---   */
	public DeadHornCoral(short id) {
		super(id, "DEAD_HORN_CORAL", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
	}

	public DeadHornCoral(int id) {
		this((short) id);
	}

	private DeadHornCoral(DeadHornCoral old) {
		this(old.id);
	}

}