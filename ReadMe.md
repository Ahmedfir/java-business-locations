# java-business-locations

Parses the files passed as parameter and extract the business logic locations, then outputs them a json file with.

This module has been first developed to study code naturalness captured by generative language (see https://github.com/Ahmedfir/CodeBERT-nt).
So if you use this tool in your research, we would appreciate that you cite:

    @article{khanfir2022codebertnt,
      title={CodeBERT-nt: code naturalness via CodeBERT},
      author={Khanfir, Ahmed and Jimenez, Matthieu and Papadakis, Mike and Traon, Yves Le},
      journal={arXiv preprint arXiv:2208.06042},
      year={2022}
    }


## Runner:

`edu.lu.uni.serval.javabusinesslocs.GetLocations`.

You can see example usages of this runner and their output in our `test` folder.
You can call this runner using either from source-code or using our released jar, i.e. see how the jar is called using python scripts in `https://github.com/Ahmedfir/cbnt`.

## Example params with input (files , (lines)) to parse and an output directory:

`
  -in=/PATH_TO_REPO/Cli/src/java/org/apache/commons/cli2/commandline/WriteableCommandLineImpl.java::132@133@135
  -in=/PATH_TO_REPO/Cli/src/java/org/apache/commons/cli2/WriteableCommandLine.java::47
  -in=/PATH_TO_REPO/Cli/src/java/org/apache/commons/cli2/option/ArgumentImpl.java::144
  -out=/PATH_TO_OUT/output/locations/f/Cli_13
`

## Example params with input (files , (lines)) exclude (files , (lines)) to parse and an output directory:

`
  -in=/PATH_TO_REPO/Cli/src/java/org/apache/commons/cli2/commandline/WriteableCommandLineImpl.java
  -in=/PATH_TO_REPO/Cli/src/java/org/apache/commons/cli2/WriteableCommandLine.java
  -in=/PATH_TO_REPO/Cli/src/java/org/apache/commons/cli2/option/ArgumentImpl.java
  -ex=/PATH_TO_REPO/Cli/src/java/org/apache/commons/cli2/commandline/WriteableCommandLineImpl.java::132@133@135
  -ex=/PATH_TO_REPO/Cli/src/java/org/apache/commons/cli2/WriteableCommandLine.java::47
  -ex=/PATH_TO_REPO/Cli/src/java/org/apache/commons/cli2/option/ArgumentImpl.java::144
  -out=/PATH_TO_OUT/output/locations/f/Cli_13
`

## Next todos:

- More testing.
- Better documentation.
- Better input checking. (check and handle duplicate files args)
- Better output handling. (check and handle duplicate output locations)


#### Credits:
We use Spoon for AST parsing: https://spoon.gforge.inria.fr/index.html
The majority of these locations have been used before in μBERT: https://github.com/rdegiovanni/mBERT.