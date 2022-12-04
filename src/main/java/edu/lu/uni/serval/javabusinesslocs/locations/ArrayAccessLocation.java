package edu.lu.uni.serval.javabusinesslocs.locations;

import edu.lu.uni.serval.javabusinesslocs.output.CodePosition;
import edu.lu.uni.serval.javabusinesslocs.output.Operators;
import spoon.reflect.code.CtArrayAccess;
import spoon.reflect.code.CtExpression;
import spoon.reflect.cu.SourcePosition;

import static edu.lu.uni.serval.javabusinesslocs.output.Operators.ArrayMutator;

public class ArrayAccessLocation extends BusinessLocation<CtArrayAccess> {

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
    protected Operators getOperatorByType(CtArrayAccess ctElement) {
        return ArrayMutator;
    }
}
