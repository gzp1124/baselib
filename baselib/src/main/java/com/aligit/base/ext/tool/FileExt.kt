@file:JvmName("FileUtil")

package com.aligit.base.ext.tool

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import com.aligit.base.common.AppContext
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import java.io.File
import java.io.FileInputStream

/**
 * <pre>
 * author : gzp1124
 *
 * @version : 1.0
 * @date : 2020/10/19
 * @description : TODO
 * </pre>
 */
/**
 * @return 检查sd卡是否可用
 */
fun isSdCardAvailable(): Boolean {
    return Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()
}

/**
 * @return 获取SD根文件
 */
fun getSDRootFile(): File? {
    return if (isSdCardAvailable()) {
        AppContext.getExternalFilesDir(null)
    } else {
        null
    }
}

/**
 * @return 公共下载文件夹
 */
fun getPublicDownloadDir(): String {
    return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath
}

/**
 * @return 获取指定文件大小(单位：字节)
 */
fun File.getFileSize(): Long {
    var size: Long = 0
    if (exists()) {
        FileInputStream(this).use {
            size = it.available().toLong()
        }
    }
    return size
}

/**
 * @return 磁盘缓存路径
 */
fun Context.diskCachePath(): String? {
    return if (isSdCardAvailable() || !Environment.isExternalStorageRemovable()) {
        externalCacheDir?.path
    } else {
        cacheDir.path
    }
}

/**
 * 图片选择的获取图片真实路径
 */
fun LocalMedia.getMyRealPath(context: Context): String {
    return context.getPathFromString(this.availablePath)
}

/**
 * 参数可以是真实路径，也可以是 uri 字符串
 * @return 根据路径字符串返回真实文件路径
 */
fun Context.getPathFromString(str:String):String{
    return if (!PictureMimeType.isContent(str)) str else getPathFromUriString(str)
}

/**
 * @return 根据 content://media/external 字符串类型的 URI ，来获取真实的文件路径
 */
fun Context.getPathFromUriString(uriString:String): String{
    return getPathFromUri(Uri.parse(uriString))
}

/**
 * @return 根据 uri 获取真实的文件路径
 */
fun Context.getPathFromUri(uri: Uri): String {
    val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

    // DocumentProvider
    if (isKitKat && DocumentsContract.isDocumentUri(this, uri)) {
        // ExternalStorageProvider
        if (isExternalStorageDocument(uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":".toRegex()).toTypedArray()
            val type = split[0]
            if ("primary".equals(type, ignoreCase = true)) {
                return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
            }

            // TODO handle non-primary volumes
        } else if (isDownloadsDocument(uri)) {
            val id = DocumentsContract.getDocumentId(uri)
            val contentUri: Uri = ContentUris.withAppendedId(
                Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)
            )
            return getDataColumn(this, contentUri, null, null)
        } else if (isMediaDocument(uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":".toRegex()).toTypedArray()
            val type = split[0]
            var contentUri: Uri? = null
            if ("image" == type) {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            } else if ("video" == type) {
                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            } else if ("audio" == type) {
                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            }
            val selection = "_id=?"
            val selectionArgs = arrayOf(
                split[1]
            )
            return getDataColumn(this, contentUri, selection, selectionArgs)
        }
    } else if ("content".equals(uri.getScheme(), ignoreCase = true)) {
        var segPath = uri.lastPathSegment
        if (segPath.getLength() == 0) segPath = ""
        // Return the remote address
        return if (isGooglePhotosUri(uri)) segPath!! else getDataColumn(
            this,
            uri,
            null,
            null
        )
    } else if ("file".equals(uri.getScheme(), ignoreCase = true)) {
        return if ( uri.path.getLength() > 0) uri.path!! else ""
    }
    return ""
}

fun getDataColumn(
    context: Context, uri: Uri?, selection: String?,
    selectionArgs: Array<String>?
): String {
    var cursor: Cursor? = null
    val column = "_data"
    val projection = arrayOf(
        column
    )
    try {
        cursor = uri?.let {
            context.contentResolver.query(
                it, projection, selection, selectionArgs,
                null
            )
        }
        if (cursor != null && cursor.moveToFirst()) {
            val index: Int = cursor.getColumnIndexOrThrow(column)
            return cursor.getString(index)
        }
    } finally {
        cursor?.close()
    }
    return ""
}


/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is ExternalStorageProvider.
 */
fun isExternalStorageDocument(uri: Uri): Boolean {
    return "com.android.externalstorage.documents" == uri.getAuthority()
}

/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is DownloadsProvider.
 */
fun isDownloadsDocument(uri: Uri): Boolean {
    return "com.android.providers.downloads.documents" == uri.getAuthority()
}

/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is MediaProvider.
 */
fun isMediaDocument(uri: Uri): Boolean {
    return "com.android.providers.media.documents" == uri.getAuthority()
}

/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is Google Photos.
 */
fun isGooglePhotosUri(uri: Uri): Boolean {
    return "com.google.android.apps.photos.content" == uri.getAuthority()
}