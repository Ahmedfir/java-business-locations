package edu.lu.uni.serval.javabusinesslocs.d4j;

import com.opencsv.exceptions.CsvException;
import edu.lu.uni.serval.javabusinesslocs.cli.CliRequest;
import edu.lu.uni.serval.javabusinesslocs.output.*;
import edu.lu.uni.serval.javabusinesslocs.utils.Checker;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class D4JProjectSysTest {


    private final D4jProject project;
    private final Set<String> targetFiles;
    private final String commentedLinesCsv;
    private final D4jHelper d4jHelper;
    private final String outputDir;
    private List<FileLocations> resultLocks;


    public D4JProjectSysTest(String pidBid, String fixedBugProjectsPath, String defects4jPath,
                             String java8, Set<String> targetFiles, String commentedLinesCsv) {
        this.project = new D4jProject(pidBid, commentedLinesCsv, targetFiles);
        this.outputDir = "src/test/resources/tmp/D4JProjectSysTest/" + pidBid;
        this.targetFiles = targetFiles;
        this.commentedLinesCsv = commentedLinesCsv;
        this.d4jHelper = new D4jHelper(fixedBugProjectsPath, defects4jPath, this.outputDir, java8);
    }

    public void checkout() throws IOException {
        this.project.checkout(this.d4jHelper);
    }

    public void treatTargetFiles() throws IOException {
        CliRequest cliReq = getCliReq();
        this.resultLocks = cliReq.start();
    }

    public CliRequest getCliReq() {
        String[] req = getCliReqStr().stream().toArray(String[]::new);
        return CliRequest.parseArgs(req);
    }

    public D4jProject getProject() {
        return this.project;
    }

    public List<String> getCliReqStr() {
        List<String> req = new ArrayList<>();
        for (String f : targetFiles) {
            req.add("-in=" + f);
        }
        req.add("-out=" + this.outputDir);
        return req;
    }


    public void verifyNoTokensOnCommentedLines(Set<Integer> commentedLines,
                                               Map<LineLocations, List<Location>> mismatchLocations) throws IOException, CsvException {
        Map<String, Set<D4jProject.SrcLineChars>> notCommentedLines = this.project.loadNotCommentedLines();
        for (FileLocations resultLock : this.resultLocks) {
            Set<D4jProject.SrcLineChars> ncl = notCommentedLines.get(resultLock.getFile_path().replace(this.project.getRepoPath(),""));
            List<Integer> lines = ncl.stream().mapToInt(x -> x.line).boxed().collect(Collectors.toList());
            for (ClassLocations classPrediction : resultLock.getClassPredictions()) {
                for (MethodLocations methodPrediction : classPrediction.getMethodPredictions()) {
                    for (LineLocations line_prediction : methodPrediction.getLine_predictions()) {
                        if (!lines.contains(line_prediction.getLine_number())) {
                            commentedLines.add(line_prediction.getLine_number());
                        } else {
                            D4jProject.SrcLineChars line = ncl.stream().filter(x -> x.line == line_prediction.getLine_number()).findFirst().get();
                            List<Location> list = line_prediction.getLocations().stream().filter(x -> !(x.getCodePosition().getStartPosition() >= line.startChar && x.getCodePosition().getStartPosition() <= line.endChar)).collect(Collectors.toList());
                            if (!Checker.isTrimNlOrEmpty(list)) {
                                mismatchLocations.put(line_prediction, list);
                            }
                        }
                    }
                }
            }
        }
    }
}
