package milmit.advancegram.messenger;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.ui.ActionBar.Theme;

import java.util.Calendar;
import java.util.Date;


import milmit.advancegram.messenger.camera.CameraXUtils;
import milmit.advancegram.messenger.helpers.MonetIconsHelper;
import milmit.advancegram.messenger.helpers.SharedPreferencesHelper;
import milmit.advancegram.messenger.translator.AutoTranslateConfig;
import milmit.advancegram.messenger.translator.BaseTranslator;
import milmit.advancegram.messenger.translator.DeepLTranslator;
import milmit.advancegram.messenger.translator.Translator;

public class AdvanceGramConfig extends SettingsManager {
    public static final int TAB_TYPE_TEXT = 0;
    public static final int TAB_TYPE_MIX = 1;
    public static final int TAB_TYPE_ICON = 2;

    public static final int DOWNLOAD_BOOST_DEFAULT = 0;
    public static final int DOWNLOAD_BOOST_FAST = 1;
    public static final int DOWNLOAD_BOOST_EXTREME = 2;

    private static final Object sync = new Object();
    public static boolean hidePhoneNumber;
    public static boolean hideContactNumber;
    public static boolean fullTime;
    public static boolean roundedNumbers;
    public static boolean confirmCall;
    public static boolean PersianCalendar;
    public static boolean mediaFlipByTap;
    public static boolean jumpChannel;
    public static boolean hideKeyboard;
    public static boolean gifAsVideo;
    public static boolean showFolderWhenForward;
    public static boolean useRearCamera;
    public static boolean sendConfirm;
    public static boolean useSystemFont;
    public static boolean useSystemEmoji;
    public static boolean showGreetings;
    public static boolean showTranslate;
    public static boolean showSaveMessage;
    public static boolean showRepeat;
    public static boolean showNoQuoteForward;
    public static boolean showCopyPhoto;
    public static boolean showMessageDetails;
    public static boolean betaUpdates;
    public static boolean notifyUpdates;
    public static boolean avatarBackgroundDarken;
    public static boolean avatarBackgroundBlur;
    public static boolean avatarAsDrawerBackground;
    public static boolean betterAudioQuality;
    public static boolean localPremium;
    public static boolean showReportMessage;
    public static boolean showGradientColor;
    public static boolean showAvatarImage;
    public static boolean advEasterSound;
    public static boolean pacmanForced;
    public static boolean smartButtons;
    public static boolean disableAppBarShadow;
    public static boolean accentAsNotificationColor;
    public static boolean showDeleteDownloadedFile;
    public static boolean isChineseUser = false;
    public static boolean showSantaHat;
    public static boolean showSnowFalling;
    public static boolean useCameraXOptimizedMode;
    public static boolean disableProximityEvents;
    public static boolean devOptEnabled;
    public static boolean verifyLinkTip;
    public static boolean voicesAgc;
    public static boolean turnSoundOnVDKey;
    public static boolean openArchiveOnPull;
    public static boolean slidingChatTitle;
    public static boolean confirmStickersGIFs;
    public static boolean showIDAndDC;
    public static boolean xiaomiBlockedInstaller;
    public static boolean searchIconInActionBar;
    public static boolean autoTranslate;
    public static boolean showPencilIcon;
    public static boolean keepTranslationMarkdown;
    public static boolean uploadSpeedBoost;
    public static boolean hideTimeOnSticker;
    public static boolean showStatusInChat;
    public static boolean showPatpat;
    public static boolean unlockedChupa;
    public static boolean hideAllTab;
    public static boolean hideSendAsChannel;
    public static boolean showNameInActionBar;
    public static boolean stickersSorting;
    public static String translationTarget = "app";
    public static String translationKeyboardTarget = "app";
    public static String updateData;
    public static String drawerItems;
    public static String oldBuildVersion = null;
    public static String languagePackVersioning;
    public static String doNotTranslateLanguages;
    public static int deepLFormality;
    public static int translationProvider;
    public static int lastUpdateStatus = 0;
    public static int tabMode = 0;
    public static int remindedUpdate = 0;
    public static int oldDownloadedVersion = 0;
    public static int blurIntensity = 0;
    public static int eventType = 0;
    public static int buttonStyleType = 0;
    public static int translatorStyle = 0;
    public static int cameraType;
    public static int cameraResolution;
    public static int maxRecentStickers;
    public static int stickerSizeStack = 0;
    public static int dcStyleType;
    public static int idType;
    public static long lastUpdateCheck = 0;
    public static int downloadSpeedBoost;
    public static int unlockedSecretIcon;
    public static boolean is24Hours;

