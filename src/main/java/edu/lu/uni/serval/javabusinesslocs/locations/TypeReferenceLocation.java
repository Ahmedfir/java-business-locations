package edu.lu.uni.serval.javabusinesslocs.locations;

import edu.lu.uni.serval.javabusinesslocs.locator.LocsUtils;
import edu.lu.uni.serval.javabusinesslocs.output.CodePosition;
import edu.lu.uni.serval.javabusinesslocs.output.Operators;

import spoon.reflect.cu.CompilationUnit;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.reference.CtTypeReference;
import spoon.support.reflect.cu.position.SourcePositionImpl;

import static edu.lu.uni.serval.javabusinesslocs.output.Operators.TypeReferenceMutator;

public class TypeReferenceLocation extends BusinessLocation<CtTypeReference> {

    protected TypeReferenceLocation(int firstMutantId, CtTypeReference ctElement) throws UnhandledElementException {
        super(firstMutantId, ctElement);
    }

    @Override
    protected CodePosition getCodePosition(CtTypeReference ctElement) throws UnhandledElementException {
        String originalStr = getOriginalValueOfTheNode(ctElement);
        SourcePosition origPosition = LocsUtils.getSourcePosition(ctElement);

        //compute token to mutate position
        int start = origPosition.getSourceStart();
        int end = origPosition.getSourceEnd();
        CompilationUnit origUnit = origPosition.getCompilationUnit();
        SourcePosition position = new SourcePositionImpl(origUnit,start,end,origUnit.getLineSeparatorPositions());
        if (!position.isValidPosition()) return super.getCodePosition(ctElement);
        return new CodePosition(position.getSourceStart(), position.getSourceEnd());

    }

    @Override
    protected String getOriginalValueOfTheNode(CtTypeReference ctElement) {
        return ctElement.getSimpleName();
    }

    @Override
    protected Operators getOperatorByType(CtTypeReference ctElement) {
        return TypeReferenceMutator;
    }

}
