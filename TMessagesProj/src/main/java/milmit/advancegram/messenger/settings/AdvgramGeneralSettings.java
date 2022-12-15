package milmit.advancegram.messenger.settings;

import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LanguageDetector;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.BulletinFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;

import milmit.advancegram.messenger.AdvanceGramConfig;
import milmit.advancegram.messenger.components.DcStyleSelector;
import milmit.advancegram.messenger.helpers.PopupHelper;
import milmit.advancegram.messenger.translator.AutoTranslateConfig;
import milmit.advancegram.messenger.translator.BaseTranslator;
import milmit.advancegram.messenger.translator.DeepLTranslator;
import milmit.advancegram.messenger.translator.Translator;
import milmit.advancegram.messenger.translator.TranslatorHelper;

public class AdvgramGeneralSettings extends BaseSettingsActivity {
    private final boolean supportLanguageDetector;

    private int divisorPrivacyRow;
    private int privacyHeaderRow;
    private int phoneNumberSwitchRow;
    private int phoneContactsSwitchRow;
    private int translationHeaderRow;
    private int showTranslateButtonRow;
    private int translationStyle;
    private int translationProviderSelectRow;
    private int destinationLanguageSelectRow;
    private int doNotTranslateSelectRow;
    private int autoTranslateRow;
    private int keepMarkdownRow;
    private int divisorTranslationRow;
    private int hintTranslation1;
    private int hintTranslation2;
    private int dcIdSettingsHeaderRow;
    private int dcStyleSelectorRow;
    private int dcIdRow;
    private int idTypeRow;
    private int divisorDCIdRow;
    private int hintIdRow;
    private int notificationHeaderRow;
    private int notificationAccentRow;
    private int dividerNotificationRow;
    private int callHeaderRow;
    private int confirmCallSwitchRow;
    private int deepLFormalityRow;

    public AdvgramGeneralSettings() {
        supportLanguageDetector = LanguageDetector.hasSupport();
    }

    @Override
    protected String getActionBarTitle() {
        return LocaleController.getString("General", R.string.General);
    }