    static {
        loadConfig(true);
    }


    public static void loadConfig(boolean firstLoad) {
        synchronized (sync) {
            if (configLoaded) {
                return;
            }
            //VERSION_CHECK
            if (firstLoad) {
                boolean backupFileExist = backupFile().exists();
                DB_VERSION = SharedPreferencesHelper.getInt("DB_VERSION", 0);
                if (DB_VERSION < BuildVars.BUILD_VERSION && backupFileExist) {
                    restoreBackup(backupFile(), true);
                }
                SharedPreferencesHelper.putValue("DB_VERSION", DB_VERSION = BuildVars.BUILD_VERSION);
                SharedPreferencesHelper.registerOnSharedPreferenceChangeListener((sharedPreferences, s) -> executeBackup());
            }
            isChineseUser = ApplicationLoader.applicationContext.getResources().getBoolean(R.bool.isChineseUser);
            hidePhoneNumber = SharedPreferencesHelper.getBoolean("hidePhoneNumber", true);
            hideContactNumber = SharedPreferencesHelper.getBoolean("hideContactNumber", true);
            fullTime = SharedPreferencesHelper.getBoolean("fullTime", false);
            roundedNumbers = SharedPreferencesHelper.getBoolean("roundedNumbers", true);
            confirmCall = SharedPreferencesHelper.getBoolean("confirmCall", true);
            PersianCalendar = SharedPreferencesHelper.getBoolean("PersianCalendar", false);
            mediaFlipByTap = SharedPreferencesHelper.getBoolean("mediaFlipByTap", true);
            jumpChannel = SharedPreferencesHelper.getBoolean("jumpChannel", true);
            hideKeyboard = SharedPreferencesHelper.getBoolean("hideKeyboard", false);
            gifAsVideo = SharedPreferencesHelper.getBoolean("gifAsVideo", false);
            showFolderWhenForward = SharedPreferencesHelper.getBoolean("showFolderWhenForward", true);
            useRearCamera = SharedPreferencesHelper.getBoolean("useRearCamera", false);
            sendConfirm = SharedPreferencesHelper.getBoolean("sendConfirm", false);
            useSystemFont = SharedPreferencesHelper.getBoolean("useSystemFont", false);
            useSystemEmoji = SharedPreferencesHelper.getBoolean("useSystemEmoji", false);
            showGreetings = SharedPreferencesHelper.getBoolean("showGreetings", true);
            showTranslate = SharedPreferencesHelper.getBoolean("showTranslate", false);
            showSaveMessage = SharedPreferencesHelper.getBoolean("showSaveMessage", false);
            showRepeat = SharedPreferencesHelper.getBoolean("showRepeat", false);
            showNoQuoteForward = SharedPreferencesHelper.getBoolean("showNoQuoteForward", false);
            showMessageDetails = SharedPreferencesHelper.getBoolean("showMessageDetails", false);
            betaUpdates = SharedPreferencesHelper.getBoolean("betaUpdates", false);
            notifyUpdates = SharedPreferencesHelper.getBoolean("notifyUpdates", true);
            avatarBackgroundDarken = SharedPreferencesHelper.getBoolean("avatarBackgroundDarken", false);
            avatarBackgroundBlur = SharedPreferencesHelper.getBoolean("avatarBackgroundBlur", false);
            avatarAsDrawerBackground = SharedPreferencesHelper.getBoolean("avatarAsDrawerBackground", false);
            showReportMessage = SharedPreferencesHelper.getBoolean("showReportMessage", true);
            showGradientColor = SharedPreferencesHelper.getBoolean("showGradientColor", false);
            showAvatarImage = SharedPreferencesHelper.getBoolean("showAvatarImage", true);
            advEasterSound = SharedPreferencesHelper.getBoolean("advEasterSound", true);
            pacmanForced = SharedPreferencesHelper.getBoolean("pacmanForced", false);
            smartButtons = SharedPreferencesHelper.getBoolean("smartButtons", false);
            disableAppBarShadow = SharedPreferencesHelper.getBoolean("disableAppBarShadow", false);
            accentAsNotificationColor = SharedPreferencesHelper.getBoolean("accentAsNotificationColor", false);
            showDeleteDownloadedFile = SharedPreferencesHelper.getBoolean("showDeleteDownloadedFile", false);
            lastUpdateCheck = SharedPreferencesHelper.getLong("lastUpdateCheck", 0);
            lastUpdateStatus = SharedPreferencesHelper.getInt("lastUpdateStatus", 0);
            remindedUpdate = SharedPreferencesHelper.getInt("remindedUpdate", 0);
            translationTarget = SharedPreferencesHelper.getString("translationTarget", "app");
            translationKeyboardTarget = SharedPreferencesHelper.getString("translationKeyboardTarget", "app");
            updateData = SharedPreferencesHelper.getString("updateData", "");
            drawerItems = SharedPreferencesHelper.getString("drawerItems", "[]");
            oldDownloadedVersion = SharedPreferencesHelper.getInt("oldDownloadedVersion", 0);
            eventType = SharedPreferencesHelper.getInt("eventType", 0);
            buttonStyleType = SharedPreferencesHelper.getInt("buttonStyleType", 0);
            tabMode = SharedPreferencesHelper.getInt("tabMode", 1);
            translatorStyle = SharedPreferencesHelper.getInt("translatorStyle", BaseTranslator.INLINE_STYLE);
            blurIntensity = SharedPreferencesHelper.getInt("blurIntensity", 75);
            oldBuildVersion = SharedPreferencesHelper.getString("oldBuildVersion", null);
            stickerSizeStack = SharedPreferencesHelper.getInt("stickerSizeStack", 14);
            deepLFormality = SharedPreferencesHelper.getInt("deepLFormality", DeepLTranslator.FORMALITY_DEFAULT);
            translationProvider = SharedPreferencesHelper.getInt("translationProvider", Translator.PROVIDER_GOOGLE);
            showSantaHat = SharedPreferencesHelper.getBoolean("showSantaHat", true);
            showSnowFalling = SharedPreferencesHelper.getBoolean("showSnowFalling", true);
            cameraType = getInt("cameraType", CameraXUtils.getDefault());
            cameraResolution = getInt("cameraResolution", CameraXUtils.getCameraResolution());
            useCameraXOptimizedMode = SharedPreferencesHelper.getBoolean("useCameraXOptimizedMode", SharedConfig.getDevicePerformanceClass() != SharedConfig.PERFORMANCE_CLASS_HIGH);
            disableProximityEvents = SharedPreferencesHelper.getBoolean("disableProximityEvents", false);
            verifyLinkTip = SharedPreferencesHelper.getBoolean("verifyLinkTip", false);
            languagePackVersioning = SharedPreferencesHelper.getString("languagePackVersioning", "{}");
            xiaomiBlockedInstaller = SharedPreferencesHelper.getBoolean("xiaomiBlockedInstaller", false);
            voicesAgc = SharedPreferencesHelper.getBoolean("voicesAgc", false);
            turnSoundOnVDKey = SharedPreferencesHelper.getBoolean("turnSoundOnVDKey", true);
            openArchiveOnPull = SharedPreferencesHelper.getBoolean("openArchiveOnPull", false);
            slidingChatTitle = SharedPreferencesHelper.getBoolean("slidingChatTitle", false);
            confirmStickersGIFs = SharedPreferencesHelper.getBoolean("confirmStickersGIFs", false);
            showIDAndDC = SharedPreferencesHelper.getBoolean("showIDAndDC", false);
            doNotTranslateLanguages = SharedPreferencesHelper.getString("doNotTranslateLanguages", "[\"app\"]");
            dcStyleType = SharedPreferencesHelper.getInt("dcStyleType", 0);
            idType = SharedPreferencesHelper.getInt("idType", 0);
            searchIconInActionBar = SharedPreferencesHelper.getBoolean("searchIconInActionBar", false);
            autoTranslate = SharedPreferencesHelper.getBoolean("autoTranslate", false);
            showCopyPhoto = SharedPreferencesHelper.getBoolean("showCopyPhoto", false);
            showPencilIcon = SharedPreferencesHelper.getBoolean("showPencilIcon", false);
            keepTranslationMarkdown = SharedPreferencesHelper.getBoolean("keepTranslationMarkdown", true);
            hideTimeOnSticker = SharedPreferencesHelper.getBoolean("hideTimeOnSticker", false);
            showStatusInChat = SharedPreferencesHelper.getBoolean("showStatusInChat", false);
            unlockedSecretIcon = SharedPreferencesHelper.getInt("unlockedSecretIcon", 0);
            showPatpat = SharedPreferencesHelper.getBoolean("showPatpat", false);
            unlockedChupa = SharedPreferencesHelper.getBoolean("unlockedChupa", false);
            hideAllTab = SharedPreferencesHelper.getBoolean("hideAllTab", false);
            hideSendAsChannel = SharedPreferencesHelper.getBoolean("hideSendAsChannel", false);
            showNameInActionBar = SharedPreferencesHelper.getBoolean("showNameInActionBar", false);
            stickersSorting = SharedPreferencesHelper.getBoolean("stickersSorting", true);
            is24Hours = SharedPreferencesHelper.getBoolean("is24Hours", true);
            //EXPERIMENTAL OPTIONS
            devOptEnabled = SharedPreferencesHelper.getBoolean("devOptEnabled", false);

            String dS = devOptEnabled ? "" : "_disabled";
            maxRecentStickers = SharedPreferencesHelper.getInt("maxRecentStickers" + dS, 20);
            betterAudioQuality = SharedPreferencesHelper.getBoolean("betterAudioQuality" + dS, false);
            localPremium = SharedPreferencesHelper.getBoolean("localPremium" , false);
            downloadSpeedBoost = SharedPreferencesHelper.getInt("downloadSpeedBoost" + dS, 0);
            uploadSpeedBoost = SharedPreferencesHelper.getBoolean("uploadSpeedBoost" + dS, false);
            configLoaded = true;
            migrate();
        }
    }

