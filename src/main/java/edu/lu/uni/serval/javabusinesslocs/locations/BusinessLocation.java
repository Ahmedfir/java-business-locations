package edu.lu.uni.serval.javabusinesslocs.locations;

import edu.lu.uni.serval.javabusinesslocs.output.CodePosition;
import edu.lu.uni.serval.javabusinesslocs.output.Location;

import edu.lu.uni.serval.javabusinesslocs.output.Operators;
import spoon.reflect.code.*;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.reference.CtFieldReference;
import spoon.reflect.reference.CtTypeReference;

import java.io.IOException;

import static edu.lu.uni.serval.javabusinesslocs.output.Operators.LiteralMutator;

public class BusinessLocation<T extends CtElement> extends Location {

    private final static int MAX_EXPECTED_TOKENS = 5;
    protected PositionChecker positionChecker;

    /**
     * @param firstMutantId
     * @param ctElement
     * @return
     * @see {https://github.com/rdegiovanni/mBERT}
     * @see {CodeBERTOperatorMutator#process(spoon.reflect.declaration.CtElement)}
     */
    public static BusinessLocation createBusinessLocation(int firstMutantId, CtElement ctElement, PositionChecker positionChecker) throws UnhandledElementException, IOException {
        BusinessLocation res;
        if (ctElement instanceof CtBinaryOperator) {
            res= new BinaryOperatorLocation(firstMutantId, (CtBinaryOperator) ctElement, positionChecker);
        } else if (ctElement instanceof CtUnaryOperator) {
            res= new UnaryOperatorLocation(firstMutantId, (CtUnaryOperator) ctElement, positionChecker);
        } else if (ctElement instanceof CtAssignment) {
            res= new AssignmentLocation(firstMutantId, (CtAssignment) ctElement, positionChecker);
        } else if (ctElement instanceof CtArrayRead ||
                ctElement instanceof CtArrayWrite) {
            res= new ArrayAccessLocation(firstMutantId, (CtArrayAccess) ctElement, positionChecker);
        } else if (ctElement instanceof CtFieldReference) {
            res= new FieldReferenceLocation(firstMutantId, (CtFieldReference) ctElement, positionChecker);
        } else if (ctElement instanceof CtTypeReference) {
            res= new TypeReferenceLocation(firstMutantId, (CtTypeReference) ctElement, positionChecker);
        } else if (ctElement instanceof CtInvocation) {
            res= new InvocationLocation(firstMutantId, (CtInvocation) ctElement, positionChecker);
        } else {
            res= new BusinessLocation(firstMutantId, ctElement, positionChecker);
        }
        return res;
    }


    protected BusinessLocation(int firstMutantId, T ctElement, PositionChecker positionChecker) throws UnhandledElementException, IOException {
        this.positionChecker = positionChecker;
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


    protected CodePosition getCodePosition(T ctElement) throws UnhandledElementException, IOException {
        SourcePosition pos = ctElement.getPosition();
        CodePosition cp;
        if (pos == null || !pos.isValidPosition()) {
            throw new UnhandledElementException(getNodeTypeName(ctElement), "getCodePosition = " + pos);
        }
        String elementText = ctElement.toString();
        cp = new CodePosition(pos.getSourceStart(), pos.getSourceEnd());
        if (!isValidPosition(elementText, cp.getStartPosition(), cp.getEndPosition())) {
            cp = new CodePosition(pos.getSourceStart() - pos.getLine(), pos.getSourceEnd() - pos.getLine());
        }
        if (!isValidPosition(elementText, cp.getStartPosition(), cp.getEndPosition())) {
            throw new UnhandledElementException(getNodeTypeName(ctElement), "getCodePosition = " + pos);
        }
        return cp;
    }

    protected boolean isValidPosition(String text, int start, int end) throws IOException {
        return positionChecker == null || positionChecker.isValidPosition(text, start, end);
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

    public int getExpectedMutants() {
        return MAX_EXPECTED_TOKENS;
    }

    /**
     * @see {https://github.com/rdegiovanni/mBERT}
     * @see {CodeBERTOperatorMutator#process(spoon.reflect.declaration.CtElement)}
     */
    protected Operators getOperatorByType(T ctElement) throws UnhandledElementException {
        if (ctElement instanceof CtVariableRead ||
                ctElement instanceof CtVariableWrite ||
                ctElement instanceof CtConditional ||
                ctElement instanceof CtThisAccess ||
                ctElement instanceof CtLiteral) {
            return LiteralMutator;
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
