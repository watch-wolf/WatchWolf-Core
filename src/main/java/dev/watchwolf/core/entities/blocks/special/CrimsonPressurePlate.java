package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class CrimsonPressurePlate extends Block implements Powerable {
	/*   --- POWERABLE INTERFACE ---   */
	@RelevantBlockData
	private boolean powered;
	@Override
	public boolean isPowered() {
		return this.powered;
	}
	@Override

	public Powerable setPowered(boolean val) {
		CrimsonPressurePlate current = new CrimsonPressurePlate(this);
		current.powered = val;
		return current;
	}

	/*   --- CONSTRUCTORS ---   */
	public CrimsonPressurePlate(short id) {
		super(id, "CRIMSON_PRESSURE_PLATE", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
		this.powered = false;
	}

	public CrimsonPressurePlate(int id) {
		this((short) id);
	}

	private CrimsonPressurePlate(CrimsonPressurePlate old) {
		this(old.id);
		this.powered = old.powered;
	}

}