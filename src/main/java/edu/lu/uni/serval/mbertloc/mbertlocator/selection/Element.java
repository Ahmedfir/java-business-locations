package edu.lu.uni.serval.mbertloc.mbertlocator.selection;

import spoon.reflect.declaration.CtElement;

public class Element {

    public final CtElement ctElement;
    public final Method method;

    public Element(CtElement ctElement, Method method){
        this.ctElement = ctElement;
        this.method = method;
    }

}
