package uk.co.alt236.gradlecache2mvn.artifacts;

public interface MavenArtifact {
    String getArtifactId();

    String getVersion();

    String getGroupId();
}