    private static void migrate() {
        if (translationProvider == Translator.PROVIDER_NIU) {
            setTranslationProvider(Translator.PROVIDER_GOOGLE);
        }
        AutoTranslateConfig.migrate();
    }

    public static void toggleHidePhone() {
        SharedPreferencesHelper.putValue("hidePhoneNumber", hidePhoneNumber ^= true);
    }

    public static void toggleHideContactNumber() {
        SharedPreferencesHelper.putValue("hideContactNumber", hideContactNumber ^= true);
    }

    public static void toggleFullTime() {
        SharedPreferencesHelper.putValue("fullTime", fullTime ^= true);
    }

    public static void toggleRoundedNumbers() {
        SharedPreferencesHelper.putValue("roundedNumbers", roundedNumbers ^= true);
    }

    public static void toggleConfirmCall() {
        SharedPreferencesHelper.putValue("confirmCall", confirmCall ^= true);
    }

    public static void togglePersianCalendar() {
        SharedPreferencesHelper.putValue("PersianCalendar", PersianCalendar ^= true);
    }

    public static void toggleMediaFlipByTap() {
        SharedPreferencesHelper.putValue("mediaFlipByTap", mediaFlipByTap ^= true);
    }

    public static void toggleJumpChannel() {
        SharedPreferencesHelper.putValue("jumpChannel", jumpChannel ^= true);
    }

