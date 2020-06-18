package uk.co.alt236.gradlecache2mvn.core.exporter.classifier

import uk.co.alt236.gradlecache2mvn.core.artifacts.ArtifactFile

data class ClassifiedFiles internal constructor(val pomFiles: List<ArtifactFile>,
                                                val primaryArtifactFiles: List<ArtifactFile>,
                                                val secondaryArtifactFiles: List<ArtifactFile>,
                                                val otherFiles: List<ArtifactFile>) {

    val nonPomFileCount: Int
        get() = primaryArtifactFiles.size + secondaryArtifactFiles.size + otherFiles.size

}