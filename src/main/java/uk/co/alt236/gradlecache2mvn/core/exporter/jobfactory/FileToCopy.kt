package uk.co.alt236.gradlecache2mvn.core.exporter.jobfactory

import uk.co.alt236.gradlecache2mvn.core.artifacts.ArtifactFile
import java.io.File

data class FileToCopy constructor(val source: ArtifactFile,
                                  val destination: File)