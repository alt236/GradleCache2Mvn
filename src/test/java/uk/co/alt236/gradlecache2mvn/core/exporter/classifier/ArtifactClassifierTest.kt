package uk.co.alt236.gradlecache2mvn.core.exporter.classifier

import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import uk.co.alt236.gradlecache2mvn.core.artifacts.ArtifactFile
import uk.co.alt236.gradlecache2mvn.core.artifacts.gradle.GradleMavenArtifactGroup
import uk.co.alt236.gradlecache2mvn.core.exporter.classifier.ArtifactClassifier.classify
import java.io.File
import java.util.*
import kotlin.streams.toList

class ArtifactClassifierTest {
    @Test
    @Throws(Exception::class)
    fun testClassify_valid() {
        val validName = "$artifactId-$version"
        val primaryItem = "$validName.jar"
        val secondaryItem = "$validName-other.jar"
        val pomFile = "blah.pom"
        val otherFile = "blah.md5"

        val group = createArtifactGroup(
                primaryItem,
                secondaryItem,
                pomFile,
                otherFile)

        val (pomFiles, primaryArtifactFiles, secondaryArtifactFiles, otherFiles) = classify(group)

        assertNotNull(primaryArtifactFiles)
        assertNotNull(secondaryArtifactFiles)
        assertNotNull(pomFiles)
        assertNotNull(otherFiles)

        assertListSize("Primary", 1, primaryArtifactFiles)
        assertListSize("Secondary", 1, secondaryArtifactFiles)
        assertListSize("Pom", 1, pomFiles)
        assertListSize("Other", 1, otherFiles)

        assertEquals(primaryItem, primaryArtifactFiles[0].fileName)
        assertEquals(secondaryItem, secondaryArtifactFiles[0].fileName)
        assertEquals(pomFile, pomFiles[0].fileName)
        assertEquals(otherFile, otherFiles[0].fileName)
    }

    @Test
    @Throws(Exception::class)
    fun testClassify_other_files() {
        val files = arrayOf("blah.md5", "blah.sha1", "blah", "blah.MD5", "blah.SHA1")
        val group = createArtifactGroup(*files)
        val (pomFiles, primaryArtifactFiles, secondaryArtifactFiles, otherFiles) = classify(group)

        assertListSize("Primary", 0, primaryArtifactFiles)
        assertListSize("Secondary", 0, secondaryArtifactFiles)
        assertListSize("Pom", 0, pomFiles)
        assertListSize("Other", files.size, otherFiles)
    }

    @Test
    @Throws(Exception::class)
    fun testClassify_empty() {
        val group = createArtifactGroup()
        val (pomFiles, primaryArtifactFiles, secondaryArtifactFiles, otherFiles) = classify(group)

        assertNotNull(primaryArtifactFiles)
        assertNotNull(secondaryArtifactFiles)
        assertNotNull(pomFiles)
        assertNotNull(otherFiles)
    }

    private companion object {
        private const val groupId = "foo"
        private const val artifactId = "bar"
        private const val version = "baz"

        private fun createArtifactGroup(vararg filenames: String): GradleMavenArtifactGroup {
            val artifacts: MutableList<ArtifactFile> = ArrayList()
            for (filename in filenames) {
                artifacts.add(createArtifactFile(filename))
            }
            return GradleMavenArtifactGroup(groupId, artifactId, version, artifacts)
        }

        private fun createArtifactFile(filename: String): ArtifactFile {
            return ArtifactFile(File(filename), groupId, artifactId, version)
        }

        private fun assertListSize(message: String, expected: Int, list: List<ArtifactFile>) {
            val contents = list.stream().map { it.fileName }.toList()

            Assert.assertEquals("List: $message. contents: $contents", expected.toLong(), list.size.toLong())
        }
    }
}