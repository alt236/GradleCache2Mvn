package uk.co.alt236.gradlecache2mvn.core.exporter.jobfactory

internal data class CopyJobs constructor(val filesToCopy: List<FileToCopy>,
                                         private val error: Boolean) {

    fun hasError() = error
}