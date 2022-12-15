package milmit.advancegram.messenger.parts

import android.util.Base64
import cn.hutool.http.HttpResponse
import cn.hutool.http.HttpUtil
import kotlinx.coroutines.*
import milmit.advancegram.messenger.AdvConfig
import milmit.advancegram.messenger.utils.DnsFactory
import milmit.advancegram.messenger.utils.StrUtil
import org.telegram.messenger.FileLog


import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

fun loadProxiesPublic(urls: List<String>, exceptions: MutableMap<String, Exception>): List<String> {
    if (!AdvConfig.enablePublicProxy.Bool())
        return emptyList()
    // Try DoH first ( github.com is often blocked
    try {
        val content = DnsFactory.getTxts("nachonekodayo.sekai.icu").joinToString()

        val proxiesString = StrUtil.getSubString(content, "#NekoXStart#", "#NekoXEnd#")
        if (proxiesString.equals(content)) {
            throw Exception("DoH get public proxy: Not found")
        }

        //MilMit remove
    } catch (e: Exception) {
        FileLog.e(e)
    }

    // Try Other Urls
    return loadProxies(urls, exceptions)
}


fun loadProxies(urls: List<String>, exceptions: MutableMap<String, Exception>): List<String> {

    return runBlocking {

        suspendCoroutine {

            val ret = AtomicBoolean()
            val cl = AtomicInteger(urls.size)
            var defer: List<String>? = null

            for (url in urls) {
                launch(Dispatchers.IO) {
                    try {
                        var subX = ""
                        var subY = ""
                        var urlFinal = url
                        if (url.count { it == '@' } == 2) {
                            subX = url.substringAfter("@")
                                    .substringBefore("@")
                            subY = url.substringAfterLast("@")
                            urlFinal = url.substringBefore("@")
                        }
                        var nextUrl = url
                        var resp: HttpResponse
                        while (true) {
                            resp = HttpUtil.createGet(nextUrl).timeout(10 * 1000).execute();
                            if (resp.status == 301 || resp.status == 302 || resp.status == 307) {
                                nextUrl = resp.header("Location");
                                continue;
                            }
                            break;
                        }
                        var content = resp.body()
                        if (subX.isNotBlank()) {
                            content = content.substringAfter(subX)
                                    .substringBefore(subY)
                        }

                        if (url.contains("https://api.github.com")) {
                            content = content.replace("\\n", "", false)
                            content = String(Base64.decode(content, Base64.NO_PADDING))
                        }

//MilMit remove
                        FileLog.d(url)
                        FileLog.d("Success")
                    } catch (e: Exception) {
                        FileLog.d(url)
                        FileLog.e(e)
                        exceptions[url] = e
                        if (cl.decrementAndGet() == 0) {
                            if (defer != null) {
                                it.resume(defer!!)
                            } else {
                                it.resumeWithException(e)
                            }
                        }
                    }
                }
            }
        }
    }
}