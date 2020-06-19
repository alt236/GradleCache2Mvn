package uk.co.alt236.gradlecache2mvn;

import uk.co.alt236.gradlecache2mvn.cli.CommandLineWrapper;
import uk.co.alt236.gradlecache2mvn.core.artifacts.gradle.GradleMavenArtifactGroup;
import uk.co.alt236.gradlecache2mvn.core.exporter.Exporter;
import uk.co.alt236.gradlecache2mvn.core.exporter.Result;
import uk.co.alt236.gradlecache2mvn.core.reader.GradleCacheReader;
import uk.co.alt236.gradlecache2mvn.resources.Strings;
import uk.co.alt236.gradlecache2mvn.util.Logger;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/*package*/ class GradleCache2mvn {

    private final CommandLineWrapper commandLine;
    private final Strings strings;

    GradleCache2mvn(final Strings strings, final CommandLineWrapper commandLine) {
        this.strings = strings;
        this.commandLine = commandLine;
    }

    private static String sanePath(final String path) {
        final String retVal;

        if (path == null) {
            retVal = "";
        } else {
            retVal = path.replaceFirst("^~", System.getProperty("user.home"));
        }

        return retVal;
    }

    public static String humanReadableByteCount(long bytes, boolean si) {
        final int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        final int exp = (int) (Math.log(bytes) / Math.log(unit));
        final String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");

        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);

    }

    void doWork() {
        final String input = commandLine.getInputDirectory();
        final String output = commandLine.getOutputDirectory();
        final boolean verbose = commandLine.isVerbose();
        final boolean dryRun = commandLine.isDryRun();
        final boolean hideNoPomError = commandLine.isHideNoPomFoundError();
        final boolean overwriteDifferentFiles = commandLine.isOverwriteDifferentFiles();

        Logger.setMode(verbose ? Logger.Mode.ALL : Logger.Mode.IMPORTANT);

        final String saneInput = sanePath(
                input == null || input.isEmpty()
                        ? strings.getString("default_gradle_cache_location")
                        : input);

        final String saneOutput = sanePath(output);

        Logger.logImportant("Input (Gradle cache location): " + saneInput);
        Logger.logImportant("Output (Maven repo location): " + saneOutput);
        Logger.logImportant("Overwrite non-identical files: " + overwriteDifferentFiles);
        Logger.logImportant("Hide No POM file found error: " + hideNoPomError);
        Logger.logImportant("Dry run: " + dryRun);

        final List<GradleMavenArtifactGroup> artifacts = new GradleCacheReader(sanePath(saneInput)).getDependencies();
        final Result result = new Exporter(hideNoPomError).export(artifacts, saneOutput, dryRun, overwriteDifferentFiles);

        final int artifactCount = artifacts.size();
        final int errors = result.getErrors();
        final int copied = result.getCopied();
        final int skipped = result.getSkipped();
        final long bytes = result.getBytesCopied();

        final int fileCount = artifacts.stream()
                .map(GradleMavenArtifactGroup::getArtifacts)
                .filter(Objects::nonNull)
                .mapToInt(Collection::size)
                .sum();

        Logger.logImportant(
                "Done! Artifacts: %d, Files %d, Copied %d, Skipped %d, Errors %d. %s copied.",
                artifactCount, fileCount, copied, skipped, errors, humanReadableByteCount(bytes, true));
        if ((skipped + copied + errors) != fileCount) {
            throw new IllegalStateException("The sum of skipped, copied and errors is not the same as the total file count!");
        }
    }
}
