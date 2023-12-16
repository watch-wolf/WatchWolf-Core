package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class YellowCandle extends Block implements Groupable, Ignitable {
	/*   --- GROUPABLE INTERFACE ---   */
	private int groupAmountMinusOne;
	private final int maxGroupAmount;
	@Override

	public Groupable setGroupAmount(int amount) throws IllegalArgumentException {
		if (amount < 1 || amount > this.getMaxGroupAmount()) throw new IllegalArgumentException("YellowCandle block only allows grouping from 1 to " + this.getMaxGroupAmount());
		YellowCandle current = new YellowCandle(this);
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
		YellowCandle current = new YellowCandle(this);
		current.ignited = value;
		return current;
	}

	/*   --- CONSTRUCTORS ---   */
	public YellowCandle(short id) {
		super(id, "YELLOW_CANDLE", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
		this.groupAmountMinusOne = 0; // 0 is 1
		this.maxGroupAmount = 4;
		this.ignited = true;
	}

	public YellowCandle(int id) {
		this((short) id);
	}

	private YellowCandle(YellowCandle old) {
		this(old.id);
		this.groupAmountMinusOne = old.groupAmountMinusOne;
		this.ignited = old.ignited;
	}

}