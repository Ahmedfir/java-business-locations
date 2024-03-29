package edu.lu.uni.serval.javabusinesslocs.locator.selection;

import spoon.reflect.cu.SourcePosition;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtExecutable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static edu.lu.uni.serval.javabusinesslocs.locator.LocsUtils.getSourcePosition;
import static edu.lu.uni.serval.javabusinesslocs.locator.LocsUtils.isToBeProcessed;

public abstract class RandomSelection implements ElementsSelector {

    private static final int RANDOM_SEED = Integer.getInteger("RANDOM_SEED", 10);

    private final List<Element> elements;

    public RandomSelection(List<CtElement> methodsToBeMutated) {
        elements = new ArrayList<>();
        for (CtElement met : methodsToBeMutated) {
            SourcePosition sourcePosition = getSourcePosition(met);
            if (sourcePosition == null) continue;
            CtExecutable ctMethod = (CtExecutable) met;
            Method method = new Method(ctMethod.getSignature(), sourcePosition);
            List<CtElement> methodsElementsToBeMutated = ctMethod.getElements(arg0 ->
                    isToBeProcessed(arg0) && isLineToMutate(getSourcePosition(arg0).getLine()));
            if (methodsElementsToBeMutated == null || methodsElementsToBeMutated.isEmpty()) continue;
            elements.addAll(Element.parseList(methodsElementsToBeMutated, method));
        }
        Random random = new Random(RANDOM_SEED);
        Collections.shuffle(elements, random);
    }

    @Override
    public boolean hasNext() {
        return !elements.isEmpty();
    }

    @Override
    public Element next() {
        Element element = elements.get(0);
        elements.remove(0);
        return element;
    }
}
