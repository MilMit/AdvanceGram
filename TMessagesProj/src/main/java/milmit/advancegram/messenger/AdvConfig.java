package milmit.advancegram.messenger;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import org.telegram.messenger.ApplicationLoader;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import android.util.Base64;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import milmit.advancegram.messenger.config.ConfigItem;


@SuppressLint("ApplySharedPref")
public class AdvConfig {



    public static final SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("adv_cfg", Context.MODE_PRIVATE);
    public static final Object sync = new Object();
    public static final String channelAliasPrefix = "channelAliasPrefix_";

    private static boolean configLoaded = false;
    private static final ArrayList<ConfigItem> configs = new ArrayList<>();
    public static final ArrayList<DatacenterInfo> datacenterInfos = new ArrayList<>(5);

    // Configs
    public static ConfigItem migrate = addConfig("NekoConfigMigrate", ConfigItem.configTypeBool, false);
    public static ConfigItem largeAvatarInDrawer = addConfig("AvatarAsBackground", ConfigItem.configTypeInt, 0); // 0:TG Default 1:NekoX Default 2:Large Avatar
    public static ConfigItem unreadBadgeOnBackButton = addConfig("unreadBadgeOnBackButton", ConfigItem.configTypeBool, false);
    public static ConfigItem customPublicProxyIP = addConfig("customPublicProxyIP", ConfigItem.configTypeString, "");
    public static ConfigItem update_download_soucre = addConfig("update_download_soucre", ConfigItem.configTypeInt, 0); // 0: Github 1: Channel 2:CDNDrive, removed
    public static ConfigItem useCustomEmoji = addConfig("useCustomEmoji", ConfigItem.configTypeBool, false);
    public static ConfigItem repeatConfirm = addConfig("repeatConfirm", ConfigItem.configTypeBool, false);
    public static ConfigItem disableInstantCamera = addConfig("DisableInstantCamera", ConfigItem.configTypeBool, false);
    public static ConfigItem showSeconds = addConfig("showSeconds", ConfigItem.configTypeBool, false);

    public static ConfigItem enablePublicProxy = addConfig("enablePublicProxy", ConfigItem.configTypeBool, true);
    public static ConfigItem autoUpdateSubInfo = addConfig("autoUpdateSubInfo", ConfigItem.configTypeBool, true);
    public static ConfigItem lastUpdateCheckTime = addConfig("lastUpdateCheckTime", ConfigItem.configTypeLong, 0L);

    // From NekoConfig
    public static ConfigItem useIPv6 = addConfig("IPv6", ConfigItem.configTypeBool, false);
    public static ConfigItem hidePhone = addConfig("HidePhone", ConfigItem.configTypeBool, true);
    public static ConfigItem ignoreBlocked = addConfig("IgnoreBlocked", ConfigItem.configTypeBool, false);
    public static ConfigItem tabletMode = addConfig("TabletMode", ConfigItem.configTypeInt, 0);
    public static ConfigItem inappCamera = addConfig("DebugMenuEnableCamera", ConfigItem.configTypeBool, true); // fake
    public static ConfigItem smoothKeyboard = addConfig("DebugMenuEnableSmoothKeyboard", ConfigItem.configTypeBool, false);// fake

    public static ConfigItem typeface = addConfig("TypefaceUseDefault", ConfigItem.configTypeBool, false);
    public static ConfigItem nameOrder = addConfig("NameOrder", ConfigItem.configTypeInt, 1);
    public static ConfigItem mapPreviewProvider = addConfig("MapPreviewProvider", ConfigItem.configTypeInt, 0);
    public static ConfigItem transparentStatusBar = addConfig("TransparentStatusBar", ConfigItem.configTypeBool, true);
    public static ConfigItem forceBlurInChat = addConfig("forceBlurInChat", ConfigItem.configTypeBool, false);
    public static ConfigItem chatBlueAlphaValue = addConfig("forceBlurInChatAlphaValue", ConfigItem.configTypeInt, 127);
    public static ConfigItem hideProxySponsorChannel = addConfig("HideProxySponsorChannel", ConfigItem.configTypeBool, false);
    public static ConfigItem showAddToSavedMessages = addConfig("showAddToSavedMessages", ConfigItem.configTypeBool, true);
    public static ConfigItem showReport = addConfig("showReport", ConfigItem.configTypeBool, true);
    public static ConfigItem showViewHistory = addConfig("showViewHistory", ConfigItem.configTypeBool, true);
    public static ConfigItem showAdminActions = addConfig("showAdminActions", ConfigItem.configTypeBool, true);
    public static ConfigItem showChangePermissions = addConfig("showChangePermissions", ConfigItem.configTypeBool, true);
    public static ConfigItem showDeleteDownloadedFile = addConfig("showDeleteDownloadedFile", ConfigItem.configTypeBool, true);
    public static ConfigItem showMessageDetails = addConfig("showMessageDetails", ConfigItem.configTypeBool, false);
    public static ConfigItem showTranslate = addConfig("showTranslate", ConfigItem.configTypeBool, true);
    public static ConfigItem showRepeat = addConfig("showRepeat", ConfigItem.configTypeBool, false);
    public static ConfigItem showShareMessages = addConfig("showShareMessages", ConfigItem.configTypeBool, false);
    public static ConfigItem showMessageHide = addConfig("showMessageHide", ConfigItem.configTypeBool, false);

