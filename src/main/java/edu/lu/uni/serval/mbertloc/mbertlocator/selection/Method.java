package edu.lu.uni.serval.mbertloc.mbertlocator.selection;

import edu.lu.uni.serval.mbertloc.output.CodePosition;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.declaration.CtExecutable;

import static edu.lu.uni.serval.mbertloc.mbertlocator.MBertUtils.getSourcePosition;

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
