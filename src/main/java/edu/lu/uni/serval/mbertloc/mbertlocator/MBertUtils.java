package edu.lu.uni.serval.mbertloc.mbertlocator;

import spoon.reflect.code.*;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.reference.CtFieldReference;
import spoon.reflect.reference.CtTypeReference;

public final class MBertUtils {

    private MBertUtils() {
    }

    public static boolean inheritsFromAssertion(CtElement e) {
        if (e == null || e instanceof CtMethod || e instanceof CtClass)
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
            return getSourcePosition(e.getParent());
        return null;
    }



    public static boolean isImplicit(CtElement e) {
        if (e == null || e instanceof CtMethod || e instanceof CtClass)
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


    public static boolean inheritsFromConstructorCall(CtElement e) {
        if (e == null || e instanceof CtMethod || e instanceof CtClass)
            return false;
        if (e instanceof CtConstructorCall)
            return true;
        if (e.getParent() == null)
            return false;

        return inheritsFromConstructorCall(e.getParent());
    }


}
