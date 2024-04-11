package edu.lu.uni.serval.javabusinesslocs.d4j;

import com.opencsv.exceptions.CsvException;
import edu.lu.uni.serval.javabusinesslocs.utils.Checker;

import java.io.IOException;
import java.util.*;

import static edu.lu.uni.serval.javabusinesslocs.utils.CsvReader.readAll;

public class D4jProject {
    private String commentedLinesCsvFile;
    private Set<String> targetFiles;
    private final String pidBid;
    private String repoPath;
    private Map<String, Set<SrcLineChars>> commentedLines;


    public String getRepoPath() {
        return repoPath;
    }

    public D4jProject(String pidBid) {
        this.pidBid = pidBid;
    }

    public D4jProject(String pidBid, String commentedLinesCsvFile, Set<String> targetFiles) {
        this.pidBid = pidBid;
        this.commentedLinesCsvFile = commentedLinesCsvFile;
        this.targetFiles = targetFiles;
    }

    public void checkout(D4jHelper helper) throws IOException {
        this.repoPath = helper.checkoutFixedVersion(this.pidBid);
    }

    public Map<String, Set<SrcLineChars>> loadNotCommentedLines() throws IOException, CsvException {
        if (this.commentedLines == null){
            List<String[]> csvLines = readAll(this.commentedLinesCsvFile);
            Map<String, Set<SrcLineChars>> tmpCommentedLines = new HashMap<>();

            for (String[] csvLine : csvLines) {
                SrcLineChars l = new SrcLineChars(Integer.parseInt(csvLine[1]), Integer.parseInt(csvLine[2]), Integer.parseInt(csvLine[3]));
                if (Checker.isTrimNlOrEmpty(tmpCommentedLines.get(csvLine[0]))){
                    tmpCommentedLines.put(csvLine[0], new HashSet<SrcLineChars>(){{add(l);}});
                } else {
                    tmpCommentedLines.get(csvLine[0]).add(l);
                }
            }

            this.commentedLines = tmpCommentedLines;
        }
        return this.commentedLines;
    }

    public class SrcLineChars{
        public final int line,startChar,endChar;

        public SrcLineChars(int line, int startChar, int endChar) {
            this.line = line;
            this.startChar = startChar;
            this.endChar = endChar;
        }
    }
}
