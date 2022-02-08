package com.wyvernrunner.wicket.simulator;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WeightedRandomness {


    private class Entry {
        double accumulatedWeight;
        String id;
    }

    private List<Entry> entries = new ArrayList<>();
    private double accumulatedWeight;
    private Random rand = new Random();

    public void addEntry(String id, double weight) {
        accumulatedWeight += weight;
        Entry e = new Entry();
        e.id = id;
        e.accumulatedWeight = accumulatedWeight;
        entries.add(e);
    }

    public String getRandom() {
        double r = rand.nextDouble() * accumulatedWeight;

        for (Entry entry : entries) {
            if (entry.accumulatedWeight >= r) {
                return entry.id;
            }
        }
        return null; //should only happen when there are no entries
    }

}