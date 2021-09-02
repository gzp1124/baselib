package com.thirtydays.baselibdev.net.req

import androidx.databinding.ObservableField

/*
    在请求接口的时候 ObservableField 只可以包裹基本数据类型
    最终生成的请求参数 json 如下：
    {
    "clientToken":"",
    "invitationCode":"",
    "loginMethod":"PASSWORD",
    "loginParam":"111",
    "valid":{
        "b":"haha"
    },
    "valida":1.1,
    "validat":1.2,
    "validatM":1,
    "validateM":true,
    "validateMethod":"EMAIL",
    "validateParam":"222",
    "validbb":[
        {
            "b":"aa"
        },
        {
            "b":"bb"
        }
    ]
}
 */
data class TestLoginReq(
    val clientToken: ObservableField<String> = ObservableField<String>(""),
    val invitationCode: ObservableField<String> = ObservableField<String>(""),
    val loginMethod: ObservableField<String> = ObservableField<String>("PASSWORD"),
    val loginParam: ObservableField<String> = ObservableField<String>("111"),
    val validateMethod: ObservableField<String> = ObservableField<String>("EMAIL"),
    val validateM: ObservableField<Boolean> = ObservableField<Boolean>(true),
    val validatM: ObservableField<Int> = ObservableField<Int>(1),
    val validat: ObservableField<Double> = ObservableField<Double>(1.2),
    val valida: ObservableField<Float> = ObservableField<Float>(1.1f),
    val valid: B = B(ObservableField("haha")),// 这里不能写成 val valid: ObservableField<B>
    val validbb: List<B> = arrayListOf(B(ObservableField("aa")),B(ObservableField("bb"))),
    val validateParam: ObservableField<String> = ObservableField<String>("222")
)

data class B(val b:ObservableField<String>)
