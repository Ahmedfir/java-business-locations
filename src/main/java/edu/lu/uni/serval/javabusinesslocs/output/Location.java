package edu.lu.uni.serval.javabusinesslocs.output;

import java.io.Serializable;
import java.util.Objects;

// {'score': 0.34352532029151917, 'token': 23796, 'token_str': 'null', 'match': False, 'cosine': 0.77328160405159}
public class Location implements Serializable {

    protected String node;
    protected CodePosition codePosition;
    protected String nodeType;
    protected int firstMutantId;
    protected String operator;
    protected String suffix;

    public Location() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return firstMutantId == location.firstMutantId && Objects.equals(codePosition, location.codePosition) && Objects.equals(node, location.node) && Objects.equals(operator, location.operator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codePosition, node, firstMutantId, operator);
    }
}
