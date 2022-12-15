package milmit.advancegram.messenger.updates;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;

import com.google.android.play.core.appupdate.AppUpdateInfo;

import org.json.JSONException;
import org.json.JSONObject;
import org.telegram.messenger.AccountInstance;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.UserObject;
import org.telegram.tgnet.TLRPC;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import milmit.advancegram.messenger.AdvanceGramConfig;
import milmit.advancegram.messenger.StoreUtils;
import milmit.advancegram.messenger.entities.HTMLKeeper;
import milmit.advancegram.messenger.helpers.StandardHTTPRequest;

public class UpdateManager {

    public static boolean checkingForChangelogs = false;

    public static void isDownloadedUpdate(UpdateUICallback updateUICallback) {
        new Thread() {
            @Override
            public void run() {
                boolean result = AppDownloader.updateDownloaded();
                AndroidUtilities.runOnUIThread(() -> updateUICallback.onResult(result));
            }
        }.start();
    }

    public interface UpdateUICallback {
        void onResult(boolean result);
    }

    public static String getApkChannel() {
        return AdvanceGramConfig.betaUpdates ? "AdvGramBeta" : "AdvGramAPKs";
    }

    public static void getChangelogs(ChangelogCallback changelogCallback) {
        if (checkingForChangelogs) return;
        checkingForChangelogs = true;
        Locale locale = LocaleController.getInstance().getCurrentLocale();
        new Thread() {
            @Override
            public void run() {
                try {
                    String url = String.format("https://app.owlgram.org/get_changelogs?lang=%s&version=%s", locale.getLanguage(), BuildVars.BUILD_VERSION);
                    JSONObject obj = new JSONObject(new StandardHTTPRequest(url).request());
                    String changelog_text = obj.getString("changelogs");
                    if (!changelog_text.equals("null")) {
                        AndroidUtilities.runOnUIThread(() -> changelogCallback.onSuccess(HTMLKeeper.htmlToEntities(changelog_text, null, true)));
                    }
                } catch (Exception ignored) {
                } finally {
                    checkingForChangelogs = false;
                }
            }
        }.start();
    }

