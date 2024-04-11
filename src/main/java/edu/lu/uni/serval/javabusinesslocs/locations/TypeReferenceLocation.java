package edu.lu.uni.serval.javabusinesslocs.locations;

import edu.lu.uni.serval.javabusinesslocs.locator.LocsUtils;
import edu.lu.uni.serval.javabusinesslocs.output.CodePosition;
import edu.lu.uni.serval.javabusinesslocs.output.Operators;

import spoon.reflect.cu.CompilationUnit;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.reference.CtTypeReference;
import spoon.support.reflect.cu.position.SourcePositionImpl;

import java.io.IOException;

import static edu.lu.uni.serval.javabusinesslocs.output.Operators.TypeReferenceMutator;

public class TypeReferenceLocation extends BusinessLocation<CtTypeReference> {

    protected TypeReferenceLocation(int firstMutantId, CtTypeReference ctElement, PositionChecker positionChecker) throws UnhandledElementException, IOException {
        super(firstMutantId, ctElement, positionChecker);
    }

    @Override
    protected CodePosition getCodePosition(CtTypeReference ctElement) throws UnhandledElementException, IOException {
        String originalStr = getOriginalValueOfTheNode(ctElement);
        SourcePosition origPosition = LocsUtils.getSourcePosition(ctElement);

        //compute token to mutate position
        int start = origPosition.getSourceStart();
        int end = start + originalStr.length()-1;
        CompilationUnit origUnit = origPosition.getCompilationUnit();
        SourcePosition position = new SourcePositionImpl(origUnit,start,end,origUnit.getLineSeparatorPositions());
        if (!position.isValidPosition() || !isValidPosition(ctElement.toString(), position.getSourceStart(), position.getSourceEnd())) return super.getCodePosition(ctElement);
        return new CodePosition(position.getSourceStart(), position.getSourceEnd());

    }

    @Override
    protected Operators getOperatorByType(CtTypeReference ctElement) {
        return TypeReferenceMutator;
    }

}