    public static void toggleHideKeyboard() {
        SharedPreferencesHelper.putValue("hideKeyboard", hideKeyboard ^= true);
    }

    public static void toggleGifAsVideo() {
        SharedPreferencesHelper.putValue("gifAsVideo", gifAsVideo ^= true);
    }

    public static void toggleShowFolderWhenForward() {
        SharedPreferencesHelper.putValue("showFolderWhenForward", showFolderWhenForward ^= true);
    }

    public static void toggleUseRearCamera() {
        SharedPreferencesHelper.putValue("useRearCamera", useRearCamera ^= true);
    }

    public static void toggleSendConfirm() {
        SharedPreferencesHelper.putValue("sendConfirm", sendConfirm ^= true);
    }

    public static void toggleUseSystemFont() {
        SharedPreferencesHelper.putValue("useSystemFont", useSystemFont ^= true);
    }

    public static void toggleUseSystemEmoji() {
        SharedPreferencesHelper.putValue("useSystemEmoji", useSystemEmoji ^= true);
    }

    public static void toggleShowGreetings() {
        SharedPreferencesHelper.putValue("showGreetings", showGreetings ^= true);
    }

    public static void toggleShowTranslate() {
        SharedPreferencesHelper.putValue("showTranslate", showTranslate ^= true);
    }

