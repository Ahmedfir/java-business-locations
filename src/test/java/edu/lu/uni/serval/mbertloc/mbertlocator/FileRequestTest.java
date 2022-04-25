package edu.lu.uni.serval.mbertloc.mbertlocator;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class FileRequestTest {

    @Test
    public void isLineToMutate() {
        FileRequest req = new FileRequest("file", null, new ArrayList<Integer>() {{
            add(3);
            add(5);
        }});
        assertFalse(req.isLineToMutate(4));
        assertFalse(req.isLineToMutate(10));
        assertTrue(req.isLineToMutate(3));
        assertTrue(req.isLineToMutate(5));
    }
}