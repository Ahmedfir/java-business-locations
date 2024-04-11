package edu.lu.uni.serval.javabusinesslocs.cli;

import edu.lu.uni.serval.javabusinesslocs.locator.FileRequest;
import edu.lu.uni.serval.javabusinesslocs.locator.LocationsCollector;
import edu.lu.uni.serval.javabusinesslocs.locator.MethodRequest;
import edu.lu.uni.serval.javabusinesslocs.locator.selection.SelectionMode;
import edu.lu.uni.serval.javabusinesslocs.output.FileLocations;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static edu.lu.uni.serval.javabusinesslocs.cli.CliArgPrefix.*;

public class CliRequest {

    private static final String DEFAULT_OUTPUT_DIR = "output";
    public static final String ERROR_MESSAGE = "- available requests features:\n" +
            "You can either include:" +
            "1) a file or \n " +
            "2) specific methods of a file or \n " +
            "3) specific lines of a file or \n" +
            "4) include a file excluding methods or \n " +
            "5) include a file excluding lines\n" +
            "- available arguments:\n" +
            Arrays.toString(values()) + "\n" +
            "- available selection modes: \n" +
            Arrays.toString(SelectionMode.values()) + "\n" +
            "- example args usage:\n" +
            "java edu.lu.uni.serval.javabusinesslocs.GetLocations \n" +
            "-n=10" +
            "-in=source_file_name:method_name@line@line@line:method_name@line@line@line \n" +
            "-in=source_file_name::line@line@line \n" +
            "-ex=exclude_file_name::line_to_exclude@line_to_exclude@line_to_exclude \n" +
            "-out=locations_directory\n";
    final List<FileRequest> fileRequests;
    final String outputDir;
    final Integer numberOfTokens;
    final SelectionMode selectionMode;

    public static CliRequest parseArgs(String[] args) {
        List<FileRequest> fileRequests = new ArrayList<>();
        List<FileRequest> fileExcludeRequests = new ArrayList<>();
        String locationsOutputDirectory = DEFAULT_OUTPUT_DIR;
        Integer numberOfTokens = null;
        SelectionMode selectionMode = SelectionMode.ORDERED;

        for (String arg : args) {
            try {
                CliArgPrefix cliArgPrefix = CliArgPrefix.startsWithPrefix(arg);
                String argBody = arg.replace(cliArgPrefix.argPrefix, "");

                switch (cliArgPrefix) {
                    case FILE_INCLUDE_REQUEST:
                        fileRequests.add(parseFileArgs(argBody, cliArgPrefix));
                        break;
                    case FILE_EXCLUDE_REQUEST:
                        fileExcludeRequests.add(parseFileArgs(argBody, cliArgPrefix));
                        break;
                    case OUTPUT_DIR:
                        locationsOutputDirectory = argBody;
                        break;
                    case NUMBER_OF_TOKENS:
                        numberOfTokens = Integer.parseInt(argBody);
                        break;
                    case SELECTION_MODE:
                        selectionMode = SelectionMode.forId(argBody);
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

        return new CliRequest(fileRequests, locationsOutputDirectory, numberOfTokens, selectionMode);
    }

    private CliRequest(List<FileRequest> fileRequests, String outputDir, Integer numberOfTokens, SelectionMode selectionMode) {
        this.fileRequests = fileRequests;
        this.outputDir = outputDir;
        this.numberOfTokens = numberOfTokens;
        this.selectionMode = selectionMode;
    }

    public List<FileLocations> start() throws IOException {
        LocationsCollector locationsCollector = new LocationsCollector(outputDir);
        int nextMutantId = 0;
        for (FileRequest fileRequest : fileRequests) {
            fileRequest.setLocationsCollector(locationsCollector);
            fileRequest.setNextMutantId(nextMutantId);
            System.out.println("--- locating... \n" + fileRequest + "\n");
            fileRequest.locateTokens(numberOfTokens, selectionMode); // TODO: 26/04/2022 forgot what this was about??
            if (fileRequest.numberOfTokensAchieved(numberOfTokens))
                break; // number of tokens achieved.
            nextMutantId = fileRequest.getNextMutantId();
        }
        return locationsCollector.outputResults();
    }


    private static FileRequest parseFileArgs(String argBody, CliArgPrefix cliArgPrefix) {
        assert cliArgPrefix == FILE_INCLUDE_REQUEST || cliArgPrefix == FILE_EXCLUDE_REQUEST;
        String[] splits = argBody.split(":");
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
                ", selectionMode=" + selectionMode +
                '}';
    }
}
