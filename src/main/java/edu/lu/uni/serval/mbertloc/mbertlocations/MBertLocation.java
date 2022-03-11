package edu.lu.uni.serval.mbertloc.mbertlocations;

import edu.lu.uni.serval.mbertloc.output.CodePosition;
import edu.lu.uni.serval.mbertloc.output.Location;

import edu.lu.uni.serval.mbertloc.output.MBertOperators;
import spoon.reflect.code.*;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.reference.CtFieldReference;
import spoon.reflect.reference.CtTypeReference;

import static edu.lu.uni.serval.mbertloc.output.MBertOperators.CodeBERTLiteralMutator;

public class MBertLocation<T extends CtElement> extends Location {

    private final static int MAX_EXPECTED_TOKENS = 5;

    /**
     * @param firstMutantId
     * @param ctElement
     * @return
     * @see {CodeBERTOperatorMutator#process(spoon.reflect.declaration.CtElement)}
     */
    public static MBertLocation createMBertLocation(int firstMutantId, CtElement ctElement) throws UnhandledElementException {
        if (ctElement instanceof CtBinaryOperator) {
            return new BinaryOperatorLocation(firstMutantId, (CtBinaryOperator) ctElement);
        } else if (ctElement instanceof CtUnaryOperator) {
            return new UnaryOperatorLocation(firstMutantId, (CtUnaryOperator) ctElement);
        } else if (ctElement instanceof CtAssignment) {
            return new AssignmentLocation(firstMutantId, (CtAssignment) ctElement);
        } else if (ctElement instanceof CtArrayRead ||
                ctElement instanceof CtArrayWrite) {
            return new ArrayAccessLocation(firstMutantId, (CtArrayAccess) ctElement);
        } else if (ctElement instanceof CtFieldReference) {
            return new FieldReferenceLocation(firstMutantId, (CtFieldReference) ctElement);
        } else if (ctElement instanceof CtTypeReference) {
            return new TypeReferenceLocation(firstMutantId, (CtTypeReference) ctElement);
        } else if (ctElement instanceof CtInvocation) {
            return new InvocationLocation(firstMutantId, (CtInvocation) ctElement);
        } else {
            return new MBertLocation(firstMutantId, ctElement);
        }
    }


    protected MBertLocation(int firstMutantId, T ctElement) throws UnhandledElementException {
        this.operator = getOperatorByType(ctElement).name();
        this.nodeType = getNodeTypeName(ctElement);
        this.firstMutantId = firstMutantId;
        this.codePosition = getCodePosition(ctElement);
        this.node = getOriginalValueOfTheNode(ctElement);
        this.suffix = getSuffix();
    }

    protected String getOriginalValueOfTheNode(T ctElement) {
        return ctElement.toString();
    }


    protected CodePosition getCodePosition(T ctElement) throws UnhandledElementException {
        SourcePosition pos = ctElement.getPosition();
        if (pos == null || !pos.isValidPosition()) {
            throw new UnhandledElementException(getNodeTypeName(ctElement), "getCodePosition = " + pos);
        }
        return new CodePosition(pos.getSourceStart(), pos.getSourceEnd());
    }

    protected CtClass getParentClass(T ctElement) {
        CtClass ctClass = ctElement.getParent(CtClass.class);
        return ctClass;
    }

    protected String getParentClassName(T ctElement) {
        return getParentClass(ctElement).getSimpleName();
    }

    protected Class<? extends CtElement> getNodeType(T ctElement) {
        return ctElement.getClass();
    }

    protected String getNodeTypeName(T ctElement) {
        return getNodeType(ctElement).getSimpleName();
    }

    public int getExpectedMutants(){
        return MAX_EXPECTED_TOKENS;
    }

    /**
     * @see {CodeBERTOperatorMutator#process(spoon.reflect.declaration.CtElement)}
     */
    protected MBertOperators getOperatorByType(T ctElement) throws UnhandledElementException {
        if (ctElement instanceof CtVariableRead ||
                ctElement instanceof CtVariableWrite ||
                ctElement instanceof CtConditional ||
                ctElement instanceof CtThisAccess ||
                ctElement instanceof CtLiteral) {
            return CodeBERTLiteralMutator;
        } else {
            throw new UnhandledElementException(getNodeTypeName(ctElement), "operatorByType");
        }
    }

    protected String getSuffix() {
        return "";
    }

    public static class UnhandledElementException extends Exception {
        private final String nodeType;

        public UnhandledElementException(String nodeType, String message) {
            super(message);
            this.nodeType = nodeType;
        }

        public String getNodeType() {
            return nodeType;
        }

        @Override
        public String toString() {
            return "UnhandledElementException{" +
                    "nodeType='" + nodeType + '\'' +
                    '}';
        }
    }

}
