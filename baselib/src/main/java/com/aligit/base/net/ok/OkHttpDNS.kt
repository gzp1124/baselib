package com.aligit.base.net.ok

//import com.qiniu.android.dns.DnsManager
//import com.qiniu.android.dns.IResolver
//import com.qiniu.android.dns.NetworkInfo
//import com.qiniu.android.dns.local.Resolver
//import okhttp3.Dns
//import java.io.IOException
//import java.net.InetAddress
//import java.util.*

/**
 * author : gzp1124
 * 使用的是七牛的dns，上架谷歌可能会有问题
 * 代码是正常的，要使用就放开本代码中的注释，放开OkHttpManager的第100行，在gradle中添加如下
//七牛dns https://github.com/qiniu/happy-dns-android
//    api 'com.qiniu:happy-dns:0.2.18'
 * Time: 2019/12/23 14:27
 * Des:
 */
//class OkHttpDNS : Dns {
//    private var dnsManager: DnsManager
//
//    init {
//        val resolvers = arrayOfNulls<IResolver>(1)
//        resolvers[0] = Resolver(InetAddress.getByName("119.29.29.29"))
//        dnsManager = DnsManager(NetworkInfo.normal, resolvers)
//    }
//
//    override fun lookup(hostname: String): List<InetAddress> {
//        try {
//            //获取HttpDNS解析结果
//            val ips = dnsManager.query(hostname)
//            if (ips == null || ips.isEmpty()) {
//                return Dns.SYSTEM.lookup(hostname)
//            }
//            val result: MutableList<InetAddress> = ArrayList()
//            //将ip地址数组转换成所需要的对象列表
//            for (ip in ips) {
//                result.addAll(listOf(*InetAddress.getAllByName(ip)))
//            }
//            return result
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//        //当有异常发生时，使用默认解析
//        return Dns.SYSTEM.lookup(hostname)
//    }
//}