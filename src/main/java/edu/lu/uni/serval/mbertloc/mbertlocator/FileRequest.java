package edu.lu.uni.serval.mbertloc.mbertlocator;

import edu.lu.uni.serval.mbertloc.mbertlocations.MBertLocation;
import edu.lu.uni.serval.mbertloc.output.CodePosition;
import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.List;

import static edu.lu.uni.serval.mbertloc.mbertlocator.MBertUtils.getSourcePosition;
import static edu.lu.uni.serval.mbertloc.mbertlocator.MBertUtils.isToBeProcessed;


public class FileRequest {
    private LocationsCollector locationsCollector;
    protected String javaFilePath;
    protected List<MethodRequest> methodsToMutate;
    protected List<Integer> linesToMutate;
    protected int nextMutantId = 0;
    private FileRequest excludeFileRequest;

    public FileRequest(String filePath, List<MethodRequest> methods, List<Integer> lines) {
        this.javaFilePath = filePath;
        this.linesToMutate = lines;
        this.methodsToMutate = methods;
    }

    public void setExcludeFileRequest(FileRequest excludeFileRequest) {
        this.excludeFileRequest = excludeFileRequest;
    }

    public String getJavaFilePath() {
        return javaFilePath;
    }

    public void setJavaFilePath(String javaFilePath) {
        this.javaFilePath = javaFilePath;
    }

    public List<MethodRequest> getMethodsToMutate() {
        return methodsToMutate;
    }

    public void setMethodsToMutate(List<MethodRequest> methodsToMutate) {
        this.methodsToMutate = methodsToMutate;
    }

    public List<Integer> getLinesToMutate() {
        return linesToMutate;
    }

    public void setLinesToMutate(List<Integer> linesToMutate) {
        this.linesToMutate = linesToMutate;
    }

    private Launcher createLauncher() {
        Launcher l = new Launcher();
        l.addInputResource(javaFilePath);
        l.getEnvironment().setCommentEnabled(false);
        CtModel model = l.buildModel();
        return l;
    }

    /**
     * collects a list of mutation locations.
     */
    public void locateTokens() {
        Launcher l = createLauncher();

        List<CtClass> origClasses = l.getFactory().Package().getRootPackage()
                .getElements(new TypeFilter<CtClass>(CtClass.class));
        if (origClasses == null || origClasses.isEmpty()) {
            System.err.println("Ignored File: No class found in " + javaFilePath);
            return;
        }

        CtClass origClass = origClasses.get(0);

        // iterate on each method
        List<CtElement> methodsToBeMutated = origClass.getElements(arg0 -> (arg0 instanceof CtMethod && isMethodToMutate((CtMethod) arg0)));

        for (CtElement met : methodsToBeMutated) {
            // iterate on each method
            CtMethod method = (CtMethod) met;
            // mutate the element
            SourcePosition sourcePosition = getSourcePosition(method);
            int methodStartLineNumber = sourcePosition.getLine();
            int methodEndLine = sourcePosition.getEndLine();
            CodePosition methodCodePosition = new CodePosition(sourcePosition.getSourceStart(), sourcePosition.getSourceEnd());

            List<CtElement> elementsToBeMutated = method.getElements(arg0 ->
                    isToBeProcessed(arg0) && isLineToMutate(getSourcePosition(arg0).getLine()));

            for (CtElement e : elementsToBeMutated) {

                try {
                    locationsCollector
                            .addLocation(javaFilePath, origClass.getQualifiedName(),
                                    method.getSignature(),
                                    getSourcePosition(e).getLine(),
                                    MBertLocation.createMBertLocation(nextMutantId, e),
                                    methodStartLineNumber, methodEndLine, methodCodePosition);
                    nextMutantId += 5;
                } catch (MBertLocation.UnhandledElementException exception) {
                    locationsCollector.addUnhandledMutations(exception.getNodeType());
                    System.err.println(exception);
                }

            }
        }
    }

    protected boolean isMethodToMutate(CtMethod arg0) {
        if ((methodsToMutate == null || methodsToMutate.isEmpty())
                && (linesToMutate == null || linesToMutate.isEmpty())
                && excludeFileRequest == null) // exhaustive search.
        {
            System.out.println("Exhaustive search in " + javaFilePath + " \n - no line or method specified.");
            return true;
        } else if (excludeFileRequest != null && !excludeFileRequest.isMethodToMutate(arg0)) {
            return false;
        }
        if (methodsToMutate != null) {
            for (MethodRequest methodRequest : methodsToMutate) {
                if (methodRequest.getMethodName().equals(arg0.getSimpleName())) return true;
            }
        }
        if (linesToMutate != null && !linesToMutate.isEmpty()) {
            SourcePosition sourcePosition = getSourcePosition(arg0);
            int startLine = sourcePosition.getLine();
            int endLine = sourcePosition.getEndLine();
            for (Integer line : linesToMutate) {
                if (startLine <= line && endLine >= line) return true;
            }
        }
        return false;
    }


    public boolean isLineToMutate(int line) {
        //is the line selected to be mutated?
        if (linesToMutate.isEmpty())
            return true;
        if (linesToMutate.contains(line))
            return true;
        return false;
    }

    @Override
    public String toString() {
        return "FileRequest{" +
                "javaFilePath='" + javaFilePath + '\'' +
                ", methodsToMutate=" + methodsToMutate +
                ", linesToMutate=" + linesToMutate +
                ", mutantId=" + nextMutantId +
                ", excluding_request=" + excludeFileRequest.toString() +
                '}';
    }

    public void setLocationsCollector(LocationsCollector locationsCollector) {
        this.locationsCollector = locationsCollector;
    }


    public int getNextMutantId() {
        return nextMutantId;
    }

    public void setNextMutantId(int nextMutantId) {
        this.nextMutantId = nextMutantId;
    }

}
