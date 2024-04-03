package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class DeadFireCoral extends Block {

	/*   --- CONSTRUCTORS ---   */
	public DeadFireCoral(short id) {
		super(id, "DEAD_FIRE_CORAL", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
	}

	public DeadFireCoral(int id) {
		this((short) id);
	}

	private DeadFireCoral(DeadFireCoral old) {
		this(old.id);
	}

}