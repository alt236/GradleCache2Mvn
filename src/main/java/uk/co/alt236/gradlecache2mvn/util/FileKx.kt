package uk.co.alt236.gradlecache2mvn.util

import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FilenameUtils
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException

internal object FileKx {

    fun File.getMd5(): String {
        var bis: BufferedInputStream? = null
        return try {
            bis = BufferedInputStream(FileInputStream(this))
            DigestUtils.md5Hex(bis)
        } catch (e: IOException) {
            throw IllegalStateException(e.message, e)
        } finally {
            if (bis != null) {
                StreamUtil.close(bis)
            }
        }
    }

    fun File.getSha1(): String {
        var bis: BufferedInputStream? = null
        return try {
            bis = BufferedInputStream(FileInputStream(this))
            DigestUtils.sha1Hex(bis)
        } catch (e: IOException) {
            throw IllegalStateException(e.message, e)
        } finally {
            if (bis != null) {
                StreamUtil.close(bis)
            }
        }
    }

    fun File.hasExtension(): Boolean {
        val ext = FilenameUtils.getExtension(name)
        return !ext.isNullOrEmpty()
    }

    fun File.isExtension(extension: String): Boolean {
        return FilenameUtils.isExtension(name, extension)
    }

    fun File.isExtension(extensions: Collection<String>): Boolean {
        return FilenameUtils.isExtension(name, extensions)
    }

}