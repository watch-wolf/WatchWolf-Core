package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class StoneStairs extends Block implements Orientable, Sectionable {
	/*   --- ORIENTABLE INTERFACE ---   */
	@RelevantBlockData
	private final Map<Orientable.Orientation,Boolean> orientation = new HashMap<>();
	@Override
	public boolean isOrientationSet(Orientable.Orientation o) throws IllegalArgumentException {
		Boolean result = this.orientation.get(o);
		return result;
	}
	@Override

	public Orientable setOrientation(Orientable.Orientation o, boolean value) throws IllegalArgumentException {
		StoneStairs current = new StoneStairs(this);
		current.orientation.put(o, value);
		return current;
	}
	@Override

	public Set<Orientable.Orientation> getValidOrientations() {
		return this.orientation.keySet();
	}
	/*   --- DIRECTIONABLE INTERFACE ---   */
	@RelevantBlockData
	private Sectionable.Section section;
	private final HashSet<Sectionable.Section> allowedSections = new HashSet<>();
	@Override
	public Sectionable.Section getSection() {
		return this.section;
	}
	@Override

	public Sectionable setSection(Sectionable.Section section) throws IllegalArgumentException {
		if (!this.allowedSections.contains(section)) throw new IllegalArgumentException("StoneStairs block doesn't allow the section " + section.name());
		StoneStairs current = new StoneStairs(this);
		current.section = section;
		return current;
	}
	@Override
	public Set<Sectionable.Section> getValidSections() {
		return (HashSet)this.allowedSections.clone();
	}

	/*   --- CONSTRUCTORS ---   */
	public StoneStairs(short id) {
		super(id, "STONE_STAIRS", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
		this.orientation.put(Orientable.Orientation.U, false);
		this.orientation.put(Orientable.Orientation.S, false);
		this.orientation.put(Orientable.Orientation.N, false);
		this.orientation.put(Orientable.Orientation.D, false);
		this.orientation.put(Orientable.Orientation.W, false);
		this.orientation.put(Orientable.Orientation.E, false);
		this.allowedSections.add(Sectionable.Section.INNER_RIGHT);
		this.allowedSections.add(Sectionable.Section.INNER_LEFT);
		this.allowedSections.add(Sectionable.Section.OUTER_LEFT);
		this.allowedSections.add(Sectionable.Section.STRAIGHT);
		this.allowedSections.add(Sectionable.Section.OUTER_RIGHT);
		this.section = Sectionable.Section.INNER_RIGHT;
	}

	public StoneStairs(int id) {
		this((short) id);
	}

	private StoneStairs(StoneStairs old) {
		this(old.id);
		this.orientation.putAll(old.orientation);
		this.section = old.section;
	}

}