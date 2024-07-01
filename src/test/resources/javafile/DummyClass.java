package org.apache.commons.cli2.option;

import java.util.List;

public class DummyClass extends OptionImpl implements Argument {
    private static final char NUL = '\0';

    public DummyClass(final char subsequentSeparator) {
        this.subsequentSplit = subsequentSeparator != NUL;
    }
}

