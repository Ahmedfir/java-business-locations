package edu.lu.uni.serval.javabusinesslocs.output;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileLocations extends Mappable<String, ClassLocations> implements Queryable<String> {


    private String file_path;
    private List<ClassLocations> classPredictions;

    public FileLocations() {
    }

    private FileLocations(String file_path) {
        this.file_path = file_path;
    }

    public static FileLocations newInstance(String file_path, String className, String methodSignature, int line, Location location, int methodStartLineNumber, int methodEndLine, CodePosition methodCodePosition) {
        FileLocations filePredictions = new FileLocations(file_path);
        filePredictions.addPredictions(className, methodSignature, line, location, methodStartLineNumber, methodEndLine, methodCodePosition);
        return filePredictions;
    }

    public boolean addPredictions(String className, String methodSignature, int line, Location location,
                               int methodStartLineNumber, int methodEndLine, CodePosition methodCodePosition) {
        ClassLocations classPrediction = getChildrenByQuery(className);
        if (classPrediction == null){
            classPrediction = ClassLocations.newInstance(className, methodSignature, line, location,
                    methodStartLineNumber, methodEndLine, methodCodePosition);
            if (classPredictions == null){
                classPredictions = new ArrayList<>();
            }
            classPredictions.add(classPrediction);
            return true;
        } else {
            return classPrediction.addPredictions(methodSignature, line, location,
                    methodStartLineNumber, methodEndLine, methodCodePosition);
        }
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public List<ClassLocations> getClassPredictions() {
        return classPredictions;
    }

    public void setClassPredictions(List<ClassLocations> classPredictions) {
        this.classPredictions = classPredictions;
    }

    public void addPredictions(ClassLocations predictions) {
        if (classPredictions == null) {
            classPredictions = new ArrayList<>();
        }
        classPredictions.add(predictions);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileLocations that = (FileLocations) o;
        return Objects.equals(file_path, that.file_path) && Objects.equals(classPredictions, that.classPredictions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(file_path, classPredictions);
    }

    @Override
    public String getQueryable() {
        return file_path;
    }

    @Override
    public List<ClassLocations> getItems() {
        return classPredictions;
    }
}
