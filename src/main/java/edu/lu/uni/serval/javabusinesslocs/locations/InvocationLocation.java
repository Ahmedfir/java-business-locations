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
    public CodePosition getCodePosition(CtInvocation original) throws UnhandledElementException {
        String originalOp = getOriginalValueOfTheNode(original);
        int source_end = original.getPosition().getSourceEnd();

        //we count backwards from the end of the element until the beginning of the method call
        int callee_length = original.toString().length() - original.toString().lastIndexOf("." + originalOp) - 1;
        int start =  source_end - callee_length + 1 ;

        int end = start + originalOp.length() - 1;
        //compute token to mutate position
        /*start = original.getPosition().getSourceStart();
        if (original.getTarget()!= null && !original.getTarget().isImplicit() && original.getTarget().toString().length() > 0) {
            List<CtTypeReferenceImpl> source = original.getTarget().getElements(new TypeFilter<>(CtTypeReferenceImpl.class));
            String package_full_name = source.get(0).getPackage().getSimpleName();
            //start = original.getTarget().getPosition().getSourceStart() + original.getTarget().toString().length()+1;
            callee_length = original.getTarget().toString().length() - package_full_name.length();
            start = original.getTarget().getPosition().getSourceStart() + callee_length;

        }
        end = start + originalOp.length() -1;*/
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