    public static ConfigItem eventType = addConfig("eventType", ConfigItem.configTypeInt, 0);
    public static ConfigItem actionBarDecoration = addConfig("ActionBarDecoration", ConfigItem.configTypeInt, 0);
    public static ConfigItem newYear = addConfig("ChristmasHat", ConfigItem.configTypeBool, false);
    public static ConfigItem stickerSize = addConfig("stickerSize", ConfigItem.configTypeFloat, 14.0f);
    public static ConfigItem unlimitedFavedStickers = addConfig("UnlimitedFavoredStickers", ConfigItem.configTypeBool, false);
    public static ConfigItem unlimitedPinnedDialogs = addConfig("UnlimitedPinnedDialogs", ConfigItem.configTypeBool, false);
    public static ConfigItem translationProvider = addConfig("translationProvider", ConfigItem.configTypeInt, 1);
    public static ConfigItem disablePhotoSideAction = addConfig("DisablePhotoViewerSideAction", ConfigItem.configTypeBool, false);
    public static ConfigItem openArchiveOnPull = addConfig("OpenArchiveOnPull", ConfigItem.configTypeBool, false);
    public static ConfigItem hideKeyboardOnChatScroll = addConfig("HideKeyboardOnChatScroll", ConfigItem.configTypeBool, false);
    public static ConfigItem avatarBackgroundBlur = addConfig("BlurAvatarBackground", ConfigItem.configTypeBool, false);
    public static ConfigItem avatarBackgroundDarken = addConfig("DarkenAvatarBackground", ConfigItem.configTypeBool, false);
    public static ConfigItem useSystemEmoji = addConfig("EmojiUseDefault", ConfigItem.configTypeBool, false);
    public static ConfigItem showTabsOnForward = addConfig("ShowTabsOnForward", ConfigItem.configTypeBool, false);
    public static ConfigItem rearVideoMessages = addConfig("RearVideoMessages", ConfigItem.configTypeBool, false);
    public static ConfigItem hideAllTab = addConfig("HideAllTab", ConfigItem.configTypeBool, false);
//    public static ConfigItem pressTitleToOpenAllChats = addConfig("pressTitleToOpenAllChats", configTypeBool, false);

    public static ConfigItem disableChatAction = addConfig("DisableChatAction", ConfigItem.configTypeBool, false);
    public static ConfigItem sortByUnread = addConfig("sort_by_unread", ConfigItem.configTypeBool, false);
    public static ConfigItem sortByUnmuted = addConfig("sort_by_unmuted", ConfigItem.configTypeBool, true);
    public static ConfigItem sortByUser = addConfig("sort_by_user", ConfigItem.configTypeBool, true);
    public static ConfigItem sortByContacts = addConfig("sort_by_contacts", ConfigItem.configTypeBool, true);

    public static ConfigItem disableUndo = addConfig("DisableUndo", ConfigItem.configTypeBool, false);

    public static ConfigItem filterUsers = addConfig("filter_users", ConfigItem.configTypeBool, true);
    public static ConfigItem filterContacts = addConfig("filter_contacts", ConfigItem.configTypeBool, true);
    public static ConfigItem filterGroups = addConfig("filter_groups", ConfigItem.configTypeBool, true);
    public static ConfigItem filterChannels = addConfig("filter_channels", ConfigItem.configTypeBool, true);
    public static ConfigItem filterBots = addConfig("filter_bots", ConfigItem.configTypeBool, true);
    public static ConfigItem filterAdmins = addConfig("filter_admins", ConfigItem.configTypeBool, true);
    public static ConfigItem filterUnmuted = addConfig("filter_unmuted", ConfigItem.configTypeBool, true);
    public static ConfigItem filterUnread = addConfig("filter_unread", ConfigItem.configTypeBool, true);
    public static ConfigItem filterUnmutedAndUnread = addConfig("filter_unmuted_and_unread", ConfigItem.configTypeBool, true);

