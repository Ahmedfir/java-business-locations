package edu.lu.uni.serval.mbertloc.cli;

import edu.lu.uni.serval.mbertloc.mbertlocator.FileRequest;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class CliRequestTest {

    String file_1 = "src/test/resources/javafile/ArgumentImpl.java";
    String lines_1_str = "132@133@135";
    List<Integer> lines_1 = new ArrayList<Integer>() {{
        add(132);
        add(133);
        add(135);
    }};

    String file_2 = "src/test/resources/javafile/WriteableCommandLine.java";
    String lines_2_str = "47";
    List<Integer> lines_2 = new ArrayList<Integer>() {{
        add(47);
    }};

    @Test
    public void parseArgs_multiple_included_files_multiple_lines() {
        String[] req = {"-in=" + file_1 + "::" + lines_1_str, "-in=" + file_2 + "::" + lines_2_str, "-out=out"};
        CliRequest cliRequest = CliRequest.parseArgs(req);
        assertNotNull(cliRequest.getFileRequests());
        assertEquals("out", cliRequest.getOutputDir());
        assertEquals(2, cliRequest.getFileRequests().size());
        FileRequest req1 = cliRequest.getFileRequests().get(0);
        FileRequest req2 = cliRequest.getFileRequests().get(1);
        assertTrue(file_1.equals(req1.getJavaFilePath()) && lines_1.containsAll(req1.getLinesToMutate()) && req1.getLinesToMutate().containsAll(lines_1)
                && file_2.equals(req2.getJavaFilePath()) && lines_2.containsAll(req2.getLinesToMutate()) && req2.getLinesToMutate().containsAll(lines_2) ||
                file_2.equals(req1.getJavaFilePath()) && lines_2.containsAll(req1.getLinesToMutate()) && req1.getLinesToMutate().containsAll(lines_2)
                        && file_1.equals(req2.getJavaFilePath()) && lines_1.containsAll(req2.getLinesToMutate()) && req2.getLinesToMutate().containsAll(lines_1));
    }

    @Test
    public void parseArgs_multiple_excluded_files_multiple_lines() {
        String[] req = {"-ex=" + file_1 + "::" + lines_1_str, "-ex=" + file_2 + "::" + lines_2_str, "-out=out"};
        CliRequest cliRequest = CliRequest.parseArgs(req);
        assertNotNull(cliRequest.getFileRequests());
        assertEquals("out", cliRequest.getOutputDir());
        assertEquals(2, cliRequest.getFileRequests().size());
        FileRequest req1 = cliRequest.getFileRequests().get(0);
        FileRequest req2 = cliRequest.getFileRequests().get(1);
        assertTrue(req1 instanceof ExcludeFileRequest);
        assertTrue(req2 instanceof ExcludeFileRequest);
        assertTrue(file_1.equals(req1.getJavaFilePath()) && lines_1.containsAll(req1.getLinesToMutate()) && req1.getLinesToMutate().containsAll(lines_1)
                && file_2.equals(req2.getJavaFilePath()) && lines_2.containsAll(req2.getLinesToMutate()) && req2.getLinesToMutate().containsAll(lines_2)
                ||
                file_2.equals(req1.getJavaFilePath()) && lines_2.containsAll(req1.getLinesToMutate()) && req1.getLinesToMutate().containsAll(lines_2)
                        && file_1.equals(req2.getJavaFilePath()) && lines_1.containsAll(req2.getLinesToMutate()) && req2.getLinesToMutate().containsAll(lines_1));
    }
}