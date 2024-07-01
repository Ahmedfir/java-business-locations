package edu.lu.uni.serval.javabusinesslocs.locator;

import edu.lu.uni.serval.javabusinesslocs.locations.BusinessLocation;
import edu.lu.uni.serval.javabusinesslocs.locator.selection.*;
import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.declaration.*;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static edu.lu.uni.serval.javabusinesslocs.locator.LocsUtils.getSourcePosition;
import static edu.lu.uni.serval.javabusinesslocs.locator.LocsUtils.isMethod;


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

    public List<Integer> getLinesToMutate() {
        return linesToMutate;
    }


    private Launcher createLauncher() {
        Launcher l = new Launcher();
        l.addInputResource(javaFilePath);
        l.getEnvironment().setCommentEnabled(false);
        CtModel model = l.buildModel();
        return l;
    }

    /**
     * return the methods corresponding to the passed-lines.
     */
    public void locateMethods() {
        Launcher l = createLauncher();

        List<CtClass> origClasses = l.getFactory().Package().getRootPackage()
                .getElements(new TypeFilter<>(CtClass.class));
        if (origClasses == null || origClasses.isEmpty()) {
            System.err.println("Ignored File: No class found in " + javaFilePath);
            return;
        }

        CtClass origClass = origClasses.get(0);

        // iterate on each method
        List<CtElement> methodsToBeMutated = origClass.getElements(arg0 -> (isMethod(arg0) && getSourcePosition(arg0) != null && isMethodToMutate((CtExecutable) arg0)));
        if (methodsToBeMutated == null || methodsToBeMutated.isEmpty()) {
            System.err.println("Ignored File: No method found in " + javaFilePath);
            return;
        }

        String classQualifiedName = origClass.getQualifiedName();
        ElementsSelector selector = new OrderedSelection(methodsToBeMutated) {
            @Override
            public boolean isLineToMutate(int line) {
                return FileRequest.this.isLineToMutate(line);
            }
        };


        while (selector.hasNext()) {
            Element element = selector.next();
            if (element != null) {
                try {
                    Set<BusinessLocation> businessLocs = BusinessLocation.createBusinessLocation(nextMutantId, element.ctElement);
                    for (BusinessLocation businessLoc : businessLocs) {
                        businessLoc.setFirstMutantId(nextMutantId);
                        locationsCollector.addLocation(javaFilePath, classQualifiedName, element.method.signature,
                                getSourcePosition(element.ctElement).getLine(), businessLoc, element.method.startLine,
                                element.method.endLine, element.method.codePosition);
                        nextMutantId += 5;
                    }
                } catch (BusinessLocation.UnhandledElementException exception) {
                    locationsCollector.addUnhandledMutations(exception.getNodeType());
                    System.err.println(exception);
                }
            }
        }
    }

    /**
     * collects a list of mutation locations.
     *
     * @param numberOfTokens number of tokens or locations to mask. if it's null, there's no limit.
     */
    public void locateTokens(Integer numberOfTokens, SelectionMode selectionMode) {
        Launcher l = createLauncher();

        List<CtClass> origClasses = l.getFactory().Package().getRootPackage()
                .getElements(new TypeFilter<>(CtClass.class));
        if (origClasses == null || origClasses.isEmpty()) {
            System.err.println("Ignored File: No class found in " + javaFilePath);
            return;
        }

        CtClass origClass = origClasses.get(0);

        // iterate on each method
        List<CtElement> methodsToBeMutated = origClass.getElements(arg0 -> (isMethod(arg0) && getSourcePosition(arg0) != null && isMethodToMutate((CtExecutable) arg0)));
        if (methodsToBeMutated == null || methodsToBeMutated.isEmpty()) {
            System.err.println("Ignored File: No method found in " + javaFilePath);
            return;
        }

        String classQualifiedName = origClass.getQualifiedName();
        ElementsSelector selector;
        switch (selectionMode) {
            case RANDOM:
                selector = new RandomSelection(methodsToBeMutated) {
                    @Override
                    public boolean isLineToMutate(int line) {
                        return FileRequest.this.isLineToMutate(line);
                    }
                };
                break;
            case ORDERED:
            default:
                selector = new OrderedSelection(methodsToBeMutated) {
                    @Override
                    public boolean isLineToMutate(int line) {
                        return FileRequest.this.isLineToMutate(line);
                    }
                };
                break;
        }

        while (selector.hasNext()) {
            Element element = selector.next();

            if (element != null) {
                try {
                    Set<BusinessLocation> businessLocs = BusinessLocation.createBusinessLocation(nextMutantId, element.ctElement);
                    for (BusinessLocation businessLoc : businessLocs) {
                        businessLoc.setFirstMutantId(nextMutantId);

                        boolean added = locationsCollector.addLocation(javaFilePath, classQualifiedName, element.method.signature,
                                getSourcePosition(element.ctElement).getLine(), businessLoc, element.method.startLine,
                                element.method.endLine, element.method.codePosition);
                        if(added)
                            nextMutantId += 5;
                    }
                    if (numberOfTokensAchieved(numberOfTokens)) {
                        break;
                    }
                } catch (BusinessLocation.UnhandledElementException exception) {
                    locationsCollector.addUnhandledMutations(exception.getNodeType());
                    System.err.println(exception);
                }
            }
        }
    }

    protected boolean isMethodToMutate(CtExecutable arg0) {
        if ((methodsToMutate == null || methodsToMutate.isEmpty())
                && (linesToMutate == null || linesToMutate.isEmpty())
                && excludeFileRequest == null) // exhaustive search.
        {
            //System.out.println("Exhaustive search in " + javaFilePath + " \n - no line or method specified.");
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
        if (linesToMutate == null || linesToMutate.isEmpty())
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
                ", excluding_request=" + excludeFileRequest +
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

    /**
     * @param numberOfTokens number of tokens or locations to mask. if it's null, there's no limit.
     * @return true if the numberOfTokens is not null and is achieved, otherwise false.
     */
    public boolean numberOfTokensAchieved(Integer numberOfTokens) {
        return numberOfTokens != null && nextMutantId / 5 >= numberOfTokens;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileRequest that = (FileRequest) o;
        return nextMutantId == that.nextMutantId && javaFilePath.equals(that.javaFilePath) && Objects.equals(methodsToMutate, that.methodsToMutate) && Objects.equals(linesToMutate, that.linesToMutate) && Objects.equals(excludeFileRequest, that.excludeFileRequest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(javaFilePath, methodsToMutate, linesToMutate, nextMutantId, excludeFileRequest);
    }
}
