package uk.co.alt236.gradlecache2mvn.exporter;

import org.apache.commons.io.FileUtils;
import uk.co.alt236.gradlecache2mvn.artifacts.gradle.GradleMavenArtifactGroup;
import uk.co.alt236.gradlecache2mvn.util.HashUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Exporter {
    private static final String LOG_TEMPLATE = "For artifactId: %s, groupId: %s, version: %s";

    public void export(final List<GradleMavenArtifactGroup> artifacts,
                       final String exportPath) {

        final CopyJobFactory copyJobFactory = new CopyJobFactory();
        for (final GradleMavenArtifactGroup artifactGroup : artifacts) {
            final List<CopyJobFactory.FileToCopy> filesToCopy = copyJobFactory.createJobs(artifactGroup, exportPath);

            System.out.println(String.format(Locale.US, LOG_TEMPLATE,
                    artifactGroup.getArtifactId(), artifactGroup.getGroupId(), artifactGroup.getVersion()));
            System.out.println("\tfiles to copy: " + filesToCopy.size());
            copy(filesToCopy);
        }
    }

    private void copy(List<CopyJobFactory.FileToCopy> filesToCopy) {
        for (final CopyJobFactory.FileToCopy fileToCopy : filesToCopy) {
            final File destination = fileToCopy.getDestination();
            final File source = fileToCopy.getSource().getFile();
            if (shouldCopy(fileToCopy)) {
                System.out.println("\tCopying "
                        + source
                        + " to "
                        + destination);
                try {
                    FileUtils.copyFile(source, destination, true);
                    writeMd5(fileToCopy);
                } catch (IOException e) {
                    throw new IllegalStateException(e.getMessage(), e);
                }
            } else {
                System.out.println("\tSkipping as it exists and has same MD5: "
                        + "Copying "
                        + source
                        + " to "
                        + destination);
            }
        }
    }

    private boolean shouldCopy(CopyJobFactory.FileToCopy fileToCopy) {
        final boolean retVal;
        final boolean destinationExists = fileToCopy.getDestination().exists();
        if (destinationExists) {
            final String destMd5 = HashUtil.getMd5(fileToCopy.getDestination());
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
}
