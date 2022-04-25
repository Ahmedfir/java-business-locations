package edu.lu.uni.serval.mbertloc.cli;

import edu.lu.uni.serval.mbertloc.mbertlocator.FileRequest;
import edu.lu.uni.serval.mbertloc.mbertlocator.MethodRequest;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static edu.lu.uni.serval.mbertloc.cli.CliArgPrefix.*;

public class CliRequest {

    private static final String DEFAULT_OUTPUT_DIR = "output";
    public static final String ERROR_MESSAGE = "- available requests:\n" +
            "You can either include:" +
            "1) a file or \n " +
            "2) specific methods of a file or \n " +
            "3) specific lines of a file or \n" +
            "4) include a file excluding methods or \n " +
            "5) include a file excluding lines\n" +
            "- example args usage:\n" +
            "java edu.lu.uni.serval.mbertloc.GetLocations \n" +
            "-n=10" +
            "-in=source_file_name:method_name@line@line@line:method_name@line@line@line \n" +
            "-in=source_file_name::line@line@line \n" +
            "-ex=exclude_file_name::line_to_exclude@line_to_exclude@line_to_exclude \n" +
            "-out=locations_directory\n";
    private final List<FileRequest> fileRequests;
    private final String outputDir;
    private final Integer numberOfTokens;

    public static CliRequest parseArgs(String[] args) {
        List<FileRequest> fileRequests = new ArrayList<>();
        List<FileRequest> fileExcludeRequests = new ArrayList<>();
        String locationsOutputDirectory = "output";
        Integer numberOfTokens = null;

        for (String arg : args) {
            try {
                CliArgPrefix cliArgPrefix = CliArgPrefix.startsWithPrefix(arg);
                switch (cliArgPrefix) {
                    case FILE_INCLUDE_REQUEST:
                        fileRequests.add(parseFileArgs(arg, FILE_INCLUDE_REQUEST));
                        break;
                    case FILE_EXCLUDE_REQUEST:
                        fileExcludeRequests.add(parseFileArgs(arg, FILE_EXCLUDE_REQUEST));
                        break;
                    case OUTPUT_DIR:
                        locationsOutputDirectory = arg.replace(OUTPUT_DIR.argPrefix, "");
                        break;
                    case NUMBER_OF_TOKENS:
                        numberOfTokens = Integer.parseInt(arg.replace(OUTPUT_DIR.argPrefix, ""));
                        break;
                }
            } catch (IllegalArgumentException e) {
                correctUssage("Wrong arg prefix!", e);
            }
        }

        if (fileRequests.isEmpty() && fileExcludeRequests.isEmpty()) {
            correctUssage("No File passed!");
            System.exit(1);
        } else if (fileRequests.isEmpty()) {
            fileRequests = fileExcludeRequests;

        } else if (!fileExcludeRequests.isEmpty()) {
            List<ExcludeFileRequest> excludesToAppend = new ArrayList<>();
            for (FileRequest fileRequest : fileRequests) {
                for (FileRequest fileExcludeRequest : fileExcludeRequests) {
                    if (fileExcludeRequest.getJavaFilePath().equals(fileRequest.getJavaFilePath())) {
                        ExcludeFileRequest req = (ExcludeFileRequest) fileExcludeRequest;
                        fileRequest.setExcludeFileRequest(req);
                        excludesToAppend.add(req);
                    }
                }
            }
            if (!excludesToAppend.isEmpty()) {
                fileExcludeRequests.removeAll(excludesToAppend);
            }
            /*
            If a request comes with excluded requests that are not mapped with including files,
             these files will be included except the indicated lines or methods.
            */
            if (!fileExcludeRequests.isEmpty()) {
                fileRequests.addAll(fileExcludeRequests);
            }
        }

        return new CliRequest(fileRequests, locationsOutputDirectory, numberOfTokens);
    }

    private CliRequest(List<FileRequest> fileRequests, String outputDir, Integer numberOfTokens) {
        this.fileRequests = fileRequests;
        this.outputDir = outputDir;
        this.numberOfTokens = numberOfTokens;
    }

    public List<FileRequest> getFileRequests() {
        return fileRequests;
    }

    public String getOutputDir() {
        return outputDir;
    }


    private static FileRequest parseFileArgs(String arg, CliArgPrefix cliArgPrefix) {
        assert arg.startsWith(cliArgPrefix.argPrefix) : "Wrong arg prefix! " + arg;
        assert cliArgPrefix == FILE_INCLUDE_REQUEST || cliArgPrefix == FILE_EXCLUDE_REQUEST;
        String request = arg.replace(cliArgPrefix.argPrefix, "");
        String[] splits = request.split(":");
        String filePath = splits[0];
        if (!new File(filePath).exists()) {
            correctUssage("wrong file path", new IllegalArgumentException(filePath));
            return null;
        }
        List<MethodRequest> methods = null;
        List<Integer> lines = null;
        if (splits.length > 1) {
            if (splits[1].isEmpty()) {
                lines = new ArrayList<>();
                String linesReq = splits[2];
                String[] linesReqArr = linesReq.split("@");
                for (String s : linesReqArr) {
                    lines.add(Integer.parseInt(s));
                }
            } else {
                String[] methodsRequests = Arrays.copyOfRange(splits, 1, splits.length);
                methods = new ArrayList<>();
                // todo test - refactor (move) this somewhere else.
                for (String methodsRequest : methodsRequests) {
                    String[] methodsRequestArr = methodsRequest.split("@");
                    MethodRequest methodRequest = new MethodRequest(methodsRequestArr[0]);
                    if (methodsRequestArr.length > 1) {
                        List<Integer> linesPerMethod = new ArrayList<>();
                        for (int lineIndex = 1; lineIndex < methodsRequestArr.length; lineIndex++) {
                            linesPerMethod.add(Integer.valueOf(methodsRequestArr[lineIndex]));
                        }
                        methodRequest.setLinesToMutate(linesPerMethod);
                    }
                    methods.add(methodRequest);
                }
            }
        }

        return cliArgPrefix == FILE_INCLUDE_REQUEST ?
                new FileRequest(filePath, methods, lines)
                : new ExcludeFileRequest(filePath, methods, lines);
    }


    private static void correctUssage(String s, RuntimeException... runtimeExceptions) {
        System.err.println("error: " + s);
        System.err.println(ERROR_MESSAGE);
        if (runtimeExceptions != null && runtimeExceptions.length > 0) {
            throw runtimeExceptions[0];
        }
    }

    @Override
    public String toString() {
        return "CliRequest{" +
                "fileRequests=" + fileRequests +
                ", outputDir='" + outputDir + '\'' +
                ", numberOfTokens=" + numberOfTokens +
                '}';
    }

    public Integer getNumberOfTokens() {
        return numberOfTokens;
    }
}
