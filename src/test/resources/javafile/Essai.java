
package org.apache.commons.csv;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Essai implements Serializable, Iterable<String> {


    /** The values of the record */
    private final String[] values;

    /** The parser that originates this record. This is not serialized. */
    private final transient CSVParser parser;

    public String get(final String name) {
        final Map<String, Integer> headerMap = getHeaderMapRaw();
        if (headerMap == null) {
            throw new IllegalStateException(
                    "No header mapping was specified, the record values can't be accessed by name");
        }
        for (int i = 0; i < values.length; i++) {
            System.out.println("hi");
        }

    }

}
