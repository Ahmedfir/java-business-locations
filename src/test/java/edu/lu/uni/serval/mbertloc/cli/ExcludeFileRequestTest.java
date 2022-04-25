package edu.lu.uni.serval.mbertloc.cli;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ExcludeFileRequestTest {

    @Test
    public void isLineToMutate() {
        ExcludeFileRequest req = new ExcludeFileRequest("file", null, new ArrayList<Integer>() {{
            add(3);
            add(5);
        }});
        assertTrue(req.isLineToMutate(4));
        assertTrue(req.isLineToMutate(10));
        assertFalse(req.isLineToMutate(3));
        assertFalse(req.isLineToMutate(5));
    }
}