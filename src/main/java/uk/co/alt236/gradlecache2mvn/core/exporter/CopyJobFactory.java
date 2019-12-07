package uk.co.alt236.gradlecache2mvn.core.exporter;

import uk.co.alt236.gradlecache2mvn.core.artifacts.ArtifactFile;
import uk.co.alt236.gradlecache2mvn.core.artifacts.MavenArtifact;
import uk.co.alt236.gradlecache2mvn.core.artifacts.gradle.GradleMavenArtifactGroup;
import uk.co.alt236.gradlecache2mvn.util.DuplicateFinder;
import uk.co.alt236.gradlecache2mvn.util.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

/*package*/ class CopyJobFactory {
    // Primary artifact
    // /$groupId[0]/../$groupId[n]/$artifactId/$version/$artifactId-$version.$extension;
    // Secondary Artifact
    // /$groupId[0]/../$groupId[n]/$artifactId/$version/$artifactId-$version-$classifier.$extension
    // POM
    // $artifactId-$version.pom

    private static final String FILES_WITH_SAME_NAME_TEMPLATE = "Duplicate filename(s): %s. Duplicate(s): %s";
    private static final String BASE_PATH = "/%s/%s/%s/";

    public Result createJobs(final GradleMavenArtifactGroup artifactGroup,
                             final String exportPath) {
        final boolean error;
        List<FileToCopy> filesToCopy = new ArrayList<>();
        if (validateArtifactGroup(artifactGroup)) {
            final ArtifactClassifier.ClassifiedFiles classifiedFiles = ArtifactClassifier.classify(artifactGroup);

            if (classifiedFiles.getPomFiles().isEmpty()) {
                Logger.logError("No POM file found: " + artifactGroup.getGradleDeclaration());
                error = true;
            } else if (classifiedFiles.getPomFiles().size() > 1) {
                Logger.logError(classifiedFiles.getPomFiles().size() + " POM files found: " + artifactGroup.getGradleDeclaration());
                error = true;
            } else if (classifiedFiles.getPrimaryArtifactFiles().size() > 1) {
                Logger.logError(classifiedFiles.getPomFiles().size() + " primary artifact files found: " + artifactGroup.getGradleDeclaration());
                error = true;
            } else {
                final String basePath = exportPath + getMvnDirectoryStructure(artifactGroup);
                filesToCopy = createCopyJobs(classifiedFiles, basePath);
                error = false;
            }
        } else {
            error = true;
        }

        return new Result(filesToCopy, error);
    }

    private List<FileToCopy> createCopyJobs(final ArtifactClassifier.ClassifiedFiles classifiedFiles,
                                            final String basePath) {
        final List<FileToCopy> retVal = new ArrayList<>();
        final ArtifactFile pomFile = classifiedFiles.getPomFiles().get(0); // There should only be 1

        retVal.add(new FileToCopy(pomFile, new File(basePath + pomFile.getFileName())));

        if (!classifiedFiles.getPrimaryArtifactFiles().isEmpty()) {
            final ArtifactFile primaryFile = classifiedFiles.getPrimaryArtifactFiles().get(0); // There should only be 1
            retVal.add(new FileToCopy(primaryFile, new File(basePath + primaryFile.getFileName())));
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
                artifact.getGroupId().replace(".", System.getProperty("file.separator")),
                artifact.getArtifactId(),
                artifact.getVersion());
    }

    private boolean validateArtifactGroup(GradleMavenArtifactGroup artifactGroup) {
        boolean retVal = true;
        final List<String> fileNames = new ArrayList<>();
        for (final ArtifactFile file : artifactGroup.getFiles()) {
            fileNames.add(file.getFileName());
        }

        final Set<String> duplicates = DuplicateFinder.findDuplicates(fileNames);

        if (!duplicates.isEmpty()) {
            Logger.logError(String.format(Locale.US, FILES_WITH_SAME_NAME_TEMPLATE,
                    artifactGroup.getGradleDeclaration(),
                    duplicates));
            retVal = false;
        }

        return retVal;
    }

    static class Result {
        private final List<FileToCopy> filesToCopy;
        private final boolean error;

        Result(List<FileToCopy> filesToCopy, boolean error) {
            this.filesToCopy = filesToCopy;
            this.error = error;
        }

        List<FileToCopy> getFilesToCopy() {
            return filesToCopy;
        }

        boolean hasError() {
            return error;
        }
    }

    static class FileToCopy {
        private final ArtifactFile file;
        private final File newPath;

        private FileToCopy(final ArtifactFile file,
                           final File newPath) {
            this.file = file;
            this.newPath = newPath;
        }

        ArtifactFile getSource() {
            return file;
        }

        File getDestination() {
            return newPath;
        }
    }
}
