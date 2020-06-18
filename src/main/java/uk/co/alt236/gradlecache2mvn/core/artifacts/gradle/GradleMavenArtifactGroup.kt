package uk.co.alt236.gradlecache2mvn.core.artifacts.gradle

import uk.co.alt236.gradlecache2mvn.core.artifacts.ArtifactFile
import uk.co.alt236.gradlecache2mvn.core.artifacts.MavenArtifact
import java.util.*

data class GradleMavenArtifactGroup(private val groupId: String,
                                    private val artifactId: String,
                                    private val version: String,
                                    private val files: Collection<ArtifactFile>) : MavenArtifact {

    val artifacts: MutableCollection<ArtifactFile> = Collections.unmodifiableCollection(files)

    override fun getArtifactId(): String {
        return artifactId
    }

    override fun getVersion(): String {
        return version
    }

    override fun getGroupId(): String {
        return groupId
    }

    override fun getGradleDeclaration(): String {
        return "'$groupId:$artifactId:$version'"
    }

    override fun toString(): String {
        return "GradleMavenArtifactGroup{" +
                "groupId='" + groupId + '\'' +
                ", artifactId='" + artifactId + '\'' +
                ", version='" + version + '\'' +
                ", artifacts=" + artifacts +
                '}'
    }
}