    public static void toggleShowSaveMessage() {
        SharedPreferencesHelper.putValue("showSaveMessage", showSaveMessage ^= true);
    }

    public static void toggleShowRepeat() {
        SharedPreferencesHelper.putValue("showRepeat", showRepeat ^= true);
    }

    public static void toggleShowMessageDetails() {
        SharedPreferencesHelper.putValue("showMessageDetails", showMessageDetails ^= true);
    }

    public static void toggleShowNoQuoteForwardRow() {
        SharedPreferencesHelper.putValue("showNoQuoteForward", showNoQuoteForward ^= true);
    }

    public static void toggleBetaUpdates() {
        SharedPreferencesHelper.putValue("betaUpdates", betaUpdates ^= true);
    }

    public static void toggleNotifyUpdates() {
        SharedPreferencesHelper.putValue("notifyUpdates", notifyUpdates ^= true);
    }

    public static void toggleAvatarAsDrawerBackground() {
        SharedPreferencesHelper.putValue("avatarAsDrawerBackground", avatarAsDrawerBackground ^= true);
    }

    public static void toggleAvatarBackgroundBlur() {
        SharedPreferencesHelper.putValue("avatarBackgroundBlur", avatarBackgroundBlur ^= true);
    }

    public static void toggleAvatarBackgroundDarken() {
        SharedPreferencesHelper.putValue("avatarBackgroundDarken", avatarBackgroundDarken ^= true);
    }

    public static void toggleShowReportMessage() {
        SharedPreferencesHelper.putValue("showReportMessage", showReportMessage ^= true);
    }

    public static void toggleBetterAudioQuality() {
        SharedPreferencesHelper.putValue("betterAudioQuality", betterAudioQuality ^= true);
    }

    public static void toggleShowlocalPremium() {
        SharedPreferencesHelper.putValue("localPremium", localPremium ^= true);
    }

    public static void toggleShowGradientColor() {
        SharedPreferencesHelper.putValue("showGradientColor", showGradientColor ^= true);
    }

    public static void toggleShowAvatarImage() {
        SharedPreferencesHelper.putValue("showAvatarImage", showAvatarImage ^= true);
    }

    public static void toggleAdvEasterSound() {
        SharedPreferencesHelper.putValue("advEasterSound", advEasterSound ^= true);
    }

    public static void togglePacmanForced() {
        SharedPreferencesHelper.putValue("pacmanForced", pacmanForced ^= true);
    }

    public static void toggleSmartButtons() {
        SharedPreferencesHelper.putValue("smartButtons", smartButtons ^= true);
    }

    public static void toggleAppBarShadow() {
        SharedPreferencesHelper.putValue("disableAppBarShadow", disableAppBarShadow ^= true);
    }

    public static void toggleAccentColor() {
        SharedPreferencesHelper.putValue("accentAsNotificationColor", accentAsNotificationColor ^= true);
    }

    public static void toggleShowDeleteDownloadedFile() {
        SharedPreferencesHelper.putValue("showDeleteDownloadedFile", showDeleteDownloadedFile ^= true);
    }

    public static void toggleShowCopyPhoto() {
        SharedPreferencesHelper.putValue("showCopyPhoto", showCopyPhoto ^= true);
    }

    public static void toggleShowSantaHat() {
        SharedPreferencesHelper.putValue("showSantaHat", showSantaHat ^= true);
    }

    public static void toggleShowSnowFalling() {
        SharedPreferencesHelper.putValue("showSnowFalling", showSnowFalling ^= true);
    }

    public static void toggleCameraXOptimizedMode() {
        SharedPreferencesHelper.putValue("useCameraXOptimizedMode", useCameraXOptimizedMode ^= true);
    }

    public static void toggleDisableProximityEvents() {
        SharedPreferencesHelper.putValue("disableProximityEvents", disableProximityEvents ^= true);
    }

    public static void toggleVoicesAgc() {
        SharedPreferencesHelper.putValue("voicesAgc", voicesAgc ^= true);
    }

    public static void toggleTurnSoundOnVDKey() {
        SharedPreferencesHelper.putValue("turnSoundOnVDKey", turnSoundOnVDKey ^= true);
    }

