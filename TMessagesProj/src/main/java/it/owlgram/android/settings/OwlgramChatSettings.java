package it.owlgram.android.settings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.UndoView;

import java.util.ArrayList;

import it.owlgram.android.OwlConfig;
import it.owlgram.android.camera.CameraXUtilities;
import it.owlgram.android.components.CameraTypeSelector;
import it.owlgram.android.components.StickerSizeCell;
import it.owlgram.android.helpers.AudioEnhance;
import it.owlgram.android.helpers.EntitiesHelper;
import it.owlgram.android.helpers.PopupHelper;

public class OwlgramChatSettings extends BaseFragment {
    private ActionBarMenuItem menuItem;

    private int rowCount;
    private ListAdapter listAdapter;
    private int stickerSizeHeaderRow;
    private int stickerSizeRow;
    private int stickerSizeDividerRow;
    private int chatHeaderRow;
    private int mediaSwipeByTapRow;
    private int jumpChannelRow;
    private int showGreetings;
    private int hideKeyboardRow;
    private int scrollableRow;
    private int playGifAsVideoRow;
    private int chatDividerRow;
    private int foldersHeaderRow;
    private int showFolderWhenForwardRow;
    private int foldersDividerRow;
    private int messageMenuHeaderRow;
    private int showTranslateRow;
    private int showAddToSMRow;
    private int showRepeatRow;
    private int showNoQuoteForwardRow;
    private int showReportRow;
    private int showMessageDetails;
    private int messageMenuDividerRow;
    private int audioVideoHeaderRow;
    private int rearCameraStartingRow;
    private int confirmSendRow;
    private int showDeleteRow;
    private int showHideAllTab;
    private int cameraTypeHeaderRow;
    private int cameraTypeSelectorRow;
    private int cameraXOptimizeRow;
    private int cameraXFpsRow;
    private int cameraAdviseRow;
    private int proximitySensorRow;
    private int swipePiPRow;
    private int suppressionRow;
    private int betterAudioMessagesRow;
    private int turnSoundOnVDKeyRow;
    private int openArchiveOnPullRow;

    private UndoView restartTooltip;

