package edu.lu.uni.serval.javabusinesslocs.output;

import java.io.Serializable;
import java.util.Objects;

public class CodePosition implements Serializable {
    private int startPosition;
    private int endPosition;

    public CodePosition() {
    }

    public CodePosition(int startPosition, int endPosition) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    public int getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(int startPosition) {
        this.startPosition = startPosition;
    }

    public int getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(int endPosition) {
        this.endPosition = endPosition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CodePosition that = (CodePosition) o;
        return startPosition == that.startPosition && endPosition == that.endPosition;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startPosition, endPosition);
    }

    @Override
    public String toString() {
        return "{" +
                "startPosition=" + startPosition +
                ", endPosition=" + endPosition +
                '}';
    }
}
