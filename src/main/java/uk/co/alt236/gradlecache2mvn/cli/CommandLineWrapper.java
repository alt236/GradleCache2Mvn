package uk.co.alt236.gradlecache2mvn.cli;

import org.apache.commons.cli.CommandLine;

public class CommandLineWrapper {

    private final CommandLine commandLine;

    public CommandLineWrapper(final CommandLine commandLine) {
        this.commandLine = commandLine;
//        System.out.println(commandLine.getArgList());
//        System.out.println(Arrays.toString(commandLine.getOptions()));
    }

    public boolean isDryRun() {
        return commandLine.hasOption(OptionsBuilder.ARG_DRY_RUN_LONG);
    }

    public boolean isVerbose() {
        return commandLine.hasOption(OptionsBuilder.ARG_VERBOSE_LONG);
    }

    public boolean isOverwriteDifferentFiles() {
        return commandLine.hasOption(OptionsBuilder.ARG_OVERWRITE_FILES_LONG);
    }

    public String getOutputDirectory() {
        return commandLine.getOptionValue(OptionsBuilder.ARG_MVN_DIR_LONG);
    }

    public String getInputDirectory() {
        return commandLine.getOptionValue(OptionsBuilder.ARG_GRADLE_CACHE_LONG);
    }
}
