package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class BirchPressurePlate extends Block implements Powerable {
	/*   --- POWERABLE INTERFACE ---   */
	@RelevantBlockData
	private boolean powered;
	@Override
	public boolean isPowered() {
		return this.powered;
	}
	@Override

	public Powerable setPowered(boolean val) {
		BirchPressurePlate current = new BirchPressurePlate(this);
		current.powered = val;
		return current;
	}

	/*   --- CONSTRUCTORS ---   */
	public BirchPressurePlate(short id) {
		super(id, "BIRCH_PRESSURE_PLATE", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
		this.powered = false;
	}

	public BirchPressurePlate(int id) {
		this((short) id);
	}

	private BirchPressurePlate(BirchPressurePlate old) {
		this(old.id);
		this.powered = old.powered;
	}

}