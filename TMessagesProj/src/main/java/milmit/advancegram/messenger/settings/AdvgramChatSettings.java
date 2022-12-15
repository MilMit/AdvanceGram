package milmit.advancegram.messenger.settings;

import android.text.Spannable;
import android.text.SpannableString;
import android.util.Size;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.camera.video.Quality;
import androidx.recyclerview.widget.RecyclerView;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextCheckbox2Cell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.UndoView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import milmit.advancegram.messenger.AdvanceGramConfig;
import milmit.advancegram.messenger.camera.CameraXUtils;
import milmit.advancegram.messenger.components.CameraTypeSelector;
import milmit.advancegram.messenger.components.StickerSizeCell;
import milmit.advancegram.messenger.entities.EntitiesHelper;
import milmit.advancegram.messenger.helpers.AudioEnhance;
import milmit.advancegram.messenger.helpers.PopupHelper;

public class AdvgramChatSettings extends BaseSettingsActivity implements NotificationCenter.NotificationCenterDelegate {

    private int stickerSizeHeaderRow;
    private int stickerSizeRow;
    private int stickerSizeDividerRow;
    private int chatHeaderRow;
    private int mediaSwipeByTapRow;
    private int jumpChannelRow;
    private int showGreetings;
    private int hideKeyboardRow;
    private int playGifAsVideoRow;
    private int chatDividerRow;
    private int foldersHeaderRow;
    private int showFolderWhenForwardRow;
    private int foldersDividerRow;
    private int messageMenuHeaderRow;
    private int showAddToSMRow;
    private int showRepeatRow;
    private int showNoQuoteForwardRow;
    private int showReportRow;
    private int showMessageDetailsRow;
    private int showCopyPhotoRow;
    private int showPatpatRow;
    private int audioVideoDividerRow;
    private int audioVideoHeaderRow;
    private int rearCameraStartingRow;
    private int confirmSendRow;
    private int showDeleteRow;
    private int hideAllTabRow;
    private int cameraTypeHeaderRow;
    private int cameraTypeSelectorRow;
    private int cameraXOptimizeRow;
    private int cameraXQualityRow;
    private int cameraAdviseRow;
    private int proximitySensorRow;
    private int suppressionRow;
    private int turnSoundOnVDKeyRow;
    private int openArchiveOnPullRow;
    private int confirmStickersGIFsRow;
    private int hideTimeOnStickerRow;
    private int onlineStatusRow;
    private int hideSendAsChannelRow;
    private int stickersSortingRow;

