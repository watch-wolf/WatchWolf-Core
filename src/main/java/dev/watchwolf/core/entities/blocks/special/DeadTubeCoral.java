package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class DeadTubeCoral extends Block {

	/*   --- CONSTRUCTORS ---   */
	public DeadTubeCoral(short id) {
		super(id, "DEAD_TUBE_CORAL", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
	}

	public DeadTubeCoral(int id) {
		this((short) id);
	}

	private DeadTubeCoral(DeadTubeCoral old) {
		this(old.id);
	}

}