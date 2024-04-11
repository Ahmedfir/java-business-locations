package edu.lu.uni.serval.javabusinesslocs.utils;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class CsvReader {


    public static List<String[]> readAll(String file) throws IOException, CsvException {
        CSVReader csvReader = null;
        try {
            FileReader filereader = new FileReader(file);
            csvReader = new CSVReaderBuilder(filereader)
                    .withSkipLines(1)
                    .build();
            return csvReader.readAll();
        } finally {
            if (csvReader != null) {
                try {
                    csvReader.close();
                } catch (Throwable ignored) {
                }
            }
        }
    }

}