    public static ConfigItem disableSystemAccount = addConfig("DisableSystemAccount", ConfigItem.configTypeBool, false);
//    public static ConfigItem disableProxyWhenVpnEnabled = addConfig("DisableProxyWhenVpnEnabled", configTypeBool, false);
    public static ConfigItem skipOpenLinkConfirm = addConfig("SkipOpenLinkConfirm", ConfigItem.configTypeBool, false);

    public static ConfigItem ignoreMutedCount = addConfig("IgnoreMutedCount", ConfigItem.configTypeBool, true);
//    public static ConfigItem useDefaultTheme = addConfig("UseDefaultTheme", configTypeBool, false);
    public static ConfigItem showIdAndDc = addConfig("ShowIdAndDc", ConfigItem.configTypeBool, false);

    public static ConfigItem googleCloudTranslateKey = addConfig("GoogleCloudTransKey", ConfigItem.configTypeString, "");
    public static ConfigItem cachePath = addConfig("cache_path", ConfigItem.configTypeString, "");
    public static ConfigItem customSavePath = addConfig("customSavePath", ConfigItem.configTypeString, "Advgram");

    public static ConfigItem translateToLang = addConfig("TransToLang", ConfigItem.configTypeString, ""); // "" -> translate to current language (MessageTrans.kt & TranslatorMilMit.kt)
    public static ConfigItem translateInputLang = addConfig("TransInputToLang", ConfigItem.configTypeString, "en");

    public static ConfigItem disableNotificationBubbles = addConfig("disableNotificationBubbles", ConfigItem.configTypeBool, false);

    public static ConfigItem ccToLang = addConfig("opencc_to_lang", ConfigItem.configTypeString, "");
    public static ConfigItem ccInputLang = addConfig("opencc_input_to_lang", ConfigItem.configTypeString, "");

 //   public static ConfigItem tabsTitleType = addConfig("TabTitleType", configTypeInt, NekoXConfig.TITLE_TYPE_TEXT);
    public static ConfigItem confirmAVMessage = addConfig("ConfirmAVMessage", ConfigItem.configTypeBool, false);
    public static ConfigItem askBeforeCall = addConfig("AskBeforeCalling", ConfigItem.configTypeBool, false);
    public static ConfigItem disableNumberRounding = addConfig("DisableNumberRounding", ConfigItem.configTypeBool, false);

    public static ConfigItem useSystemDNS = addConfig("useSystemDNS", ConfigItem.configTypeBool, false);
    public static ConfigItem customDoH = addConfig("customDoH", ConfigItem.configTypeString, "");
    public static ConfigItem hideProxyByDefault = addConfig("HideProxyByDefault", ConfigItem.configTypeBool, false);
    public static ConfigItem useProxyItem = addConfig("UseProxyItem", ConfigItem.configTypeBool, true);

    public static ConfigItem disableAppBarShadow = addConfig("DisableAppBarShadow", ConfigItem.configTypeBool, false);
    public static ConfigItem mediaPreview = addConfig("MediaPreview", ConfigItem.configTypeBool, true);

    public static ConfigItem proxyAutoSwitch = addConfig("ProxyAutoSwitch", ConfigItem.configTypeBool, false);

    public static ConfigItem usePersianCalendar = addConfig("UsePersiancalendar", ConfigItem.configTypeBool, false);
    public static ConfigItem displayPersianCalendarByLatin = addConfig("DisplayPersianCalendarByLatin", ConfigItem.configTypeBool, false);
    public static ConfigItem openPGPApp = addConfig("OpenPGPApp", ConfigItem.configTypeString, "");
    public static ConfigItem openPGPKeyId = addConfig("OpenPGPKey", ConfigItem.configTypeLong, 0L);

    public static ConfigItem disableVibration = addConfig("DisableVibration", ConfigItem.configTypeBool, false);
    public static ConfigItem autoPauseVideo = addConfig("AutoPauseVideo", ConfigItem.configTypeBool, false);
    public static ConfigItem disableProximityEvents = addConfig("DisableProximityEvents", ConfigItem.configTypeBool, false);

