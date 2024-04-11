package edu.lu.uni.serval.javabusinesslocs.locations;

import edu.lu.uni.serval.javabusinesslocs.utils.Checker;
import org.apache.commons.io.FileUtils;
import spoon.reflect.declaration.CtElement;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

public class TextualPositionChecker implements PositionChecker<String> {

    private final String filePath;
    private WeakReference<String> contentRef = null;
    private String encoding = "UTF-8";


    public TextualPositionChecker(String filePath) {
        this.filePath = filePath;
    }

    public String getFileContent() throws IOException {
        String fileContent = contentRef != null ? contentRef.get() : null;
        if (Checker.isTrimNlOrEmpty(fileContent)) {
            fileContent = FileUtils.readFileToString(new File(filePath), encoding);
            this.contentRef = new WeakReference<String>(fileContent);
        }
        return fileContent;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    @Override
    public boolean isValidPosition(String value, int startChar, int endChar) throws IOException {
        String check = getFileContent().substring(startChar, endChar).trim();
        System.out.println("Checking <" + check + "> matches <" + value + "> in [" + startChar + "," + endChar + "]");
        return check.trim().equals(value);
    }
}
