package com.xjx.mvp.utils

import android.os.Environment
import com.xjx.mvp.App
import java.io.*
import java.math.BigDecimal
import java.util.*
import kotlin.collections.HashMap

class FileUtils {

    companion object{
        val ROOT_DIR = "Android/data/" + App.getContext()!!.packageName
        const val DOWNLOAD_DIR = "download"
        const val CACHE_DIR = "cache"
        const val ICON_DIR = "icon"

        /**
         * 判断SD卡是否挂载
         */
        fun isSDCardAvailable(): Boolean {
            return Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()
        }

        /**
         * 获取下载目录
         */
        fun getDownloadDir(): String? {
            return getDir(DOWNLOAD_DIR)
        }

        /**
         * 获取缓存目录
         */
        fun getCacheDir(): String? {
            return getDir(CACHE_DIR)
        }

        /**
         * 获取icon目录
         */
        fun getIconDir(): String? {
            return getDir(ICON_DIR)
        }

        /**
         * 获取应用目录，当SD卡存在时，获取SD卡上的目录，当SD卡不存在时，获取应用的cache目录
         */
        fun getDir(name: String?): String? {
            val sb = StringBuilder()
            if (isSDCardAvailable()) {
                sb.append(getExternalStoragePath())
            } else {
                sb.append(getCachePath())
            }
            sb.append(name)
            sb.append(File.separator)
            val path = sb.toString()
            return if (createDirs(path)) {
                path
            } else {
                null
            }
        }

        /**
         * 获取SD下的应用目录
         */
        fun getExternalStoragePath(): String? {
            val sb = StringBuilder()
            sb.append(Environment.getExternalStorageDirectory().absolutePath)
            sb.append(File.separator)
            sb.append(ROOT_DIR)
            sb.append(File.separator)
            return sb.toString()
        }

        /**
         * 获取应用的cache目录
         */
        fun getCachePath(): String? {
            val f = App.getContext()!!.cacheDir
            return if (null == f) {
                null
            } else {
                f.absolutePath + "/"
            }
        }

        /**
         * 创建文件夹
         */
        fun createDirs(dirPath: String?): Boolean {
            val file = File(dirPath)
            return if (!file.exists() || !file.isDirectory) {
                file.mkdirs()
            } else true
        }

        /**
         * 复制文件，可以选择是否删除源文件
         */
        fun copyFile(
            srcPath: String?,
            destPath: String?,
            deleteSrc: Boolean
        ): Boolean {
            val srcFile = File(srcPath)
            val destFile = File(destPath)
            return copyFile(srcFile, destFile, deleteSrc)
        }

        /**
         * 复制文件，可以选择是否删除源文件
         */
        fun copyFile(
            srcFile: File,
            destFile: File?,
            deleteSrc: Boolean
        ): Boolean {
            if (!srcFile.exists() || !srcFile.isFile) {
                return false
            }
            var `in`: InputStream? = null
            var out: OutputStream? = null
            try {
                `in` = FileInputStream(srcFile)
                out = FileOutputStream(destFile)
                val buffer = ByteArray(1024)
                var i = -1
                while (`in`.read(buffer).also { i = it } > 0) {
                    out.write(buffer, 0, i)
                    out.flush()
                }
                if (deleteSrc) {
                    srcFile.delete()
                }
            } catch (e: Exception) {
                LogUtils.e(e)
                return false
            } finally {
                close(out)
                close(`in`)
            }
            return true
        }

        /**
         * 判断文件是否可写
         */
        fun isWriteable(path: String?): Boolean {
            return try {
                if (isEmpty(path)) {
                    return false
                }
                val f = File(path)
                f.exists() && f.canWrite()
            } catch (e: Exception) {
                LogUtils.e(e)
                false
            }
        }

        /**
         * 修改文件的权限,例如"777"等
         */
        fun chmod(path: String, mode: String) {
            try {
                val command = "chmod $mode $path"
                val runtime = Runtime.getRuntime()
                runtime.exec(command)
            } catch (e: Exception) {
                LogUtils.e(e)
            }
        }

        /**
         * 把数据写入文件
         *
         * @param is       数据流
         * @param path     文件路径
         * @param recreate 如果文件存在，是否需要删除重建
         * @return 是否写入成功
         */
        fun writeFile(
            `is`: InputStream?,
            path: String?,
            recreate: Boolean
        ): Boolean {
            var res = false
            val f = File(path)
            var fos: FileOutputStream? = null
            try {
                if (recreate && f.exists()) {
                    f.delete()
                }
                if (!f.exists() && null != `is`) {
                    val parentFile = File(f.parent)
                    parentFile.mkdirs()
                    var count = -1
                    val buffer = ByteArray(1024)
                    fos = FileOutputStream(f)
                    while (`is`.read(buffer).also { count = it } != -1) {
                        fos.write(buffer, 0, count)
                    }
                    res = true
                }
            } catch (e: Exception) {
                LogUtils.e(e)
            } finally {
                close(fos)
                close(`is`)
            }
            return res
        }

        /**
         * 把字符串数据写入文件
         *
         * @param content 需要写入的字符串
         * @param path    文件路径名称
         * @param append  是否以添加的模式写入
         * @return 是否写入成功
         */
        fun writeFile(
            content: ByteArray?,
            path: String?,
            append: Boolean
        ): Boolean {
            var res = false
            val f = File(path)
            var raf: RandomAccessFile? = null
            try {
                if (f.exists()) {
                    if (!append) {
                        f.delete()
                        f.createNewFile()
                    }
                } else {
                    f.createNewFile()
                }
                if (f.canWrite()) {
                    raf = RandomAccessFile(f, "rw")
                    raf.seek(raf.length())
                    raf.write(content)
                    res = true
                }
            } catch (e: Exception) {
                LogUtils.e(e)
            } finally {
                close(raf)
            }
            return res
        }

        /**
         * 把字符串数据写入文件
         *
         * @param content 需要写入的字符串
         * @param path    文件路径名称
         * @param append  是否以添加的模式写入
         * @return 是否写入成功
         */
        fun writeFile(
            content: String,
            path: String?,
            append: Boolean
        ): Boolean {
            return writeFile(content.toByteArray(), path, append)
        }

        /**
         * 把键值对写入文件
         *
         * @param filePath 文件路径
         * @param key      键
         * @param value    值
         * @param comment  该键值对的注释
         */
        fun writeProperties(
            filePath: String?,
            key: String?,
            value: String?,
            comment: String?
        ) {
            if (isEmpty(key) || isEmpty(filePath)) {
                return
            }
            var fis: FileInputStream? = null
            var fos: FileOutputStream? = null
            val f = File(filePath)
            try {
                if (!f.exists() || !f.isFile) {
                    f.createNewFile()
                }
                fis = FileInputStream(f)
                val p = Properties()
                p.load(fis) // 先读取文件，再把键值对追加到后面
                p.setProperty(key, value)
                fos = FileOutputStream(f)
                p.store(fos, comment)
            } catch (e: Exception) {
                LogUtils.e(e)
            } finally {
                close(fis)
                close(fos)
            }
        }

        /**
         * 根据值读取
         */
        fun readProperties(
            filePath: String?,
            key: String?,
            defaultValue: String?
        ): String? {
            if (isEmpty(key) || isEmpty(filePath)) {
                return null
            }
            var value: String? = null
            var fis: FileInputStream? = null
            val f = File(filePath)
            try {
                if (!f.exists() || !f.isFile) {
                    f.createNewFile()
                }
                fis = FileInputStream(f)
                val p = Properties()
                p.load(fis)
                value = p.getProperty(key, defaultValue)
            } catch (e: IOException) {
                LogUtils.e(e)
            } finally {
                close(fis)
            }
            return value
        }

        /**
         * 把字符串键值对的map写入文件
         */
        fun writeMap(
            filePath: String?,
            map: Map<String?, String?>?,
            append: Boolean,
            comment: String?
        ) {
            if (map == null || map.size == 0 || isEmpty(filePath)) {
                return
            }
            var fis: FileInputStream? = null
            var fos: FileOutputStream? = null
            val f = File(filePath)
            try {
                if (!f.exists() || !f.isFile) {
                    f.createNewFile()
                }
                val p = Properties()
                if (append) {
                    fis = FileInputStream(f)
                    p.load(fis) // 先读取文件，再把键值对追加到后面
                }
                p.putAll(map)
                fos = FileOutputStream(f)
                p.store(fos, comment)
            } catch (e: Exception) {
                LogUtils.e(e)
            } finally {
                close(fis)
                close(fos)
            }
        }

        /**
         * 把字符串键值对的文件读入map
         */
        fun readMap(filePath: String?, defaultValue: String?): Map<String?, String?>? {
            if (isEmpty(filePath)) {
                return null
            }
            var map: Map<String?, String?>? = HashMap()
            var fis: FileInputStream? = null
            val f = File(filePath)
            try {
                if (!f.exists() || !f.isFile) {
                    f.createNewFile()
                }
                fis = FileInputStream(f)
                val p = Properties()
                p.load(fis)
                // 因为properties继承了map，所以直接通过p来构造一个map
                map = (p as Map<String?, String?>)
            } catch (e: Exception) {
                LogUtils.e(e)
            } finally {
                close(fis)
            }
            return map
        }

        /**
         * 改名
         */
        fun copy(
            src: String?,
            des: String?,
            delete: Boolean
        ): Boolean {
            val file = File(src)
            if (!file.exists()) {
                return false
            }
            val desFile = File(des)
            var `in`: FileInputStream? = null
            var out: FileOutputStream? = null
            try {
                `in` = FileInputStream(file)
                out = FileOutputStream(desFile)
                val buffer = ByteArray(1024)
                var count = -1
                while (`in`.read(buffer).also { count = it } != -1) {
                    out.write(buffer, 0, count)
                    out.flush()
                }
            } catch (e: Exception) {
                LogUtils.e(e)
                return false
            } finally {
                close(`in`)
                close(out)
            }
            if (delete) {
                file.delete()
            }
            return true
        }

        /**
         * 获取文件存放路径（包含/）
         *
         * @param filepath dir+filename
         * @return
         */
        fun getDirFromPath(filepath: String?): String? {
            if (filepath != null && filepath.length > 0) {
                val sep = filepath.lastIndexOf('/')
                if (sep > -1 && sep < filepath.length - 1) {
                    return filepath.substring(0, sep + 1)
                }
            }
            return filepath
        }

        /**
         * 获取文件名
         *
         * @param filepath dir+filename
         */
        fun getFileNameFromPath(filepath: String?): String? {
            if (filepath != null && filepath.length > 0) {
                val sep = filepath.lastIndexOf('/')
                if (sep > -1 && sep < filepath.length - 1) {
                    return filepath.substring(sep + 1)
                }
            }
            return filepath
        }

        /**
         * 获取不带扩展名的文件名
         */
        fun getFileNameNoEx(filename: String?): String? {
            if (filename != null && filename.length > 0) {
                val dot = filename.lastIndexOf('.')
                if (dot > -1 && dot < filename.length) {
                    return filename.substring(0, dot)
                }
            }
            return filename
        }

        /**
         * 获取文件扩展名
         */
        fun getExtensionName(filename: String?): String? {
            if (filename != null && filename.length > 0) {
                val dot = filename.lastIndexOf('.')
                if (dot > -1 && dot < filename.length - 1) {
                    return filename.substring(dot + 1)
                }
            }
            return ""
        }

        fun formatSize(size: Float): String? {
            val kb: Long = 1024
            val mb = kb * 1024
            val gb = mb * 1024
            return if (size < kb) {
                String.format("%d B", size.toInt())
            } else if (size < mb) {
                String.format("%.2f KB", size / kb) // 保留两位小数
            } else if (size < gb) {
                String.format("%.2f MB", size / mb)
            } else {
                String.format("%.2f GB", size / gb)
            }
        }

        fun formateFileSize(filesize: Double): String? {
            val kiloByte = filesize / 1024
            if (kiloByte < 1) {
                return "$filesize B"
            }
            val megaByte = kiloByte / 1024
            if (megaByte < 1) {
                val result1 =
                    BigDecimal(java.lang.Double.toString(kiloByte))
                return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + " KB"
            }
            val gigaByte = megaByte / 1024
            if (gigaByte < 1) {
                val result2 =
                    BigDecimal(java.lang.Double.toString(megaByte))
                return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + " MB"
            }
            val teraBytes = gigaByte / 1024
            if (teraBytes < 1) {
                val result3 =
                    BigDecimal(java.lang.Double.toString(gigaByte))
                return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + " GB"
            }
            val petaBytes = teraBytes / 1024
            if (petaBytes < 1) {
                val result4 =
                    BigDecimal(java.lang.Double.toString(teraBytes))
                return result4.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + " TB"
            }
            val exaBytes = petaBytes / 1024
            if (exaBytes < 1) {
                val result5 =
                    BigDecimal(java.lang.Double.toString(petaBytes))
                return result5.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + " PB"
            }
            val zettaBytes = exaBytes / 1024
            if (zettaBytes < 1) {
                val result6 =
                    BigDecimal(java.lang.Double.toString(exaBytes))
                return result6.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + " EB"
            }
            val yottaBytes = zettaBytes / 1024
            if (yottaBytes < 1) {
                val result7 =
                    BigDecimal(java.lang.Double.toString(zettaBytes))
                return result7.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + " ZB"
            }
            val result8 = BigDecimal(yottaBytes)
            return result8.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + " YB"
        }

        /**
         * 关闭流
         */
        private fun close(io: Closeable?): Boolean {
            if (io != null) {
                try {
                    io.close()
                } catch (e: IOException) {
                    LogUtils.e(e)
                }
            }
            return true
        }

        /**
         * 判断字符串或集合是否为空
         *
         * @param o
         * @return
         */
        private fun isEmpty(o: Any?): Boolean {
            return if (o is List<*>) {
                o.size == 0
            } else o == null || o.toString() == ""
        }
    }
}