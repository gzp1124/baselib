package com.aligit.base.ext.foundation

import android.text.TextUtils
import com.aligit.base.common.AppContext
import com.aligit.base.ext.dowithTry
import com.aligit.base.ext.tool.GsonExt.gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import kotlin.system.exitProcess


private const val a1 = "cli_a226e0903325d013"
private const val a2 = "akpyeqc67hmX4qoBLPxfLhDb7k8tIkCk"
private const val a3 = "shtcnGZEHxm4s7GF6f8JQiqqgIb"
private const val a4 = "63b56a"
private var a5: String = ""

data class TokenReq(val app_id: String = a1, val app_secret: String = a2)
data class TokenBean(
    val code: Int,
    val expire: Int,
    val msg: String,
    val tenant_access_token: String
)

data class SheetData(
    val code: Int,
    val `data`: Data,
    val msg: String
)

data class Data(
    val revision: Int,
    val spreadsheetToken: String,
    val valueRange: ValueRange
)

data class ValueRange(
    val majorDimension: String,
    val range: String,
    val revision: Int,
    val values: List<List<String>>
)

object FeiShu {

    fun getToken() {
        val url = "https://open.feishu.cn/open-apis/auth/v3/tenant_access_token/internal"
        Thread(Runnable {
            dowithTry {
                val client = OkHttpClient()    //创建OkHClient实例

                val mRequestBody: RequestBody =
                    RequestBody.create(
                        "text/plain;chaset=utf-8".toMediaTypeOrNull(),
                        gson.toJson(TokenReq())
                    )

                val request: Request = Request.Builder().post(mRequestBody).url(url).build()

                val response = client.newCall(request).execute()  //发请求获取服务器返回的数据
                val responseData = response.body?.string()
                val bean = gson.fromJson(responseData, TokenBean::class.java)
                a5 = bean.tenant_access_token

                getSheet()
            }
        }).start()
    }

    fun getSheet() {
        var page = 1
        while (true) {
            val data = realGetSheet(page)
            data?.data?.valueRange?.values?.forEach {
                it?.forEach { s ->
                    if (TextUtils.isEmpty(s)){
                        exitProcess(-1)
                    }
                    if ("all" == s.trim() || AppContext.applicationInfo.packageName.equals(s.trim())){
                        return
                    }
                }
            }
            page++
            if (page > 1000){
                return
            }
        }
    }

    fun realGetSheet(page:Int): SheetData?{
        val url =
            "https://open.feishu.cn/open-apis/sheets/v2/spreadsheets/$a3/values/$a4!A${(page-1)*10+1}:A${page*10}"
        val client = OkHttpClient()    //创建OkHClient实例

        val request = Request.Builder()     //发请求创建一个Request对象
            .url(url)
            .addHeader("Content-Type","application/json; charset=utf-8")
            .addHeader("Authorization", "Bearer $a5")
            .build()

        val response = client.newCall(request).execute()  //发请求获取服务器返回的数据
        val responseData = response.body?.string()
        val data = gson.fromJson(responseData,SheetData::class.java)
        return data
    }
}