package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class JungleLeaves extends Block {

	/*   --- CONSTRUCTORS ---   */
	public JungleLeaves(short id) {
		super(id, "JUNGLE_LEAVES", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
	}

	public JungleLeaves(int id) {
		this((short) id);
	}

	private JungleLeaves(JungleLeaves old) {
		this(old.id);
	}

}