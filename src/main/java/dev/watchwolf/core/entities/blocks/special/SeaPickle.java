package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class SeaPickle extends Block implements Groupable {
	/*   --- GROUPABLE INTERFACE ---   */
	private int groupAmountMinusOne;
	private final int maxGroupAmount;
	@Override

	public Groupable setGroupAmount(int amount) throws IllegalArgumentException {
		if (amount < 1 || amount > this.getMaxGroupAmount()) throw new IllegalArgumentException("SeaPickle block only allows grouping from 1 to " + this.getMaxGroupAmount());
		SeaPickle current = new SeaPickle(this);
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

	/*   --- CONSTRUCTORS ---   */
	public SeaPickle(short id) {
		super(id, "SEA_PICKLE", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
		this.groupAmountMinusOne = 0; // 0 is 1
		this.maxGroupAmount = 4;
	}

	public SeaPickle(int id) {
		this((short) id);
	}

	private SeaPickle(SeaPickle old) {
		this(old.id);
		this.groupAmountMinusOne = old.groupAmountMinusOne;
	}

}