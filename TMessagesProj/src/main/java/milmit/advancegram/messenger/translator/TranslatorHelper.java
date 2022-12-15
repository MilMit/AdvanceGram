package milmit.advancegram.messenger.translator;

import android.text.TextUtils;

import androidx.core.text.HtmlCompat;
import androidx.core.util.Pair;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.MessageObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.BaseFragment;

import java.util.ArrayList;
import java.util.Locale;

import milmit.advancegram.messenger.AdvanceGramConfig;
import milmit.advancegram.messenger.entities.HTMLKeeper;
import milmit.advancegram.messenger.helpers.MessageHelper;
import milmit.advancegram.messenger.settings.DoNotTranslateSettings;

public class TranslatorHelper {

    private static final ArrayList<String> translatingIDs = new ArrayList<>();

    public static boolean isTranslating(String uid) {
        return translatingIDs.contains(uid);
    }

    public static void translate(TranslatorContext translatorContext, TranslateCallback listener) {
        if (translatingIDs.contains(translatorContext.UID)) return;
        translatingIDs.add(translatorContext.UID);
        listener.onPreTranslate();
        Translator.translate(translatorContext.translateObject, new Translator.TranslateCallBack() {
            @Override
            public void onSuccess(BaseTranslator.Result result) {
                translatingIDs.remove(translatorContext.UID);
                listener.onTranslate(result);
            }

            @Override
            public void onError(Exception e) {
                translatingIDs.remove(translatorContext.UID);
                listener.onError(e);
            }
        });
    }

    public static MessageObject applyTranslatedMessage(BaseTranslator.Result result, MessageObject messageObject, long dialog_id, BaseFragment fragment, boolean autoTranslate) {
        if (result.sourceLanguage != null || !Translator.isSupportedOutputLang(AdvanceGramConfig.translationProvider)) {
            String src_lang = result.sourceLanguage;
            if (src_lang != null) src_lang = src_lang.toUpperCase();
            String language = Translator.getTranslator(AdvanceGramConfig.translationProvider).getCurrentTargetLanguage().toUpperCase();
            if (result.translation instanceof String) {
                if (messageObject.originalEntities != null) {
                    Pair<String, ArrayList<TLRPC.MessageEntity>> entitiesResult = HTMLKeeper.htmlToEntities((String) result.translation, messageObject.originalEntities, !isSupportHTMLMode());
                    if (autoTranslate && (entitiesResult.first.equalsIgnoreCase(messageObject.originalMessage.toString()) || !TextUtils.isEmpty(src_lang) && DoNotTranslateSettings.getRestrictedLanguages().contains(src_lang.toLowerCase()))) {
                        messageObject.translating = false;
                        messageObject.translated = false;
                        messageObject.canceledTranslation = true;
                    } else {
                        messageObject.messageOwner.message = entitiesResult.first;
                        messageObject.messageOwner.entities = entitiesResult.second;
                        messageObject.translated = true;
                        messageObject.translatedLanguage = Pair.create(src_lang, language);
                        if (result.additionalInfo instanceof MessageHelper.ReplyMarkupButtonsTexts) {
                            ((MessageHelper.ReplyMarkupButtonsTexts) result.additionalInfo).applyTextToKeyboard(messageObject.messageOwner.reply_markup.rows);
                        }
                        if (fragment == null) {
                            messageObject.translating = false;
                            messageObject.caption = null;
                            messageObject.generateCaption();
                        }
                    }
                }
            } else if (result.translation instanceof TLRPC.TL_poll) {
                messageObject.translated = true;
                messageObject.translatedLanguage = Pair.create(src_lang, language);
                ((TLRPC.TL_messageMediaPoll) messageObject.messageOwner.media).poll = (TLRPC.TL_poll) result.translation;
            }
        } else {
            messageObject.translating = false;
            messageObject.translated = false;
            messageObject.canceledTranslation = true;
            messageObject.translatedLanguage = null;
        }
        if (fragment != null) {
            AndroidUtilities.runOnUIThread(() -> fragment.getMessageHelper().resetMessageContent(dialog_id, messageObject, messageObject.translated, messageObject.canceledTranslation));
        }
        return messageObject;
    }

    public static MessageObject applyTranslatedMessage(BaseTranslator.Result result, MessageObject messageObject) {
        return applyTranslatedMessage(result, messageObject, 0, null, false);
    }

