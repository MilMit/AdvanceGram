package milmit.advancegram.messenger.settings;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Cells.TextCheckCell;

import milmit.advancegram.messenger.AdvanceGramConfig;
import milmit.advancegram.messenger.StoreUtils;
import milmit.advancegram.messenger.components.UpdateCell;
import milmit.advancegram.messenger.components.UpdateCheckCell;
import milmit.advancegram.messenger.updates.ApkDownloader;
import milmit.advancegram.messenger.updates.AppDownloader;
import milmit.advancegram.messenger.updates.PlayStoreAPI;
import milmit.advancegram.messenger.updates.UpdateManager;

public class AdvgramUpdateSettings extends BaseSettingsActivity {

    private int updateSectionAvailableRow;
    private int updateSectionDividerRow;
    private int updateSectionHeader;
    private int updateCheckRow;
    private int betaUpdatesRow;
    private int notifyWhenAvailableRow;
    private int apkChannelRow;
    private boolean checkingUpdates;
    private TextCheckCell changeBetaMode;

    private UpdateManager.UpdateAvailable updateAvailable;
    private UpdateCheckCell updateCheckCell;
    private UpdateCell updateCell;

    @Override
    public boolean onFragmentCreate() {
        String data = AdvanceGramConfig.updateData;
        JSONObject jsonObject ;
        try {
            jsonObject = new JSONObject(data);
            updateAvailable = UpdateManager.loadUpdate(jsonObject);
            if (updateAvailable.isReminded()) {
                updateAvailable = null;
            } else if (updateAvailable.version <= BuildVars.BUILD_VERSION) {
                updateAvailable = null;
                AdvanceGramConfig.saveUpdateStatus(0);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        AppDownloader.setDownloadListener(new AppDownloader.UpdateListener() {
            @Override
            public void onPreStart() {
            }

            @Override
            public void onProgressChange(int percentage, long downBytes, long totBytes) {
                if (updateCell != null) {
                    updateCell.setPercentage(percentage, downBytes, totBytes);
                }
            }

            @Override
            public void onFinished() {
                if (!StoreUtils.isFromPlayStore()) changeBetaMode.setEnabled(!AppDownloader.updateDownloaded(), null);
                updateCheckCell.setCanCheckForUpdate(!AppDownloader.updateDownloaded() && !PlayStoreAPI.isRunningDownload());
                if (PlayStoreAPI.updateDownloaded()) {
                    updateCheckCell.setDownloaded();
                }
                if (updateCell != null) {
                    if (AppDownloader.updateDownloaded()) {
                        updateCell.setInstallMode();
                    } else {
                        updateCell.setConfirmMode();
                    }
                }
            }
        });
        return super.onFragmentCreate();
    }

    @Override
    protected String getActionBarTitle() {
        return LocaleController.getString("AdvUpdates", R.string.AdvUpdates);
    }

    @Override
    protected void onItemClick(View view, int position, float x, float y) {
        if (position == betaUpdatesRow) {
            if (!ApkDownloader.updateDownloaded() && !checkingUpdates) {
                AdvanceGramConfig.toggleBetaUpdates();
                ApkDownloader.cancel();
                ApkDownloader.deleteUpdate();
                listAdapter.notifyItemChanged(apkChannelRow, PARTIAL);
                if (updateAvailable != null) {
                    AdvanceGramConfig.remindUpdate(updateAvailable.version);
                    NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.appUpdateAvailable);
                    updateAvailable = null;
                    listAdapter.notifyItemRangeRemoved(updateSectionAvailableRow, 2);
                    listAdapter.notifyItemRangeChanged(updateSectionAvailableRow, 1);
                    updateRowsId();
                }
                checkUpdates();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(AdvanceGramConfig.betaUpdates);
                }
            }
        } else if (position == notifyWhenAvailableRow) {
            AdvanceGramConfig.toggleNotifyUpdates();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(AdvanceGramConfig.notifyUpdates);
            }
        } else if (position == apkChannelRow) {
            MessagesController.getInstance(currentAccount).openByUserName(UpdateManager.getApkChannel(), this, 1);
        }
    }

    @Override
    protected void updateRowsId() {
        super.updateRowsId();
        updateSectionAvailableRow = -1;
        updateSectionDividerRow = -1;
        betaUpdatesRow = -1;

        if (updateAvailable != null && !StoreUtils.isDownloadedFromAnyStore()) {
            updateSectionAvailableRow = rowCount++;
            updateSectionDividerRow = rowCount++;
        }

        updateSectionHeader = rowCount++;
        updateCheckRow = rowCount++;
        if (!StoreUtils.isDownloadedFromAnyStore()) {
            betaUpdatesRow = rowCount++;
        }
        notifyWhenAvailableRow = rowCount++;
        apkChannelRow = rowCount++;
    }

    @Override
    protected BaseListAdapter createAdapter() {
        return new ListAdapter();
    }

    private class ListAdapter extends BaseListAdapter {

        @Override
        protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, boolean partial) {
            switch (ViewType.fromInt(holder.getItemViewType())) {
                case SHADOW:
                    holder.itemView.setBackground(Theme.getThemedDrawable(context, R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
                    break;
                case UPDATE:
                    UpdateCell updateCell = (UpdateCell) holder.itemView;
                    AdvgramUpdateSettings.this.updateCell = updateCell;
                    updateCell.setUpdate(
                            updateAvailable.title,
                            updateAvailable.desc,
                            updateAvailable.note,
                            updateAvailable.banner
                    );
                    if (AppDownloader.isRunningDownload()) {
                        updateCell.setDownloadMode();
                    } else {
                        if (AppDownloader.updateDownloaded()) {
                            updateCell.setInstallMode();
                        } else {
                            updateCell.setConfirmMode();
                        }
                    }
                    updateCell.setPercentage(AppDownloader.getDownloadProgress(), AppDownloader.downloadedBytes(), AppDownloader.totalBytes());
                    break;
                case HEADER:
                    HeaderCell headerCell = (HeaderCell) holder.itemView;
                    if (position == updateSectionHeader) {
                        headerCell.setText(LocaleController.getString("InAppUpdates", R.string.InAppUpdates));
                    }
                    break;
                case UPDATE_CHECK:
                    UpdateCheckCell updateCheckCell = (UpdateCheckCell) holder.itemView;
                    updateCheckCell.loadLastStatus();
                    updateCheckCell.setCanCheckForUpdate(!AppDownloader.updateDownloaded() && !PlayStoreAPI.isRunningDownload());
                    if (PlayStoreAPI.updateDownloaded()) {
                        updateCheckCell.setDownloaded();
                    }
                    break;
                case SWITCH:
                    TextCheckCell textCheckCell = (TextCheckCell) holder.itemView;
                    textCheckCell.setEnabled(!AppDownloader.updateDownloaded() || position != betaUpdatesRow, null);
                    if (position == betaUpdatesRow) {
                        changeBetaMode = textCheckCell;
                        changeBetaMode.setTextAndValueAndCheck(LocaleController.getString("InstallBetas", R.string.InstallBetas), LocaleController.getString("InstallBetasDesc", R.string.InstallBetasDesc), AdvanceGramConfig.betaUpdates, true, true);
                    } else if (position == notifyWhenAvailableRow) {
                        textCheckCell.setTextAndValueAndCheck(LocaleController.getString("AutoUpdate", R.string.AutoUpdate), LocaleController.getString("AutoUpdatePrompt", R.string.AutoUpdatePrompt), AdvanceGramConfig.notifyUpdates, true, true);
                    }
                    break;
                case TEXT_CELL:
                    TextCell textCell = (TextCell) holder.itemView;
                    if (position == apkChannelRow) {
                        textCell.setTextAndValue(LocaleController.getString("APKsChannel", R.string.APKsChannel), "@" + UpdateManager.getApkChannel(), partial, false);
                    }
                    break;
            }
        }

        @Override
        protected boolean isEnabled(ViewType viewType, int position) {
            return viewType == ViewType.SWITCH || viewType == ViewType.TEXT_CELL;
        }

        @Override
        protected View onCreateViewHolder(ViewType viewType) {
            View view = null;
            switch (viewType) {
                case UPDATE:
                    view = new UpdateCell(context) {
                        @Override
                        protected void onInstallUpdate() {
                            super.onInstallUpdate();
                            ApkDownloader.installUpdate(getParentActivity());
                        }

                        @Override
                        protected void onConfirmUpdate() {
                            super.onConfirmUpdate();
                            if (!ApkDownloader.isRunningDownload()) {
                                ApkDownloader.downloadAPK(context, updateAvailable.link_file, updateAvailable.version);
                                updateCell.setDownloadMode();
                            }
                        }

                        @Override
                        protected void onRemindUpdate() {
                            super.onRemindUpdate();
                            updateCheckCell.setCheckTime();
                            if (updateAvailable != null) {
                                ApkDownloader.deleteUpdate();
                                AdvanceGramConfig.remindUpdate(updateAvailable.version);
                                NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.appUpdateAvailable);
                                updateCheckCell.setCheckTime();
                                updateAvailable = null;
                                listAdapter.notifyItemRangeRemoved(updateSectionAvailableRow, 2);
                                listAdapter.notifyItemRangeChanged(updateSectionAvailableRow, 1);
                                updateRowsId();
                            }
                        }
                    };
                    break;
                case UPDATE_CHECK:
                    view = new UpdateCheckCell(context, true) {
                        @Override
                        protected void onCheckUpdate() {
                            super.onCheckUpdate();
                            if (PlayStoreAPI.updateDownloaded()) {
                                PlayStoreAPI.installUpdate();
                            } else if (StoreUtils.isFromPlayStore() && !PlayStoreAPI.updateDownloaded() && AdvanceGramConfig.lastUpdateStatus == 1 && updateAvailable != null) {
                                PlayStoreAPI.openUpdatePopup(getParentActivity());
                            } else if (!AppDownloader.updateDownloaded()) {
                                checkUpdates();
                            }
                        }
                    };
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    updateCheckCell = (UpdateCheckCell) view;
                    break;
            }
            return view;
        }

        @Override
        public ViewType getViewType(int position) {
            if (position == updateSectionDividerRow) {
                return ViewType.SHADOW;
            } else if (position == updateSectionAvailableRow) {
                return ViewType.UPDATE;
            } else if (position == updateSectionHeader) {
                return ViewType.HEADER;
            } else if (position == updateCheckRow) {
                return ViewType.UPDATE_CHECK;
            } else if (position == betaUpdatesRow || position == notifyWhenAvailableRow) {
                return ViewType.SWITCH;
            } else if (position == apkChannelRow) {
                return ViewType.TEXT_CELL;
            }
            throw new IllegalArgumentException("Invalid position");
        }
    }

    private void checkUpdates() {
        updateCheckCell.setCheckingStatus();
        UpdateManager.checkUpdates(new UpdateManager.UpdateCallback() {
            @Override
            public void onSuccess(Object updateResult) {
                checkingUpdates = false;
                AdvanceGramConfig.saveLastUpdateCheck();
                if (updateResult instanceof UpdateManager.UpdateAvailable) {
                    if (updateAvailable == null) {
                        AdvanceGramConfig.saveUpdateStatus(1);
                        AdvanceGramConfig.remindUpdate(-1);
                        AdvanceGramConfig.setUpdateData(updateResult.toString());
                        updateAvailable = (UpdateManager.UpdateAvailable) updateResult;
                        if (StoreUtils.isFromPlayStore()) {
                            PlayStoreAPI.openUpdatePopup(getParentActivity());
                        } else {
                            listAdapter.notifyItemRangeInserted(updateSectionAvailableRow, 2);
                            listAdapter.notifyItemRangeChanged(updateSectionAvailableRow, 1);
                            updateRowsId();
                        }
                        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.appUpdateAvailable);
                    }
                    updateCheckCell.setUpdateAvailableStatus();
                } else {
                    AdvanceGramConfig.saveUpdateStatus(0);
                    updateCheckCell.setCheckTime();
                    if (updateAvailable != null) {
                        AdvanceGramConfig.setUpdateData("");
                        updateAvailable = null;
                        if (!StoreUtils.isFromPlayStore()) {
                            listAdapter.notifyItemRangeRemoved(updateSectionAvailableRow, 2);
                            listAdapter.notifyItemRangeChanged(updateSectionAvailableRow, 1);
                            updateRowsId();
                        }
                    }
                }
            }

            @Override
            public void onError(Exception e) {
                FileLog.e(e);
                AdvanceGramConfig.saveUpdateStatus(2);
                updateCheckCell.setFailedStatus();
            }
        },currentAccount);
    }
}
