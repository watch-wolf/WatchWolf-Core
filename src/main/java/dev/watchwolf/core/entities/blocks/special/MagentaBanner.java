package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class MagentaBanner extends Block implements Rotable {
	/*   --- ROTABLE INTERFACE ---   */
	@RelevantBlockData
	private Rotable.Rotation rotation;
	@Override

	public Rotable setRotation(Rotable.Rotation rotation) {
		MagentaBanner current = new MagentaBanner(this);
		current.rotation = rotation;
		return current;
	}
	@Override

	public Rotable.Rotation getRotation() {
		return this.rotation;
	}

	/*   --- CONSTRUCTORS ---   */
	public MagentaBanner(short id) {
		super(id, "MAGENTA_BANNER", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
		this.rotation = Rotable.Rotation.S;
	}

	public MagentaBanner(int id) {
		this((short) id);
	}

	private MagentaBanner(MagentaBanner old) {
		this(old.id);
		this.rotation = old.rotation;
	}

}