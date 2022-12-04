package edu.lu.uni.serval.javabusinesslocs.locator.selection;

import edu.lu.uni.serval.javabusinesslocs.output.CodePosition;
import spoon.reflect.cu.SourcePosition;

public class Method {

    public final int startLine;
    public final int endLine;
    public final CodePosition codePosition;
    public final String signature;

    public Method(String signature, SourcePosition sourcePosition) {
        startLine = sourcePosition.getLine();
        endLine = sourcePosition.getEndLine();
        codePosition = new CodePosition(sourcePosition.getSourceStart(), sourcePosition.getSourceEnd());
        this.signature = signature;
    }
}
