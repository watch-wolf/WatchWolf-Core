package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class TubeCoral extends Block {

	/*   --- CONSTRUCTORS ---   */
	public TubeCoral(short id) {
		super(id, "TUBE_CORAL", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
	}

	public TubeCoral(int id) {
		this((short) id);
	}

	private TubeCoral(TubeCoral old) {
		this(old.id);
	}

}