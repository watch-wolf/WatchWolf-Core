package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class StonePressurePlate extends Block implements Powerable {
	/*   --- POWERABLE INTERFACE ---   */
	@RelevantBlockData
	private boolean powered;
	@Override
	public boolean isPowered() {
		return this.powered;
	}
	@Override

	public Powerable setPowered(boolean val) {
		StonePressurePlate current = new StonePressurePlate(this);
		current.powered = val;
		return current;
	}

	/*   --- CONSTRUCTORS ---   */
	public StonePressurePlate(short id) {
		super(id, "STONE_PRESSURE_PLATE", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
		this.powered = false;
	}

	public StonePressurePlate(int id) {
		this((short) id);
	}

	private StonePressurePlate(StonePressurePlate old) {
		this(old.id);
		this.powered = old.powered;
	}

}