    @Override
    public boolean onFragmentCreate() {
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiLoaded);
        return super.onFragmentCreate();
    }

    @Override
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiLoaded);
    }

    @Override
    protected void onItemClick(View view, int position, float x, float y) {
        if (position == mediaSwipeByTapRow) {
            AdvanceGramConfig.toggleMediaFlipByTap();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(AdvanceGramConfig.mediaFlipByTap);
            }
        } else if (position == jumpChannelRow) {
            AdvanceGramConfig.toggleJumpChannel();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(AdvanceGramConfig.jumpChannel);
            }
        } else if (position == showGreetings) {
            AdvanceGramConfig.toggleShowGreetings();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(AdvanceGramConfig.showGreetings);
            }
        } else if (position == hideKeyboardRow) {
            AdvanceGramConfig.toggleHideKeyboard();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(AdvanceGramConfig.hideKeyboard);
            }
        } else if (position == playGifAsVideoRow) {
            AdvanceGramConfig.toggleGifAsVideo();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(AdvanceGramConfig.gifAsVideo);
            }
        } else if (position == showFolderWhenForwardRow) {
            AdvanceGramConfig.toggleShowFolderWhenForward();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(AdvanceGramConfig.showFolderWhenForward);
            }
        } else if (position == rearCameraStartingRow) {
            AdvanceGramConfig.toggleUseRearCamera();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(AdvanceGramConfig.useRearCamera);
            }
        } else if (position == confirmSendRow) {
            AdvanceGramConfig.toggleSendConfirm();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(AdvanceGramConfig.sendConfirm);
            }
        } else if (position == showAddToSMRow) {
            AdvanceGramConfig.toggleShowSaveMessage();
            if (view instanceof TextCheckbox2Cell) {
                ((TextCheckbox2Cell) view).setChecked(AdvanceGramConfig.showSaveMessage);
            }
        } else if (position == showRepeatRow) {
            AdvanceGramConfig.toggleShowRepeat();
            if (view instanceof TextCheckbox2Cell) {
                ((TextCheckbox2Cell) view).setChecked(AdvanceGramConfig.showRepeat);
            }
        } else if (position == showMessageDetailsRow) {
            AdvanceGramConfig.toggleShowMessageDetails();
            if (view instanceof TextCheckbox2Cell) {
                ((TextCheckbox2Cell) view).setChecked(AdvanceGramConfig.showMessageDetails);
            }
        } else if (position == showNoQuoteForwardRow) {
            AdvanceGramConfig.toggleShowNoQuoteForwardRow();
            if (view instanceof TextCheckbox2Cell) {
                ((TextCheckbox2Cell) view).setChecked(AdvanceGramConfig.showNoQuoteForward);
            }
        } else if (position == showReportRow) {
            AdvanceGramConfig.toggleShowReportMessage();
            if (view instanceof TextCheckbox2Cell) {
                ((TextCheckbox2Cell) view).setChecked(AdvanceGramConfig.showReportMessage);
            }
        } else if (position == showDeleteRow) {
            AdvanceGramConfig.toggleShowDeleteDownloadedFile();
            if (view instanceof TextCheckbox2Cell) {
                ((TextCheckbox2Cell) view).setChecked(AdvanceGramConfig.showDeleteDownloadedFile);
            }
        } else if (position == showCopyPhotoRow) {
            AdvanceGramConfig.toggleShowCopyPhoto();
            if (view instanceof TextCheckbox2Cell) {
                ((TextCheckbox2Cell) view).setChecked(AdvanceGramConfig.showCopyPhoto);
            }
        } else if (position == hideAllTabRow) {
            AdvanceGramConfig.toggleHideAllTab();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(AdvanceGramConfig.hideAllTab);
            }
            reloadDialogs();
        } else if (position == showPatpatRow) {
            AdvanceGramConfig.toggleShowPatpat();
            if (view instanceof TextCheckbox2Cell) {
                ((TextCheckbox2Cell) view).setChecked(AdvanceGramConfig.showPatpat);
            }
        } else if (position == cameraXOptimizeRow) {
            AdvanceGramConfig.toggleCameraXOptimizedMode();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(AdvanceGramConfig.useCameraXOptimizedMode);
            }
        } else if (position == cameraXQualityRow) {
            Map<Quality, Size> availableSizes = CameraXUtils.getAvailableVideoSizes();
            Stream<Integer> tmp = availableSizes.values().stream().sorted(Comparator.comparingInt(Size::getWidth).reversed()).map(Size::getHeight);
            ArrayList<Integer> types = tmp.collect(Collectors.toCollection(ArrayList::new));
            ArrayList<String> arrayList = types.stream().map(p -> p + "p").collect(Collectors.toCollection(ArrayList::new));
            PopupHelper.show(arrayList, LocaleController.getString("CameraQuality", R.string.CameraQuality), types.indexOf(AdvanceGramConfig.cameraResolution), context, i -> {
                AdvanceGramConfig.saveCameraResolution(types.get(i));
                listAdapter.notifyItemChanged(cameraXQualityRow, PARTIAL);
            });
        } else if (position == proximitySensorRow) {
            AdvanceGramConfig.toggleDisableProximityEvents();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(AdvanceGramConfig.disableProximityEvents);
            }
            restartTooltip.showWithAction(0, UndoView.ACTION_CACHE_WAS_CLEARED, null, null);
        } else if (position == suppressionRow) {
            AdvanceGramConfig.toggleVoicesAgc();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(AdvanceGramConfig.voicesAgc);
            }
        } else if (position == turnSoundOnVDKeyRow) {
            AdvanceGramConfig.toggleTurnSoundOnVDKey();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(AdvanceGramConfig.turnSoundOnVDKey);
            }
        } else if (position == openArchiveOnPullRow) {
            AdvanceGramConfig.toggleOpenArchiveOnPull();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(AdvanceGramConfig.openArchiveOnPull);
            }
        } else if (position == confirmStickersGIFsRow) {
            AdvanceGramConfig.toggleConfirmStickersGIFs();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(AdvanceGramConfig.confirmStickersGIFs);
            }
        } else if (position == hideTimeOnStickerRow) {
            AdvanceGramConfig.toggleHideTimeOnSticker();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(AdvanceGramConfig.hideTimeOnSticker);
            }
        } else if (position == onlineStatusRow) {
            AdvanceGramConfig.toggleShowStatusInChat();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(AdvanceGramConfig.showStatusInChat);
            }
        } else if (position == hideSendAsChannelRow) {
            AdvanceGramConfig.toggleHideSendAsChannel();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(AdvanceGramConfig.hideSendAsChannel);
            }
            getNotificationCenter().postNotificationName(NotificationCenter.updateInterfaces, MessagesController.UPDATE_MASK_CHAT);
        } else if (position == stickersSortingRow) {
            AdvanceGramConfig.toggleStickersSorting();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(AdvanceGramConfig.stickersSorting);
            }
        }
    }

    @Override
    protected void onMenuItemClick(int id) {
        super.onMenuItemClick(id);
        if (id == 1) {
            AdvanceGramConfig.setStickerSize(14);
            menuItem.setVisibility(View.GONE);
            listAdapter.notifyItemChanged(stickerSizeRow, new Object());
        }
    }

    @Override
    protected String getActionBarTitle() {
        return LocaleController.getString("Chat", R.string.Chat);
    }

    @Override
    protected ActionBarMenuItem createMenuItem() {
        ActionBarMenu menu = actionBar.createMenu();
        ActionBarMenuItem menuItem = menu.addItem(0, R.drawable.ic_ab_other);
        menuItem.setContentDescription(LocaleController.getString("AccDescrMoreOptions", R.string.AccDescrMoreOptions));
        menuItem.addSubItem(1, R.drawable.msg_reset, LocaleController.getString("ResetStickersSize", R.string.ResetStickersSize));
        menuItem.setVisibility(AdvanceGramConfig.stickerSizeStack != 14.0f ? View.VISIBLE : View.GONE);
        return menuItem;
    }

    @Override
    protected void updateRowsId() {
        super.updateRowsId();
        cameraTypeHeaderRow = -1;
        cameraTypeSelectorRow = -1;
        cameraXOptimizeRow = -1;
        cameraXQualityRow = -1;
        cameraAdviseRow = -1;
        suppressionRow = -1;

        stickerSizeHeaderRow = rowCount++;
        stickerSizeRow = rowCount++;
        stickerSizeDividerRow = rowCount++;

        if (CameraXUtils.isCameraXSupported()) {
            cameraTypeHeaderRow = rowCount++;
            cameraTypeSelectorRow = rowCount++;
            if (AdvanceGramConfig.cameraType == 1) {
                cameraXOptimizeRow = rowCount++;
                cameraXQualityRow = rowCount++;
            }
            cameraAdviseRow = rowCount++;
        }

        chatHeaderRow = rowCount++;
        mediaSwipeByTapRow = rowCount++;
        jumpChannelRow = rowCount++;
        showGreetings = rowCount++;
        stickersSortingRow = rowCount++;
        playGifAsVideoRow = rowCount++;
        hideKeyboardRow = rowCount++;
        hideSendAsChannelRow = rowCount++;
        openArchiveOnPullRow = rowCount++;
        onlineStatusRow = rowCount++;
        chatDividerRow = rowCount++;

        audioVideoHeaderRow = rowCount++;
        if (AudioEnhance.isAvailable()) {
            suppressionRow = rowCount++;
        }
        turnSoundOnVDKeyRow = rowCount++;
        proximitySensorRow = rowCount++;
        rearCameraStartingRow = rowCount++;
        confirmSendRow = rowCount++;
        confirmStickersGIFsRow = rowCount++;
        hideTimeOnStickerRow = rowCount++;
        audioVideoDividerRow = rowCount++;

        foldersHeaderRow = rowCount++;
        hideAllTabRow = rowCount++;
        showFolderWhenForwardRow = rowCount++;
        foldersDividerRow = rowCount++;

        messageMenuHeaderRow = rowCount++;
        showDeleteRow = rowCount++;
        showCopyPhotoRow = rowCount++;
        showNoQuoteForwardRow = rowCount++;
        showAddToSMRow = rowCount++;
        showRepeatRow = rowCount++;
        showPatpatRow = rowCount++;
        showReportRow = rowCount++;
        showMessageDetailsRow = rowCount++;
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
                    if (position == chatHeaderRow) {
                        headerCell.setText(LocaleController.getString("Chat", R.string.Chat));
                    } else if (position == foldersHeaderRow) {
                        headerCell.setText(LocaleController.getString("Filters", R.string.Filters));
                    } else if (position == audioVideoHeaderRow) {
                        headerCell.setText(LocaleController.getString("MediaSettings", R.string.MediaSettings));
                    } else if (position == messageMenuHeaderRow) {
                        headerCell.setText(LocaleController.getString("ContextMenu", R.string.ContextMenu));
                    } else if (position == stickerSizeHeaderRow) {
                        headerCell.setText(LocaleController.getString("StickersSize", R.string.StickersSize));
                    } else if (position == cameraTypeHeaderRow) {
                        headerCell.setText(LocaleController.getString("CameraType", R.string.CameraType));
                    }
                    break;
                case SWITCH:
                    TextCheckCell textCheckCell = (TextCheckCell) holder.itemView;
                    textCheckCell.setEnabled(true, null);
                    if (position == mediaSwipeByTapRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("FlipMediaByTapping", R.string.FlipMediaByTapping), AdvanceGramConfig.mediaFlipByTap, true);
                    } else if (position == jumpChannelRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("JumpToNextChannel", R.string.JumpToNextChannel), AdvanceGramConfig.jumpChannel, true);
                    } else if (position == showGreetings) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("GreetingSticker", R.string.GreetingSticker), AdvanceGramConfig.showGreetings, true);
                    } else if (position == hideKeyboardRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("HideChatKeyboard", R.string.HideChatKeyboard), AdvanceGramConfig.hideKeyboard, true);
                    } else if (position == playGifAsVideoRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("GIFsAsVideo", R.string.GIFsAsVideo), AdvanceGramConfig.gifAsVideo, true);
                    } else if (position == showFolderWhenForwardRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("FoldersWhenForwarding", R.string.FoldersWhenForwarding), AdvanceGramConfig.showFolderWhenForward, true);
                    } else if (position == rearCameraStartingRow) {
                        textCheckCell.setTextAndValueAndCheck(LocaleController.getString("UseRearRoundVideos", R.string.UseRearRoundVideos), LocaleController.getString("UseRearRoundVideosDesc", R.string.UseRearRoundVideosDesc), AdvanceGramConfig.useRearCamera, true, true);
                    } else if (position == confirmSendRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("ConfirmAVMessages", R.string.ConfirmAVMessages), AdvanceGramConfig.sendConfirm, true);
                    } else if (position == hideAllTabRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("HideAllChatsFolder", R.string.HideAllChatsFolder), AdvanceGramConfig.hideAllTab, true);
                    } else if (position == cameraXOptimizeRow) {
                        textCheckCell.setTextAndValueAndCheck(LocaleController.getString("PerformanceMode", R.string.PerformanceMode), LocaleController.getString("PerformanceModeDesc", R.string.PerformanceModeDesc), AdvanceGramConfig.useCameraXOptimizedMode, true, true);
                    } else if (position == proximitySensorRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("DisableProximityEvents", R.string.DisableProximityEvents), AdvanceGramConfig.disableProximityEvents, true);
                    } else if (position == suppressionRow) {
                        textCheckCell.setTextAndValueAndCheck(LocaleController.getString("VoiceEnhancements", R.string.VoiceEnhancements), LocaleController.getString("VoiceEnhancementsDesc", R.string.VoiceEnhancementsDesc), AdvanceGramConfig.voicesAgc, true, true);
                    } else if (position == turnSoundOnVDKeyRow) {
                        textCheckCell.setTextAndValueAndCheck(LocaleController.getString("TurnSoundOnVDKey", R.string.TurnSoundOnVDKey), LocaleController.getString("TurnSoundOnVDKeyDesc", R.string.TurnSoundOnVDKeyDesc), AdvanceGramConfig.turnSoundOnVDKey, true, true);
                    } else if (position == openArchiveOnPullRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("OpenArchiveOnPull", R.string.OpenArchiveOnPull), AdvanceGramConfig.openArchiveOnPull, true);
                    } else if (position == confirmStickersGIFsRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("ConfirmStickersGIFs", R.string.ConfirmStickersGIFs), AdvanceGramConfig.confirmStickersGIFs, true);
                    } else if (position == hideTimeOnStickerRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("HideTimeOnSticker", R.string.HideTimeOnSticker), AdvanceGramConfig.hideTimeOnSticker, false);
                    } else if (position == onlineStatusRow) {
                        textCheckCell.setTextAndValueAndCheck(LocaleController.getString("OnlineStatus", R.string.OnlineStatus), LocaleController.getString("OnlineStatusDesc", R.string.OnlineStatusDesc), AdvanceGramConfig.showStatusInChat, true, false);
                    } else if (position == hideSendAsChannelRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("HideSendAsChannel", R.string.HideSendAsChannel), AdvanceGramConfig.hideSendAsChannel, true);
                    } else if (position == stickersSortingRow) {
                        textCheckCell.setTextAndValueAndCheck(LocaleController.getString("AutomaticSorting", R.string.AutomaticSorting), LocaleController.getString("AutomaticSortingDesc", R.string.AutomaticSortingDesc), AdvanceGramConfig.stickersSorting, true, true);
                    }
                    break;
                case TEXT_HINT_WITH_PADDING:
                    TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell) holder.itemView;
                    if (position == cameraAdviseRow) {
                        String advise;
                        switch (AdvanceGramConfig.cameraType) {
                            case 0:
                                advise = LocaleController.getString("DefaultCameraDesc", R.string.DefaultCameraDesc);
                                break;
                            case 1:
                                advise = LocaleController.getString("CameraXDesc", R.string.CameraXDesc);
                                break;
                            case 2:
                            default:
                                advise = LocaleController.getString("SystemCameraDesc", R.string.SystemCameraDesc);
                                break;
                        }
                        Spannable htmlParsed = new SpannableString(AndroidUtilities.fromHtml(advise));
                        textInfoPrivacyCell.setText(EntitiesHelper.getUrlNoUnderlineText(htmlParsed));
                    }
                    break;
                case SETTINGS:
                    TextSettingsCell textSettingsCell = (TextSettingsCell) holder.itemView;
                    textSettingsCell.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
                    if (position == cameraXQualityRow) {
                        textSettingsCell.setTextAndValue(LocaleController.getString("CameraQuality", R.string.CameraQuality), AdvanceGramConfig.cameraResolution + "p", partial,false);
                    }
                    break;
                case CHECKBOX:
                    TextCheckbox2Cell textCheckbox2Cell = (TextCheckbox2Cell) holder.itemView;
                    if (position == showDeleteRow) {
                        textCheckbox2Cell.setTextAndCheck(LocaleController.getString("ClearFromCache", R.string.ClearFromCache), AdvanceGramConfig.showDeleteDownloadedFile, true);
                    } else if (position == showNoQuoteForwardRow) {
                        textCheckbox2Cell.setTextAndCheck(LocaleController.getString("NoQuoteForward", R.string.NoQuoteForward), AdvanceGramConfig.showNoQuoteForward, true);
                    } else if (position == showAddToSMRow) {
                        textCheckbox2Cell.setTextAndCheck(LocaleController.getString("AddToSavedMessages", R.string.AddToSavedMessages), AdvanceGramConfig.showSaveMessage, true);
                    } else if (position == showRepeatRow) {
                        textCheckbox2Cell.setTextAndCheck(LocaleController.getString("Repeat", R.string.Repeat), AdvanceGramConfig.showRepeat, true);
                    } else if (position == showReportRow) {
                        textCheckbox2Cell.setTextAndCheck(LocaleController.getString("ReportChat", R.string.ReportChat), AdvanceGramConfig.showReportMessage, true);
                    } else if (position == showMessageDetailsRow) {
                        textCheckbox2Cell.setTextAndCheck(LocaleController.getString("MessageDetails", R.string.MessageDetails), AdvanceGramConfig.showMessageDetails, false);
                    } else if (position == showCopyPhotoRow) {
                        textCheckbox2Cell.setTextAndCheck(LocaleController.getString("CopyPhoto", R.string.CopyPhoto), AdvanceGramConfig.showCopyPhoto, true);
                    } else if (position == showPatpatRow) {
                        textCheckbox2Cell.setTextAndCheck(LocaleController.getString("Patpat", R.string.Patpat), AdvanceGramConfig.showPatpat, true);
                    }
                    break;
            }
        }

        @Override
        protected boolean isEnabled(ViewType viewType, int position) {
            return viewType == ViewType.SWITCH || viewType == ViewType.SETTINGS || viewType == ViewType.CHECKBOX;
        }

        @Override
        protected View onCreateViewHolder(ViewType viewType) {
            View view = null;
            switch (viewType) {
                case STICKER_SIZE:
                    view = new StickerSizeCell(context, parentLayout) {
                        @Override
                        protected void onSeek() {
                            super.onSeek();
                            if (AdvanceGramConfig.stickerSizeStack != 14) {
                                menuItem.setVisibility(VISIBLE);
                            } else {
                                menuItem.setVisibility(INVISIBLE);
                            }
                        }
                    };
                    break;
                case CAMERA_SELECTOR:
                    view = new CameraTypeSelector(context) {
                        @Override
                        protected void onSelectedCamera(int cameraSelected) {
                            super.onSelectedCamera(cameraSelected);
                            int oldValue = AdvanceGramConfig.cameraType;
                            AdvanceGramConfig.saveCameraType(cameraSelected);
                            if (cameraSelected == 1) {
                                updateRowsId();
                                listAdapter.notifyItemInserted(cameraXOptimizeRow);
                                listAdapter.notifyItemInserted(cameraXQualityRow);
                                listAdapter.notifyItemChanged(cameraAdviseRow);
                            } else if (oldValue == 1){
                                listAdapter.notifyItemRemoved(cameraXOptimizeRow);
                                listAdapter.notifyItemRemoved(cameraXQualityRow);
                                listAdapter.notifyItemChanged(cameraAdviseRow - 1);
                                updateRowsId();
                            } else {
                                listAdapter.notifyItemChanged(cameraAdviseRow);
                            }
                        }
                    };
                    break;
            }
            if (view != null) view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
            return view;
        }

        @Override
        public ViewType getViewType(int position) {
            if (position == chatDividerRow || position == foldersDividerRow || position == audioVideoDividerRow ||
                    position == stickerSizeDividerRow) {
                return ViewType.SHADOW;
            } else if (position == chatHeaderRow || position == foldersHeaderRow || position == audioVideoHeaderRow ||
                    position == messageMenuHeaderRow || position == stickerSizeHeaderRow || position == cameraTypeHeaderRow) {
                return ViewType.HEADER;
            } else if (position == mediaSwipeByTapRow || position == jumpChannelRow || position == hideKeyboardRow ||
                    position == playGifAsVideoRow || position == showFolderWhenForwardRow ||
                    position == rearCameraStartingRow || position == confirmSendRow || position == showGreetings ||
                    position == cameraXOptimizeRow || position == proximitySensorRow || position == suppressionRow ||
                    position == turnSoundOnVDKeyRow || position == openArchiveOnPullRow || position == confirmStickersGIFsRow ||
                    position == hideTimeOnStickerRow || position == onlineStatusRow || position == hideAllTabRow ||
                    position == hideSendAsChannelRow || position == stickersSortingRow) {
                return ViewType.SWITCH;
            } else if (position == stickerSizeRow) {
                return ViewType.STICKER_SIZE;
            } else if (position == cameraTypeSelectorRow) {
                return ViewType.CAMERA_SELECTOR;
            } else if (position == cameraAdviseRow) {
                return ViewType.TEXT_HINT_WITH_PADDING;
            } else if (position == cameraXQualityRow) {
                return ViewType.SETTINGS;
            } else if (position == showDeleteRow || position == showNoQuoteForwardRow || position == showAddToSMRow ||
                    position == showRepeatRow || position == showReportRow ||
                    position == showMessageDetailsRow || position == showCopyPhotoRow || position == showPatpatRow) {
                return ViewType.CHECKBOX;
            }
            throw new IllegalArgumentException("Invalid position");
        }
    }

    @Override
    public void didReceivedNotification(int id, int account, final Object... args) {
        if (id == NotificationCenter.emojiLoaded) {
            if (listView != null) {
                listView.invalidateViews();
            }
        }
    }
}
