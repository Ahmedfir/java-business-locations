package edu.lu.uni.serval.mbertloc;

import edu.lu.uni.serval.mbertloc.cli.CliRequest;
import edu.lu.uni.serval.mbertloc.mbertlocator.FileRequest;
import edu.lu.uni.serval.mbertloc.mbertlocator.LocationsCollector;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

public class GetLocations {

    public static void main(String... args) {
        try {
            CliRequest cliRequest = CliRequest.parseArgs(args);
            System.out.println("--- Initialisation --- \n" + cliRequest + "\n -----------------");
            LocationsCollector locationsCollector = new LocationsCollector(cliRequest.getOutputDir());
            int nextMutantId = 0;
            for (FileRequest fileRequest : cliRequest.getFileRequests()) {
                fileRequest.setLocationsCollector(locationsCollector);
                fileRequest.setNextMutantId(nextMutantId);
                System.out.println("--- locating... \n" + fileRequest + "\n");
                fileRequest.locateTokens(cliRequest.getNumberOfTokens());
                if (fileRequest.numberOfTokensAchieved(cliRequest.getNumberOfTokens()))
                    break; // number of tokens achieved.

                nextMutantId = fileRequest.getNextMutantId();
            }
            locationsCollector.outputResults();
        } catch (Throwable throwable) {
            System.err.println("Failed = " + Arrays.toString(args));
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            throwable.printStackTrace(pw);
            System.err.println(sw);
            System.exit(100);
        }
    }


}
