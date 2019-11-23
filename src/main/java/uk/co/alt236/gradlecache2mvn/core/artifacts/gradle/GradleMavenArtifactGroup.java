package uk.co.alt236.gradlecache2mvn.core.artifacts.gradle;

import uk.co.alt236.gradlecache2mvn.core.artifacts.ArtifactFile;
import uk.co.alt236.gradlecache2mvn.core.artifacts.MavenArtifact;

import java.util.Collection;
import java.util.Collections;

public class GradleMavenArtifactGroup implements MavenArtifact {

    private final String groupId;
    private final String artifactId;
    private final String version;
    private final Collection<ArtifactFile> files;

    public GradleMavenArtifactGroup(String groupId,
                                    String artifactId,
                                    String version,
                                    Collection<ArtifactFile> files) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.files = Collections.unmodifiableCollection(files);
    }

    @Override
    public String getArtifactId() {
        return artifactId;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public String getGroupId() {
        return groupId;
    }

    public Collection<ArtifactFile> getFiles() {
        return files;
    }

    public String getGradleDeclaration() {
        return "'" + groupId + ":" + artifactId + ":" + version + "'";
    }

    @Override
    public String toString() {
        return "GradleMavenArtifactGroup{" +
                "groupId='" + groupId + '\'' +
                ", artifactId='" + artifactId + '\'' +
                ", version='" + version + '\'' +
                ", files=" + files +
                '}';
    }
}
