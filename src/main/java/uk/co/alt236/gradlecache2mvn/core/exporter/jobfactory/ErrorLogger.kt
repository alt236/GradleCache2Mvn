package uk.co.alt236.gradlecache2mvn.core.exporter.jobfactory

import uk.co.alt236.gradlecache2mvn.core.artifacts.gradle.GradleMavenArtifactGroup
import uk.co.alt236.gradlecache2mvn.core.exporter.classifier.ArtifactClassifier
import uk.co.alt236.gradlecache2mvn.util.Logger

internal object ErrorLogger {

    @JvmStatic
    fun logNoPomFilesFound(artifactGroup: GradleMavenArtifactGroup, classifiedFiles: ArtifactClassifier.ClassifiedFiles) {
        Logger.logError("No POM file found: ${artifactGroup.gradleDeclaration}. Non-POM file count: ${classifiedFiles.nonPomFileCount}.")
    }

    @JvmStatic
    fun logMultiplePomFilesFound(artifactGroup: GradleMavenArtifactGroup, classifiedFiles: ArtifactClassifier.ClassifiedFiles) {
        Logger.logError("${classifiedFiles.pomFiles.size} POM files found: ${artifactGroup.gradleDeclaration}.")
    }

    @JvmStatic
    fun logMultiplePrimaryArtifacts(artifactGroup: GradleMavenArtifactGroup, classifiedFiles: ArtifactClassifier.ClassifiedFiles) {
        Logger.logError("${classifiedFiles.primaryArtifactFiles.size} primary artifact files found: ${artifactGroup.gradleDeclaration}.")
    }

}