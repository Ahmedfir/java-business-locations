package edu.lu.uni.serval.javabusinesslocs.locator;

import spoon.reflect.code.*;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.declaration.*;
import spoon.reflect.reference.CtFieldReference;
import spoon.reflect.reference.CtTypeReference;

public final class LocsUtils {

    private LocsUtils() {
    }

    public static boolean inheritsFromAssertion(CtElement e) {
        if (e == null || isMethod(e) || e instanceof CtClass)
            return false;
        if (e instanceof CtAssert)
            return true;
        if (e.getParent() == null)
            return false;

        return inheritsFromAssertion(e.getParent());
    }


    public static SourcePosition getSourcePosition(CtElement e) {
        if (e == null)
            return null;
        if (e.getPosition() != null && e.getPosition().isValidPosition())
            return e.getPosition();
        if (e.getParent() != null)
            System.err.println("returning parent position for " + e.getClass());
        return getSourcePosition(e.getParent());
    }


    public static boolean isImplicit(CtElement e) {
        if (e == null || isMethod(e) || e instanceof CtClass)
            return false;
        if (e.isImplicit())
            return true;
        if (e.getParent() == null)
            return false;

        return isImplicit(e.getParent());
    }

    public static boolean isToBeProcessed(CtElement candidate) {
        //first we list exceptions
        if (isImplicit(candidate))
            return false;
        if (candidate instanceof CtConstructorCall ||
                candidate instanceof CtTypeAccess ||
                candidate instanceof CtNewArray ||
                candidate instanceof CtAnnotation ||
                inheritsFromAssertion(candidate)
        )
            return false;
        if (candidate instanceof CtExpression
                || candidate instanceof CtFieldReference)
            return true;
        if (candidate instanceof CtTypeReference && candidate.getParent() != null
                && candidate.getParent() instanceof CtTypeAccess
                && !inheritsFromConstructorCall(candidate)) {
            return true;
        }
        return false;
    }

    public static boolean isMethod(CtElement e) {
        return e instanceof CtMethod || e instanceof CtConstructor;
    }


    public static boolean inheritsFromConstructorCall(CtElement e) {
        if (e == null || isMethod(e) || e instanceof CtClass)
            return false;
        if (e instanceof CtConstructorCall)
            return true;
        if (e.getParent() == null)
            return false;

        return inheritsFromConstructorCall(e.getParent());
    }


}