    public static void toggleOpenArchiveOnPull() {
        SharedPreferencesHelper.putValue("openArchiveOnPull", openArchiveOnPull ^= true);
    }

    public static void toggleSlidingChatTitle() {
        SharedPreferencesHelper.putValue("slidingChatTitle", slidingChatTitle ^= true);
    }

    public static void toggleConfirmStickersGIFs() {
        SharedPreferencesHelper.putValue("confirmStickersGIFs", confirmStickersGIFs ^= true);
    }

    public static void toggleShowIDAndDC() {
        SharedPreferencesHelper.putValue("showIDAndDC", showIDAndDC ^= true);
    }

    public static void toggleSearchIconInActionBar() {
        SharedPreferencesHelper.putValue("searchIconInActionBar", searchIconInActionBar ^= true);
    }

    public static void toggleShowPencilIcon() {
        SharedPreferencesHelper.putValue("showPencilIcon", showPencilIcon ^= true);
    }

    public static void toggleKeepTranslationMarkdown() {
        SharedPreferencesHelper.putValue("keepTranslationMarkdown", keepTranslationMarkdown ^= true);
    }

    public static void toggleUploadSpeedBoost() {
        SharedPreferencesHelper.putValue("uploadSpeedBoost", uploadSpeedBoost ^= true);
    }

    public static void toggleHideTimeOnSticker() {
        SharedPreferencesHelper.putValue("hideTimeOnSticker", hideTimeOnSticker ^= true);
    }

    public static void toggleShowStatusInChat() {
        SharedPreferencesHelper.putValue("showStatusInChat", showStatusInChat ^= true);
    }

    public static void toggleShowPatpat() {
        SharedPreferencesHelper.putValue("showPatpat", showPatpat ^= true);
    }

    public static void toggleHideAllTab() {
        SharedPreferencesHelper.putValue("hideAllTab", hideAllTab ^= true);
    }

    public static void toggleHideSendAsChannel() {
        SharedPreferencesHelper.putValue("hideSendAsChannel", hideSendAsChannel ^= true);
    }

    public static void toggleShowNameInActionBar() {
        SharedPreferencesHelper.putValue("showNameInActionBar", showNameInActionBar ^= true);
    }

    public static void toggleStickersSorting() {
        SharedPreferencesHelper.putValue("stickersSorting", stickersSorting ^= true);
    }

    public static void toggleis24Hours() {
        SharedPreferencesHelper.putValue("is24Hours", is24Hours ^= true);
    }

    public static void unlockChupa() {
        SharedPreferencesHelper.putValue("unlockedChupa", unlockedChupa = true);
    }

    public static void setUnlockedSecretIcon(int value) {
        SharedPreferencesHelper.putValue("unlockedSecretIcon", unlockedSecretIcon = value);
    }

    public static void setXiaomiBlockedInstaller() {
        SharedPreferencesHelper.putValue("xiaomiBlockedInstaller", xiaomiBlockedInstaller = true);
    }

    public static void setVerifyLinkTip(boolean shown) {
        SharedPreferencesHelper.putValue("verifyLinkTip", verifyLinkTip = shown);
    }

    public static void setAutoTranslate(boolean enabled) {
        SharedPreferencesHelper.putValue("autoTranslate", autoTranslate = enabled);
    }

    public static void setTranslationProvider(int provider) {
        SharedPreferencesHelper.putValue("translationProvider", translationProvider = provider);
    }

    public static void setTranslationTarget(String target) {
        SharedPreferencesHelper.putValue("translationTarget", translationTarget = target);
    }

    public static void setTranslationKeyboardTarget(String target) {
        SharedPreferencesHelper.putValue("translationKeyboardTarget", translationKeyboardTarget = target);
    }

    public static void setDeepLFormality(int formality) {
        SharedPreferencesHelper.putValue("deepLFormality", deepLFormality = formality);
    }

    public static void setTranslatorStyle(int style) {
        SharedPreferencesHelper.putValue("translatorStyle", translatorStyle = style);
    }

    public static void setTabMode(int mode) {
        SharedPreferencesHelper.putValue("tabMode", tabMode = mode);
    }

    public static void setStickerSize(int size) {
        SharedPreferencesHelper.putValue("stickerSizeStack", stickerSizeStack = size);
    }

