package milmit.advancegram.messenger.translator.Nekox.source

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import milmit.advancegram.messenger.translator.Nekox.NekoXTranslator

import milmit.advancegram.messenger.translator.Nekox.deepl.DeepLTranslatorRaw
import org.telegram.messenger.LocaleController
import org.telegram.messenger.R


object DeepLTranslator : NekoXTranslator {

    val targetLanguages = listOf("DE", "EN", "ES", "FR", "IT", "JA", "NL", "PL", "PT", "RU", "ZH")

    val client = DeepLTranslatorRaw()

    override suspend fun doTranslate(from: String, to: String, query: String): String {

        if (to !in targetLanguages) {

            throw UnsupportedOperationException(LocaleController.getString("TranslateApiUnsupported", R.string.TranslateApiUnsupported))

        }

        return withContext(Dispatchers.IO) { client.translate(query, "auto", to) }
    }
}
