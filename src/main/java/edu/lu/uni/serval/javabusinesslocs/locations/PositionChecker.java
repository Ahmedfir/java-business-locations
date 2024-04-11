package edu.lu.uni.serval.javabusinesslocs.locations;

import spoon.reflect.declaration.CtElement;

import java.io.IOException;

public interface PositionChecker<T> {

    boolean isValidPosition(T element, int startChar, int endChar) throws IOException;

}
