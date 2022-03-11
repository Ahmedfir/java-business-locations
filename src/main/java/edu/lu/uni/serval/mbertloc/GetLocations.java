package edu.lu.uni.serval.mbertloc;


import edu.lu.uni.serval.mbertloc.mbertlocator.FileRequest;
import edu.lu.uni.serval.mbertloc.mbertlocator.LocationsCollector;
import edu.lu.uni.serval.mbertloc.mbertlocator.MethodRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GetLocations {

    // todo add some variable checking : files exists...
    public static FileRequest parseFileArgs(String arg){
        String request = arg.replace("-in=", "");
        String[] splits = request.split(":");
        String filePath = splits[0];
        List<MethodRequest> methods = null;
        List<Integer> lines = null;
        if (splits.length > 1){
            if (splits[1].isEmpty()){
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
                    if (methodsRequestArr.length > 1){
                        List<Integer> linesPerMethod = new ArrayList<>();
                        for (int lineIndex = 1; lineIndex< methodsRequestArr.length; lineIndex++){
                            linesPerMethod.add(Integer.valueOf(methodsRequestArr[lineIndex]));
                        }
                        methodRequest.setLinesToMutate(linesPerMethod);
                    }
                    methods.add(methodRequest);
                }
            }
        }

        FileRequest fileRequest = new FileRequest(filePath,methods,lines);
        return fileRequest;
    }

    public static void main(String...args) throws IOException {
        List<FileRequest> fileRequests = new ArrayList<>();
        String locationsOutputDirectory = "output";


        for (int i = 0; i< args.length; i++ ) {
            if (args[i].startsWith("-in=")) {
                fileRequests.add(parseFileArgs(args[i]));
            } else if (args[i].startsWith("-out=")) {
                locationsOutputDirectory = args[i].replace("-out=", "");
            }
        }

        if (fileRequests.isEmpty()) {
            correctUssage("No File passed!");
            System.exit(1);
        }

        System.out.println("--- Initialisation ---");
        System.out.println("--- Output to the directory: \n" + locationsOutputDirectory);
        System.out.println("--- Target locations : \n" + fileRequests);

        LocationsCollector locationsCollector = new LocationsCollector(locationsOutputDirectory);
        int nextMutantId = 0;
        for (FileRequest fileRequest : fileRequests) {
            fileRequest.setLocationsCollector(locationsCollector);
            fileRequest.setNextMutantId(nextMutantId);
            System.out.println("--- locating... \n" + fileRequests + "\n");
            fileRequest.locateTokens();
            nextMutantId = fileRequest.getNextMutantId();
        }

        locationsCollector.outputResults();
    }

    private static void correctUssage(String s) {
        System.err.println("error: "+s);
        System.err.println("java edu.lu.uni.serval.mbertloc.GetLocations \n" +
                "-in=source_file_name:method_name@line@line@line:method_name@line@line@line \n" +
                "-in=source_file_name::line@line@line \n" +
                "-out=locations_directory\n" );
    }


}
