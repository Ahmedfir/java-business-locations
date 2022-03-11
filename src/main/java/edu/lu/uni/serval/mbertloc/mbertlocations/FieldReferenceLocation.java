package edu.lu.uni.serval.mbertloc.mbertlocations;

import edu.lu.uni.serval.mbertloc.mbertlocator.MBertUtils;
import edu.lu.uni.serval.mbertloc.output.CodePosition;
import edu.lu.uni.serval.mbertloc.output.MBertOperators;

import spoon.reflect.cu.CompilationUnit;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.reference.CtFieldReference;
import spoon.support.reflect.cu.position.SourcePositionImpl;

import static edu.lu.uni.serval.mbertloc.output.MBertOperators.CodeBERTFieldReferenceMutator;

public class FieldReferenceLocation extends MBertLocation<CtFieldReference> {

    protected FieldReferenceLocation(int firstMutantId, CtFieldReference ctElement) throws UnhandledElementException {
        super(firstMutantId, ctElement);
    }

    @Override
    protected CodePosition getCodePosition(CtFieldReference ctElement) throws UnhandledElementException {
        SourcePosition origPosition = MBertUtils.getSourcePosition(ctElement);
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
    protected MBertOperators getOperatorByType(CtFieldReference ctElement) {
        return CodeBERTFieldReferenceMutator;
    }
}
