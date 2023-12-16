package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class CaveVinesPlant extends Block implements Ageable {
	/*   --- AGEABLE INTERFACE ---   */
	@RelevantBlockData
	private int age;
	private final int maxAge;
	@Override

	public Ageable setAge(int age) throws IllegalArgumentException {
		if (age > this.getMaxAge()) throw new IllegalArgumentException("CaveVinesPlant block only allows age from 0 to " + this.getMaxAge());
		CaveVinesPlant current = new CaveVinesPlant(this);
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
	public CaveVinesPlant(short id) {
		super(id, "CAVE_VINES_PLANT", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
		this.age = 0;
		this.maxAge = 1;
	}

	public CaveVinesPlant(int id) {
		this((short) id);
	}

	private CaveVinesPlant(CaveVinesPlant old) {
		this(old.id);
		this.age = old.age;
	}

}