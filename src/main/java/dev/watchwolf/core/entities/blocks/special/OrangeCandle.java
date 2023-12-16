package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class OrangeCandle extends Block implements Groupable, Ignitable {
	/*   --- GROUPABLE INTERFACE ---   */
	private int groupAmountMinusOne;
	private final int maxGroupAmount;
	@Override

	public Groupable setGroupAmount(int amount) throws IllegalArgumentException {
		if (amount < 1 || amount > this.getMaxGroupAmount()) throw new IllegalArgumentException("OrangeCandle block only allows grouping from 1 to " + this.getMaxGroupAmount());
		OrangeCandle current = new OrangeCandle(this);
		current.groupAmountMinusOne = amount - 1;
		return current;
	}
	@RelevantBlockData
	@Override

	public int getGroupAmount() {
		return this.groupAmountMinusOne + 1;
	}
	@Override

	public int getMaxGroupAmount() {
		return this.maxGroupAmount;
	}
	/*   --- IGNITABLE INTERFACE ---   */
	@RelevantBlockData
	private boolean ignited;
	@Override
	public boolean isIgnited() {
		return this.ignited;
	}
	@Override

	public Ignitable setIgnited(boolean value) {
		OrangeCandle current = new OrangeCandle(this);
		current.ignited = value;
		return current;
	}

	/*   --- CONSTRUCTORS ---   */
	public OrangeCandle(short id) {
		super(id, "ORANGE_CANDLE", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
		this.groupAmountMinusOne = 0; // 0 is 1
		this.maxGroupAmount = 4;
		this.ignited = true;
	}

	public OrangeCandle(int id) {
		this((short) id);
	}

	private OrangeCandle(OrangeCandle old) {
		this(old.id);
		this.groupAmountMinusOne = old.groupAmountMinusOne;
		this.ignited = old.ignited;
	}

}