package edu.lu.uni.serval.javabusinesslocs.output;


import java.util.ArrayList;

import java.util.List;
import java.util.Objects;

public class MethodLocations extends Mappable<Integer, LineLocations> implements Queryable<String> {

    private int startLineNumber;
    private int endLineNumber;
    private String methodSignature;
    private CodePosition codePosition;
    private List<LineLocations> line_predictions;

    public MethodLocations() {
    }

    private MethodLocations(int startLineNumber, int endLineNumber, String methodSignature, CodePosition codePosition) {
        this.startLineNumber = startLineNumber;
        this.endLineNumber = endLineNumber;
        this.methodSignature = methodSignature;
        this.codePosition = codePosition;
    }

    public static MethodLocations newInstance(String methodSignature, int line, Location location,
                                              int methodStartLineNumber, int methodEndLine, CodePosition methodCodePosition) {
        MethodLocations pred = new MethodLocations(methodStartLineNumber, methodEndLine, methodSignature, methodCodePosition);
        pred.addPredictions(line, location);
        return pred;
    }

    public void addPredictions(int line, Location location) {
        LineLocations pred = getChildrenByQuery(line);
        if (pred == null){
            pred = LineLocations.newInstance(line, location);
            if (line_predictions == null) {
                line_predictions = new ArrayList<>();
            }
            line_predictions.add(pred);
        } else {
            pred.addPredictions(location);
        }
    }


    public int getStartLineNumber() {
        return startLineNumber;
    }

    public int getEndLineNumber() {
        return endLineNumber;
    }

    public CodePosition getCodePosition() {
        return codePosition;
    }


    public String getMethodSignature() {
        return methodSignature;
    }

    public void setMethodSignature(String methodSignature) {
        this.methodSignature = methodSignature;
    }

    public List<LineLocations> getLine_predictions() {
        return line_predictions;
    }

    public void setLine_predictions(List<LineLocations> line_predictions) {
        this.line_predictions = line_predictions;
    }

    public void addPredictions(LineLocations lineLocations) {
        if (line_predictions == null) {
            line_predictions = new ArrayList<>();
        }
        line_predictions.add(lineLocations);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodLocations that = (MethodLocations) o;
        return Objects.equals(methodSignature, that.methodSignature) && Objects.equals(line_predictions, that.line_predictions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(methodSignature, line_predictions);
    }

    @Override
    public String getQueryable() {
        return methodSignature;
    }

    @Override
    public List<LineLocations> getItems() {
        return line_predictions;
    }

}
