package edu.lu.uni.serval.javabusinesslocs.locations;

import edu.lu.uni.serval.javabusinesslocs.output.Operators;
import edu.lu.uni.serval.javabusinesslocs.output.CodePosition;
import spoon.reflect.code.CtBinaryOperator;
import spoon.reflect.cu.CompilationUnit;
import spoon.reflect.cu.SourcePosition;
import spoon.support.reflect.cu.position.SourcePositionImpl;

import java.io.IOException;

import static edu.lu.uni.serval.javabusinesslocs.output.Operators.BinaryOperatorMutator;

public class BinaryOperatorLocation extends BusinessLocation<CtBinaryOperator> {

    protected BinaryOperatorLocation(int firstMutantId, CtBinaryOperator ctElement, PositionChecker positionChecker) throws UnhandledElementException, IOException {
        super(firstMutantId, ctElement, positionChecker);
    }

    @Override
    protected CodePosition getCodePosition(CtBinaryOperator ctElement) throws UnhandledElementException, IOException {
        int start = ctElement.getLeftHandOperand().getPosition().getSourceEnd()+1;
        int end = ctElement.getRightHandOperand().getPosition().getSourceStart() -1;
        CompilationUnit origUnit = ctElement.getPosition().getCompilationUnit();
        SourcePosition position = new SourcePositionImpl(origUnit,start,end,origUnit.getLineSeparatorPositions());
        if (!position.isValidPosition() || !isValidPosition(ctElement.toString(), position.getSourceStart(), position.getSourceEnd())) return super.getCodePosition(ctElement);

        return new CodePosition(position.getSourceStart(), position.getSourceEnd());
    }

    @Override
    protected String getOriginalValueOfTheNode(CtBinaryOperator ctElement) {
        return ctElement.getKind().toString();
    }

    @Override
    protected Operators getOperatorByType(CtBinaryOperator ctElement) {
        return BinaryOperatorMutator;
    }
}
