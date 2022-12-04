package edu.lu.uni.serval.javabusinesslocs.locator.selection;

import spoon.reflect.cu.SourcePosition;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtExecutable;

import java.util.List;

import static edu.lu.uni.serval.javabusinesslocs.locator.LocsUtils.getSourcePosition;
import static edu.lu.uni.serval.javabusinesslocs.locator.LocsUtils.isToBeProcessed;

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
        methodsElementsToBeMutated = null;
        this.currentElementPos = -1;
        this.currentMethod = null;
        while (methodsElementsToBeMutated == null && currentMethod == null && notLastMethod()) {
            currentMethodPos++;
            CtExecutable ctMethod = (CtExecutable) methodsToBeMutated.get(currentMethodPos);
            SourcePosition sourcePosition = getSourcePosition(ctMethod);
            if (sourcePosition != null) {
                methodsElementsToBeMutated = ctMethod.getElements(arg0 ->
                        isToBeProcessed(arg0) && isLineToMutate(getSourcePosition(arg0).getLine()));
                this.currentMethod = new Method(ctMethod.getSignature(), sourcePosition);
            }
        }
    }

    private boolean notLastMethod() {
        return notLastItem(methodsToBeMutated, currentMethodPos);
    }

    private boolean notLastElement() {
        return notLastItem(methodsElementsToBeMutated, currentElementPos);
    }

    private static boolean notLastItem(List items, int pos) {
        return items != null && !items.isEmpty() && pos < items.size() - 1;
    }
}
