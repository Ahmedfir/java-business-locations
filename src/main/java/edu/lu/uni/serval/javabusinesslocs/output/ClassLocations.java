package edu.lu.uni.serval.javabusinesslocs.output;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ClassLocations extends Mappable<String, MethodLocations> implements Queryable<String> {


    private String qualifiedName;
    private List<MethodLocations> methodPredictions;

    public ClassLocations() {
    }

    private ClassLocations(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    public static ClassLocations newInstance(String className, String methodSignature, int line, Location location,
                                             int methodStartLineNumber, int methodEndLine, CodePosition methodCodePosition) {
        ClassLocations pred = new ClassLocations(className);
        pred.addPredictions(methodSignature, line, location, methodStartLineNumber, methodEndLine, methodCodePosition);
        return pred;
    }

    public void addPredictions(String methodSignature, int line, Location location,
                               int methodStartLineNumber, int methodEndLine, CodePosition methodCodePosition) {
        MethodLocations pred = getChildrenByQuery(methodSignature);
        if (pred == null){
            pred = MethodLocations.newInstance(methodSignature, line, location,
                    methodStartLineNumber, methodEndLine, methodCodePosition);
            if (methodPredictions == null){
                methodPredictions = new ArrayList<>();
            }
            methodPredictions.add(pred);
        } else {
            pred.addPredictions(line, location);
        }
    }


    public void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    public List<MethodLocations> getMethodPredictions() {
        return methodPredictions;
    }

    public void setMethodPredictions(List<MethodLocations> methodPredictions) {
        this.methodPredictions = methodPredictions;
    }

    public void addPredictions(MethodLocations methodLocations) {
        if (methodPredictions == null) {
            methodPredictions = new ArrayList<>();
        }
        methodPredictions.add(methodLocations);
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    @Override
    public String getQueryable() {
        return qualifiedName;
    }

    @Override
    public List<MethodLocations> getItems() {
        return methodPredictions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassLocations that = (ClassLocations) o;
        return Objects.equals(qualifiedName, that.qualifiedName) && Objects.equals(methodPredictions, that.methodPredictions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(qualifiedName, methodPredictions);
    }
}
