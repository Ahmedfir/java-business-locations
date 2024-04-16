package edu.lu.uni.serval.javabusinesslocs;

import edu.lu.uni.serval.javabusinesslocs.cli.CliRequest;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

import static edu.lu.uni.serval.javabusinesslocs.locations.BusinessLocation.CONDITIONS_AS_TKN;

public class GetLocations {

    public static void main(String... args) {
        try {
            String[] req = {"-in=" + "src/test/resources/javafile/CSVRecord.java" + "::" , "-out=" + "src/test/resources/tmp"};
            CONDITIONS_AS_TKN = true;
            CliRequest cliRequest = CliRequest.parseArgs(req);
            System.out.println("--- Initialisation --- \n" + cliRequest + "\n -----------------");
            cliRequest.start();
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
