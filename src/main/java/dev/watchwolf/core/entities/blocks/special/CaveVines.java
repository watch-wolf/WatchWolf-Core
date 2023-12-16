package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class CaveVines extends Block implements Ageable {
	/*   --- AGEABLE INTERFACE ---   */
	@RelevantBlockData
	private int age;
	private final int maxAge;
	@Override

	public Ageable setAge(int age) throws IllegalArgumentException {
		if (age > this.getMaxAge()) throw new IllegalArgumentException("CaveVines block only allows age from 0 to " + this.getMaxAge());
		CaveVines current = new CaveVines(this);
		current.age = age;
		return current;
	}
	@Override

	public int getAge() {
		return this.age;
	}
	@Override

	public int getMaxAge() {
		return this.maxAge;
	}

	/*   --- CONSTRUCTORS ---   */
	public CaveVines(short id) {
		super(id, "CAVE_VINES", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
		this.age = 0;
		this.maxAge = 1;
	}

	public CaveVines(int id) {
		this((short) id);
	}

	private CaveVines(CaveVines old) {
		this(old.id);
		this.age = old.age;
	}

}