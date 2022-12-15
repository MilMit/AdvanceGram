package milmit.advancegram.messenger.translator.Nekox.source

import android.os.SystemClock
import cn.hutool.http.HttpUtil
import milmit.advancegram.messenger.translator.Nekox.NekoXTranslator

import milmit.advancegram.messenger.utils.applyUserAgent
import org.json.JSONObject
import org.telegram.messenger.LocaleController
import org.telegram.messenger.R

object LingoTranslator : NekoXTranslator {

    override suspend fun doTranslate(from: String, to: String, query: String): String {

        if (to !in listOf("zh", "en", "es", "fr", "ja", "ru")) {

            error(LocaleController.getString("TranslateApiUnsupported", R.string.TranslateApiUnsupported))

        }

        val response = HttpUtil.createPost("https://api.interpreter.caiyunai.com/v1/translator")
                .header("Content-Type", "application/json; charset=UTF-8")
                .header("X-Authorization", "token 9sdftiq37bnv410eon2l") // 白嫖
                .applyUserAgent()
                .body(JSONObject().apply {
                    put("source", query)
                    put("trans_type", "${from}2$to")
                    put("request_id", SystemClock.elapsedRealtime().toString())
                    put("detect", true)
                }.toString())
                .execute()

        if (response.status != 200) {

            error("HTTP ${response.status} : ${response.body()}")

        }

        return JSONObject(response.body()).getString("target")

    }

}