package uk.co.alt236.gradlecache2mvn.core.exporter

import org.apache.commons.io.FileUtils
import uk.co.alt236.gradlecache2mvn.core.exporter.jobfactory.FileToCopy
import java.io.File

internal class FileWriter {

    fun write(fileToCopy: FileToCopy) {
        val destination = fileToCopy.destination
        val source = fileToCopy.source.file

        FileUtils.copyFile(source, destination, true)
        writeMd5(fileToCopy)
        writeSha1(fileToCopy)
    }

    private fun writeMd5(fileToCopy: FileToCopy) {
        val destination = fileToCopy.destination
        val destinationMd5 = File(fileToCopy.destination.absolutePath + ".md5")

        // It has to be 2 spaces between the fields;
        val content = fileToCopy.source.md5 + "  " + destination.name
        FileUtils.writeStringToFile(destinationMd5, content, "UTF-8")
    }

    private fun writeSha1(fileToCopy: FileToCopy) {
        val destination = fileToCopy.destination
        val destinationMd5 = File(fileToCopy.destination.absolutePath + ".sha1")

        // It has to be 2 spaces between the fields;
        val content = fileToCopy.source.sha1 + "  " + destination.name
        FileUtils.writeStringToFile(destinationMd5, content, "UTF-8")
    }

}