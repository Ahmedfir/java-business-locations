package edu.lu.uni.serval.mbertloc.mbertlocations;

import edu.lu.uni.serval.mbertloc.output.CodePosition;
import edu.lu.uni.serval.mbertloc.output.MBertOperators;
import spoon.reflect.code.CtArrayAccess;
import spoon.reflect.code.CtExpression;
import spoon.reflect.cu.SourcePosition;

import static edu.lu.uni.serval.mbertloc.output.MBertOperators.CodeBERTArrayMutator;

public class ArrayAccessLocation extends MBertLocation<CtArrayAccess> {

    protected ArrayAccessLocation(int firstMutantId, CtArrayAccess ctElement) throws UnhandledElementException {
        super(firstMutantId, ctElement);
    }

    @Override
    protected CodePosition getCodePosition(CtArrayAccess ctElement) throws UnhandledElementException {
        CtExpression index_expr = ctElement.getIndexExpression();
        SourcePosition position = index_expr.getPosition();
        if (position == null || !position.isValidPosition()) return super.getCodePosition(ctElement);
        return new CodePosition(position.getSourceStart(), position.getSourceEnd());
    }

    @Override
    protected String getOriginalValueOfTheNode(CtArrayAccess ctElement) {
        return ctElement.getIndexExpression().toString();
    }

    @Override
    protected MBertOperators getOperatorByType(CtArrayAccess ctElement) {
        return CodeBERTArrayMutator;
    }
}