    public static MessageObject resetTranslatedMessage(long dialog_id, BaseFragment fragment, MessageObject messageObject) {
        if (messageObject.originalMessage instanceof String) {
            messageObject.messageOwner.message = (String) messageObject.originalMessage;
            messageObject.messageText = messageObject.messageOwner.message;
            if (messageObject.originalEntities != null) {
                messageObject.messageOwner.entities = new ArrayList<>(messageObject.originalEntities);
            }
            if (messageObject.originalReplyMarkupRows != null) {
                messageObject.originalReplyMarkupRows.applyTextToKeyboard(messageObject.messageOwner.reply_markup.rows);
            }
        } else if (messageObject.originalMessage instanceof TLRPC.TL_poll) {
            ((TLRPC.TL_messageMediaPoll) messageObject.messageOwner.media).poll = (TLRPC.TL_poll) messageObject.originalMessage;
        }
        messageObject.translatedLanguage = null;
        return fragment.getMessageHelper().resetMessageContent(dialog_id, messageObject, false, true);
    }

    public static MessageObject resetTranslatedCaption(MessageObject messageObject) {
        if (messageObject.originalMessage instanceof String) {
            messageObject.messageOwner.message = (String) messageObject.originalMessage;
            messageObject.translating = false;
            messageObject.translated = false;
            messageObject.caption = null;
            messageObject.generateCaption();
        }
        return messageObject;
    }

    public static boolean isSupportHTMLMode() {
        return isSupportHTMLMode(AdvanceGramConfig.translationProvider);
    }

    public static boolean isSupportHTMLMode(int provider) {
        return provider == Translator.PROVIDER_GOOGLE ||
                provider == Translator.PROVIDER_YANDEX ||
                provider == Translator.PROVIDER_DEEPL ||
                provider == Translator.PROVIDER_TELEGRAM;
    }

    public static boolean isSupportAutoTranslate() {
        return isSupportAutoTranslate(AdvanceGramConfig.translationProvider);
    }

    public static boolean isSupportAutoTranslate(int provider) {
        return provider == Translator.PROVIDER_GOOGLE ||
                provider == Translator.PROVIDER_YANDEX ||
                provider == Translator.PROVIDER_DEEPL ||
                provider == Translator.PROVIDER_DUCKDUCKGO;
    }

    public static class TranslatorContext {
        private final String UID;
        private final Object translateObject;

        public TranslatorContext(String ID, String messageObject) {
            UID = ID;
            translateObject = messageObject;
        }

        public TranslatorContext(MessageObject messageObject) {
            UID = messageObject.getChatId() + "_" + messageObject.getId();
            BaseTranslator.AdditionalObjectTranslation additionalObjectTranslation = new BaseTranslator.AdditionalObjectTranslation();
            additionalObjectTranslation.translation = messageObject.type == MessageObject.TYPE_POLL ? ((TLRPC.TL_messageMediaPoll) messageObject.messageOwner.media).poll : messageObject.messageOwner.message;
            messageObject.originalMessage = additionalObjectTranslation.translation;
            if (messageObject.messageOwner.reply_markup != null && messageObject.messageOwner.reply_markup.rows.size() > 0) {
                messageObject.originalReplyMarkupRows = new MessageHelper.ReplyMarkupButtonsTexts(messageObject.messageOwner.reply_markup.rows);
                additionalObjectTranslation.additionalInfo = new MessageHelper.ReplyMarkupButtonsTexts(messageObject.messageOwner.reply_markup.rows);
            }
            if (messageObject.messageOwner.entities != null && additionalObjectTranslation.translation instanceof String) {
                messageObject.originalEntities = messageObject.messageOwner.entities;
                if (isSupportHTMLMode() && AdvanceGramConfig.keepTranslationMarkdown) {
                    additionalObjectTranslation.translation = HTMLKeeper.entitiesToHtml((String) additionalObjectTranslation.translation, messageObject.originalEntities, false);
                }
            }
            translateObject = additionalObjectTranslation;
        }
    }

    public static String languageName(String code) {
        Locale language = Locale.forLanguageTag(code);
        String fromString = !TextUtils.isEmpty(language.getScript()) ? String.valueOf(HtmlCompat.fromHtml(language.getDisplayScript(), HtmlCompat.FROM_HTML_MODE_LEGACY)) : language.getDisplayName();
        return AndroidUtilities.capitalize(fromString);
    }

    public interface TranslateCallback {
        void onTranslate(BaseTranslator.Result result);

        void onPreTranslate();

        void onError(Exception error);
    }
}
