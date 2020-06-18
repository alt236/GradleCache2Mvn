package uk.co.alt236.gradlecache2mvn.core.exporter

import uk.co.alt236.gradlecache2mvn.core.exporter.jobfactory.FileToCopy
import uk.co.alt236.gradlecache2mvn.util.FileKx.getMd5

internal class CopyEvaluator(private val overwriteDifferentFiles: Boolean) {

    fun evaluate(fileToCopy: FileToCopy): Action {
        val destinationExists = fileToCopy.destination.exists()

        if (!destinationExists) {
            return Action.COPY
        }

        return if (overwriteDifferentFiles) {
            if (areFilesSame(fileToCopy)) {
                Action.SKIP_SAME_HASH
            } else {
                Action.COPY
            }
        } else {
            Action.SKIP_FILE_EXISTS
        }
    }

    private fun areFilesSame(fileToCopy: FileToCopy): Boolean {
        val isFileSizeSame = fileToCopy.destination.length() == fileToCopy.source.length

        if (!isFileSizeSame) {
            return false;
        }

        return fileToCopy.destination.getMd5() == fileToCopy.source.md5
    }


    enum class Action {
        COPY,
        SKIP_SAME_HASH,
        SKIP_FILE_EXISTS
    }
}