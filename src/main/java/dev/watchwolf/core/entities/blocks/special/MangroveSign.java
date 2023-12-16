package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class MangroveSign extends Block implements Rotable {
	/*   --- ROTABLE INTERFACE ---   */
	@RelevantBlockData
	private Rotable.Rotation rotation;
	@Override

	public Rotable setRotation(Rotable.Rotation rotation) {
		MangroveSign current = new MangroveSign(this);
		current.rotation = rotation;
		return current;
	}
	@Override

	public Rotable.Rotation getRotation() {
		return this.rotation;
	}

	/*   --- CONSTRUCTORS ---   */
	public MangroveSign(short id) {
		super(id, "MANGROVE_SIGN", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
		this.rotation = Rotable.Rotation.S;
	}

	public MangroveSign(int id) {
		this((short) id);
	}

	private MangroveSign(MangroveSign old) {
		this(old.id);
		this.rotation = old.rotation;
	}

}