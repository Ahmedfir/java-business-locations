package edu.lu.uni.serval.javabusinesslocs;

import edu.lu.uni.serval.javabusinesslocs.cli.CliRequest;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

import static edu.lu.uni.serval.javabusinesslocs.locations.BusinessLocation.CONDITIONS_AS_TKN;

public class GetLocations {

    public static void main(String... args) {
        //String[] req  = {"-in=src/test/resources/javafile/ExtendedBufferedReader.java::", "-out=src/test/resources/tmp"};
        //CONDITIONS_AS_TKN = true;
        try {
            CliRequest cliRequest = CliRequest.parseArgs(args);
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