    @Override
    protected void onItemClick(View view, int position, float x, float y) {
        if (position == phoneNumberSwitchRow) {
            AdvanceGramConfig.toggleHidePhone();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(AdvanceGramConfig.hidePhoneNumber);
            }
            reloadInterface();
            reloadMainInfo();
        } else if (position == phoneContactsSwitchRow) {
            AdvanceGramConfig.toggleHideContactNumber();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(AdvanceGramConfig.hideContactNumber);
            }
        } else if (position == dcIdRow) {
            AdvanceGramConfig.toggleShowIDAndDC();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(AdvanceGramConfig.showIDAndDC);
            }
            reloadInterface();
        } else if (position == translationStyle) {
            ArrayList<String> arrayList = new ArrayList<>();
            ArrayList<Integer> types = new ArrayList<>();
            arrayList.add(LocaleController.getString("TranslatorTypeAdv", R.string.TranslatorTypeAdv));
            types.add(BaseTranslator.INLINE_STYLE);
            arrayList.add(LocaleController.getString("TranslatorTypeTG", R.string.TranslatorTypeTG));
            types.add(BaseTranslator.DIALOG_STYLE);
            PopupHelper.show(arrayList, LocaleController.getString("TranslatorType", R.string.TranslatorType), types.indexOf(AdvanceGramConfig.translatorStyle), context, i -> {
                AdvanceGramConfig.setTranslatorStyle(types.get(i));
                listAdapter.notifyItemChanged(translationStyle, PARTIAL);
            });
        } else if (position == translationProviderSelectRow) {
            final int oldProvider = AdvanceGramConfig.translationProvider;
            Translator.showTranslationProviderSelector(context, param -> {
                if (param) {
                    listAdapter.notifyItemChanged(translationProviderSelectRow, PARTIAL);
                } else {
                    listAdapter.notifyItemRangeChanged(translationProviderSelectRow, 2, PARTIAL);
                }
                listAdapter.notifyItemChanged(hintTranslation2);
                if (oldProvider != AdvanceGramConfig.translationProvider) {
                    int index = deepLFormalityRow;
                    index = index == -1 ? doNotTranslateSelectRow: index;

                    boolean oldProviderSupportAuto = TranslatorHelper.isSupportAutoTranslate(oldProvider);
                    boolean newProviderSupportAuto = TranslatorHelper.isSupportAutoTranslate();
                    boolean oldProviderSupportHtml = TranslatorHelper.isSupportHTMLMode(oldProvider);
                    boolean newProviderSupportHtml = TranslatorHelper.isSupportHTMLMode();

                    if (oldProviderSupportAuto != newProviderSupportAuto && oldProviderSupportHtml != newProviderSupportHtml) {
                        listAdapter.notifyItemChanged(index + 1);
                    } else if (oldProviderSupportAuto != newProviderSupportAuto) {
                        if (newProviderSupportAuto) {
                            listAdapter.notifyItemInserted(index + 1);
                        } else {
                            listAdapter.notifyItemRemoved(index + 1);
                        }
                        listAdapter.notifyItemChanged(index);
                    }else if (oldProviderSupportHtml != newProviderSupportHtml) {
                        if (newProviderSupportHtml) {
                            listAdapter.notifyItemInserted(index + 2);
                        } else {
                            listAdapter.notifyItemRemoved(index + 2);
                        }
                        listAdapter.notifyItemChanged(index + 1);
                    }

                    if (oldProvider == Translator.PROVIDER_DEEPL) {
                        listAdapter.notifyItemChanged(destinationLanguageSelectRow, PARTIAL);
                        listAdapter.notifyItemRemoved(deepLFormalityRow);
                        updateRowsId();
                    } else if (AdvanceGramConfig.translationProvider == Translator.PROVIDER_DEEPL) {
                        updateRowsId();
                        listAdapter.notifyItemChanged(destinationLanguageSelectRow, PARTIAL);
                        listAdapter.notifyItemInserted(deepLFormalityRow);
                    } else if (oldProviderSupportHtml != newProviderSupportHtml) {
                        updateRowsId();
                    } else if (oldProviderSupportAuto != newProviderSupportAuto) {
                        updateRowsId();
                    }
                    listAdapter.notifyItemChanged(doNotTranslateSelectRow, PARTIAL);
                }
            });
        } else if (position == destinationLanguageSelectRow) {
            presentFragment(new SelectLanguageSettings());
        } else if (position == doNotTranslateSelectRow) {
            if (!supportLanguageDetector) {
                BulletinFactory.of(this).createErrorBulletinSubtitle(LocaleController.getString("BrokenMLKit", R.string.BrokenMLKit), LocaleController.getString("BrokenMLKitDetail", R.string.BrokenMLKitDetail), null).show();
                return;
            }
            presentFragment(new DoNotTranslateSettings());
        } else if (position == deepLFormalityRow) {
            ArrayList<String> arrayList = new ArrayList<>();
            ArrayList<Integer> types = new ArrayList<>();
            arrayList.add(LocaleController.getString("DeepLFormalityDefault", R.string.DeepLFormalityDefault));
            types.add(DeepLTranslator.FORMALITY_DEFAULT);
            arrayList.add(LocaleController.getString("DeepLFormalityMore", R.string.DeepLFormalityMore));
            types.add(DeepLTranslator.FORMALITY_MORE);
            arrayList.add(LocaleController.getString("DeepLFormalityLess", R.string.DeepLFormalityLess));
            types.add(DeepLTranslator.FORMALITY_LESS);
            PopupHelper.show(arrayList, LocaleController.getString("DeepLFormality", R.string.DeepLFormality), types.indexOf(AdvanceGramConfig.deepLFormality), context, i -> {
                AdvanceGramConfig.setDeepLFormality(types.get(i));
                listAdapter.notifyItemChanged(deepLFormalityRow, PARTIAL);
            });
        } else if (position == confirmCallSwitchRow) {
            AdvanceGramConfig.toggleConfirmCall();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(AdvanceGramConfig.confirmCall);
            }
        } else if (position == notificationAccentRow) {
            AdvanceGramConfig.toggleAccentColor();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(AdvanceGramConfig.accentAsNotificationColor);
            }
        } else if (position == idTypeRow) {
            ArrayList<String> arrayList = new ArrayList<>();
            ArrayList<Integer> types = new ArrayList<>();
            arrayList.add("Bot API");
            types.add(0);
            arrayList.add("Telegram API");
            types.add(1);
            PopupHelper.show(arrayList, LocaleController.getString("IDType", R.string.IDType), types.indexOf(AdvanceGramConfig.idType), context, i -> {
                AdvanceGramConfig.setIdType(types.get(i));
                listAdapter.notifyItemChanged(idTypeRow, PARTIAL);
                reloadInterface();
            });
        } else if (position == autoTranslateRow) {
            if (!supportLanguageDetector) {
                BulletinFactory.of(this).createErrorBulletinSubtitle(LocaleController.getString("BrokenMLKit", R.string.BrokenMLKit), LocaleController.getString("BrokenMLKitDetail", R.string.BrokenMLKitDetail), null).show();
                return;
            }
            presentFragment(new AutoTranslateSettings());
        } else if (position == keepMarkdownRow) {
            AdvanceGramConfig.toggleKeepTranslationMarkdown();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(AdvanceGramConfig.keepTranslationMarkdown);
            }
        } else if (position == showTranslateButtonRow) {
            AdvanceGramConfig.toggleShowTranslate();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(AdvanceGramConfig.showTranslate);
            }
        }
    }

    @Override
    protected void updateRowsId() {
        super.updateRowsId();
        privacyHeaderRow = rowCount++;
        phoneNumberSwitchRow = rowCount++;
        phoneContactsSwitchRow = rowCount++;
        divisorPrivacyRow = rowCount++;
        translationHeaderRow = rowCount++;
        showTranslateButtonRow = rowCount++;
        translationStyle = rowCount++;
        translationProviderSelectRow = rowCount++;
        destinationLanguageSelectRow = rowCount++;
        doNotTranslateSelectRow = rowCount++;
        deepLFormalityRow = AdvanceGramConfig.translationProvider == Translator.PROVIDER_DEEPL ? rowCount++ : -1;
        autoTranslateRow = TranslatorHelper.isSupportAutoTranslate() ? rowCount++ : -1;
        keepMarkdownRow = TranslatorHelper.isSupportHTMLMode() ? rowCount++ : -1;
        divisorTranslationRow = rowCount++;
        hintTranslation1 = rowCount++;
        hintTranslation2 = rowCount++;
        dcIdSettingsHeaderRow = rowCount++;
        dcStyleSelectorRow = rowCount++;
        dcIdRow = rowCount++;
        idTypeRow = rowCount++;
        divisorDCIdRow = rowCount++;
        hintIdRow = rowCount++;
        notificationHeaderRow = rowCount++;
        notificationAccentRow = rowCount++;
        dividerNotificationRow = rowCount++;
        callHeaderRow = rowCount++;
        confirmCallSwitchRow = rowCount++;
    }

    @Override
    protected BaseListAdapter createAdapter() {
        return new ListAdapter();
    }

    private class ListAdapter extends BaseListAdapter {

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, boolean partial) {
            switch (ViewType.fromInt(holder.getItemViewType())) {
                case SHADOW:
                    holder.itemView.setBackground(Theme.getThemedDrawable(context, R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
                    break;
                case HEADER:
                    HeaderCell headerCell = (HeaderCell) holder.itemView;
                    if (position == privacyHeaderRow) {
                        headerCell.setText(LocaleController.getString("PrivacyTitle", R.string.PrivacyTitle));
                    } else if (position == translationHeaderRow) {
                        headerCell.setText(LocaleController.getString("TranslateMessages", R.string.TranslateMessages));
                    } else if (position == callHeaderRow) {
                        headerCell.setText(LocaleController.getString("Calls", R.string.Calls));
                    } else if (position == dcIdSettingsHeaderRow) {
                        headerCell.setText(LocaleController.getString("DC_IDSettings", R.string.DC_IDSettings));
                    } else if (position == notificationHeaderRow) {
                        headerCell.setText(LocaleController.getString("Notifications", R.string.Notifications));
                    }
                    break;
                case SWITCH:
                    TextCheckCell textCheckCell = (TextCheckCell) holder.itemView;
                    textCheckCell.setEnabled(true, null);
                    if (position == phoneNumberSwitchRow) {
                        textCheckCell.setTextAndValueAndCheck(LocaleController.getString("HidePhone", R.string.HidePhone), LocaleController.getString("HidePhoneDesc", R.string.HidePhoneDesc), AdvanceGramConfig.hidePhoneNumber, true, true);
                    } else if (position == phoneContactsSwitchRow) {
                        textCheckCell.setTextAndValueAndCheck(LocaleController.getString("HidePhoneOthers", R.string.HidePhoneOthers), LocaleController.getString("HidePhoneOthersDesc", R.string.HidePhoneOthersDesc), AdvanceGramConfig.hideContactNumber, true, true);
                    } else if (position == dcIdRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("ShowID_DC", R.string.ShowID_DC), AdvanceGramConfig.showIDAndDC, true);
                    } else if (position == confirmCallSwitchRow) {
                        textCheckCell.setTextAndValueAndCheck(LocaleController.getString("ConfirmCalls", R.string.ConfirmCalls), LocaleController.getString("ConfirmCallsDesc", R.string.ConfirmCallsDesc), AdvanceGramConfig.confirmCall, true, true);
                    } else if (position == notificationAccentRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("AccentAsNotificationColor", R.string.AccentAsNotificationColor), AdvanceGramConfig.accentAsNotificationColor, true);
                    } else if (position == keepMarkdownRow) {
                        textCheckCell.setTextAndValueAndCheck(LocaleController.getString("KeepMarkdown", R.string.KeepMarkdown), LocaleController.getString("KeepMarkdownDesc", R.string.KeepMarkdownDesc), AdvanceGramConfig.keepTranslationMarkdown, true, false);
                    } else if (position == showTranslateButtonRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("ShowTranslateButton", R.string.ShowTranslateButton), AdvanceGramConfig.showTranslate, true);
                    }
                    break;
                case SETTINGS:
                    TextSettingsCell textSettingsCell = (TextSettingsCell) holder.itemView;
                    textSettingsCell.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
                    if (position == translationProviderSelectRow) {
                        Pair<ArrayList<String>, ArrayList<Integer>> providers = Translator.getProviders();
                        ArrayList<String> names = providers.first;
                        ArrayList<Integer> types = providers.second;
                        if (names == null || types == null) {
                            return;
                        }
                        int index = types.indexOf(AdvanceGramConfig.translationProvider);
                        if (index < 0) {
                            textSettingsCell.setTextAndValue(LocaleController.getString("TranslationProviderShort", R.string.TranslationProviderShort), names.get(Translator.PROVIDER_GOOGLE), partial,true);
                        } else {
                            textSettingsCell.setTextAndValue(LocaleController.getString("TranslationProviderShort", R.string.TranslationProviderShort), names.get(index), partial,true);
                        }
                    } else if (position == destinationLanguageSelectRow) {
                        String language = AdvanceGramConfig.translationTarget;
                        CharSequence value;
                        if (language.equals("app")) {
                            value = LocaleController.getString("Default", R.string.Default);
                        } else {
                            Locale locale = Locale.forLanguageTag(language);
                            if (!TextUtils.isEmpty(locale.getScript())) {
                                value = HtmlCompat.fromHtml(AndroidUtilities.capitalize(locale.getDisplayScript()), HtmlCompat.FROM_HTML_MODE_LEGACY);
                            } else {
                                value = AndroidUtilities.capitalize(locale.getDisplayName());
                            }
                        }
                        textSettingsCell.setTextAndValue(LocaleController.getString("TranslationLanguage", R.string.TranslationLanguage), value, partial,true);
                    } else if (position == doNotTranslateSelectRow) {
                        String doNotTranslateCellValue = null;
                        HashSet<String> langCodes = DoNotTranslateSettings.getRestrictedLanguages(false);
                        if (langCodes.size() == 1) {
                            try {
                                String language = langCodes.iterator().next();
                                if (language.equals("app")) {
                                    doNotTranslateCellValue = LocaleController.getString("Default", R.string.Default);
                                } else {
                                    Locale locale = Locale.forLanguageTag(language);
                                    if (!TextUtils.isEmpty(locale.getScript())) {
                                        doNotTranslateCellValue = HtmlCompat.fromHtml(AndroidUtilities.capitalize(locale.getDisplayScript()), HtmlCompat.FROM_HTML_MODE_LEGACY).toString();
                                    } else {
                                        doNotTranslateCellValue = AndroidUtilities.capitalize(locale.getDisplayName());
                                    }
                                }
                            } catch (Exception ignored) {
                            }
                        } else if (langCodes.size() == 0) {
                            doNotTranslateCellValue = LocaleController.getString("EmptyExceptions", R.string.EmptyExceptions);
                        }
                        if (doNotTranslateCellValue == null)
                            doNotTranslateCellValue = String.format(LocaleController.getPluralString("Languages", langCodes.size()), langCodes.size());
                        if (!supportLanguageDetector) {
                            doNotTranslateCellValue = LocaleController.getString("EmptyExceptions", R.string.EmptyExceptions);
                            textSettingsCell.setAlpha(0.5f);
                        }
                        textSettingsCell.setTextAndValue(LocaleController.getString("DoNotTranslate", R.string.DoNotTranslate), doNotTranslateCellValue, partial,true);
                    } else if (position == deepLFormalityRow) {
                        String value;
                        switch (AdvanceGramConfig.deepLFormality) {
                            case DeepLTranslator.FORMALITY_DEFAULT:
                                value = LocaleController.getString("DeepLFormalityDefault", R.string.DeepLFormalityDefault);
                                break;
                            case DeepLTranslator.FORMALITY_MORE:
                                value = LocaleController.getString("DeepLFormalityMore", R.string.DeepLFormalityMore);
                                break;
                            case DeepLTranslator.FORMALITY_LESS:
                            default:
                                value = LocaleController.getString("DeepLFormalityLess", R.string.DeepLFormalityLess);
                                break;
                        }
                        textSettingsCell.setTextAndValue(LocaleController.getString("DeepLFormality", R.string.DeepLFormality), value, partial,true);
                    } else if (position == translationStyle) {
                        String value;
                        switch (AdvanceGramConfig.translatorStyle) {
                            case BaseTranslator.INLINE_STYLE:
                                value = LocaleController.getString("TranslatorTypeAdv", R.string.TranslatorTypeAdv);
                                break;
                            case BaseTranslator.DIALOG_STYLE:
                            default:
                                value = LocaleController.getString("TranslatorTypeTG", R.string.TranslatorTypeTG);
                                break;
                        }
                        textSettingsCell.setTextAndValue(LocaleController.getString("TranslatorType", R.string.TranslatorType), value, partial,true);
                    } else if (position == idTypeRow) {
                        String value;
                        switch (AdvanceGramConfig.idType) {
                            case 0:
                                value = "Bot API";
                                break;
                            default:
                            case 1:
                                value = "Telegram API";
                                break;
                        }
                        textSettingsCell.setTextAndValue(LocaleController.getString("IDType", R.string.IDType), value, partial,false);
                    } else if (position == autoTranslateRow) {
                        String value;
                        if (supportLanguageDetector) {
                            value = AdvanceGramConfig.autoTranslate ? LocaleController.getString("UseLessDataAlways", R.string.UseLessDataAlways) : LocaleController.getString("UseLessDataNever", R.string.UseLessDataNever);
                            int always = AutoTranslateConfig.getExceptions(true).size();
                            int never = AutoTranslateConfig.getExceptions(false).size();
                            if (always > 0 && never > 0) {
                                value += " (-" + never + ", +" + always + ")";
                            } else if (always > 0) {
                                value += " (+" + always + ")";
                            } else if (never > 0) {
                                value += " (-" + never + ")";
                            }
                        } else {
                            value = LocaleController.getString("UseLessDataNever", R.string.UseLessDataNever);
                        }
                        textSettingsCell.setTextAndValue(LocaleController.getString("AutoTranslate", R.string.AutoTranslate), value, keepMarkdownRow != -1);
                        if (!supportLanguageDetector) textSettingsCell.setAlpha(0.5f);
                    }
                    break;
                case TEXT_HINT_WITH_PADDING:
                    TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell) holder.itemView;
                    if (position == hintTranslation1) {
                        textInfoPrivacyCell.setTopPadding(0);
                        textInfoPrivacyCell.setText(LocaleController.getString("TranslateMessagesInfo1", R.string.TranslateMessagesInfo1));
                    } else if (position == hintTranslation2) {
                        Pair<ArrayList<String>, ArrayList<Integer>> providers = Translator.getProviders();
                        ArrayList<String> names = providers.first;
                        ArrayList<Integer> types = providers.second;
                        if (names == null || types == null) {
                            return;
                        }
                        int index = types.indexOf(AdvanceGramConfig.translationProvider);
                        if (index < 0) {
                            index = types.indexOf(Translator.PROVIDER_GOOGLE);
                        }
                        textInfoPrivacyCell.setTopPadding(0);
                        textInfoPrivacyCell.setText(LocaleController.formatString("TranslationProviderInfo", R.string.TranslationProviderInfo, names.get(index)));
                    } else if (position == hintIdRow) {
                        textInfoPrivacyCell.setTopPadding(0);
                        textInfoPrivacyCell.setText(LocaleController.getString("IDTypeAbout", R.string.IDTypeAbout));
                    }
                    break;
            }
        }

        @Override
        protected boolean isEnabled(ViewType viewType, int position) {
            if (position == autoTranslateRow || position == doNotTranslateSelectRow) {
                return supportLanguageDetector;
            }
            return viewType == ViewType.SWITCH || viewType == ViewType.SETTINGS;
        }

        @Override
        protected View onCreateViewHolder(ViewType viewType) {
            View view = null;
            if (viewType == ViewType.DC_STYLE_SELECTOR) {
                view = new DcStyleSelector(context) {
                    @Override
                    protected void onSelectedStyle() {
                        super.onSelectedStyle();
                        reloadInterface();
                    }
                };
                view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
            }
            return view;
        }

        @Override
        public ViewType getViewType(int position) {
            if (position == divisorPrivacyRow || position == divisorTranslationRow || position == divisorDCIdRow ||
                    position == dividerNotificationRow) {
                return ViewType.SHADOW;
            } else if (position == privacyHeaderRow || position == translationHeaderRow || position == callHeaderRow ||
                    position == dcIdSettingsHeaderRow || position == notificationHeaderRow) {
                return ViewType.HEADER;
            } else if (position == phoneNumberSwitchRow || position == phoneContactsSwitchRow || position == dcIdRow ||
                    position == confirmCallSwitchRow || position == notificationAccentRow || position == keepMarkdownRow ||
                    position == showTranslateButtonRow) {
                return ViewType.SWITCH;
            } else if (position == translationProviderSelectRow || position == destinationLanguageSelectRow || position == deepLFormalityRow ||
                    position == translationStyle || position == doNotTranslateSelectRow || position == idTypeRow || position == autoTranslateRow) {
                return ViewType.SETTINGS;
            } else if (position == hintTranslation1 || position == hintTranslation2 || position == hintIdRow) {
                return ViewType.TEXT_HINT_WITH_PADDING;
            } else if (position == dcStyleSelectorRow) {
                return ViewType.DC_STYLE_SELECTOR;
            }
            throw new IllegalArgumentException("Invalid position");
        }
    }
}
