package uk.co.alt236.gradlecache2mvn.core.exporter

import uk.co.alt236.gradlecache2mvn.core.exporter.jobfactory.FileToCopy
import uk.co.alt236.gradlecache2mvn.util.Hasher

internal class CopyEvaluator(private val overwriteDifferentFiles: Boolean) {

    fun evaluate(fileToCopy: FileToCopy): Action {
        val destinationExists = fileToCopy.destination.exists()

        return if (destinationExists) {
            return if (overwriteDifferentFiles) {
                val destMd5 = Hasher.getMd5(fileToCopy.destination)
                val sourceMd5 = fileToCopy.source.md5
                if (sourceMd5 == destMd5) {
                    Action.SKIP_SAME_HASH
                } else {
                    Action.COPY
                }
            } else {
                Action.SKIP_FILE_EXISTS
            }
        } else {
            Action.COPY
        }
    }

    enum class Action {
        COPY,
        SKIP_SAME_HASH,
        SKIP_FILE_EXISTS
    }
}