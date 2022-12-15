package milmit.advancegram.messenger.settings;

import android.os.Build;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.SlideChooseView;

import java.util.ArrayList;

import milmit.advancegram.messenger.AdvanceGramConfig;
import milmit.advancegram.messenger.components.LabsHeader;
import milmit.advancegram.messenger.helpers.MonetIconsHelper;
import milmit.advancegram.messenger.helpers.PopupHelper;

public class AdvgramExperimentalSettings extends BaseSettingsActivity {

    private int checkBoxExperimentalRow;
    private int headerImageRow;
    private int bottomHeaderRow;
    private int headerExperimental;
    private int betterAudioCallRow;
    private int maxRecentStickersRow;
    private int experimentalMessageAlert;
    private int monetIconRow;
    private int downloadDividersRow;
    private int headerDownloadSpeed;
    private int downloadSpeedBoostRow;
    private int uploadSpeedBoostRow;

    @Override
    protected String getActionBarTitle() {
        return LocaleController.getString("Experimental", R.string.Experimental);
    }

    @Override
    protected void onItemClick(View view, int position, float x, float y) {
        if (position == betterAudioCallRow) {
            AdvanceGramConfig.toggleBetterAudioQuality();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(AdvanceGramConfig.betterAudioQuality);
            }
        } else if (position == maxRecentStickersRow) {
            int[] counts = {20, 30, 40, 50, 80, 100, 120, 150, 180, 200};
            ArrayList<String> types = new ArrayList<>();
            for (int count : counts) {
                if (count <= getMessagesController().maxRecentStickersCount) {
                    types.add(String.valueOf(count));
                }
            }
            PopupHelper.show(types, LocaleController.getString("MaxRecentStickers", R.string.MaxRecentStickers), types.indexOf(String.valueOf(AdvanceGramConfig.maxRecentStickers)), context, i -> {
                AdvanceGramConfig.setMaxRecentStickers(Integer.parseInt(types.get(i)));
                listAdapter.notifyItemChanged(maxRecentStickersRow, PARTIAL);
            });
        } else if (position == checkBoxExperimentalRow) {
            if (view instanceof TextCheckCell) {
                TextCheckCell textCheckCell = (TextCheckCell) view;
                if (MonetIconsHelper.isSelectedMonet()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
                    builder.setMessage(LocaleController.getString("DisableExperimentalAlert", R.string.DisableExperimentalAlert));
                    builder.setPositiveButton(LocaleController.getString("AutoDeleteConfirm", R.string.AutoDeleteConfirm), (dialogInterface, i) -> {
                        MonetIconsHelper.switchToMonet();
                        toggleExperimentalMode(textCheckCell);
                        AlertDialog progressDialog = new AlertDialog(getParentActivity(), 3);
                        progressDialog.show();
                        AndroidUtilities.runOnUIThread(progressDialog::dismiss, 2000);
                    });
                    builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                    builder.show();
                } else {
                    toggleExperimentalMode(textCheckCell);
                }
            }
        } else if (position == monetIconRow) {
            MonetIconsHelper.switchToMonet();
            AlertDialog progressDialog = new AlertDialog(getParentActivity(), 3);
            progressDialog.show();
            AndroidUtilities.runOnUIThread(progressDialog::dismiss, 2000);
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(MonetIconsHelper.isSelectedMonet());
            }
        } else if (position == uploadSpeedBoostRow) {
            AdvanceGramConfig.toggleUploadSpeedBoost();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(AdvanceGramConfig.uploadSpeedBoost);
            }
        }
    }

    private void toggleExperimentalMode(TextCheckCell textCheckCell) {
        AdvanceGramConfig.toggleDevOpt();
        boolean isEnabled = AdvanceGramConfig.isDevOptEnabled();
        textCheckCell.setChecked(isEnabled);
        textCheckCell.setText(isEnabled ? LocaleController.getString("OnModeCheckTitle", R.string.OnModeCheckTitle) : LocaleController.getString("OffModeCheckTitle", R.string.OffModeCheckTitle));
        textCheckCell.setBackgroundColorAnimated(isEnabled, Theme.getColor(isEnabled ? Theme.key_windowBackgroundChecked : Theme.key_windowBackgroundUnchecked));
        if (isEnabled) {
            listAdapter.notifyItemRemoved(experimentalMessageAlert);
            updateRowsId();
            listAdapter.notifyItemRangeInserted(headerImageRow, rowCount - headerImageRow);
        } else {
            listAdapter.notifyItemRangeRemoved(headerImageRow, rowCount - headerImageRow);
            updateRowsId();
            listAdapter.notifyItemInserted(experimentalMessageAlert);
        }
    }

    @Override
    protected void updateRowsId() {
        super.updateRowsId();
        headerImageRow = -1;
        bottomHeaderRow = -1;
        headerExperimental = -1;
        betterAudioCallRow = -1;
        maxRecentStickersRow = -1;
        monetIconRow = -1;
        downloadDividersRow = -1;
        headerDownloadSpeed = -1;
        downloadSpeedBoostRow = -1;
        uploadSpeedBoostRow = -1;
        experimentalMessageAlert = -1;

        checkBoxExperimentalRow = rowCount++;
        if (AdvanceGramConfig.isDevOptEnabled()) {
            headerImageRow = rowCount++;
            bottomHeaderRow = rowCount++;
            headerExperimental = rowCount++;
            betterAudioCallRow = rowCount++;
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.S || Build.VERSION.SDK_INT == Build.VERSION_CODES.S_V2) {
                monetIconRow = rowCount++;
            }
            maxRecentStickersRow = rowCount++;
            downloadDividersRow = rowCount++;
            headerDownloadSpeed = rowCount++;
            downloadSpeedBoostRow = rowCount++;
            uploadSpeedBoostRow = rowCount++;
        } else {
            experimentalMessageAlert = rowCount++;
        }
    }

    @Override
    protected BaseListAdapter createAdapter() {
        return new ListAdapter();
    }

    private class ListAdapter extends BaseListAdapter {

        @Override
        protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, boolean partial) {
            switch (ViewType.fromInt(holder.getItemViewType())) {
                case SWITCH:
                    TextCheckCell textCheckCell = (TextCheckCell) holder.itemView;
                    if (position == betterAudioCallRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("MediaStreamVoip", R.string.MediaStreamVoip), AdvanceGramConfig.betterAudioQuality, true);
                    } else if (position == checkBoxExperimentalRow) {
                        boolean isEnabled = AdvanceGramConfig.isDevOptEnabled();
                        textCheckCell.setDrawCheckRipple(true);
                        textCheckCell.setTextAndCheck(isEnabled ? LocaleController.getString("OnModeCheckTitle", R.string.OnModeCheckTitle) : LocaleController.getString("OffModeCheckTitle", R.string.OffModeCheckTitle), isEnabled, false);
                        textCheckCell.setBackgroundColor(Theme.getColor(isEnabled ? Theme.key_windowBackgroundChecked : Theme.key_windowBackgroundUnchecked));
                        textCheckCell.setColors(Theme.key_windowBackgroundCheckText, Theme.key_switchTrackBlue, Theme.key_switchTrackBlueChecked, Theme.key_switchTrackBlueThumb, Theme.key_switchTrackBlueThumbChecked);
                        textCheckCell.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                        textCheckCell.setHeight(56);
                    } else if (position == monetIconRow) {
                        textCheckCell.setTextAndValueAndCheck(LocaleController.getString("MonetIcon", R.string.MonetIcon), LocaleController.getString("MonetIconDesc", R.string.MonetIconDesc), MonetIconsHelper.isSelectedMonet(), true, true);
                    } else if (position == uploadSpeedBoostRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("FasterUploadSpeed", R.string.FasterUploadSpeed), AdvanceGramConfig.uploadSpeedBoost, false);
                    }
                    break;
                case TEXT_HINT_WITH_PADDING:
                    TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell) holder.itemView;
                    if (position == bottomHeaderRow) {
                        textInfoPrivacyCell.setText(LocaleController.getString("ExperimentalDesc", R.string.ExperimentalDesc));
                    } else if (position == experimentalMessageAlert) {
                        textInfoPrivacyCell.setText(LocaleController.getString("ExperimentalOff", R.string.ExperimentalOff));
                    }
                    break;
                case HEADER:
                    HeaderCell headerCell = (HeaderCell) holder.itemView;
                    if (position == headerExperimental) {
                        headerCell.setText(LocaleController.getString("General", R.string.General));
                    } else if (position == headerDownloadSpeed) {
                        headerCell.setText(LocaleController.getString("DownloadSpeed", R.string.DownloadSpeed));
                    }
                    break;
                case SETTINGS:
                    TextSettingsCell textCell = (TextSettingsCell) holder.itemView;
                    textCell.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
                    if (position == maxRecentStickersRow) {
                        textCell.setTextAndValue(LocaleController.getString("MaxRecentStickers", R.string.MaxRecentStickers), String.valueOf(AdvanceGramConfig.maxRecentStickers), partial, true);
                    }
                    break;
            }
        }

        @Override
        protected boolean isEnabled(ViewType viewType, int position) {
            return viewType == ViewType.SWITCH && position != checkBoxExperimentalRow || viewType == ViewType.SETTINGS;
        }

        @Override
        protected View onCreateViewHolder(ViewType viewType) {
            View view = null;
            switch (viewType) {
                case IMAGE_HEADER:
                    view = new LabsHeader(context);
                    break;
                case SLIDE_CHOOSE:
                    SlideChooseView slideChooseView = new SlideChooseView(context);
                    view = slideChooseView;
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    ArrayList<String> arrayList = new ArrayList<>();
                    ArrayList<Integer> types = new ArrayList<>();
                    arrayList.add(LocaleController.getString("DownloadSpeedDefault", R.string.DownloadSpeedDefault));
                    types.add(AdvanceGramConfig.DOWNLOAD_BOOST_DEFAULT);
                    arrayList.add(LocaleController.getString("DownloadSpeedFast", R.string.DownloadSpeedFast));
                    types.add(AdvanceGramConfig.DOWNLOAD_BOOST_FAST);
                    arrayList.add(LocaleController.getString("DownloadSpeedExtreme", R.string.DownloadSpeedExtreme));
                    types.add(AdvanceGramConfig.DOWNLOAD_BOOST_EXTREME);
                    slideChooseView.setCallback(index -> AdvanceGramConfig.setDownloadSpeedBoost(types.get(index)));
                    slideChooseView.setOptions(types.indexOf(AdvanceGramConfig.downloadSpeedBoost), arrayList.toArray(new String[0]));
                    slideChooseView.setDivider(true);
                    break;
            }
            return view;
        }

        @Override
        public ViewType getViewType(int position) {
            if (position == downloadDividersRow) {
                return ViewType.SHADOW;
            } else if (position == betterAudioCallRow || position == checkBoxExperimentalRow || position == monetIconRow ||
                    position == uploadSpeedBoostRow) {
                return ViewType.SWITCH;
            } else if (position == headerImageRow) {
                return ViewType.IMAGE_HEADER;
            } else if (position == bottomHeaderRow || position == experimentalMessageAlert) {
                return ViewType.TEXT_HINT_WITH_PADDING;
            } else if (position == headerExperimental || position == headerDownloadSpeed) {
                return ViewType.HEADER;
            } else if (position == maxRecentStickersRow) {
                return ViewType.SETTINGS;
            } else if (position == downloadSpeedBoostRow) {
                return ViewType.SLIDE_CHOOSE;
            }
            throw new IllegalArgumentException("Invalid position");
        }
    }
}
