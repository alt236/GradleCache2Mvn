package uk.co.alt236.gradlecache2mvn.core.artifacts

import uk.co.alt236.gradlecache2mvn.util.Hasher
import java.io.File

data class ArtifactFile(val file: File,
                        private val groupId: String,
                        private val artifactId: String,
                        private val version: String) : MavenArtifact {

    val md5: String by lazy { Hasher.getMd5(file) }
    val sha1: String by lazy { Hasher.getSha1(file) }

    val fileName: String = file.name

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
        return "ArtifactFile{" +
                "file=" + file.name +
                ", md5='" + md5 + '\'' +
                ", sha1='" + sha1 + '\'' +
                '}'
    }

}