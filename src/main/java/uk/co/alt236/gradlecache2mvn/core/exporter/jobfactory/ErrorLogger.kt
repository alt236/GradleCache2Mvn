package uk.co.alt236.gradlecache2mvn.core.exporter.jobfactory

import uk.co.alt236.gradlecache2mvn.core.artifacts.gradle.GradleMavenArtifactGroup
import uk.co.alt236.gradlecache2mvn.core.exporter.classifier.ClassifiedFiles
import uk.co.alt236.gradlecache2mvn.util.Logger

internal class ErrorLogger(private val hideNoPomError: Boolean) {

    fun logNoPomFilesFound(artifactGroup: GradleMavenArtifactGroup, classifiedFiles: ClassifiedFiles) {
        if (hideNoPomError) {
            return
        }

        Logger.logError("No POM file found: ${artifactGroup.gradleDeclaration}. Non-POM file count: ${classifiedFiles.nonPomFileCount}.")
    }

    fun logMultiplePomFilesFound(artifactGroup: GradleMavenArtifactGroup, classifiedFiles: ClassifiedFiles) {
        Logger.logError("${classifiedFiles.pomFiles.size} POM files found: ${artifactGroup.gradleDeclaration}.")
    }

    fun logMultiplePrimaryArtifacts(artifactGroup: GradleMavenArtifactGroup, classifiedFiles: ClassifiedFiles) {
        Logger.logError("${classifiedFiles.primaryArtifactFiles.size} primary artifact files found: ${artifactGroup.gradleDeclaration}.")
    }

}