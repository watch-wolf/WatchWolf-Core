package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class CyanBanner extends Block implements Rotable {
	/*   --- ROTABLE INTERFACE ---   */
	@RelevantBlockData
	private Rotable.Rotation rotation;
	@Override

	public Rotable setRotation(Rotable.Rotation rotation) {
		CyanBanner current = new CyanBanner(this);
		current.rotation = rotation;
		return current;
	}
	@Override

	public Rotable.Rotation getRotation() {
		return this.rotation;
	}

	/*   --- CONSTRUCTORS ---   */
	public CyanBanner(short id) {
		super(id, "CYAN_BANNER", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
		this.rotation = Rotable.Rotation.S;
	}

	public CyanBanner(int id) {
		this((short) id);
	}

	private CyanBanner(CyanBanner old) {
		this(old.id);
		this.rotation = old.rotation;
	}

}