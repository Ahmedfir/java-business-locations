package edu.lu.uni.serval.javabusinesslocs.locator.selection;

import java.util.Iterator;

public interface ElementsSelector extends Iterator<Element> {
    boolean isLineToMutate(int line);
}
