package milmit.advancegram.messenger.settings;

import android.content.DialogInterface;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BuildConfig;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.R;
import org.telegram.messenger.browser.Browser;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Cells.TextDetailSettingsCell;
import org.telegram.ui.Components.BulletinFactory;

import milmit.advancegram.messenger.Crashlytics;
import milmit.advancegram.messenger.AdvanceGramConfig;
import milmit.advancegram.messenger.StoreUtils;

public class AdvgramSettings extends BaseSettingsActivity {

    private int divisorInfoRow;
    private int categoryHeaderRow;
    private int generalSettingsRow;
    private int appearanceSettingsRow;
    private int chatSettingsRow;
    private int updateSettingsRow;
    private int experimentalSettingsRow;
    private int infoHeaderRow;
    private int channelAdvRow;
    private int channelUpdatesRow;
    private int groupUpdatesRow;
    private int sourceCodeRow;
    private int supportTranslationRow;
    private int supportDonationRow;
    private int bugReportRow;

    @Override
    protected String getActionBarTitle() {
        return LocaleController.getString("AdvSetting", R.string.AdvSetting);
    }

    @Override
    protected ActionBarMenuItem createMenuItem() {
        ActionBarMenu menu = actionBar.createMenu();
        ActionBarMenuItem menuItem = menu.addItem(0, R.drawable.ic_ab_other);
        menuItem.setContentDescription(LocaleController.getString("AccDescrMoreOptions", R.string.AccDescrMoreOptions));
        menuItem.addSubItem(1, R.drawable.round_settings_backup_restore, LocaleController.getString("ExportSettings", R.string.ExportSettings));
        menuItem.addSubItem(2, R.drawable.round_settings_backup_reset, LocaleController.getString("ThemeResetToDefaultsTitle", R.string.ThemeResetToDefaultsTitle));
        return menuItem;
    }

