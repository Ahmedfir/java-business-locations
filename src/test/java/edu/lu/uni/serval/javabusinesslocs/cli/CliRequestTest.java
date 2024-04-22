package edu.lu.uni.serval.javabusinesslocs.cli;

import edu.lu.uni.serval.javabusinesslocs.locator.FileRequest;
import edu.lu.uni.serval.javabusinesslocs.locator.LocationsCollector;
import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static edu.lu.uni.serval.javabusinesslocs.locations.BusinessLocation.IF_CONDITIONS_AS_TKN;
import static edu.lu.uni.serval.javabusinesslocs.locations.BusinessLocation.CONDITIONS_AS_TKN;
import static org.junit.Assert.*;

public class CliRequestTest {

    private static final String FILE_3 = "src/test/resources/javafile/Role.java";
    private static final String FILE_4 = "src/test/resources/javafile/User.java";
    private static final String FILE_5 = "src/test/resources/javafile/UserRole.java";

    private static final String file_with_conditions = "src/test/resources/javafile/ExtendedBufferedReader.java";
    private static final String file_1 = "src/test/resources/javafile/ArgumentImpl.java";
    private static final String lines_1_str = "109@115@124@126";
    private static final List<Integer> lines_1 = new ArrayList<Integer>() {{
        add(109);
        add(115);
        add(124);
        add(126);
    }};

    private static final String file_2 = "src/test/resources/javafile/WriteableCommandLine.java";
    private static final String lines_2_str = "47";
    private static final List<Integer> lines_2 = new ArrayList<Integer>() {{
        add(47);
    }};

    private final static FileRequest IFREQ1 = new FileRequest(file_1, null, lines_1);
    private final static FileRequest EFREQ1 = new ExcludeFileRequest(file_1, null, lines_1);
    private final static FileRequest IFREQ2 = new FileRequest(file_2, null, lines_2);
    private final static FileRequest EFREQ2 = new ExcludeFileRequest(file_2, null, lines_2);

    private static Path outputDir;
    private static Path expectedDir;

    @BeforeClass
    public static void beforeClass() throws Exception {
        expectedDir = Paths.get("src/test/resources/expected").resolve(CliRequestTest.class.getSimpleName());
        outputDir = Paths.get("src/test/resources/tmp").resolve(CliRequestTest.class.getSimpleName());
        Files.createDirectories(outputDir);
    }

    @Test
    public void parseArgs_multiple_included_files_multiple_lines() {
        String[] req = {"-in=" + file_1 + "::" + lines_1_str, "-in=" + file_2 + "::" + lines_2_str, "-out=out", "-n=5"};
        CliRequest cliRequest = CliRequest.parseArgs(req);
        assertNotNull(cliRequest.fileRequests);
        assertEquals("out", cliRequest.outputDir);
        assertEquals(2, cliRequest.fileRequests.size());
        assertEquals(new Integer(5), cliRequest.numberOfTokens);
        FileRequest req1 = cliRequest.fileRequests.get(0);
        FileRequest req2 = cliRequest.fileRequests.get(1);
        assertEquals(IFREQ1, req1);
        assertEquals(IFREQ2, req2);
        assertTrue(file_1.equals(req1.getJavaFilePath()) && lines_1.containsAll(req1.getLinesToMutate()) && req1.getLinesToMutate().containsAll(lines_1)
                && file_2.equals(req2.getJavaFilePath()) && lines_2.containsAll(req2.getLinesToMutate()) && req2.getLinesToMutate().containsAll(lines_2)
        );
    }

    // fixme move this somewhere else
    @Test
    public void test_equals_INFR_EXFR() {
        assertNotEquals(IFREQ1, EFREQ1);
        assertNotEquals(IFREQ2, EFREQ2);
        assertNotEquals(EFREQ1, EFREQ2);
        assertNotEquals(IFREQ1, IFREQ2);
    }

    @Test
    public void parseArgs_multiple_excluded_files_multiple_lines() {
        String[] req = {"-ex=" + file_1 + "::" + lines_1_str, "-ex=" + file_2 + "::" + lines_2_str, "-out=out"};
        CliRequest cliRequest = CliRequest.parseArgs(req);
        assertNotNull(cliRequest.fileRequests);
        assertEquals("out", cliRequest.outputDir);
        assertEquals(2, cliRequest.fileRequests.size());
        assertNull(cliRequest.numberOfTokens);
        FileRequest req1 = cliRequest.fileRequests.get(0);
        FileRequest req2 = cliRequest.fileRequests.get(1);
        assertTrue(req1 instanceof ExcludeFileRequest);
        assertTrue(req2 instanceof ExcludeFileRequest);
        assertEquals(EFREQ1, req1);
        assertEquals(EFREQ2, req2);
        assertTrue(file_1.equals(req1.getJavaFilePath()) && lines_1.containsAll(req1.getLinesToMutate()) && req1.getLinesToMutate().containsAll(lines_1)
                && file_2.equals(req2.getJavaFilePath()) && lines_2.containsAll(req2.getLinesToMutate()) && req2.getLinesToMutate().containsAll(lines_2)
        );
    }


