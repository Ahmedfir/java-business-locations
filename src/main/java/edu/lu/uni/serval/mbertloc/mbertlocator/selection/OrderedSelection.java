package edu.lu.uni.serval.mbertloc.mbertlocator.selection;

import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtExecutable;

import java.util.List;

import static edu.lu.uni.serval.mbertloc.mbertlocator.MBertUtils.getSourcePosition;
import static edu.lu.uni.serval.mbertloc.mbertlocator.MBertUtils.isToBeProcessed;

public abstract class OrderedSelection implements ElementsSelector {

    private final List<CtElement> methodsToBeMutated;

    private List<CtElement> methodsElementsToBeMutated;
    private Method currentMethod;
    private int currentMethodPos;
    private int currentElementPos;

    public OrderedSelection(List<CtElement> methodsToBeMutated) {
        this.methodsToBeMutated = methodsToBeMutated;
        currentMethodPos = -1;
        nextMethod();
    }

    @Override
    public boolean hasNext() {
        return notLastMethod() || notLastElement();
    }

    @Override
    public Element next() {
        if (notLastElement()) {
            currentElementPos++;
            CtElement ctElement = methodsElementsToBeMutated.get(currentElementPos);
            return new Element(ctElement, currentMethod);
        } else if (notLastMethod()) {
            nextMethod();
            return next();
        }
        return null;
    }


    private void nextMethod() {
        CtExecutable ctMethod = null;
        while ((methodsElementsToBeMutated == null || methodsElementsToBeMutated.isEmpty()) && notLastMethod()) {
            currentMethodPos++;
            ctMethod = (CtExecutable) methodsToBeMutated.get(currentMethodPos);
            methodsElementsToBeMutated = ctMethod.getElements(arg0 ->
                    isToBeProcessed(arg0) && isLineToMutate(getSourcePosition(arg0).getLine()));

        }
        this.currentElementPos = -1;
        this.currentMethod = new Method(ctMethod);
    }

    private boolean notLastMethod() {
        return notLastItem(methodsToBeMutated, currentMethodPos);
    }

    private boolean notLastElement() {
        return notLastItem(methodsElementsToBeMutated, currentElementPos);
    }

    private static boolean notLastItem(List items, int pos) {
        return pos < items.size() - 1;
    }
}
