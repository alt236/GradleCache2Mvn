package uk.co.alt236.gradlecache2mvn.reader;

import uk.co.alt236.gradlecache2mvn.artifacts.gradle.GradleMavenArtifactGroup;
import uk.co.alt236.gradlecache2mvn.util.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GradleCacheReader {

    private final String cachePath;
    private final GradleMavenArtifactFactory creator;

    public GradleCacheReader(final String cachePath) {
        this.cachePath = cachePath;
        this.creator = new GradleMavenArtifactFactory();
    }

    public final List<GradleMavenArtifactGroup> getDependencies() {
        final File cacheDir = new File(cachePath);
        if (!cacheDir.exists()) {
            throw new InvalidCacheDirectoryException(cachePath + " does not exist!");
        }

        if (!cacheDir.isDirectory()) {
            throw new InvalidCacheDirectoryException(cachePath + " is not a directory!");
        }

        final File[] subfolders = FileUtil.getSubfolders(cacheDir);
        return getDependencies(subfolders);
    }

    private List<GradleMavenArtifactGroup> getDependencies(File[] groupIdFolders) {
        final List<GradleMavenArtifactGroup> retVal = new ArrayList<>();

        for (final File groupIdFolder : groupIdFolders) {
            final List<GradleMavenArtifactGroup> dependencies = creator.getArtifactsForGroupId(groupIdFolder);
            retVal.addAll(dependencies);
        }

        return retVal;
    }
}
