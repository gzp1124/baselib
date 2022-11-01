package com.aligit.base.utils

import com.aligit.base.ext.tool.getLength
import com.blankj.utilcode.util.EncodeUtils
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStreamReader
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

/**
 gzip 压缩的工具
 对于 gzip 压缩字符串，一般的应用场景是字符串太长，压缩后再通过接口上传到服务器
 正常 gzip 压缩完是 byte 数组，不能直接传给接口
 一般会有两种处理方式
    1. 把 byte 数组使用 base64 进行编码为字符串：这种方式比较常用。
    2. 把 byte 数组重新编码为字符串，比如 ISO-8859-1 编码

 最核心的3个方法
    compressToByte - 将字符串进行 gzip 压缩为 byte 数组
    EncodeUtils.base64Encode2String - 字符串进行 base64 编码
    compressToCodeStr - 将字符串进行 gzip 压缩为 byte 再进行编码为字符串
 */
object GZipUtil {
    /**
     * 字符串进行 gzip 压缩
     * 要将 gzip 压缩后的内容传递到服务器，使用：EncodeUtils.base64Encode2String(compress(str))
     */
    fun compressToByte(str: String?): ByteArray? {
        if (str == null || str.length == 0) {
            return null
        }
        return compressToByte(str.toByteArray(charset("UTF-8")))
    }

    fun compressToByte(byte: ByteArray): ByteArray? {
        try {
            val obj = ByteArrayOutputStream()
            val gzip = GZIPOutputStream(obj)
            gzip.write(byte)
            gzip.flush()
            gzip.close()
            return obj.toByteArray()
        } catch (e: Exception) {
            return null
        }
    }

    /**
     * gzip 压缩成带有编码的字符串，默认 ISO-8859-1
     * 相当于先进行 gzip 压缩，在转为 对应编码的字符串
     */
    fun compressToCodeStr(str:String?,code:String = "ISO-8859-1"):String?{
        try {
            if (str == null || str.length == 0) {
                return str
            }
            val obj = ByteArrayOutputStream()
            val gzip = GZIPOutputStream(obj)
            gzip.write(str.toByteArray(charset("UTF-8")))
            gzip.close()
            return obj.toString(code)
        }catch (e:Exception){
            return null
        }
    }

    /**
     * 先 gzip 压缩 再 base64 转成字符串
     * 这种是正规的 gzip 然后请求接口的方式，接口的参数一般使用 base64 压缩的
     */
    fun compressToBase64Str(str: String?): String? {
        return EncodeUtils.base64Encode2String(compressToByte(str))
    }

    fun compressToBase64Str(byte: ByteArray): String? {
        return EncodeUtils.base64Encode2String(compressToByte(byte))
    }


    /**
     * gzip 解压缩
     * 先把压缩的值用 base64 解压缩再 gzip 解压
     */
    fun decompressBase64(compressed: String?): String? {
        if (compressed.getLength() == 0) {
            return ""
        } else {
            return decompress(EncodeUtils.base64Decode(compressed))
        }
    }

    /**
     * gzip 解压缩
     * 先把字符串变 byte 数组：EncodeUtils.base64Decode(str)
     */
    fun decompress(compressed: ByteArray?): String? {
        try {
            val outStr = StringBuilder()
            if (compressed == null || compressed.size == 0) {
                return ""
            }
            if (isCompressed(compressed)) {
                val gis = GZIPInputStream(ByteArrayInputStream(compressed))
                val bufferedReader = BufferedReader(InputStreamReader(gis, "UTF-8"))
                var line: String?
                while (bufferedReader.readLine().also { line = it } != null) {
                    outStr.append(line)
                }
            } else {
                outStr.append(compressed)
            }
            return outStr.toString()
        } catch (e: Exception) {
            return null
        }
    }

    /**
     * 是否被 gzip 压缩
     */
    fun isCompressed(compressed: String): Boolean{
        return isCompressed(EncodeUtils.base64Decode(compressed))
    }

    fun isCompressed(compressed: ByteArray): Boolean {
        return compressed[0] == GZIPInputStream.GZIP_MAGIC.toByte() && compressed[1] == (GZIPInputStream.GZIP_MAGIC shr 8).toByte()
    }
}