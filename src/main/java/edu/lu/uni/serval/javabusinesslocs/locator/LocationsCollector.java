package edu.lu.uni.serval.javabusinesslocs.locator;

import com.google.gson.Gson;
import edu.lu.uni.serval.javabusinesslocs.output.CodePosition;
import edu.lu.uni.serval.javabusinesslocs.output.FileLocations;
import edu.lu.uni.serval.javabusinesslocs.output.Location;
import edu.lu.uni.serval.javabusinesslocs.output.Mappable;
import edu.lu.uni.serval.javabusinesslocs.utils.GsonHolder;


import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Singleton class to collect all mutants.
 */
public class LocationsCollector extends Mappable<String, FileLocations> {

    public static final String DEFAULT_JSON_LOCATIONS_FILE_NAME = "locations.json";
    private String locationsFileName = DEFAULT_JSON_LOCATIONS_FILE_NAME;

    private final List<FileLocations> fileLocations;
    private final Map<String, Integer> unhandledTokens;
    private String locationsDirPath;


    public LocationsCollector(String locationsDirPath) {
        this.fileLocations = new ArrayList<>();
        this.unhandledTokens = new HashMap<>();
        this.locationsDirPath = locationsDirPath;
    }


    public List<FileLocations> outputResults() {

        // print the results to file as json.
        Gson gson = GsonHolder.getGson();
        try {
            Files.createDirectories(Paths.get(locationsDirPath));
            String statsFilePath = Paths.get(locationsDirPath).resolve(locationsFileName).toString();
            System.out.println("printing_json_results in " + statsFilePath);
            FileWriter writer = new FileWriter(statsFilePath);
            gson.toJson(fileLocations, writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("++++");
            System.out.println(gson.toJson(fileLocations));
        }
        return fileLocations;
    }

    public void addLocation(String fileToBeMutated, String className,
                            String methodSignature,
                            int line,
                            Location location, int methodStartLineNumber, int methodEndLine, CodePosition methodCodePosition) {
        FileLocations filePrediction = getChildrenByQuery(fileToBeMutated);
        if (filePrediction == null) {
            filePrediction = FileLocations.newInstance(fileToBeMutated, className, methodSignature, line, location,
                    methodStartLineNumber, methodEndLine, methodCodePosition);
            fileLocations.add(filePrediction);
        } else {
            filePrediction.addPredictions(className, methodSignature, line, location,
                    methodStartLineNumber, methodEndLine, methodCodePosition);
        }
    }


    @Override
    public List<FileLocations> getItems() {
        return fileLocations;
    }

    public void addUnhandledMutations(String nodeType) {
        Integer count = unhandledTokens.get(nodeType);
        if (count == null)
            count = 0;
        unhandledTokens.put(nodeType, count + 1);
    }

    public void outputUnhandledTokens() {
        System.out.println(" - unhandled tokens stats : \ncount , nodeType \n");
        for (String nodeType : unhandledTokens.keySet()) {
            System.out.println(unhandledTokens.get(nodeType) + " , " + nodeType + " \n");
        }
    }
}
