package com.aligit.base.ext.foundation

import android.os.Parcelable
import android.text.TextUtils
import com.blankj.utilcode.util.GsonUtils
import com.tencent.mmkv.MMKV
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * <pre>
 * author : gzp1124
 * time   : 2020/6/28
 * desc   :
 * </pre>
 */
@Suppress("UNCHECKED_CAST")
class Mmkv<T>(val name: String, val default: T?, val clazz: Class<T>? = null) : ReadWriteProperty<Any?, T> {
    /*
    如果要保存 Parcelable 和 其他任意对象 则 clazz 为必须设置，否则会报错
    最后一个参数 clazz 用于保存 Parcelable 或者其他任意类型的对象
        保存 Parcelable 使用的是 mmkv 自带的方法
        保存其他类型的对象，使用的是 把对象转换成 json 字符串进行保存

        Parcelable 和 其他对象 不建议使用 mmkv 进行保存
     */

    private val mmkv by lazy {
        MMKV.defaultMMKV()
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T = findPreference(name)

    private fun findPreference(key: String): T {
        return when (default) {
            is Double -> mmkv.decodeDouble(key, default)
            is Boolean -> mmkv.decodeBool(key, default)
            is Long -> mmkv.decodeLong(key, default)
            is Int -> mmkv.decodeInt(key, default)
            is Float -> mmkv.decodeFloat(key, default)
            is String -> mmkv.decodeString(key, default)
            is ByteArray -> mmkv.decodeBytes(key, default)
            is Parcelable -> getParcelable()
            else -> getObj()
        } as T
    }

    fun getParcelable(): T? {
        if (clazz == null) throw RuntimeException("mmkv save ${name} value is parcelable, clazz cannot be null")
        return mmkv.decodeParcelable(name, clazz as Class<Parcelable> , default as Parcelable) as T?
    }
    fun setParcelable(value: Parcelable) {
        mmkv.encode(name, value)
    }
    fun getObj(): T? {
        val res = mmkv.decodeString(name)
        return if (TextUtils.isEmpty(res)) {
            default
        } else {
            if (clazz == null) throw RuntimeException("mmkv save ${name} value is object, clazz cannot be null")
            GsonUtils.fromJson(res, clazz)
        }
    }
    fun setObj(value: T) {
        mmkv.encode(name, GsonUtils.toJson(value))
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        putPreference(name, value)
    }

    private fun putPreference(key: String, value: T) {
        mmkv.apply {
            when (value) {
                is Double -> encode(key, value)
                is Boolean -> encode(key, value)
                is Long -> encode(key, value)
                is Int -> encode(key, value)
                is Float -> encode(key, value)
                is String -> encode(key, value)
                is ByteArray -> encode(key, value)
                is Parcelable -> setParcelable(value)
                else -> setObj(value)
            }
        }
    }

}


/**
 * 保存对象类型使用该类，默认值可以为空，返回值也可为空
 * 基本类型不会出现空
 */
@Suppress("UNCHECKED_CAST")
class MmkvObj<T>(val name: String, val default: T?, val clazz: Class<T>) : ReadWriteProperty<Any?, T?> {
    /*
    如果要保存 Parcelable 和 其他任意对象 则 clazz 为必须设置，否则会报错
    最后一个参数 clazz 用于保存 Parcelable 或者其他任意类型的对象
        保存 Parcelable 使用的是 mmkv 自带的方法
        保存其他类型的对象，使用的是 把对象转换成 json 字符串进行保存

        Parcelable 和 其他对象 不建议使用 mmkv 进行保存
     */

    private val mmkv by lazy {
        MMKV.defaultMMKV()
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T? = findPreference(name)

    private fun findPreference(key: String): T? {
        return when (default) {
            is Parcelable -> getParcelable()
            else -> getObj()
        }
    }

    fun getParcelable(): T? {
        if (clazz == null) throw RuntimeException("mmkv save ${name} value is parcelable, clazz cannot be null")
        return mmkv.decodeParcelable(name, clazz as Class<Parcelable> , default as Parcelable) as T?
    }
    fun setParcelable(value: Parcelable) {
        mmkv.encode(name, value)
    }
    fun getObj(): T? {
        val res = mmkv.decodeString(name)
        return if (TextUtils.isEmpty(res)) {
            default
        } else {
            if (clazz == null) throw RuntimeException("mmkv save ${name} value is object, clazz cannot be null")
            GsonUtils.fromJson(res, clazz)
        }
    }
    fun setObj(value: T?) {
        mmkv.encode(name, GsonUtils.toJson(value))
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        putPreference(name, value)
    }

    private fun putPreference(key: String, value: T?) {
        mmkv.apply {
            when (value) {
                is Parcelable -> setParcelable(value)
                else -> setObj(value)
            }
        }
    }

}