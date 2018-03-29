package uk.co.alt236.gradlecache2mvn.core.exporter;

import org.apache.commons.io.FileUtils;
import uk.co.alt236.gradlecache2mvn.core.artifacts.gradle.GradleMavenArtifactGroup;
import uk.co.alt236.gradlecache2mvn.util.Hasher;
import uk.co.alt236.gradlecache2mvn.util.Logger;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Exporter {
    private static final String LOG_TEMPLATE = "For artifactId: %s, groupId: %s, version: %s";

    public Result export(final List<GradleMavenArtifactGroup> artifacts,
                         final String exportPath,
                         final boolean dryRun) {

        final CopyJobFactory copyJobFactory = new CopyJobFactory();
        int errors = 0;
        int copied = 0;
        int skipped = 0;

        for (final GradleMavenArtifactGroup artifactGroup : artifacts) {
            final CopyJobFactory.Result copyJobs = copyJobFactory.createJobs(artifactGroup, exportPath);
            if (!copyJobs.hasError()) {
                Logger.log(LOG_TEMPLATE, artifactGroup.getArtifactId(), artifactGroup.getGroupId(), artifactGroup.getVersion());
                Logger.log("\tfiles to copy: " + copyJobs.getFilesToCopy().size());
                final Result result = copy(copyJobs.getFilesToCopy(), dryRun);
                errors += result.getErrors();
                copied += result.getCopied();
                skipped += result.getSkipped();
            } else {
                errors += artifactGroup.getFiles().size();
            }
        }

        return new Result(copied, skipped, errors);
    }

    private Result copy(List<CopyJobFactory.FileToCopy> filesToCopy, boolean dryRun) {
        int copied = 0;
        int skipped = 0;

        for (final CopyJobFactory.FileToCopy fileToCopy : filesToCopy) {
            final File destination = fileToCopy.getDestination();
            final File source = fileToCopy.getSource().getFile();
            if (shouldCopy(fileToCopy)) {
                copied++;
                Logger.logImportant("\tCopying %s to %s", source, destination);
                if (!dryRun) {
                    try {
                        FileUtils.copyFile(source, destination, true);
                        writeMd5(fileToCopy);
                    } catch (IOException e) {
                        throw new IllegalStateException(e.getMessage(), e);
                    }
                } else {
                    Logger.logImportant("\t---DRY RUN---");
                }
            } else {
                skipped++;
                Logger.log("\tSkipping as it exists and is the same: Copying %s to %s%n", source, destination);
            }
        }

        return new Result(copied, skipped, 0);
    }

    private boolean shouldCopy(CopyJobFactory.FileToCopy fileToCopy) {
        final boolean retVal;
        final boolean destinationExists = fileToCopy.getDestination().exists();
        if (destinationExists) {
            final String destMd5 = Hasher.getMd5(fileToCopy.getDestination());
            retVal = !destMd5.equals(fileToCopy.getSource().getMd5());
        } else {
            retVal = true;
        }

        return retVal;
    }

    private void writeMd5(CopyJobFactory.FileToCopy fileToCopy) throws IOException {
        final File destination = fileToCopy.getDestination();
        final File destinationMd5 = new File(fileToCopy.getDestination().getAbsolutePath() + ".md5");

        // It has to be 2 spaces between the fields;
        final String content = fileToCopy.getSource().getMd5() + "  " + destination.getName();

        FileUtils.writeStringToFile(destinationMd5, content, "UTF-8");
    }

    public static class Result {
        private final int copied;
        private final int skipped;
        private final int errors;


        public Result(int copied, int skipped, int errors) {
            this.copied = copied;
            this.skipped = skipped;
            this.errors = errors;
        }

        public int getCopied() {
            return copied;
        }

        public int getSkipped() {
            return skipped;
        }

        public int getErrors() {
            return errors;
        }
    }
}
