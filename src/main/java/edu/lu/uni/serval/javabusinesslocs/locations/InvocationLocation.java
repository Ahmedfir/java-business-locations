package edu.lu.uni.serval.javabusinesslocs.locations;

import edu.lu.uni.serval.javabusinesslocs.output.CodePosition;
import edu.lu.uni.serval.javabusinesslocs.output.Operators;

import spoon.reflect.code.CtInvocation;
import spoon.reflect.cu.CompilationUnit;
import spoon.reflect.cu.SourcePosition;
import spoon.support.reflect.cu.position.SourcePositionImpl;

import static edu.lu.uni.serval.javabusinesslocs.output.Operators.InvocationMutator;

public class InvocationLocation extends BusinessLocation<CtInvocation> {

    protected InvocationLocation(int firstMutantId, CtInvocation ctElement) throws UnhandledElementException {
        super(firstMutantId, ctElement);
    }

    @Override
    protected CodePosition getCodePosition(CtInvocation original) throws UnhandledElementException {
        String originalOp = getOriginalValueOfTheNode(original);

        //compute token to mutate position
        int start = original.getPosition().getSourceStart();
        if (original.getTarget()!= null && !original.getTarget().isImplicit() && original.getTarget().toString().length() > 0)
            start = original.getTarget().getPosition().getSourceStart() + original.getTarget().toString().length()+1;
        int end = start + originalOp.length()-1;
        CompilationUnit origUnit = original.getPosition().getCompilationUnit();
        SourcePosition position = new SourcePositionImpl(origUnit,start,end,origUnit.getLineSeparatorPositions());
        if (!position.isValidPosition()) return super.getCodePosition(original);
        return new CodePosition(position.getSourceStart(), position.getSourceEnd());
    }

    @Override
    protected String getOriginalValueOfTheNode(CtInvocation ctElement) {
        return ctElement.getExecutable().getSimpleName();
    }

    @Override
    protected Operators getOperatorByType(CtInvocation ctElement) {
        return InvocationMutator;
    }


}
