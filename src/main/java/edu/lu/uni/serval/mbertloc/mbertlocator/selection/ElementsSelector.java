package edu.lu.uni.serval.mbertloc.mbertlocator.selection;

import java.util.Iterator;

public interface ElementsSelector extends Iterator<Element> {
    boolean isLineToMutate(int line);
}
