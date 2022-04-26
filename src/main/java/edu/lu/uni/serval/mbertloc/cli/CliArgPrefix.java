package edu.lu.uni.serval.mbertloc.cli;

enum CliArgPrefix {
    NUMBER_OF_TOKENS("-n="),
    FILE_INCLUDE_REQUEST("-in="),
    FILE_EXCLUDE_REQUEST("-ex="),
    OUTPUT_DIR("-out="),
    SELECTION_MODE("-selection=") ;

    final String argPrefix;

    CliArgPrefix(String argPrefix) {
        this.argPrefix = argPrefix;
    }

    static CliArgPrefix startsWithPrefix(String arg) {
        for (CliArgPrefix cap : CliArgPrefix.values()) {
            if (arg.startsWith(cap.argPrefix)) {
                return cap;
            }
        }
        throw new IllegalArgumentException(arg);
    }

}