    public static void setDcStyleType(int type) {
        SharedPreferencesHelper.putValue("dcStyleType", dcStyleType = type);
    }

    public static void setIdType(int type) {
        SharedPreferencesHelper.putValue("idType", idType = type);
    }

    public static String currentNotificationVersion() {
        return BuildVars.BUILD_VERSION_STRING + "_" + BuildVars.BUILD_VERSION;
    }

    public static void updateCurrentVersion() {
        SharedPreferencesHelper.putValue("oldBuildVersion", oldBuildVersion = currentNotificationVersion());
    }

    public static void saveLastUpdateCheck() {
        saveLastUpdateCheck(false);
    }

    public static void saveLastUpdateCheck(boolean isReset) {
        SharedPreferencesHelper.putValue("lastUpdateCheck", lastUpdateCheck = isReset ? 0 : new Date().getTime());
    }

    public static void saveUpdateStatus(int status) {
        SharedPreferencesHelper.putValue("lastUpdateStatus", lastUpdateStatus = status);
    }

    public static void saveBlurIntensity(int intensity) {
        SharedPreferencesHelper.putValue("blurIntensity", blurIntensity = intensity);
    }

    public static void remindUpdate(int version) {
        SharedPreferencesHelper.putValue("remindedUpdate", remindedUpdate = version);
        saveUpdateStatus(0);
    }

    public static void saveOldVersion(int version) {
        SharedPreferencesHelper.putValue("oldDownloadedVersion", oldDownloadedVersion = version);
    }

    public static void saveButtonStyle(int type) {
        SharedPreferencesHelper.putValue("buttonStyleType", buttonStyleType = type);
    }

    public static void saveEventType(int type) {
        SharedPreferencesHelper.putValue("eventType", eventType = type);
    }

    public static void saveCameraType(int type) {
        SharedPreferencesHelper.putValue("cameraType", cameraType = type);
    }

    public static void saveCameraResolution(int resolution) {
        SharedPreferencesHelper.putValue("cameraResolution", cameraResolution = resolution);
    }

    public static void setMaxRecentStickers(int size) {
        SharedPreferencesHelper.putValue("maxRecentStickers", maxRecentStickers = size);
    }

    public static void setUpdateData(String data) {
        SharedPreferencesHelper.putValue("updateData", updateData = data);
    }

    public static void setDrawerItems(String data) {
        SharedPreferencesHelper.putValue("drawerItems", drawerItems = data);
    }

    public static void setLanguagePackVersioning(String data) {
        SharedPreferencesHelper.putValue("languagePackVersioning", languagePackVersioning = data);
    }

    public static void setDoNotTranslateLanguages(String data) {
        SharedPreferencesHelper.putValue("doNotTranslateLanguages", doNotTranslateLanguages = data);
    }

    public static void setDownloadSpeedBoost(int boost) {
        SharedPreferencesHelper.putValue("downloadSpeedBoost", downloadSpeedBoost = boost);
    }

    public static int getNotificationColor() {
        if (accentAsNotificationColor) {
            int color = 0;
            if (Theme.getActiveTheme().hasAccentColors()) {
                color = Theme.getActiveTheme().getAccentColor(Theme.getActiveTheme().currentAccentId);
            }
            if (color == 0) {
                color = Theme.getColor(Theme.key_actionBarDefault) | 0xff000000;
            }
            float brightness = AndroidUtilities.computePerceivedBrightness(color);
            if (brightness >= 0.721f || brightness <= 0.279f) {
                color = Theme.getColor(Theme.key_windowBackgroundWhiteBlueHeader) | 0xff000000;
            }
            return color;
        } else {
            return 0xff11acfa;
        }
    }

    public static void toggleDevOpt() {
        SharedPreferencesHelper.putValue("devOptEnabled", devOptEnabled ^= true);
        loadConfig(configLoaded = false);
    }

    public static boolean isDevOptEnabled() {
        return devOptEnabled || betterAudioQuality || localPremium || MonetIconsHelper.isSelectedMonet() || maxRecentStickers != 20;
    }

    public static boolean canShowFireworks() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int monthOfYear = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        return monthOfYear == 1 && dayOfMonth == 1;
    }
    public static long[] officialChats = {
            1129702535,1671192392,
    };

    public static long[] developers = {
            153080470,581240475,885348075,937295760,1593227959
    };

}