package mcsrbot.benchmark;

import java.util.ArrayList;
import java.util.List;

public class BenchmarkSet {
    public String name = "New Benchmark Set";

    // true = generate seeds when the set starts
    // false = use the saved seeds below
    public boolean randomSeeds = false;

    // Used when randomSeeds is true
    public int randomSeedCount = 10;

    // Used when randomSeeds is false
    public List<String> seeds = new ArrayList<>();

    public BenchmarkSet() {
    }

    public BenchmarkSet(String name) {
        this.name = name;
    }
}