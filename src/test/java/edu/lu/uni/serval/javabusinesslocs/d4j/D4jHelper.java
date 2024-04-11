package edu.lu.uni.serval.javabusinesslocs.d4j;


import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.lu.uni.serval.javabusinesslocs.utils.Configuration;
import edu.lu.uni.serval.javabusinesslocs.utils.GitUtils;
import edu.lu.uni.serval.javabusinesslocs.utils.ShellUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class D4jHelper {

    private static Logger log = LoggerFactory.getLogger(D4jHelper.class);


    private final String fixedBugProjectsPath;
    private final String defects4jPath;
    private final String outputPath;
    private final String java8;


    public D4jHelper(String fixedBugProjectsPath, String defects4jPath, String outputPath, String java8) {
        this.fixedBugProjectsPath = fixedBugProjectsPath;
        this.defects4jPath = defects4jPath;
        this.outputPath = outputPath;
        this.java8 = java8;
    }

    public String checkoutFixedVersion(String pidBid) throws IOException {
        String repoPath = Paths.get(fixedBugProjectsPath, pidBid).toAbsolutePath().toString();
        File repoDir = new File(repoPath);
        if (repoDir.isDirectory()) {
            GitUtils.checkoutProjectRepoHead(repoPath);
        }
        if (!repoDir.isDirectory()) {
            String[] splits = pidBid.split("_");
            // /home/users/akhanfir/envlib/defects4j/framework/bin/defects4j checkout -p Mockito -v 38f -w /scratch/users/akhanfir/d4j_projects/f/Mockito_38
            List<String> cmd = new ArrayList<>();
            cmd.add("JAVA_HOME=" + java8);
            cmd.add(Paths.get(defects4jPath, "framework/bin/defects4j").toString());
            cmd.add("checkout");
            cmd.add("-p");
            cmd.add(splits[0]);
            cmd.add("-v");
            cmd.add(splits[1] + "f");
            cmd.add("-w");
            cmd.add(repoPath);
            log.info("checkoutFixedVersion = " + cmd);

            String res = ShellUtils.shellRun(new ArrayList<String>() {{
                add(String.join(" ", cmd));
            }}, pidBid, Configuration.SHELL_RUN_TIMEOUT_SEC);
            log.info("res = " + res);
        }
        return repoPath;
    }
}