 //   public static ConfigItem ignoreContentRestrictions = addConfig("ignoreContentRestrictions", configTypeBool, !BuildVars.isPlay);
   //miladedit
    public static ConfigItem showCantOpenAlert = addConfig("showCantOpenAlert", ConfigItem.configTypeBool, true);
    public static ConfigItem GoogleDoh = addConfig("GoogleDoh", ConfigItem.configTypeBool, true);
    public static ConfigItem GooglevisionQrReader = addConfig("GooglevisionQrReader", ConfigItem.configTypeBool, false);
    public static ConfigItem BottomBuilderChannelcreate = addConfig("BottomBuilderChannelcreate", ConfigItem.configTypeBool, false);
    public static ConfigItem BottomBuilderProfileActivity = addConfig("BottomBuilderProfileActivity", ConfigItem.configTypeBool, false);
    public static ConfigItem isMapsInstalled = addConfig("isMapsInstalled", ConfigItem.configTypeBool, false);
    //
    public static ConfigItem useChatAttachMediaMenu = addConfig("UseChatAttachEnterMenu", ConfigItem.configTypeBool, true);
    public static ConfigItem disableLinkPreviewByDefault = addConfig("DisableLinkPreviewByDefault", ConfigItem.configTypeBool, false);
    public static ConfigItem sendCommentAfterForward = addConfig("SendCommentAfterForward", ConfigItem.configTypeBool, true);
//    public static ConfigItem increaseVoiceMessageQuality = addConfig("IncreaseVoiceMessageQuality", configTypeBool, true);
    public static ConfigItem disableTrending = addConfig("DisableTrending", ConfigItem.configTypeBool, true);
    public static ConfigItem dontSendGreetingSticker = addConfig("DontSendGreetingSticker", ConfigItem.configTypeBool, false);
    public static ConfigItem hideTimeForSticker = addConfig("HideTimeForSticker", ConfigItem.configTypeBool, false);
    public static ConfigItem takeGIFasVideo = addConfig("TakeGIFasVideo", ConfigItem.configTypeBool, false);
    public static ConfigItem maxRecentStickerCount = addConfig("maxRecentStickerCount", ConfigItem.configTypeInt, 20);
    public static ConfigItem disableSwipeToNext = addConfig("disableSwipeToNextChannel", ConfigItem.configTypeBool, true);
    public static ConfigItem disableRemoteEmojiInteractions = addConfig("disableRemoteEmojiInteractions", ConfigItem.configTypeBool, true);
    public static ConfigItem disableChoosingSticker = addConfig("disableChoosingSticker", ConfigItem.configTypeBool, false);
    public static ConfigItem hideGroupSticker = addConfig("hideGroupSticker", ConfigItem.configTypeBool, false);
    public static ConfigItem disablePremiumStickerAnimation = addConfig("disablePremiumStickerAnimation", ConfigItem.configTypeBool, false);
    public static ConfigItem hideSponsoredMessage = addConfig("hideSponsoredMessage", ConfigItem.configTypeBool, false);
    public static ConfigItem rememberAllBackMessages = addConfig("rememberAllBackMessages", ConfigItem.configTypeBool, false);
    public static ConfigItem hideSendAsChannel = addConfig("hideSendAsChannel", ConfigItem.configTypeBool, false);
    public static ConfigItem showSpoilersDirectly = addConfig("showSpoilersDirectly", ConfigItem.configTypeBool, false);
    public static ConfigItem reactions = addConfig("reactions", ConfigItem.configTypeInt, 0);
    public static ConfigItem showBottomActionsWhenSelecting = addConfig("showBottomActionsWhenSelecting", ConfigItem.configTypeBool, false);

    public static ConfigItem labelChannelUser = addConfig("labelChannelUser", ConfigItem.configTypeBool, false);
    public static ConfigItem channelAlias = addConfig("channelAlias", ConfigItem.configTypeBool, false);

    public static ConfigItem disableAutoDownloadingWin32Executable = addConfig("Win32ExecutableFiles", ConfigItem.configTypeBool, true);
    public static ConfigItem disableAutoDownloadingArchive = addConfig("ArchiveFiles", ConfigItem.configTypeBool, true);