    @Override
    protected void onMenuItemClick(int id) {
        super.onMenuItemClick(id);
        if (id == 1) {
            AdvanceGramConfig.shareSettings(getParentActivity());
        } else if (id == 2) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(LocaleController.getString("ThemeResetToDefaultsTitle", R.string.ThemeResetToDefaultsTitle));
            builder.setMessage(LocaleController.getString("ResetSettingsAlert", R.string.ResetSettingsAlert));
            builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
            builder.setPositiveButton(LocaleController.getString("Reset", R.string.Reset), (dialogInterface, i) -> {
                int differenceUI = AdvanceGramConfig.getDifferenceUI();
                AdvanceGramConfig.resetSettings();
                Theme.lastHolidayCheckTime = 0;
                Theme.dialogs_holidayDrawable = null;
                reloadDialogs();
                reloadMainInfo();
                AdvanceGramConfig.doRebuildUIWithDiff(differenceUI, parentLayout);
                BulletinFactory.of(AdvgramSettings.this).createSimpleBulletin(R.raw.forward, LocaleController.getString("ResetSettingsHint", R.string.ResetSettingsHint)).show();
            });
            AlertDialog alertDialog = builder.create();
            showDialog(alertDialog);
            TextView button = (TextView) alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            if (button != null) {
                button.setTextColor(Theme.getColor(Theme.key_dialogTextRed2));
            }
        }
    }

    @Override
    protected void onItemClick(View view, int position, float x, float y) {
         if (position == channelUpdatesRow) {
            MessagesController.getInstance(currentAccount).openByUserName("milmit_net", this, 1);
        }else if (position == channelAdvRow) {
             MessagesController.getInstance(currentAccount).openByUserName("advancegram_official", this, 1);
         } else if (position == groupUpdatesRow) {
            MessagesController.getInstance(currentAccount).openByUserName("advancegram_officialForum", this, 1);
        }  else if (position == sourceCodeRow) {
            Browser.openUrl(getParentActivity(), "https://github.com/MilMit/AdvanceGram");
        } else if (position == supportTranslationRow) {
            Browser.openUrl(getParentActivity(), "https://milmit.net/appstore/translations");
        } else if (position == generalSettingsRow) {
            presentFragment(new AdvgramGeneralSettings());
        } else if (position == chatSettingsRow) {
            presentFragment(new AdvgramChatSettings());
        } else if (position == updateSettingsRow) {
            presentFragment(new AdvgramUpdateSettings());
        } else if (position == experimentalSettingsRow) {
            presentFragment(new AdvgramExperimentalSettings());
        } else if (position == supportDonationRow) {
            Browser.openUrl(getParentActivity(), "https://milmit.net/appstore/donations");
        } else if (position == appearanceSettingsRow) {
            presentFragment(new AdvgramAppearanceSettings());
        } else if (position == bugReportRow) {
            AndroidUtilities.addToClipboard(Crashlytics.getReportMessage() + "\n\n#bug");
            BulletinFactory.of(AdvgramSettings.this).createCopyBulletin(LocaleController.getString("ReportDetailsCopied", R.string.ReportDetailsCopied)).show();
        }
    }

    @Override
    protected void updateRowsId() {
        super.updateRowsId();
        updateSettingsRow = -1;

        categoryHeaderRow = rowCount++;
        generalSettingsRow = rowCount++;
        appearanceSettingsRow = rowCount++;
        chatSettingsRow = rowCount++;
        experimentalSettingsRow = rowCount++;
        if (StoreUtils.isFromCheckableStore() || !StoreUtils.isDownloadedFromAnyStore()) {
            updateSettingsRow = rowCount++;
        }
        divisorInfoRow = rowCount++;
        infoHeaderRow = rowCount++;
        channelUpdatesRow = rowCount++;
        channelAdvRow = rowCount++;
        groupUpdatesRow = rowCount++;
        sourceCodeRow = rowCount++;
        supportTranslationRow = rowCount++;
        supportDonationRow = rowCount++;
        bugReportRow = rowCount++;
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
                case TEXT_CELL:
                    TextCell textCell = (TextCell) holder.itemView;
                    textCell.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
                    if (position == generalSettingsRow) {
                        textCell.setTextAndIcon(LocaleController.getString("General", R.string.General), R.drawable.msg_media, true);
                    } else if (position == chatSettingsRow) {
                        textCell.setTextAndIcon(LocaleController.getString("Chat", R.string.Chat), R.drawable.msg_msgbubble3, true);
                    } else if (position == updateSettingsRow) {
                        textCell.setTextAndIcon(LocaleController.getString("AdvUpdates", R.string.AdvUpdates), R.drawable.round_update_white_28, false);
                    } else if (position == channelUpdatesRow) {
                        textCell.setTextAndValueAndIcon(LocaleController.getString("Programcreatorschannel", R.string.Programcreatorschannel), "@" + "MilMit", R.drawable.ic_milmit, true);
                    } else if (position == channelAdvRow) {
                        textCell.setTextAndValueAndIcon(LocaleController.getString("OfficialChannel", R.string.OfficialChannel), "@" + "Advancegram Official", R.drawable.advgram_notification, true);
                    } else if (position == groupUpdatesRow) {
                        textCell.setTextAndValueAndIcon(LocaleController.getString("OfficialGroup", R.string.OfficialGroup), "@" + "Advancegram Forum", R.drawable.msg_groups, true);
                    } else if (position == experimentalSettingsRow) {
                        textCell.setTextAndIcon(LocaleController.getString("Experimental", R.string.Experimental), R.drawable.outline_science_white, true);
                    } else if (position == appearanceSettingsRow) {
                        textCell.setTextAndIcon(LocaleController.getString("Appearance", R.string.Appearance), R.drawable.settings_appearance, true);
                    }
                    break;
                case HEADER:
                    HeaderCell headerCell = (HeaderCell) holder.itemView;
                    if (position == categoryHeaderRow) {
                        headerCell.setText(LocaleController.getString("Settings", R.string.Settings));
                    } else if (position == infoHeaderRow) {
                        headerCell.setText(LocaleController.getString("Info", R.string.Info));
                    }
                    break;
                case DETAILED_SETTINGS:
                    TextDetailSettingsCell textDetailCell = (TextDetailSettingsCell) holder.itemView;
                    textDetailCell.setMultilineDetail(true);
                    if (position == supportTranslationRow) {
                        textDetailCell.setTextAndValueAndIcon(LocaleController.getString("TranslateAdv", R.string.TranslateAdv), LocaleController.getString("TranslateAdvDesc", R.string.TranslateAdvDesc), R.drawable.round_translate_white_28, true);
                    } else if (position == supportDonationRow) {
                        textDetailCell.setTextAndValueAndIcon(LocaleController.getString("Donate", R.string.Donate), LocaleController.getString("DonateDesc", R.string.DonateDesc), R.drawable.round_favorite_border_white, true);
                    } else if (position == sourceCodeRow) {
                        String commitInfo = String.format("%s commit, %s", BuildConfig.GIT_COMMIT_HASH, LocaleController.formatDateAudio(BuildConfig.GIT_COMMIT_DATE, false));
                        textDetailCell.setTextAndValueAndIcon(LocaleController.getString("SourceCode", R.string.SourceCode), commitInfo, R.drawable.outline_source_white_28, true);
                    } else if (position == bugReportRow) {
                        textDetailCell.setTextAndValueAndIcon(LocaleController.getString("CopyReportDetails", R.string.CopyReportDetails), LocaleController.getString("CopyReportDetailsDesc", R.string.CopyReportDetailsDesc), R.drawable.bug_report, false);
                    }
                    break;
            }
        }

        @Override
        protected boolean isEnabled(ViewType viewType, int position) {
            return viewType == ViewType.TEXT_CELL || viewType == ViewType.DETAILED_SETTINGS;
        }

        @Override
        public ViewType getViewType(int position) {
            if (position == divisorInfoRow) {
                return ViewType.SHADOW;
            } else if (position == generalSettingsRow || position == chatSettingsRow || position == updateSettingsRow ||
                    position == channelUpdatesRow || position == channelAdvRow || position == groupUpdatesRow ||
                    position == experimentalSettingsRow || position == appearanceSettingsRow) {
                return ViewType.TEXT_CELL;
            } else if (position == categoryHeaderRow || position == infoHeaderRow) {
                return ViewType.HEADER;
            } else if (position == supportTranslationRow || position == supportDonationRow || position == sourceCodeRow || position == bugReportRow) {
                return ViewType.DETAILED_SETTINGS;
            }
            throw new IllegalArgumentException("Invalid position");
        }
    }
}
