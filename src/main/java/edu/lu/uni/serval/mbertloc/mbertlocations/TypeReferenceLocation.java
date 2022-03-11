package edu.lu.uni.serval.mbertloc.mbertlocations;

import edu.lu.uni.serval.mbertloc.mbertlocator.MBertUtils;
import edu.lu.uni.serval.mbertloc.output.CodePosition;
import edu.lu.uni.serval.mbertloc.output.MBertOperators;

import spoon.reflect.cu.CompilationUnit;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.reference.CtTypeReference;
import spoon.support.reflect.cu.position.SourcePositionImpl;

import static edu.lu.uni.serval.mbertloc.output.MBertOperators.CodeBERTTypeReferenceMutator;

public class TypeReferenceLocation extends MBertLocation<CtTypeReference> {

    protected TypeReferenceLocation(int firstMutantId, CtTypeReference ctElement) throws UnhandledElementException {
        super(firstMutantId, ctElement);
    }

    @Override
    protected CodePosition getCodePosition(CtTypeReference ctElement) throws UnhandledElementException {
        String originalStr = getOriginalValueOfTheNode(ctElement);
        SourcePosition origPosition = MBertUtils.getSourcePosition(ctElement);

        //compute token to mutate position
        int start = origPosition.getSourceStart();
        int end = start + originalStr.length()-1;
        CompilationUnit origUnit = origPosition.getCompilationUnit();
        SourcePosition position = new SourcePositionImpl(origUnit,start,end,origUnit.getLineSeparatorPositions());
        if (!position.isValidPosition()) return super.getCodePosition(ctElement);
        return new CodePosition(position.getSourceStart(), position.getSourceEnd());

    }

    @Override
    protected MBertOperators getOperatorByType(CtTypeReference ctElement) {
        return CodeBERTTypeReferenceMutator;
    }

}
