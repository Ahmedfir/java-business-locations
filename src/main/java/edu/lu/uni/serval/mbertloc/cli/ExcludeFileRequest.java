package edu.lu.uni.serval.mbertloc.cli;

import edu.lu.uni.serval.mbertloc.mbertlocator.FileRequest;
import edu.lu.uni.serval.mbertloc.mbertlocator.MethodRequest;
import spoon.reflect.declaration.CtExecutable;

import java.util.List;

import static edu.lu.uni.serval.mbertloc.cli.CliRequest.ERROR_MESSAGE;

class ExcludeFileRequest extends FileRequest {

    ExcludeFileRequest(String filePath, List<MethodRequest> methods, List<Integer> lines) {
        super(filePath, methods, lines);
        assert lines != null && !lines.isEmpty() || methods != null && !methods.isEmpty() :
                "excluding complete file is not implemented. \n" + ERROR_MESSAGE;
    }

    @Override
    public boolean isMethodToMutate(CtExecutable arg0) {
        return !super.isMethodToMutate(arg0);
    }

    @Override
    public boolean isLineToMutate(int line) {
        return !super.isLineToMutate(line);
    }

    @Override
    public String toString() {
        return "ExcludeFileRequest{" +
                "javaFilePath='" + this.javaFilePath + '\'' +
                ", methodsToExclude=" + this.methodsToMutate +
                ", linesToExclude=" + this.linesToMutate +
                ", mutantId=" + this.nextMutantId +
                '}';
    }
}
