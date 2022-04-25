# mBERT-locations

Parses the files passed as parameter and extract the locations targeted by mBERT.

Outputs a json file with those locations.

## runner:

`edu.lu.uni.serval.mbertloc.GetLocations`

## example params with input (files , (lines)) to parse and an output directory:

`
  -in=/PATH_TO_REPO/Cli/src/java/org/apache/commons/cli2/commandline/WriteableCommandLineImpl.java::132@133@135
  -in=/PATH_TO_REPO/Cli/src/java/org/apache/commons/cli2/WriteableCommandLine.java::47
  -in=/PATH_TO_REPO/Cli/src/java/org/apache/commons/cli2/option/ArgumentImpl.java::144
  -out=/PATH_TO_OUT/output/mBERTlocations/f/Cli_13
`

## example params with input (files , (lines)) exclude (files , (lines)) to parse and an output directory:

`
  -in=/PATH_TO_REPO/Cli/src/java/org/apache/commons/cli2/commandline/WriteableCommandLineImpl.java
  -in=/PATH_TO_REPO/Cli/src/java/org/apache/commons/cli2/WriteableCommandLine.java
  -in=/PATH_TO_REPO/Cli/src/java/org/apache/commons/cli2/option/ArgumentImpl.java
  -ex=/PATH_TO_REPO/Cli/src/java/org/apache/commons/cli2/commandline/WriteableCommandLineImpl.java::132@133@135
  -ex=/PATH_TO_REPO/Cli/src/java/org/apache/commons/cli2/WriteableCommandLine.java::47
  -ex=/PATH_TO_REPO/Cli/src/java/org/apache/commons/cli2/option/ArgumentImpl.java::144
  -out=/PATH_TO_OUT/output/mBERTlocations/f/Cli_13
`
similar to :



## Next todos:

- testing.
- better input checking. (check and handle duplicate files args)
- better output handling. (check and handle duplicate output locations)

