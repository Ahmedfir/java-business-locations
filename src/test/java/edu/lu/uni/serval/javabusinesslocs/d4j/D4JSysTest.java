package edu.lu.uni.serval.javabusinesslocs.d4j;

import com.opencsv.exceptions.CsvException;
import edu.lu.uni.serval.javabusinesslocs.output.LineLocations;
import edu.lu.uni.serval.javabusinesslocs.output.Location;
import edu.lu.uni.serval.javabusinesslocs.utils.Checker;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

public class D4JSysTest {

    // fixme refactor
    private static final String fixedBugProjectsPath = "/Users/ahmed.khanfir/Documents/ibirpublic/IBIR/D4J/projects/";
    private static final String defects4jPath = "/Users/ahmed.khanfir/Documents/ibirpublic/IBIR/D4J/defects4j/";
    private static final String java8 = "/Library/Java/JavaVirtualMachines/jdk1.8.0_212.jdk/Contents/Home";

    @Test
    public void check_chart10_does_not_have_mutants_in_comments() throws IOException, CsvException {
        D4JProjectSysTest prt = new D4JProjectSysTest("Chart_10", fixedBugProjectsPath, defects4jPath, java8,
                new HashSet<String>() {{
                    add(fixedBugProjectsPath+"Chart_10"+"/source/org/jfree/chart/imagemap/StandardToolTipTagFragmentGenerator.java");
                }}, "src/test/resources/d4j/Chart_10/no_comments_lines.csv");
        prt.checkout();
        prt.treatTargetFiles();

        Set<Integer> commentedLines = new HashSet<>();
        Map<LineLocations, List<Location>> mismatchLocations = new HashMap<>();
        prt.verifyNoTokensOnCommentedLines(commentedLines, mismatchLocations);
        Assert.assertTrue(Checker.isTrimNlOrEmpty(commentedLines));
        Assert.assertTrue("all of these are outside " +mismatchLocations, mismatchLocations.isEmpty());
    }
}
