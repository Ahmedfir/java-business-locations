package edu.lu.uni.serval.javabusinesslocs.locations;

import edu.lu.uni.serval.javabusinesslocs.output.CodePosition;
import edu.lu.uni.serval.javabusinesslocs.output.Operators;
import spoon.reflect.code.CtArrayAccess;
import spoon.reflect.code.CtExpression;
import spoon.reflect.cu.SourcePosition;

import java.io.IOException;

import static edu.lu.uni.serval.javabusinesslocs.output.Operators.ArrayMutator;

public class ArrayAccessLocation extends BusinessLocation<CtArrayAccess> {

    protected ArrayAccessLocation(int firstMutantId, CtArrayAccess ctElement, PositionChecker positionChecker) throws UnhandledElementException, IOException {
        super(firstMutantId, ctElement, positionChecker);
    }

    @Override
    protected CodePosition getCodePosition(CtArrayAccess original) throws UnhandledElementException, IOException  {
        CtExpression index_expr = original.getIndexExpression();
        SourcePosition position = index_expr.getPosition();
        if (position == null || !position.isValidPosition() || !isValidPosition(original.toString(), position.getSourceStart(), position.getSourceEnd())) return super.getCodePosition(original);
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
