package edu.lu.uni.serval.mbertloc.mbertlocations;

import edu.lu.uni.serval.mbertloc.output.MBertOperators;
import edu.lu.uni.serval.mbertloc.output.CodePosition;
import spoon.reflect.code.CtBinaryOperator;
import spoon.reflect.cu.CompilationUnit;
import spoon.reflect.cu.SourcePosition;
import spoon.support.reflect.cu.position.SourcePositionImpl;

import static edu.lu.uni.serval.mbertloc.output.MBertOperators.CodeBERTBinaryOperatorMutator;

public class BinaryOperatorLocation extends MBertLocation<CtBinaryOperator> {

    protected BinaryOperatorLocation(int firstMutantId, CtBinaryOperator ctElement) throws UnhandledElementException {
        super(firstMutantId, ctElement);
    }

    @Override
    protected CodePosition getCodePosition(CtBinaryOperator ctElement) throws UnhandledElementException {
        int start = ctElement.getLeftHandOperand().getPosition().getSourceEnd()+1;
        int end = ctElement.getRightHandOperand().getPosition().getSourceStart() -1;
        CompilationUnit origUnit = ctElement.getPosition().getCompilationUnit();
        SourcePosition position = new SourcePositionImpl(origUnit,start,end,origUnit.getLineSeparatorPositions());
        if (!position.isValidPosition()) return super.getCodePosition(ctElement);
        return new CodePosition(position.getSourceStart(), position.getSourceEnd());
    }

    @Override
    protected String getOriginalValueOfTheNode(CtBinaryOperator ctElement) {
        return ctElement.getKind().toString();
    }

    @Override
    protected MBertOperators getOperatorByType(CtBinaryOperator ctElement) {
        return CodeBERTBinaryOperatorMutator;
    }
}
