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

import java.util.LinkedHashSet;
import java.util.Set;

import static edu.lu.uni.serval.javabusinesslocs.output.Operators.LiteralMutator;

public class BusinessLocation<T extends CtElement> extends Location {

    private final static int MAX_EXPECTED_TOKENS = Integer.getInteger("PREDICTIONS_NUMBER", 5);
    /**
     * Set this true if you want to get the locations of the if conditions, otherwise false.
     * e.g. pass -DIF_CONDITIONS_AS_TKN=true
     * input source code: if (a==b)
     * output location added of: "a==b"
     *
     * @see IfConditionReferenceLocation
     * default value is false
     */
    public static boolean IF_CONDITIONS_AS_TKN = Boolean.getBoolean("IF_CONDITIONS_AS_TKN");
    public static boolean CONDITIONS_AS_TKN = Boolean.getBoolean("CONDITIONS_AS_TKN");

    /**
     * @param firstMutantId
     * @param ctElement
     * @return
     * @see {https://github.com/rdegiovanni/mBERT}
     * @see {CodeBERTOperatorMutator#process(spoon.reflect.declaration.CtElement)}
     */
    public static Set<BusinessLocation> createBusinessLocation(int firstMutantId, CtElement ctElement) throws UnhandledElementException {
        Set<BusinessLocation> res = new LinkedHashSet<>();
        // simple tokens parsing first
        if (ctElement instanceof CtBinaryOperator) {
            res.add(new BinaryOperatorLocation(firstMutantId, (CtBinaryOperator) ctElement));
        } else if (ctElement instanceof CtUnaryOperator) {
            res.add(new UnaryOperatorLocation(firstMutantId, (CtUnaryOperator) ctElement));
        } else if (ctElement instanceof CtAssignment) {
            res.add(new AssignmentLocation(firstMutantId, (CtAssignment) ctElement));
        } else if (ctElement instanceof CtArrayRead ||
                ctElement instanceof CtArrayWrite) {
            res.add(new ArrayAccessLocation(firstMutantId, (CtArrayAccess) ctElement));
        } else if (ctElement instanceof CtFieldReference) {
            res.add(new FieldReferenceLocation(firstMutantId, (CtFieldReference) ctElement));
        } else if (ctElement instanceof CtTypeReference) {
            res.add(new TypeReferenceLocation(firstMutantId, (CtTypeReference) ctElement));
        } else if (ctElement instanceof CtInvocation) {
            res.add(new InvocationLocation(firstMutantId, (CtInvocation) ctElement));
        } else if(!(ctElement instanceof CtLoop || ctElement instanceof CtIf)){
            //check that it's not a loop or if otherwise it enters here
            res.add(new BusinessLocation(firstMutantId, ctElement));
        }

        // parse complex (full conditions) tokens.
        if (IF_CONDITIONS_AS_TKN || CONDITIONS_AS_TKN){
            if (ctElement instanceof CtIf) {
                res.add(new IfConditionReferenceLocation(firstMutantId, ((CtIf) ctElement).getCondition()));
            }
        }

        if(CONDITIONS_AS_TKN) {
            if(ctElement instanceof CtWhile) {
                res.add(new WhileConditionLocation(firstMutantId, ((CtWhile) ctElement).getLoopingExpression()));
            } else if(ctElement instanceof CtFor) {
                res.add(new ForConditionLocation(firstMutantId, ((CtFor) ctElement).getExpression()));
            } else if (ctElement instanceof CtDo)
                res.add(new DoConditionLocation(firstMutantId, ((CtDo) ctElement).getLoopingExpression()));
        }

        return res;
    }


    protected BusinessLocation(int firstMutantId, T ctElement) throws UnhandledElementException {
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
