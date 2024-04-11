package edu.lu.uni.serval.javabusinesslocs.locations;

import edu.lu.uni.serval.javabusinesslocs.output.CodePosition;
import edu.lu.uni.serval.javabusinesslocs.output.Operators;


import spoon.reflect.code.CtAssignment;
import spoon.reflect.cu.CompilationUnit;
import spoon.reflect.cu.SourcePosition;
import spoon.support.reflect.cu.position.SourcePositionImpl;

import java.io.IOException;

import static edu.lu.uni.serval.javabusinesslocs.output.Operators.AssignmentMutator;

public class AssignmentLocation extends BusinessLocation<CtAssignment> {

    protected AssignmentLocation(int firstMutantId, CtAssignment ctElement, PositionChecker positionChecker) throws UnhandledElementException, IOException {
        super(firstMutantId, ctElement, positionChecker);
    }

    @Override
    protected CodePosition getCodePosition(CtAssignment original) throws UnhandledElementException, IOException {
        //compute token to mutate position
        int start = original.getAssigned().getPosition().getSourceEnd() + 1;
        int end = original.getAssignment().getPosition().getSourceStart() - 1;
        CompilationUnit origUnit = original.getPosition().getCompilationUnit();
        SourcePosition position = new SourcePositionImpl(origUnit, start, end, origUnit.getLineSeparatorPositions());

        if (!position.isValidPosition() || !isValidPosition(original.toString(), position.getSourceStart(), position.getSourceEnd()))
            return super.getCodePosition(original);
        return new CodePosition(position.getSourceStart(), position.getSourceEnd());
    }

    /**
     * @return string to add after the predicted token.
     * @see {https://github.com/rdegiovanni/mBERT}
     * @see {CodeBERTAssignmentMutator#mutate(spoon.reflect.code.CtAssignment, spoon.reflect.declaration.CtClass)}
     */
    @Override
    protected String getSuffix() {
        return "=";
    }

    @Override
    public int getExpectedMutants() {
        return 2 * super.getExpectedMutants(); // 5 with prefix an 5 without.
    }

    @Override
    protected Operators getOperatorByType(CtAssignment ctElement) {
        return AssignmentMutator;
    }
}
