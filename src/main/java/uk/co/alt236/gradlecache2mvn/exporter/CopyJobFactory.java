package uk.co.alt236.gradlecache2mvn.exporter;

import uk.co.alt236.gradlecache2mvn.artifacts.ArtifactFile;
import uk.co.alt236.gradlecache2mvn.artifacts.MavenArtifact;
import uk.co.alt236.gradlecache2mvn.artifacts.gradle.GradleMavenArtifactGroup;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/*package*/ class CopyJobFactory {
    private static final String LOG_TEMPLATE = "For artifactId: %s, groupId: %s, version: %s";

    // Primary artifact
    // /$groupId[0]/../$groupId[n]/$artifactId/$version/$artifactId-$version.$extension;
    // Secondary Artifact
    // /$groupId[0]/../$groupId[n]/$artifactId/$version/$artifactId-$version-$classifier.$extension
    // POM
    // $artifactId-$version.pom

    private static final String BASE_PATH = "/%s/%s/%s/";

    public List<FileToCopy> createJobs(final GradleMavenArtifactGroup artifactGroup,
                                       final String exportPath) {
        List<FileToCopy> retVal = new ArrayList<>();

        final ArtifactClassifier.ClassifiedFiles classifiedFiles =
                ArtifactClassifier.classify(artifactGroup);

        if (classifiedFiles.getPomFiles().isEmpty()) {
            System.err.println("ERROR: No POM file found: " + artifactGroup);
        } else if (classifiedFiles.getPomFiles().size() > 1) {
            System.err.println("ERROR: " + classifiedFiles.getPomFiles().size() + " POM files found: " + artifactGroup);
        } else if (classifiedFiles.getPrimaryArtifactFiles().size() > 1) {
            System.err.println("ERROR: " + classifiedFiles.getPomFiles().size() + " Primary artifact files found: " + artifactGroup);
        } else {
            final String basePath = exportPath + getMvnDirectoryStructure(artifactGroup);
            retVal = createCopyJobs(classifiedFiles, basePath);

            System.out.println(String.format(Locale.US, LOG_TEMPLATE, artifactGroup.getArtifactId(), artifactGroup.getGroupId(), artifactGroup.getVersion()));
            for (FileToCopy fileToCopy : retVal) {
                System.out.println("\tWill save " + fileToCopy.getFile().getFileName() + " as " + fileToCopy.getNewPath());
            }
        }

        return retVal;
    }

    private List<FileToCopy> createCopyJobs(final ArtifactClassifier.ClassifiedFiles classifiedFiles,
                                            final String basePath) {
        final List<FileToCopy> retVal = new ArrayList<>();
        final ArtifactFile pomFile = classifiedFiles.getPomFiles().get(0); // There should only be 1

        retVal.add(
                new FileToCopy(pomFile, new File(basePath + pomFile.getFileName())));

        if (!classifiedFiles.getPrimaryArtifactFiles().isEmpty()) {
            final ArtifactFile primaryFile = classifiedFiles.getPrimaryArtifactFiles().get(0); // There should only be 1
            retVal.add(
                    new FileToCopy(primaryFile, new File(basePath + primaryFile.getFileName())));
        }

        retVal.addAll(
                classifiedFiles.getSecondaryArtifactFiles()
                        .stream()
                        .map(secondary
                                -> new FileToCopy(secondary, new File(basePath + secondary.getFileName())))
                        .collect(Collectors.toList()));

        return retVal;
    }

    private String getMvnDirectoryStructure(final MavenArtifact artifact) {
        return String.format(
                Locale.US,
                BASE_PATH,
                artifact.getGroupId(),
                artifact.getArtifactId(),
                artifact.getVersion());
    }

    public static class FileToCopy {
        private final ArtifactFile file;
        private final File newPath;

        private FileToCopy(final ArtifactFile file,
                           final File newPath) {
            this.file = file;
            this.newPath = newPath;
        }

        public ArtifactFile getFile() {
            return file;
        }

        public File getNewPath() {
            return newPath;
        }
    }
}