    @Override
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        updateRowsId(true);
        return true;
    }

    @Override
    public View createView(Context context) {
        actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        actionBar.setTitle(LocaleController.getString("Chat", R.string.Chat));
        actionBar.setAllowOverlayTitle(false);
        if (AndroidUtilities.isTablet()) {
            actionBar.setOccupyStatusBar(false);
        }
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int id) {
                if (id == -1) {
                    finishFragment();
                } else if (id == 1) {
                    OwlConfig.setStickerSize(14);
                    menuItem.setVisibility(View.GONE);
                    listAdapter.notifyItemChanged(stickerSizeRow, new Object());
                }
            }
        });

        ActionBarMenu menu = actionBar.createMenu();
        menuItem = menu.addItem(0, R.drawable.ic_ab_other);
        menuItem.setContentDescription(LocaleController.getString("AccDescrMoreOptions", R.string.AccDescrMoreOptions));
        menuItem.addSubItem(1, R.drawable.msg_reset, LocaleController.getString("ResetStickersSize", R.string.ResetStickersSize));
        menuItem.setVisibility(OwlConfig.stickerSizeStack != 14.0f ? View.VISIBLE : View.GONE);

        listAdapter = new ListAdapter(context);
        fragmentView = new FrameLayout(context);
        fragmentView.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        FrameLayout frameLayout = (FrameLayout) fragmentView;

        RecyclerListView listView = new RecyclerListView(context);
        listView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        listView.setVerticalScrollBarEnabled(false);
        listView.setAdapter(listAdapter);
        if (listView.getItemAnimator() != null) {
            ((DefaultItemAnimator) listView.getItemAnimator()).setDelayAnimations(false);
        }
        frameLayout.addView(listView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));
        listView.setOnItemClickListener((view, position, x, y) -> {
            if (position == mediaSwipeByTapRow) {
                OwlConfig.toggleMediaFlipByTap();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(OwlConfig.mediaFlipByTap);
                }
            } else if (position == jumpChannelRow) {
                OwlConfig.toggleJumpChannel();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(OwlConfig.jumpChannel);
                }
            } else if (position == showGreetings) {
                OwlConfig.toggleShowGreetings();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(OwlConfig.showGreetings);
                }
            } else if (position == hideKeyboardRow) {
                OwlConfig.toggleHideKeyboard();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(OwlConfig.hideKeyboard);
                }
            } else if (position == playGifAsVideoRow) {
                OwlConfig.toggleGifAsVideo();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(OwlConfig.gifAsVideo);
                }
            } else if (position == showFolderWhenForwardRow) {
                OwlConfig.toggleShowFolderWhenForward();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(OwlConfig.showFolderWhenForward);
                }
            } else if (position == rearCameraStartingRow) {
                OwlConfig.toggleUseRearCamera();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(OwlConfig.useRearCamera);
                }
            } else if (position == confirmSendRow) {
                OwlConfig.toggleSendConfirm();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(OwlConfig.sendConfirm);
                }
            } else if (position == showTranslateRow) {
                OwlConfig.toggleShowTranslate();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(OwlConfig.showTranslate);
                }
            } else if (position == showAddToSMRow) {
                OwlConfig.toggleShowSaveMessage();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(OwlConfig.showSaveMessage);
                }
            } else if (position == showRepeatRow) {
                OwlConfig.toggleShowRepeat();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(OwlConfig.showRepeat);
                }
            } else if (position == showMessageDetails) {
                OwlConfig.toggleShowMessageDetails();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(OwlConfig.showMessageDetails);
                }
            } else if (position == showNoQuoteForwardRow) {
                OwlConfig.toggleShowNoQuoteForwardRow();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(OwlConfig.showNoQuoteForward);
                }
            } else if (position == showReportRow) {
                OwlConfig.toggleShowReportMessage();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(OwlConfig.showReportMessage);
                }
            } else if (position == scrollableRow) {
                OwlConfig.toggleScrollableChatPreview();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(OwlConfig.scrollableChatPreview);
                }
            } else if (position == showDeleteRow) {
                OwlConfig.toggleShowDeleteDownloadedFile();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(OwlConfig.showDeleteDownloadedFile);
                }
            } else if (position == showHideAllTab) {
                OwlConfig.toggleHideAllTab();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(OwlConfig.hideAllTab);
                }
                getNotificationCenter().postNotificationName(NotificationCenter.dialogFiltersUpdated);
            } else if (position == cameraXOptimizeRow) {
                OwlConfig.toggleCameraXOptimizedMode();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(OwlConfig.useCameraXOptimizedMode);
                }
            } else if (position == cameraXFpsRow) {
                ArrayList<String> arrayList = new ArrayList<>();
                ArrayList<Integer> types = new ArrayList<>();
                arrayList.add("60 Fps");
                types.add(60);
                arrayList.add("30 Fps");
                types.add(30);
                PopupHelper.show(arrayList, LocaleController.getString("MotionSmoothness", R.string.MotionSmoothness), types.indexOf(OwlConfig.cameraXFps), context, i -> {
                    OwlConfig.saveCameraXFps(types.get(i));
                    listAdapter.notifyItemChanged(cameraXFpsRow);
                });
            } else if (position == proximitySensorRow) {
                OwlConfig.toggleDisableProximityEvents();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(OwlConfig.disableProximityEvents);
                }
                restartTooltip.showWithAction(0, UndoView.ACTION_CACHE_WAS_CLEARED, null, null);
            } else if (position == swipePiPRow) {
                OwlConfig.toggleSwipeToPiP();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(OwlConfig.swipeToPiP);
                }
            } else if (position == suppressionRow) {
                OwlConfig.toggleVoicesAgc();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(OwlConfig.voicesAgc);
                }
            } else if (position == betterAudioMessagesRow) {
                OwlConfig.toggleIncreaseAudioMessages();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(OwlConfig.increaseAudioMessages);
                }
            } else if (position == turnSoundOnVDKeyRow) {
                OwlConfig.toggleturnSoundOnVDKey();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(OwlConfig.turnSoundOnVDKey);
                }
            } else if (position == openArchiveOnPullRow) {
                OwlConfig.toggleOpenArchiveOnPull();
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(OwlConfig.openArchiveOnPull);
                }
            }
        });
        restartTooltip = new UndoView(context);
        restartTooltip.setInfoText(LocaleController.formatString("RestartAppToApplyChanges", R.string.RestartAppToApplyChanges));
        frameLayout.addView(restartTooltip, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, Gravity.BOTTOM | Gravity.LEFT, 8, 0, 8, 8));
        return fragmentView;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void updateRowsId(boolean notify) {
        rowCount = 0;
        cameraTypeHeaderRow = -1;
        cameraTypeSelectorRow = -1;
        cameraXOptimizeRow = -1;
        cameraXFpsRow = -1;
        cameraAdviseRow = -1;
        suppressionRow = -1;

        stickerSizeHeaderRow = rowCount++;
        stickerSizeRow = rowCount++;
        stickerSizeDividerRow = rowCount++;

        chatHeaderRow = rowCount++;
        mediaSwipeByTapRow = rowCount++;
        jumpChannelRow = rowCount++;
        showGreetings = rowCount++;
        playGifAsVideoRow = rowCount++;
        hideKeyboardRow = rowCount++;
        scrollableRow = rowCount++;
        openArchiveOnPullRow = rowCount++;
        chatDividerRow = rowCount++;

        foldersHeaderRow = rowCount++;
        showHideAllTab = rowCount++;
        showFolderWhenForwardRow = rowCount++;
        foldersDividerRow = rowCount++;

        messageMenuHeaderRow = rowCount++;
        showDeleteRow = rowCount++;
        showNoQuoteForwardRow = rowCount++;
        showAddToSMRow = rowCount++;
        showRepeatRow = rowCount++;
        showTranslateRow = rowCount++;
        showReportRow = rowCount++;
        showMessageDetails = rowCount++;
        messageMenuDividerRow = rowCount++;
        if (CameraXUtilities.isCameraXSupported()) {
            cameraTypeHeaderRow = rowCount++;
            cameraTypeSelectorRow = rowCount++;
            if (OwlConfig.cameraType == 1) {
                cameraXOptimizeRow = rowCount++;
                cameraXFpsRow = rowCount++;
            }
            cameraAdviseRow = rowCount++;
        }
        audioVideoHeaderRow = rowCount++;
        if (AudioEnhance.isAvailable()) {
            suppressionRow = rowCount++;
        }
        betterAudioMessagesRow = rowCount++;
        turnSoundOnVDKeyRow = rowCount++;
        swipePiPRow = rowCount++;
        proximitySensorRow = rowCount++;
        rearCameraStartingRow = rowCount++;
        confirmSendRow = rowCount++;

        if (listAdapter != null && notify) {
            listAdapter.notifyDataSetChanged();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onResume() {
        super.onResume();
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
    }

    private class ListAdapter extends RecyclerListView.SelectionAdapter {
        private final Context mContext;

        public ListAdapter(Context context) {
            mContext = context;
        }

        @Override
        public int getItemCount() {
            return rowCount;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            switch (holder.getItemViewType()) {
                case 1:
                    holder.itemView.setBackground(Theme.getThemedDrawable(mContext, R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
                    break;
                case 2:
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
                case 3:
                    TextCheckCell textCheckCell = (TextCheckCell) holder.itemView;
                    textCheckCell.setEnabled(true, null);
                    if (position == mediaSwipeByTapRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("FlipMediaByTapping", R.string.FlipMediaByTapping), OwlConfig.mediaFlipByTap, true);
                    } else if (position == jumpChannelRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("JumpToNextChannel", R.string.JumpToNextChannel), OwlConfig.jumpChannel, true);
                    } else if (position == showGreetings) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("GreetingSticker", R.string.GreetingSticker), OwlConfig.showGreetings, true);
                    } else if (position == hideKeyboardRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("HideChatKeyboard", R.string.HideChatKeyboard), OwlConfig.hideKeyboard, true);
                    } else if (position == playGifAsVideoRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("GIFsAsVideo", R.string.GIFsAsVideo), OwlConfig.gifAsVideo, true);
                    } else if (position == showFolderWhenForwardRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("FoldersWhenForwarding", R.string.FoldersWhenForwarding), OwlConfig.showFolderWhenForward, true);
                    } else if (position == rearCameraStartingRow) {
                        textCheckCell.setTextAndValueAndCheck(LocaleController.getString("UseRearRoundVideos", R.string.UseRearRoundVideos), LocaleController.getString("UseRearRoundVideosDesc", R.string.UseRearRoundVideosDesc), OwlConfig.useRearCamera, true, true);
                    } else if (position == confirmSendRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("ConfirmAVMessages", R.string.ConfirmAVMessages), OwlConfig.sendConfirm, true);
                    } else if (position == showTranslateRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("TranslateMessage", R.string.TranslateMessage), OwlConfig.showTranslate, true);
                    } else if (position == showDeleteRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("ClearFromCache", R.string.ClearFromCache), OwlConfig.showDeleteDownloadedFile, true);
                    } else if (position == showAddToSMRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("AddToSavedMessages", R.string.AddToSavedMessages), OwlConfig.showSaveMessage, true);
                    } else if (position == showRepeatRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("Repeat", R.string.Repeat), OwlConfig.showRepeat, true);
                    } else if (position == showMessageDetails) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("MessageDetails", R.string.MessageDetails), OwlConfig.showMessageDetails, false);
                    } else if (position == showNoQuoteForwardRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("NoQuoteForward", R.string.NoQuoteForward), OwlConfig.showNoQuoteForward, true);
                    } else if (position == showReportRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("ReportChat", R.string.ReportChat), OwlConfig.showReportMessage, true);
                    } else if (position == scrollableRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("ScrollableChatPreview", R.string.ScrollableChatPreview), OwlConfig.scrollableChatPreview, true);
                    } else if (position == showHideAllTab) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("HideAllChatsFolder", R.string.HideAllChatsFolder), OwlConfig.hideAllTab, true);
                    } else if (position == cameraXOptimizeRow) {
                        textCheckCell.setTextAndValueAndCheck(LocaleController.getString("PerformanceMode", R.string.PerformanceMode), LocaleController.getString("PerformanceModeDesc", R.string.PerformanceModeDesc), OwlConfig.useCameraXOptimizedMode, true, true);
                    } else if (position == proximitySensorRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("DisableProximityEvents", R.string.DisableProximityEvents), OwlConfig.disableProximityEvents, true);
                    } else if (position == swipePiPRow) {
                        textCheckCell.setTextAndValueAndCheck(LocaleController.getString("SwipeToPiP", R.string.SwipeToPiP), LocaleController.getString("SwipeToPiPDesc", R.string.SwipeToPiPDesc), OwlConfig.swipeToPiP, true, true);
                    } else if (position == suppressionRow) {
                        textCheckCell.setTextAndValueAndCheck(LocaleController.getString("VoiceEnhancements", R.string.VoiceEnhancements), LocaleController.getString("VoiceEnhancementsDesc", R.string.VoiceEnhancementsDesc), OwlConfig.voicesAgc, true, true);
                    } else if (position == betterAudioMessagesRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("IncreaseVoiceMessageQuality", R.string.IncreaseVoiceMessageQuality), OwlConfig.increaseAudioMessages, true);
                    } else if (position == turnSoundOnVDKeyRow) {
                        textCheckCell.setTextAndValueAndCheck(LocaleController.getString("TurnSoundOnVDKey", R.string.TurnSoundOnVDKey), LocaleController.getString("TurnSoundOnVDKeyDesc", R.string.TurnSoundOnVDKeyDesc), OwlConfig.turnSoundOnVDKey, true, true);
                    } else if (position == openArchiveOnPullRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("OpenArchiveOnPull", R.string.OpenArchiveOnPull), OwlConfig.openArchiveOnPull, true);
                    }
                    break;
                case 6:
                    TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell) holder.itemView;
                    if (position == cameraAdviseRow) {
                        String advise;
                        switch (OwlConfig.cameraType) {
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
                        Spannable htmlParsed;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            htmlParsed = new SpannableString(Html.fromHtml(advise, Html.FROM_HTML_MODE_LEGACY));
                        }else{
                            htmlParsed = new SpannableString(Html.fromHtml(advise));
                        }
                        textInfoPrivacyCell.setText(EntitiesHelper.getUrlNoUnderlineText(htmlParsed));
                    }
                    break;
                case 7:
                    TextSettingsCell textSettingsCell = (TextSettingsCell) holder.itemView;
                    textSettingsCell.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
                    if (position == cameraXFpsRow) {
                        textSettingsCell.setTextAndValue(LocaleController.getString("MotionSmoothness", R.string.MotionSmoothness), OwlConfig.cameraXFps + " Fps", false);
                    }
                    break;
            }
        }

        @Override
        public boolean isEnabled(RecyclerView.ViewHolder holder) {
            int type = holder.getItemViewType();
            return type == 3 || type == 7;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;
            switch (viewType) {
                case 2:
                    view = new HeaderCell(mContext);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                case 3:
                    view = new TextCheckCell(mContext);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                case 4:
                    view = new StickerSizeCell(mContext, parentLayout) {
                        @Override
                        protected void onSeek() {
                            super.onSeek();
                            if (OwlConfig.stickerSizeStack != 14) {
                                menuItem.setVisibility(VISIBLE);
                            } else {
                                menuItem.setVisibility(INVISIBLE);
                            }
                        }
                    };
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                case 5:
                    view = new CameraTypeSelector(mContext) {
                        @Override
                        protected void onSelectedCamera(int cameraSelected) {
                            super.onSelectedCamera(cameraSelected);
                            if (cameraSelected == 1) {
                                updateRowsId(false);
                                listAdapter.notifyItemInserted(cameraXOptimizeRow);
                                listAdapter.notifyItemInserted(cameraXFpsRow);
                                listAdapter.notifyItemChanged(cameraAdviseRow);
                            } else {
                                listAdapter.notifyItemRemoved(cameraXOptimizeRow);
                                listAdapter.notifyItemRemoved(cameraXFpsRow);
                                listAdapter.notifyItemChanged(cameraAdviseRow - 1);
                                updateRowsId(false);
                            }
                        }
                    };
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                case 6:
                    TextInfoPrivacyCell textInfoPrivacyCell = new TextInfoPrivacyCell(mContext);
                    textInfoPrivacyCell.setBottomPadding(16);
                    view = textInfoPrivacyCell;
                    break;
                case 7:
                    view = new TextSettingsCell(mContext);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                default:
                    view = new ShadowSectionCell(mContext);
                    break;
            }
            view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
            return new RecyclerListView.Holder(view);
        }

        @Override
        public int getItemViewType(int position) {
            if(position == chatDividerRow || position == foldersDividerRow || position == messageMenuDividerRow ||
                    position == stickerSizeDividerRow) {
                return 1;
            } else if (position == chatHeaderRow || position == foldersHeaderRow || position == audioVideoHeaderRow ||
                    position == messageMenuHeaderRow || position == stickerSizeHeaderRow || position == cameraTypeHeaderRow) {
                return 2;
            } else if (position == mediaSwipeByTapRow || position == jumpChannelRow || position == hideKeyboardRow ||
                    position == playGifAsVideoRow || position == showFolderWhenForwardRow ||
                    position == rearCameraStartingRow  || position == confirmSendRow || position == showGreetings ||
                    position == showTranslateRow || position == showAddToSMRow || position == showMessageDetails ||
                    position == showNoQuoteForwardRow || position == showReportRow || position == scrollableRow ||
                    position == showDeleteRow || position == showHideAllTab || position == cameraXOptimizeRow ||
                    position == proximitySensorRow || position == swipePiPRow || position == showRepeatRow ||
                    position == suppressionRow || position == betterAudioMessagesRow || position == turnSoundOnVDKeyRow ||
                    position == openArchiveOnPullRow) {
                return 3;
            } else if (position == stickerSizeRow) {
                return 4;
            } else if (position == cameraTypeSelectorRow) {
                return 5;
            } else if (position == cameraAdviseRow) {
                return 6;
            } else if (position == cameraXFpsRow) {
                return 7;
            }
            return 1;
        }
    }
}