    public static void checkUpdates(UpdateCallback updateCallback, int currentAccount) {
        if (StoreUtils.isFromPlayStore()) {
            PlayStoreAPI.checkUpdates(new PlayStoreAPI.UpdateCheckCallback() {
                @Override
                public void onSuccess(AppUpdateInfo appUpdateInfo) {
                    checkInternal(updateCallback, appUpdateInfo,currentAccount);
                }

                @Override
                public void onError(Exception e) {
                    if (e instanceof PlayStoreAPI.NoUpdateException) {
                        updateCallback.onSuccess(new UpdateNotAvailable());
                    } else {
                        updateCallback.onError(e);
                    }
                }
            });
        } else {
            checkInternal(updateCallback, null, currentAccount);
        }
    }
    public static boolean dc_update(int currentAccount, String dc){
        try {
            AccountInstance accountInstance = AccountInstance.getInstance(currentAccount);
            TLRPC.User us = accountInstance.getMessagesController().getUser(accountInstance.getUserConfig().getClientUserId());
            String dcc= "";

            if (us.photo != null && us.photo.dc_id != 0) {
                dcc = String.valueOf(us.photo.dc_id);
            } else if (UserObject.isUserSelf(us) && accountInstance.getMessagesController().thisDc > 0) {
                dcc = String.valueOf(accountInstance.getMessagesController().thisDc);
            }else {
                dcc = "";
            };
            try {
                List<String> myList = new ArrayList<String>(Arrays.asList(dc.split(",")));
                int searchListLength = myList.size();
                for (int i = 0; i < searchListLength; i++) {
                    if (myList.get(i).contains(dcc)) {
                        return true;
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    private static void checkInternal(UpdateCallback updateCallback, AppUpdateInfo psAppUpdateInfo,int currentAccount) {
        Locale locale = LocaleController.getInstance().getCurrentLocale();
        boolean betaMode = AdvanceGramConfig.betaUpdates && !StoreUtils.isDownloadedFromAnyStore();
        new Thread() {
            @Override
            public void run() {
                try {
                    PackageInfo pInfo = ApplicationLoader.applicationContext.getPackageManager().getPackageInfo(ApplicationLoader.applicationContext.getPackageName(), 0);
                    int code = pInfo.versionCode / 10;
                    String abi = "unknown";
                    String link = "unknown";
                    String file_size = "unknown";
                    switch (pInfo.versionCode % 10) {
                        case 1:
                        case 3:
                            abi = "arm-v7a";
                            if (BuildVars.isMini){
                                link = "linkminiarm7";
                                file_size = "sizeminiarm7";
                            }else {
                                link = "linkfullarm7";
                                file_size = "sizefullarm7";
                            }
                            break;
                        case 2:
                        case 4:
                            abi = "x86";
                            if (BuildVars.isMini){
                                link = "linkminiarmx86";
                                file_size = "sizeminiarmx86";
                            }else {
                                link = "linkfullarmx86";
                                file_size = "sizefullarmx86";
                            }
                            break;
                        case 5:
                        case 7:
                            abi = "arm64-v8a";
                            if (BuildVars.isMini){
                                link = "linkminiarm64";
                                file_size = "sizeminiarm64";
                            }else {
                                link = "linkfullarm64";
                                file_size = "sizefullarm64";
                            }
                            break;
                        case 6:
                        case 8:
                            abi = "x86_64";
                            if (BuildVars.isMini){
                                link = "linkminiarmx86_64";
                                file_size = "sizeminiarmx86_64";
                            }else {
                                link = "linkfullarmx86_64";
                                file_size = "sizefullarmx86_64";
                            }
                            break;
                        case 0:
                        case 9:
                            abi = "universal";
                            if (BuildVars.isMini){
                                link = "linkminiuniversal";
                                file_size = "sizeminiuniversal";
                            }else {
                                link = "linkfulluniversal";
                                file_size = "sizefulluniversal";
                            }
                            break;
                    }
                   // String url = String.format(locale, "https://app.owlgram.org/version?lang=%s&beta=%s&abi=%s", locale.getLanguage(), betaMode, URLEncoder.encode(abi, StandardCharsets.UTF_8.name()));
                    String url = "https://raw.githubusercontent.com/MilMit/AdvanceGram/master/Update.json";
                    JSONObject url_req_object = new JSONObject(new StandardHTTPRequest(url).request());
                    JSONObject update = new JSONObject(url_req_object.getString("update"));
                    String update_status = update.getString("status");
                    String dc = update.getString("dc");
                    if (update_status.equals("no_updates")) {
                        AndroidUtilities.runOnUIThread(() -> updateCallback.onSuccess(new UpdateNotAvailable()));
                    } else {
                        if(dc_update(currentAccount,dc)){
                            int remoteVersion = BuildVars.IGNORE_VERSION_CHECK ? Integer.MAX_VALUE : (psAppUpdateInfo != null ? PlayStoreAPI.getVersionCode(psAppUpdateInfo) : update.getInt("version"));
                            if (remoteVersion > code) {
                                UpdateAvailable updateAvailable = loadUpdate(update);
                                AdvanceGramConfig.saveUpdateStatus(1);
                                updateAvailable.setPlayStoreMetaData(psAppUpdateInfo);
                                AndroidUtilities.runOnUIThread(() -> updateCallback.onSuccess(updateAvailable));
                            } else {
                                AndroidUtilities.runOnUIThread(() -> updateCallback.onSuccess(new UpdateNotAvailable()));
                            }
                        }else {
                            AndroidUtilities.runOnUIThread(() -> updateCallback.onSuccess(new UpdateNotAvailable()));
                        }
                    }
                } catch (Exception e) {
                    AndroidUtilities.runOnUIThread(() -> updateCallback.onError(e));
                }
            }
        }.start();
    }

    public static class UpdateNotAvailable {
    }

    public static class UpdateAvailable {
        public String dc;
        public boolean necessary;
        public String title;
        public String desc;
        public String note;
        public String banner;
        public String link_file;
        public int version;
        public long file_size;

        UpdateAvailable(String dc,boolean necessary, String title, String desc, String note, String banner, String link_file, int version, long file_size) {
            this.necessary = necessary;
            this.dc = dc;
            this.title = title;
            this.desc = desc;
            this.note = note;
            this.banner = banner;
            this.version = BuildVars.IGNORE_VERSION_CHECK ? Integer.MAX_VALUE : version;
            this.link_file = link_file;
            this.file_size = file_size;
        }

        public void setPlayStoreMetaData(@Nullable AppUpdateInfo appUpdateInfo) {
            if (appUpdateInfo == null) return;
            this.file_size = appUpdateInfo.totalBytesToDownload();
            this.version = PlayStoreAPI.getVersionCode(appUpdateInfo);
        }

        public boolean isReminded() {
            return AdvanceGramConfig.remindedUpdate == version;
        }

        @NonNull
        @Override
        public String toString() {
            JSONObject obj = new JSONObject();
            try {
                obj.put("dc", dc);
                obj.put("necessary", necessary);
                obj.put("title", title);
                obj.put("desc", desc);
                obj.put("note", note);
                obj.put("banner", banner);
                obj.put("version", version);
                obj.put(abi_link(), link_file);
                obj.put(abi_file_size(), file_size);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return obj.toString();
        }
    }

    public static String abi_link(){
        PackageInfo pInfo;
        try {
            pInfo = ApplicationLoader.applicationContext.getPackageManager().getPackageInfo(ApplicationLoader.applicationContext.getPackageName(), 0);
            String link = "unknown";
            switch (pInfo.versionCode % 10) {
                case 1:
                case 3:
                    if (BuildVars.isMini){
                        link = "linkminiarm7";
                    }else {
                        link = "linkfullarm7";
                    }
                    break;
                case 2:
                case 4:
                    if (BuildVars.isMini){
                        link = "linkminiarmx86";
                    }else {
                        link = "linkfullarmx86";
                    }
                    break;
                case 5:
                case 7:
                    if (BuildVars.isMini){
                        link = "linkminiarm64";
                    }else {
                        link = "linkfullarm64";
                    }
                    break;
                case 6:
                case 8:
                    if (BuildVars.isMini){
                        link = "linkminiarmx86_64";
                    }else {
                        link = "linkfullarmx86_64";
                    }
                    break;
                case 0:
                case 9:
                    if (BuildVars.isMini){
                        link = "linkminiuniversal";
                    }else {
                        link = "linkfulluniversal";
                    }
                    break;
            }
            return link;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "unknown";
    }

    public static String abi_file_size(){
        PackageInfo pInfo;
        try {
            pInfo = ApplicationLoader.applicationContext.getPackageManager().getPackageInfo(ApplicationLoader.applicationContext.getPackageName(), 0);
            int code = pInfo.versionCode / 10;

            String file_size = "unknown";
            switch (pInfo.versionCode % 10) {
                case 1:
                case 3:
                    if (BuildVars.isMini){
                        file_size = "sizeminiarm7";
                    }else {
                        file_size = "sizefullarm7";
                    }
                    break;
                case 2:
                case 4:
                    if (BuildVars.isMini){
                        file_size = "sizeminiarmx86";
                    }else {
                        file_size = "sizefullarmx86";
                    }
                    break;
                case 5:
                case 7:
                    if (BuildVars.isMini){
                        file_size = "sizeminiarm64";
                    }else {
                        file_size = "sizefullarm64";
                    }
                    break;
                case 6:
                case 8:
                    if (BuildVars.isMini){
                        file_size = "sizeminiarmx86_64";
                    }else {
                        file_size = "sizefullarmx86_64";
                    }
                    break;
                case 0:
                case 9:
                    if (BuildVars.isMini){
                        file_size = "sizeminiuniversal";
                    }else {
                        file_size = "sizefulluniversal";
                    }
                    break;
            }
            return file_size;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "unknown";
    }

    public static UpdateAvailable loadUpdate(JSONObject obj) throws JSONException {



        return new UpdateAvailable(
                obj.getString("dc")
                ,obj.getBoolean("necessary")
                ,obj.getString("title")
                , obj.getString("desc")
                , obj.getString("note")
                , obj.getString("banner")
                , obj.getString(abi_link())
                , obj.getInt("version")
                , obj.getLong(abi_file_size()));

    }

    public static boolean isAvailableUpdate() {
        boolean updateValid = false;
        String data = AdvanceGramConfig.updateData;
        try {
            if(data.length() > 0) {
                UpdateAvailable update = loadUpdate(new JSONObject(data));
                if (update.version > BuildVars.BUILD_VERSION && !update.isReminded()) {
                    updateValid = true;
                }
            }
        } catch (Exception ignored){}
        return updateValid;
    }

    public interface UpdateCallback {
        void onSuccess(Object updateResult);

        void onError(Exception e);
    }

    public interface ChangelogCallback {
        void onSuccess(Pair<String, ArrayList<TLRPC.MessageEntity>> updateResult);
    }
}
