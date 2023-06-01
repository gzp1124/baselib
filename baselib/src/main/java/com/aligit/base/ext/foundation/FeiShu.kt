package com.aligit.base.ext.foundation

import android.text.TextUtils
import com.blankj.utilcode.util.AppUtils
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import kotlin.system.exitProcess


private const val a1 = "cli_a226e0903325d013" // app_id
private const val a2 = "akpyeqc67hmX4qoBLPxfLhDb7k8tIkCk" // app_secret
private const val a3 = "shtcnGZEHxm4s7GF6f8JQiqqgIb" // 哪个文档，一般不用改
private var a5: String = "" // 放 Token 的变量，不用设置
private const val a4 = "msExFo" // 哪个表格 sheet ，一般不用改
private const val a6 = "A" // 哪一列，要修改

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

/**
 * 使用方式
 *  修改上方的 a6，具体哪一列查看文档：https://ifbz8hy4hh.feishu.cn/sheets/shtcnGZEHxm4s7GF6f8JQiqqgIb
 *  在最开始的地方调用 FeiShu.getToken() 即可
 */
class FeiShu {

    fun getToken() {
        if (AppUtils.getAppPackageName().startsWith("com.gzp")){
            // 自己的包不用检查
            return
        }
        val url = "https://open.feishu.cn/open-apis/auth/v3/tenant_access_token/internal"
        Thread(Runnable {
            try {
                val client = OkHttpClient()    //创建OkHClient实例

                val mRequestBody: RequestBody =
                    RequestBody.create(
                        "text/plain;chaset=utf-8".toMediaTypeOrNull(),
                        Gson().toJson(TokenReq())
                    )

                val request: Request = Request.Builder().post(mRequestBody).url(url).build()

                val response = client.newCall(request).execute()  //发请求获取服务器返回的数据
                val responseData = response.body?.string()
                val bean = Gson().fromJson(responseData, TokenBean::class.java)
                a5 = bean.tenant_access_token

                getSheet()
            } catch (e: Exception) {
            }
        }).start()
    }

    private fun getSheet() {
        var page = 1
        while (true) {
            val data = realGetSheet(page)
            data?.data?.valueRange?.values?.forEach {
                it?.forEach { s ->
                    if (TextUtils.isEmpty(s)) {
                        exitProcess(1)
                        throw RuntimeException()
                    }
                    if ("all" == s.trim() || AppUtils.getAppPackageName().startsWith("com.gzp") || AppUtils.getAppPackageName() == s.trim()) {
                        return
                    }
                }
            }
            page++
            if (page > 1000) {
                return
            }
        }
    }

    val u1 = "https://open.feishu.cn/open-apis/sheets/v2/spreadsheets/"
    private fun realGetSheet(page: Int): SheetData? {
        val u2 = "$a3/values/$a4!$a6${(page - 1) * 10 + 1}:$a6${page * 10}"
        val client = OkHttpClient()    //创建OkHClient实例

        val request = Request.Builder()     //发请求创建一个Request对象
            .url(u1 + u2)
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .addHeader("Authorization", "Bearer $a5")
            .build()

        val response = client.newCall(request).execute()  //发请求获取服务器返回的数据
        val responseData = response.body?.string()
        val data = Gson().fromJson(responseData, SheetData::class.java)
        return data
    }
}