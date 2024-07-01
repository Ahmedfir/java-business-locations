package edu.lu.uni.serval.javabusinesslocs.output;


import java.util.*;

public class LineLocations implements Queryable<Integer> {
    private int line_number;
    private Set<Location> locations;

    public LineLocations() {
    }

    private LineLocations(int line_number) {
        this.line_number = line_number;
    }

    public static LineLocations newInstance(int line, Location location) {
        LineLocations pred = new LineLocations(line);
        pred.addPredictions(location);
        return pred;
    }

    public void setLine_number(int line_number) {
        this.line_number = line_number;
    }

    public boolean addPredictions(Location location) {
        if (locations == null) {
            locations = new LinkedHashSet<>();
        }
        return locations.add(location);
    }

    public Set<Location> getLocations() {
        return locations;
    }

    public int getLine_number() {
        return line_number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineLocations that = (LineLocations) o;
        return line_number == that.line_number && Objects.equals(locations, that.locations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(line_number, locations);
    }

    @Override
    public Integer getQueryable() {
        return line_number;
    }
}