    @Test
    public void sys_test__if_condition_location() throws IOException {
        Path expectDir = expectedDir.resolve("sys_test__if_condition_location");
        //assertTrue(expectDir.toFile().isDirectory());
        Path expectedJson = expectDir.resolve(LocationsCollector.DEFAULT_JSON_LOCATIONS_FILE_NAME);
        File expectedFile = expectedJson.toFile();
        //assertTrue(expectedFile.isFile());

        Path outDir = outputDir.resolve("sys_test__if_condition_location");
        Files.createDirectories(outDir);
        //assertTrue(outputDir.toFile().isDirectory());
        File outFile = outDir.resolve(LocationsCollector.DEFAULT_JSON_LOCATIONS_FILE_NAME).toFile();

        String[] req = {"-in=" + file_with_conditions + "::" , "-out=" + outDir};
        CONDITIONS_AS_TKN = true;
        CliRequest cliRequest = CliRequest.parseArgs(req);
        cliRequest.start();
        assertTrue("The files differ!", FileUtils.contentEquals(expectedFile, outFile));
    }

    @Test
    public void sys_test__process_one_files_multiple_lines() throws IOException {
        Path expectDir = expectedDir.resolve("sys_test__process_one_files_multiple_lines");
        assertTrue(expectDir.toFile().isDirectory());
        Path expectedJson = expectDir.resolve(LocationsCollector.DEFAULT_JSON_LOCATIONS_FILE_NAME);
        File expectedFile = expectedJson.toFile();
        assertTrue(expectedFile.isFile());

        Path outDir = outputDir.resolve("sys_test__process_one_files_multiple_lines");
        Files.createDirectories(outDir);
        assertTrue(outputDir.toFile().isDirectory());
        File outFile = outDir.resolve(LocationsCollector.DEFAULT_JSON_LOCATIONS_FILE_NAME).toFile();

        String[] req = {"-in=" + file_1 + "::" + lines_1_str, "-out=" + outDir, "-n=5"};
        CliRequest cliRequest = CliRequest.parseArgs(req);
        cliRequest.start();
        assertTrue("The files differ!", FileUtils.contentEquals(expectedFile, outFile));
    }

    @Test
    public void sys_test__process_one_file_no_lines() throws IOException {
        Path expectDir = expectedDir.resolve("sys_test__process_one_file_no_lines");
        assertTrue(expectDir.toFile().isDirectory());
        Path expectedJson = expectDir.resolve(LocationsCollector.DEFAULT_JSON_LOCATIONS_FILE_NAME);
        File expectedFile = expectedJson.toFile();
        assertTrue(expectedFile.isFile());

        Path outDir = outputDir.resolve("sys_test__process_one_file_no_lines");
        Files.createDirectories(outDir);
        assertTrue(outputDir.toFile().isDirectory());
        File outFile = outDir.resolve(LocationsCollector.DEFAULT_JSON_LOCATIONS_FILE_NAME).toFile();

        String[] req = {"-in=" + file_1 , "-out=" + outDir, "-n=5","-selection=random"};
        CliRequest cliRequest = CliRequest.parseArgs(req);
        cliRequest.start();
        assertTrue("The files differ!", FileUtils.contentEquals(expectedFile, outFile));
    }

    @Test
    public void sys_test__process_3_files_no_lines() throws IOException {
        Path expectDir = expectedDir.resolve("sys_test__process_3_files_no_lines");
        assertTrue(expectDir.toFile().isDirectory());
        Path expectedJson = expectDir.resolve(LocationsCollector.DEFAULT_JSON_LOCATIONS_FILE_NAME);
        File expectedFile = expectedJson.toFile();
        assertTrue(expectedFile.isFile());

        Path outDir = outputDir.resolve("sys_test__process_3_files_no_lines");
        Files.createDirectories(outDir);
        assertTrue(outputDir.toFile().isDirectory());
        File outFile = outDir.resolve(LocationsCollector.DEFAULT_JSON_LOCATIONS_FILE_NAME).toFile();

        String[] req = {"-in=" + FILE_3 ,"-in=" + FILE_4 ,"-in=" + FILE_5 , "-out=" + outDir};
        CliRequest cliRequest = CliRequest.parseArgs(req);
        cliRequest.start();
        assertTrue("The files differ!", FileUtils.contentEquals(expectedFile, outFile));
    }

    @AfterClass
    public static void afterClass() throws Exception {
        //FileUtils.deleteDirectory(outputDir.toFile());
    }
}