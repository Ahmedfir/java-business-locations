package edu.lu.uni.serval.mbertloc.mbertlocator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MethodRequest {
    private String methodName;
    private List<Integer>  linesToMutate;

    public MethodRequest(String methodName, List<Integer> linesToMutate) {
        this.methodName = methodName;
        this.linesToMutate = linesToMutate;
    }

    public MethodRequest(String methodName) {
        this.methodName = methodName;
        this.linesToMutate = new ArrayList<>();
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<Integer> getLinesToMutate() {
        return linesToMutate;
    }

    public void setLinesToMutate(List<Integer> linesToMutate) {
        this.linesToMutate = linesToMutate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodRequest that = (MethodRequest) o;
        return Objects.equals(methodName, that.methodName) && Objects.equals(linesToMutate, that.linesToMutate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(methodName, linesToMutate);
    }

    @Override
    public String toString() {
        return "MethodRequest{" +
                "methodName='" + methodName + '\'' +
                ", linesToMutate=" + linesToMutate +
                '}';
    }
}
