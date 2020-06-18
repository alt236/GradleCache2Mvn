package uk.co.alt236.gradlecache2mvn.core.exporter.classifier

import uk.co.alt236.gradlecache2mvn.core.artifacts.ArtifactFile
import uk.co.alt236.gradlecache2mvn.core.artifacts.gradle.GradleMavenArtifactGroup
import uk.co.alt236.gradlecache2mvn.util.FileKx.hasExtension
import uk.co.alt236.gradlecache2mvn.util.FileKx.isExtension
import java.util.*

object ArtifactClassifier {
    private val OTHER_EXTENSIONS: Set<String> = setOf("md5", "MD5", "sha1", "SHA1")

    @JvmStatic
    fun classify(artifactGroup: GradleMavenArtifactGroup): ClassifiedFiles {
        val pomFiles: MutableList<ArtifactFile> = ArrayList()
        val primaryArtifactFiles: MutableList<ArtifactFile> = ArrayList()
        val secondaryArtifactFiles: MutableList<ArtifactFile> = ArrayList()
        val otherFiles: MutableList<ArtifactFile> = ArrayList()

        for (file in artifactGroup.artifacts) {
            //
            when {
                !file.file.hasExtension() -> otherFiles.add(file)
                file.file.isExtension(OTHER_EXTENSIONS) -> otherFiles.add(file)
                file.file.isExtension("pom") -> pomFiles.add(file)
                else -> {
                    val fileName = file.fileName
                    val expectedPrimaryName = file.artifactId + "-" + file.version + "."
                    if (fileName.startsWith(expectedPrimaryName)) {
                        primaryArtifactFiles.add(file)
                    } else {
                        secondaryArtifactFiles.add(file)
                    }
                }
            }
        }

        return ClassifiedFiles(
                pomFiles,
                primaryArtifactFiles,
                secondaryArtifactFiles,
                otherFiles)
    }
}