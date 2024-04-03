package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class PolishedBlackstonePressurePlate extends Block implements Powerable {
	/*   --- POWERABLE INTERFACE ---   */
	@RelevantBlockData
	private boolean powered;
	@Override
	public boolean isPowered() {
		return this.powered;
	}
	@Override

	public Powerable setPowered(boolean val) {
		PolishedBlackstonePressurePlate current = new PolishedBlackstonePressurePlate(this);
		current.powered = val;
		return current;
	}

	/*   --- CONSTRUCTORS ---   */
	public PolishedBlackstonePressurePlate(short id) {
		super(id, "POLISHED_BLACKSTONE_PRESSURE_PLATE", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
		this.powered = false;
	}

	public PolishedBlackstonePressurePlate(int id) {
		this((short) id);
	}

	private PolishedBlackstonePressurePlate(PolishedBlackstonePressurePlate old) {
		this(old.id);
		this.powered = old.powered;
	}

}