package edu.lu.uni.serval.javabusinesslocs.locator;

import spoon.reflect.code.*;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.declaration.*;
import spoon.reflect.reference.CtFieldReference;
import spoon.reflect.reference.CtTypeReference;

import static edu.lu.uni.serval.javabusinesslocs.locations.BusinessLocation.CONDITIONS_AS_TKN;
import static edu.lu.uni.serval.javabusinesslocs.locations.BusinessLocation.IF_CONDITIONS_AS_TKN;


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
        if (e == null) {
            return null;
        }
        if (e.getPosition() != null && e.getPosition().isValidPosition()) {
            return e.getPosition();
        }
        //if (e.getParent() != null){
        // System.err.println("returning parent position for " + e.getClass());
        //}
        return getSourcePosition(e.getParent());
    }


    public static boolean isImplicit(CtElement e) {
        //makes the children of else if(condition) not implicit
        //allows parsing locations in else if
        if(e.getParent() instanceof CtIf)
            e.setImplicit(false);

        if (e == null || isMethod(e) || e instanceof CtClass)
            return false;
        if (e.isImplicit())
            return true;
        if (e.getParent() == null)
            return false;
        return isImplicit(e.getParent());
    }

    public static boolean isToBeProcessed(CtElement candidate) {
        //while, for, do and if are labeled implicit by the method isImplicit
        if((CONDITIONS_AS_TKN && candidate instanceof CtLoop)
            ||((CONDITIONS_AS_TKN || IF_CONDITIONS_AS_TKN) && candidate instanceof CtIf)){
            return !candidate.isImplicit();
        }

        if (isImplicit(candidate)) {
            return false;
        }
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
