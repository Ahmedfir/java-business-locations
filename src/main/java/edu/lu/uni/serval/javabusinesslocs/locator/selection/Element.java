package edu.lu.uni.serval.javabusinesslocs.locator.selection;

import spoon.reflect.declaration.CtElement;

import java.util.ArrayList;
import java.util.List;

public class Element {

    public final CtElement ctElement;
    public final Method method;

    public Element(CtElement ctElement, Method method){
        this.ctElement = ctElement;
        this.method = method;
    }

    public static List<Element> parseList(List<CtElement> methodsElementsToBeMutated, Method method) {
        List<Element> elements = new ArrayList<>();
        for (CtElement e : methodsElementsToBeMutated){
            elements.add(new Element(e, method));
        }
        return elements;
    }
}