    public static ConfigItem enableStickerPin = addConfig("EnableStickerPin", ConfigItem.configTypeBool, false);
    public static ConfigItem useMediaStreamInVoip = addConfig("UseMediaStreamInVoip", ConfigItem.configTypeBool, false);
    public static ConfigItem customAudioBitrate = addConfig("customAudioBitrate", ConfigItem.configTypeInt, 32);
    public static ConfigItem disableGroupVoipAudioProcessing = addConfig("disableGroupVoipAudioProcessing", ConfigItem.configTypeBool, false);
    public static ConfigItem enhancedFileLoader = addConfig("enhancedFileLoader", ConfigItem.configTypeBool, false);
  //  public static ConfigItem useOSMDroidMap = addConfig("useOSMDroidMap", configTypeBool, !BuildVars.isGServicesCompiled);
    public static ConfigItem mapDriftingFixForGoogleMaps = addConfig("mapDriftingFixForGoogleMaps", ConfigItem.configTypeBool, true);

    // priv branch changes
    public static ConfigItem localPremium = addConfig("localPremium", ConfigItem.configTypeBool, true);

    static {
        loadConfig(false);
        checkMigrate(false);
    }

    public static ConfigItem addConfig(String k, int t, Object d) {
        ConfigItem a = new ConfigItem(k, t, d);
        configs.add(a);
        return a;
    }

    public static void loadConfig(boolean force) {
        synchronized (sync) {
            if (configLoaded && !force) {
                return;
            }
            for (int i = 0; i < configs.size(); i++) {
                ConfigItem o = configs.get(i);

                if (o.type == ConfigItem.configTypeBool) {
                    o.value = preferences.getBoolean(o.key, (boolean) o.defaultValue);
                }
                if (o.type == ConfigItem.configTypeInt) {
                    o.value = preferences.getInt(o.key, (int) o.defaultValue);
                }
                if (o.type == ConfigItem.configTypeLong) {
                    o.value = preferences.getLong(o.key, (Long) o.defaultValue);
                }
                if (o.type == ConfigItem.configTypeFloat) {
                    o.value = preferences.getFloat(o.key, (Float) o.defaultValue);
                }
                if (o.type == ConfigItem.configTypeString) {
                    o.value = preferences.getString(o.key, (String) o.defaultValue);
                }
                if (o.type == ConfigItem.configTypeSetInt) {
                    Set<String> ss = preferences.getStringSet(o.key, new HashSet<>());
                    HashSet<Integer> si = new HashSet<>();
                    for (String s : ss) {
                        si.add(Integer.parseInt(s));
                    }
                    o.value = si;
                }
                if (o.type == ConfigItem.configTypeMapIntInt) {
                    String cv = preferences.getString(o.key, "");
                    // Log.e("NC", String.format("Getting pref %s val %s", o.key, cv));
                    if (cv.length() == 0) {
                        o.value = new HashMap<Integer, Integer>();
                    } else {
                        try {
                            byte[] data = Base64.decode(cv, Base64.DEFAULT);
                            ObjectInputStream ois = new ObjectInputStream(
                                    new ByteArrayInputStream(data));
                            o.value = (HashMap<Integer, Integer>) ois.readObject();
                            if (o.value == null) {
                                o.value = new HashMap<Integer, Integer>();
                            }
                            ois.close();
                        } catch (Exception e) {
                            o.value = new HashMap<Integer, Integer>();
                        }
                    }
                }
            }
            for (int a = 1; a <= 5; a++) {
                datacenterInfos.add(new DatacenterInfo(a));
            }
            configLoaded = true;
        }
    }

