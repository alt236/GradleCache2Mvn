package uk.co.alt236.gradlecache2mvn.core.reader;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import uk.co.alt236.gradlecache2mvn.core.artifacts.ArtifactFile;
import uk.co.alt236.gradlecache2mvn.core.artifacts.gradle.GradleMavenArtifactGroup;
import uk.co.alt236.gradlecache2mvn.util.FileUtil;
import uk.co.alt236.gradlecache2mvn.util.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/*package*/ class GradleMavenArtifactFactory {
    private static final RegexFileFilter FILE_FILTER = new RegexFileFilter("^(.*?)");
    private static final String LOG_ARTIFACT =
            "Artifact:  %s";
    private static final String LOG_FILES = "           * %s %s";

    public List<GradleMavenArtifactGroup> getArtifactsForGroupId(File folder) {
        final List<GradleMavenArtifactGroup> retVal = new ArrayList<>();

        final String groupId = folder.getName();
        final File[] artifactIds = FileUtil.getSubfolders(folder);

        for (final File artifactIdFolder : artifactIds) {
            final String artifactId = artifactIdFolder.getName();

            final File[] versionFolders = FileUtil.getSubfolders(artifactIdFolder);
            for (final File versionFolder : versionFolders) {
                final String version = versionFolder.getName();
                final List<ArtifactFile> files = getFiles(groupId, artifactId, version, versionFolder);
                final GradleMavenArtifactGroup artifactGroup = new GradleMavenArtifactGroup(groupId, artifactId, version, files);
                Logger.log(String.format(Locale.US, LOG_ARTIFACT, artifactGroup.getGradleDeclaration()));

                for (final ArtifactFile file : files) {
                    final String fileName = file.getFileName();
                    final String hash = file.getMd5();
                    Logger.log(String.format(Locale.US, LOG_FILES, fileName, hash));
                }
                retVal.add(artifactGroup);
            }
        }

        return retVal;
    }

    private List<ArtifactFile> getFiles(final String groupId,
                                        final String artifactId,
                                        final String version,
                                        final File folder) {

        final List<File> files = new ArrayList<>(FileUtils.listFiles(
                folder,
                FILE_FILTER,
                DirectoryFileFilter.DIRECTORY));

        return files.stream().map(
                file -> new ArtifactFile(
                        file,
                        groupId,
                        artifactId,
                        version))
                .sorted(Comparator.comparing(ArtifactFile::getFileName))
                .collect(Collectors.toList());
    }
}
