package edu.lu.uni.serval.javabusinesslocs.locations;

import edu.lu.uni.serval.javabusinesslocs.locator.LocsUtils;
import edu.lu.uni.serval.javabusinesslocs.output.CodePosition;
import edu.lu.uni.serval.javabusinesslocs.output.Operators;

import spoon.reflect.cu.CompilationUnit;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.reference.CtFieldReference;
import spoon.support.reflect.cu.position.SourcePositionImpl;

import static edu.lu.uni.serval.javabusinesslocs.output.Operators.FieldReferenceMutator;

public class FieldReferenceLocation extends BusinessLocation<CtFieldReference> {

    protected FieldReferenceLocation(int firstMutantId, CtFieldReference ctElement) throws UnhandledElementException {
        super(firstMutantId, ctElement);
    }

    @Override
    protected CodePosition getCodePosition(CtFieldReference ctElement) throws UnhandledElementException {
        SourcePosition origPosition = LocsUtils.getSourcePosition(ctElement);
        int end = origPosition.getSourceEnd();

        int start = end - (getOriginalValueOfTheNode(ctElement).length() - 1);
        CompilationUnit origUnit = origPosition.getCompilationUnit();
        SourcePosition position = new SourcePositionImpl(origUnit,start,end,origUnit.getLineSeparatorPositions());

        if (!position.isValidPosition()) return super.getCodePosition(ctElement);
        return new CodePosition(position.getSourceStart(), position.getSourceEnd());
    }

    @Override
    protected String getOriginalValueOfTheNode(CtFieldReference ctElement) {
        return ctElement.getSimpleName();
    }

    @Override
    protected Operators getOperatorByType(CtFieldReference ctElement) {
        return FieldReferenceMutator;
    }
}