    public static void checkMigrate(boolean force) {
        // TODO remove this after some versions.
        if (migrate.Bool() || force)
            return;

        migrate.setConfigBool(true);

        // NekoConfig.java read & migrate
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("nekoconfig", Activity.MODE_PRIVATE);

        if (preferences.contains("typeface"))
            typeface.setConfigBool(preferences.getInt("typeface", 0) != 0);
        if (preferences.contains("nameOrder"))
            nameOrder.setConfigInt(preferences.getInt("nameOrder", 1));
        if (preferences.contains("mapPreviewProvider"))
            mapPreviewProvider.setConfigInt(preferences.getInt("mapPreviewProvider", 0));
        if (preferences.contains("transparentStatusBar"))
            transparentStatusBar.setConfigBool(preferences.getBoolean("transparentStatusBar", false));
        if (preferences.contains("hideProxySponsorChannel"))
            hideProxySponsorChannel.setConfigBool(preferences.getBoolean("hideProxySponsorChannel", false));
        if (preferences.contains("showAddToSavedMessages"))
            showAddToSavedMessages.setConfigBool(preferences.getBoolean("showAddToSavedMessages", true));
        if (preferences.contains("showReport"))
            showReport.setConfigBool(preferences.getBoolean("showReport", true));
        if (preferences.contains("showViewHistory"))
            showViewHistory.setConfigBool(preferences.getBoolean("showViewHistory", true));
        if (preferences.contains("showAdminActions"))
            showAdminActions.setConfigBool(preferences.getBoolean("showAdminActions", true));
        if (preferences.contains("showChangePermissions"))
            showChangePermissions.setConfigBool(preferences.getBoolean("showChangePermissions", true));
        if (preferences.contains("showDeleteDownloadedFile"))
            showDeleteDownloadedFile.setConfigBool(preferences.getBoolean("showDeleteDownloadedFile", true));
        if (preferences.contains("showMessageDetails"))
            showMessageDetails.setConfigBool(preferences.getBoolean("showMessageDetails", false));
        if (preferences.contains("showTranslate"))
            showTranslate.setConfigBool(preferences.getBoolean("showTranslate", true));
        if (preferences.contains("showRepeat"))
            showRepeat.setConfigBool(preferences.getBoolean("showRepeat", false));
        if (preferences.contains("showShareMessages"))
            showShareMessages.setConfigBool(preferences.getBoolean("showShareMessages", false));
        if (preferences.contains("showMessageHide"))
            showMessageHide.setConfigBool(preferences.getBoolean("showMessageHide", false));

        if (preferences.contains("eventType"))
            eventType.setConfigInt(preferences.getInt("eventType", 0));
        if (preferences.contains("actionBarDecoration"))
            actionBarDecoration.setConfigInt(preferences.getInt("actionBarDecoration", 0));
        if (preferences.contains("newYear"))
            newYear.setConfigBool(preferences.getBoolean("newYear", false));
        if (preferences.contains("stickerSize"))
            stickerSize.setConfigFloat(preferences.getFloat("stickerSize", 14.0f));
        if (preferences.contains("unlimitedFavedStickers"))
            unlimitedFavedStickers.setConfigBool(preferences.getBoolean("unlimitedFavedStickers", false));
        if (preferences.contains("unlimitedPinnedDialogs"))
            unlimitedPinnedDialogs.setConfigBool(preferences.getBoolean("unlimitedPinnedDialogs", false));
        if (preferences.contains("translationProvider"))
            translationProvider.setConfigInt(preferences.getInt("translationProvider", 1));
        if (preferences.contains("disablePhotoSideAction"))
            disablePhotoSideAction.setConfigBool(preferences.getBoolean("disablePhotoSideAction", true));
        if (preferences.contains("openArchiveOnPull"))
            openArchiveOnPull.setConfigBool(preferences.getBoolean("openArchiveOnPull", false));
        if (preferences.contains("showHiddenFeature"))             //showHiddenFeature.setConfigBool(preferences.getBoolean("showHiddenFeature", false));
            if (preferences.contains("hideKeyboardOnChatScroll"))
                hideKeyboardOnChatScroll.setConfigBool(preferences.getBoolean("hideKeyboardOnChatScroll", false));
        if (preferences.contains("avatarBackgroundBlur"))
            avatarBackgroundBlur.setConfigBool(preferences.getBoolean("avatarBackgroundBlur", false));
        if (preferences.contains("avatarBackgroundDarken"))
            avatarBackgroundDarken.setConfigBool(preferences.getBoolean("avatarBackgroundDarken", false));
        if (preferences.contains("useSystemEmoji"))
            useSystemEmoji.setConfigBool(preferences.getBoolean("useSystemEmoji", false));
        if (preferences.contains("showTabsOnForward"))
            showTabsOnForward.setConfigBool(preferences.getBoolean("showTabsOnForward", false));
        if (preferences.contains("rearVideoMessages"))
            rearVideoMessages.setConfigBool(preferences.getBoolean("rearVideoMessages", false));
        if (preferences.contains("hideAllTab"))
            hideAllTab.setConfigBool(preferences.getBoolean("hideAllTab", false));
        if (preferences.contains("disable_chat_action"))
            disableChatAction.setConfigBool(preferences.getBoolean("disable_chat_action", false));
        if (preferences.contains("sort_by_unread"))
            sortByUnread.setConfigBool(preferences.getBoolean("sort_by_unread", false));
        if (preferences.contains("sort_by_unmuted"))
            sortByUnmuted.setConfigBool(preferences.getBoolean("sort_by_unmuted", true));
        if (preferences.contains("sort_by_user"))
            sortByUser.setConfigBool(preferences.getBoolean("sort_by_user", true));
        if (preferences.contains("sort_by_contacts"))
            sortByContacts.setConfigBool(preferences.getBoolean("sort_by_contacts", true));

        if (preferences.contains("disable_undo"))
            disableUndo.setConfigBool(preferences.getBoolean("disable_undo", false));

        if (preferences.contains("filter_users"))
            filterUsers.setConfigBool(preferences.getBoolean("filter_users", true));
        if (preferences.contains("filter_contacts"))
            filterContacts.setConfigBool(preferences.getBoolean("filter_contacts", true));
        if (preferences.contains("filter_groups"))
            filterGroups.setConfigBool(preferences.getBoolean("filter_groups", true));
        if (preferences.contains("filter_channels"))
            filterChannels.setConfigBool(preferences.getBoolean("filter_channels", true));
        if (preferences.contains("filter_bots"))
            filterBots.setConfigBool(preferences.getBoolean("filter_bots", true));
        if (preferences.contains("filter_admins"))
            filterAdmins.setConfigBool(preferences.getBoolean("filter_admins", true));
        if (preferences.contains("filter_unmuted"))
            filterUnmuted.setConfigBool(preferences.getBoolean("filter_unmuted", true));
        if (preferences.contains("filter_unread"))
            filterUnread.setConfigBool(preferences.getBoolean("filter_unread", true));
        if (preferences.contains("filter_unmuted_and_unread"))
            filterUnmutedAndUnread.setConfigBool(preferences.getBoolean("filter_unmuted_and_unread", true));

        if (preferences.contains("disable_system_account"))
            disableSystemAccount.setConfigBool(preferences.getBoolean("disable_system_account", false));
        if (preferences.contains("skip_open_link_confirm"))
            skipOpenLinkConfirm.setConfigBool(preferences.getBoolean("skip_open_link_confirm", false));

        if (preferences.contains("ignore_muted_count"))
            ignoreMutedCount.setConfigBool(preferences.getBoolean("ignore_muted_count", true));
//        if (preferences.contains("use_default_theme"))
//            useDefaultTheme.setConfigBool(preferences.getBoolean("use_default_theme", false));
        if (preferences.contains("show_id_and_dc"))
            showIdAndDc.setConfigBool(preferences.getBoolean("show_id_and_dc", false));

        if (preferences.contains("google_cloud_translate_key"))
            googleCloudTranslateKey.setConfigString(preferences.getString("google_cloud_translate_key", null));
        if (preferences.contains("cache_path"))
            cachePath.setConfigString(preferences.getString("cache_path", null));

        if (preferences.contains("trans_to_lang"))
            translateToLang.setConfigString(preferences.getString("trans_to_lang", ""));
        if (preferences.contains("trans_input_to_lang"))
            translateInputLang.setConfigString(preferences.getString("trans_input_to_lang", "en"));

        if (preferences.contains("opencc_to_lang"))
            ccToLang.setConfigString(preferences.getString("opencc_to_lang", null));
        if (preferences.contains("opencc_input_to_lang"))
            ccInputLang.setConfigString(preferences.getString("opencc_input_to_lang", null));

        if (preferences.contains("tabsTitleType"))
     //       tabsTitleType.setConfigInt(preferences.getInt("tabsTitleType", NekoXConfig.TITLE_TYPE_TEXT));
        if (preferences.contains("confirmAVMessage"))
            confirmAVMessage.setConfigBool(preferences.getBoolean("confirmAVMessage", false));
        if (preferences.contains("askBeforeCall"))
            askBeforeCall.setConfigBool(preferences.getBoolean("askBeforeCall", false));
        if (preferences.contains("disableNumberRounding"))
            disableNumberRounding.setConfigBool(preferences.getBoolean("disableNumberRounding", false));

        if (preferences.contains("useSystemDNS"))
            useSystemDNS.setConfigBool(preferences.getBoolean("useSystemDNS", false));
        if (preferences.contains("customDoH"))
            customDoH.setConfigString(preferences.getString("customDoH", ""));
        if (preferences.contains("hide_proxy_by_default"))
            hideProxyByDefault.setConfigBool(preferences.getBoolean("hide_proxy_by_default", false));
        if (preferences.contains("use_proxy_item"))
            useProxyItem.setConfigBool(preferences.getBoolean("use_proxy_item", true));

        if (preferences.contains("disableAppBarShadow"))
            disableAppBarShadow.setConfigBool(preferences.getBoolean("disableAppBarShadow", false));
        if (preferences.contains("mediaPreview"))
            mediaPreview.setConfigBool(preferences.getBoolean("mediaPreview", true));

        if (preferences.contains("proxy_auto_switch"))
            proxyAutoSwitch.setConfigBool(preferences.getBoolean("proxy_auto_switch", false));

        if (preferences.contains("openPGPApp"))
            openPGPApp.setConfigString(preferences.getString("openPGPApp", ""));
        if (preferences.contains("openPGPKeyId"))
            openPGPKeyId.setConfigLong(preferences.getLong("openPGPKeyId", 0L));

        if (preferences.contains("disableVibration"))
            disableVibration.setConfigBool(preferences.getBoolean("disableVibration", false));
        if (preferences.contains("autoPauseVideo"))
            autoPauseVideo.setConfigBool(preferences.getBoolean("autoPauseVideo", false));
        if (preferences.contains("disableProximityEvents"))
            disableProximityEvents.setConfigBool(preferences.getBoolean("disableProximityEvents", false));

        if (preferences.contains("ignoreContentRestrictions"))
       //     ignoreContentRestrictions.setConfigBool(preferences.getBoolean("ignoreContentRestrictions", !BuildVars.isPlay));
        if (preferences.contains("useChatAttachMediaMenu"))
            useChatAttachMediaMenu.setConfigBool(preferences.getBoolean("useChatAttachMediaMenu", true));
        if (preferences.contains("disableLinkPreviewByDefault"))
            disableLinkPreviewByDefault.setConfigBool(preferences.getBoolean("disableLinkPreviewByDefault", false));
        if (preferences.contains("sendCommentAfterForward"))
            sendCommentAfterForward.setConfigBool(preferences.getBoolean("sendCommentAfterForward", true));
//        if (preferences.contains("increaseVoiceMessageQuality"))
//            increaseVoiceMessageQuality.setConfigBool(preferences.getBoolean("increaseVoiceMessageQuality", true));
        if (preferences.contains("disableTrending"))
            disableTrending.setConfigBool(preferences.getBoolean("disableTrending", true));
        if (preferences.contains("dontSendGreetingSticker"))
            dontSendGreetingSticker.setConfigBool(preferences.getBoolean("dontSendGreetingSticker", false));
        if (preferences.contains("hideTimeForSticker"))
            hideTimeForSticker.setConfigBool(preferences.getBoolean("hideTimeForSticker", false));
        if (preferences.contains("takeGIFasVideo"))
            takeGIFasVideo.setConfigBool(preferences.getBoolean("takeGIFasVideo", false));
        if (preferences.contains("maxRecentStickerCount"))
            maxRecentStickerCount.setConfigInt(preferences.getInt("maxRecentStickerCount", 20));
        if (preferences.contains("disableSwipeToNext"))
            disableSwipeToNext.setConfigBool(preferences.getBoolean("disableSwipeToNext", true));
        if (preferences.contains("disableRemoteEmojiInteractions"))
            disableRemoteEmojiInteractions.setConfigBool(preferences.getBoolean("disableRemoteEmojiInteractions", true));
        if (preferences.contains("disableChoosingSticker"))
            disableChoosingSticker.setConfigBool(preferences.getBoolean("disableChoosingSticker", false));

        if (preferences.contains("disableAutoDownloadingWin32Executable"))
            disableAutoDownloadingWin32Executable.setConfigBool(preferences.getBoolean("disableAutoDownloadingWin32Executable", true));
        if (preferences.contains("disableAutoDownloadingArchive"))
            disableAutoDownloadingArchive.setConfigBool(preferences.getBoolean("disableAutoDownloadingArchive", true));

        if (preferences.contains("enableStickerPin"))
            enableStickerPin.setConfigBool(preferences.getBoolean("enableStickerPin", false));
        if (preferences.contains("useMediaStreamInVoip"))
            useMediaStreamInVoip.setConfigBool(preferences.getBoolean("useMediaStreamInVoip", false));
        if (preferences.contains("customAudioBitrate"))
            customAudioBitrate.setConfigInt(preferences.getInt("customAudioBitrate", 32));
        if (preferences.contains("disableGroupVoipAudioProcessing"))
            disableGroupVoipAudioProcessing.setConfigBool(preferences.getBoolean("disableGroupVoipAudioProcessing", false));
    }

    public static class DatacenterInfo {

        public int id;

        public long pingId;
        public long ping;
        public boolean checking;
        public boolean available;
        public long availableCheckTime;

        public DatacenterInfo(int i) {
            id = i;
        }
    }

   // public static boolean fixDriftingForGoogleMaps() {
    //    return BuildVars.isGServicesCompiled && !useOSMDroidMap.Bool() && mapDriftingFixForGoogleMaps.Bool();
   // }
}
