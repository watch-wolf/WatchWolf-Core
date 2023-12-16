package dev.watchwolf.core.entities;

import dev.watchwolf.core.entities.items.Item;

import java.util.ArrayList;
import java.util.List;

public class Container {
    private final Item []items;

    public Container(Item []items) {
        this.items = Container.clone(items);
    }

    public Container(List<Item> items) {
        this(items.toArray(new Item[0]));
    }

    public Item []getItems() {
        return Container.clone(this.items);
    }

    public int getSize() {
        return this.items.length;
    }

    private static Item []clone(Item []from) {
        Item []r = new Item[from.length];
        for (int n = 0; n < r.length; n++) {
            if (from[n] == null) continue;
            r[n] = new Item(from[n]);
        }
        return r;
    }
}
