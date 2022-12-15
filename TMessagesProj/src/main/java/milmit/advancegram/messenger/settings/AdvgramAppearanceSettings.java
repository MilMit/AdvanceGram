package milmit.advancegram.messenger.settings;

import android.transition.TransitionManager;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Cells.TextCheckCell;

import milmit.advancegram.messenger.AdvanceGramConfig;
import milmit.advancegram.messenger.components.BlurIntensityCell;
import milmit.advancegram.messenger.components.DrawerProfilePreviewCell;
import milmit.advancegram.messenger.components.DynamicButtonSelector;
import milmit.advancegram.messenger.components.ThemeSelectorDrawerCell;

public class AdvgramAppearanceSettings extends BaseSettingsActivity {
    private DrawerProfilePreviewCell profilePreviewCell;

    private int drawerRow;
    private int drawerAvatarAsBackgroundRow;
    private int showGradientRow;
    private int showAvatarRow;
    private int drawerDarkenBackgroundRow;
    private int drawerBlurBackgroundRow;
    private int drawerDividerRow;
    private int editBlurHeaderRow;
    private int editBlurRow;
    private int editBlurDividerRow;
    private int themeDrawerHeader;
    private int themeDrawerRow;
    private int themeDrawerDividerRow;
    private int menuItemsRow;
    private int dynamicButtonHeaderRow;
    private int dynamicButtonRow;
    private int dynamicDividerRow;
    private int fontsAndEmojiHeaderRow;
    private int useSystemFontRow;
    private int useSystemEmojiRow;
    private int fontsAndEmojiDividerRow;
    private int appearanceHeaderRow;
    private int forcePacmanRow;
    private int showSantaHatRow;
    private int showFallingSnowRow;
    private int messageTimeSwitchRow;
    private int roundedNumberSwitchRow;
    private int smartButtonsRow;
    private int appBarShadowRow;
    private int slidingTitleRow;
    private int searchIconInActionBarRow;
    private int appearanceDividerRow;
    private int showPencilIconRow;
    private int showInActionBarRow;

    @Override
    protected String getActionBarTitle() {
        return LocaleController.getString("Appearance", R.string.Appearance);
    }

    @Override
    protected void onItemClick(View view, int position, float x, float y) {
        if (position == showAvatarRow) {
            AdvanceGramConfig.toggleShowAvatarImage();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(AdvanceGramConfig.showAvatarImage);
            }
            reloadMainInfo();
            listAdapter.notifyItemChanged(drawerRow, PARTIAL);
        } else if (position == showGradientRow) {
            AdvanceGramConfig.toggleShowGradientColor();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(AdvanceGramConfig.showGradientColor);
            }
            reloadMainInfo();
            listAdapter.notifyItemChanged(drawerRow, PARTIAL);
        } else if (position == drawerDarkenBackgroundRow) {
            AdvanceGramConfig.toggleAvatarBackgroundDarken();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(AdvanceGramConfig.avatarBackgroundDarken);
            }
            reloadMainInfo();
            listAdapter.notifyItemChanged(drawerRow, PARTIAL);
        } else if (position == drawerBlurBackgroundRow) {
            AdvanceGramConfig.toggleAvatarBackgroundBlur();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(AdvanceGramConfig.avatarBackgroundBlur);
            }
            reloadMainInfo();
            listAdapter.notifyItemChanged(drawerRow, PARTIAL);
            if (AdvanceGramConfig.avatarBackgroundBlur) {
                listAdapter.notifyItemRangeInserted(drawerDividerRow, 3);
            } else {
                listAdapter.notifyItemRangeRemoved(drawerDividerRow, 3);
            }
            updateRowsId();
        } else if (position == drawerAvatarAsBackgroundRow) {
            AdvanceGramConfig.toggleAvatarAsDrawerBackground();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(AdvanceGramConfig.avatarAsDrawerBackground);
            }
            reloadMainInfo();
            TransitionManager.beginDelayedTransition(profilePreviewCell);
            listAdapter.notifyItemChanged(drawerRow, PARTIAL);
            if (AdvanceGramConfig.avatarAsDrawerBackground) {
                updateRowsId();
                listAdapter.notifyItemRangeInserted(showGradientRow, 4 + (AdvanceGramConfig.avatarBackgroundBlur ? 3 : 0));
            } else {
                listAdapter.notifyItemRangeRemoved(showGradientRow, 4 + (AdvanceGramConfig.avatarBackgroundBlur ? 3 : 0));
                updateRowsId();
            }
        } else if (position == menuItemsRow) {
            presentFragment(new DrawerOrderSettings());
        } else if (position == useSystemFontRow) {
            AdvanceGramConfig.toggleUseSystemFont();
            AndroidUtilities.clearTypefaceCache();
            rebuildAllFragmentsWithLast();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(AdvanceGramConfig.useSystemFont);
            }
        } else if (position == useSystemEmojiRow) {
            AdvanceGramConfig.toggleUseSystemEmoji();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(AdvanceGramConfig.useSystemEmoji);
            }
        } else if (position == forcePacmanRow) {
            AdvanceGramConfig.togglePacmanForced();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(AdvanceGramConfig.pacmanForced);
            }
        } else if (position == smartButtonsRow) {
            AdvanceGramConfig.toggleSmartButtons();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(AdvanceGramConfig.smartButtons);
            }
        } else if (position == appBarShadowRow) {
            AdvanceGramConfig.toggleAppBarShadow();
            parentLayout.setHeaderShadow(AdvanceGramConfig.disableAppBarShadow ? null : parentLayout.getView().getResources().getDrawable(R.drawable.header_shadow).mutate());
            rebuildAllFragmentsWithLast();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(AdvanceGramConfig.disableAppBarShadow);
            }
        } else if (position == showSantaHatRow) {
            AdvanceGramConfig.toggleShowSantaHat();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(AdvanceGramConfig.showSantaHat);
            }
            Theme.lastHolidayCheckTime = 0;
            Theme.dialogs_holidayDrawable = null;
            reloadMainInfo();
        } else if (position == showFallingSnowRow) {
            AdvanceGramConfig.toggleShowSnowFalling();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(AdvanceGramConfig.showSnowFalling);
            }
            Theme.lastHolidayCheckTime = 0;
            Theme.dialogs_holidayDrawable = null;
            reloadMainInfo();
        } else if (position == slidingTitleRow) {
            AdvanceGramConfig.toggleSlidingChatTitle();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(AdvanceGramConfig.slidingChatTitle);
            }
        } else if (position == messageTimeSwitchRow) {
            AdvanceGramConfig.toggleFullTime();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(AdvanceGramConfig.fullTime);
            }
            LocaleController.getInstance().recreateFormatters();
        } else if (position == roundedNumberSwitchRow) {
            AdvanceGramConfig.toggleRoundedNumbers();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(AdvanceGramConfig.roundedNumbers);
            }
        } else if (position == searchIconInActionBarRow) {
            AdvanceGramConfig.toggleSearchIconInActionBar();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(AdvanceGramConfig.searchIconInActionBar);
            }
        } else if (position == showPencilIconRow) {
            AdvanceGramConfig.toggleShowPencilIcon();
            parentLayout.rebuildAllFragmentViews(false, false);
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(AdvanceGramConfig.showPencilIcon);
            }
        } else if (position == showInActionBarRow) {
            AdvanceGramConfig.toggleShowNameInActionBar();
            reloadDialogs();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(AdvanceGramConfig.showNameInActionBar);
            }
        }
    }

    @Override
    protected void updateRowsId() {
        super.updateRowsId();
        showGradientRow = -1;
        showAvatarRow = -1;
        drawerDarkenBackgroundRow = -1;
        drawerBlurBackgroundRow = -1;
        editBlurHeaderRow = -1;
        editBlurRow = -1;
        editBlurDividerRow = -1;
        showSantaHatRow = -1;
        showFallingSnowRow = -1;

        drawerRow = rowCount++;
        drawerAvatarAsBackgroundRow = rowCount++;
        if (AdvanceGramConfig.avatarAsDrawerBackground) {
            showGradientRow = rowCount++;
            showAvatarRow = rowCount++;
            drawerDarkenBackgroundRow = rowCount++;
            drawerBlurBackgroundRow = rowCount++;
        }
        drawerDividerRow = rowCount++;
        if (AdvanceGramConfig.avatarBackgroundBlur && AdvanceGramConfig.avatarAsDrawerBackground) {
            editBlurHeaderRow = rowCount++;
            editBlurRow = rowCount++;
            editBlurDividerRow = rowCount++;
        }

        themeDrawerHeader = rowCount++;
        themeDrawerRow = rowCount++;
        menuItemsRow = rowCount++;
        themeDrawerDividerRow = rowCount++;

        dynamicButtonHeaderRow = rowCount++;
        dynamicButtonRow = rowCount++;
        dynamicDividerRow = rowCount++;

        fontsAndEmojiHeaderRow = rowCount++;
        useSystemFontRow = rowCount++;
        useSystemEmojiRow = rowCount++;
        fontsAndEmojiDividerRow = rowCount++;

        appearanceHeaderRow = rowCount++;
        forcePacmanRow = rowCount++;
        if (((Theme.getEventType() == 0 && AdvanceGramConfig.eventType == 0) || AdvanceGramConfig.eventType == 1)) {
            showSantaHatRow = rowCount++;
            showFallingSnowRow = rowCount++;
        }
        messageTimeSwitchRow = rowCount++;
        roundedNumberSwitchRow = rowCount++;
        showPencilIconRow = rowCount++;
        smartButtonsRow = rowCount++;
        appBarShadowRow = rowCount++;
        slidingTitleRow = rowCount++;
        showInActionBarRow = rowCount++;
        searchIconInActionBarRow = rowCount++;
        appearanceDividerRow = rowCount++;
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
                    if (position == editBlurHeaderRow) {
                        headerCell.setText(LocaleController.getString("BlurIntensity", R.string.BlurIntensity));
                    } else if (position == themeDrawerHeader) {
                        headerCell.setText(LocaleController.getString("SideBarIconSet", R.string.SideBarIconSet));
                    } else if (position == dynamicButtonHeaderRow) {
                        headerCell.setText(LocaleController.getString("ButtonShape", R.string.ButtonShape));
                    } else if (position == fontsAndEmojiHeaderRow) {
                        headerCell.setText(LocaleController.getString("FontsAndEmojis", R.string.FontsAndEmojis));
                    } else if (position == appearanceHeaderRow) {
                        headerCell.setText(LocaleController.getString("Appearance", R.string.Appearance));
                    }
                    break;
                case SWITCH:
                    TextCheckCell textCheckCell = (TextCheckCell) holder.itemView;
                    if (position == showGradientRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("ShadeBackground", R.string.ShadeBackground), AdvanceGramConfig.showGradientColor, true);
                    } else if (position == showAvatarRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("ShowAvatar", R.string.ShowAvatar), AdvanceGramConfig.showAvatarImage, drawerBlurBackgroundRow != -1);
                    } else if (position == drawerAvatarAsBackgroundRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("AvatarAsBackground", R.string.AvatarAsBackground), AdvanceGramConfig.avatarAsDrawerBackground, AdvanceGramConfig.avatarAsDrawerBackground);
                    } else if (position == drawerBlurBackgroundRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("AvatarBlur", R.string.AvatarBlur), AdvanceGramConfig.avatarBackgroundBlur, !AdvanceGramConfig.avatarBackgroundBlur);
                    } else if (position == drawerDarkenBackgroundRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("AvatarDarken", R.string.AvatarDarken), AdvanceGramConfig.avatarBackgroundDarken, true);
                    } else if (position == useSystemFontRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("UseSystemFonts", R.string.UseSystemFonts), AdvanceGramConfig.useSystemFont, true);
                    } else if (position == useSystemEmojiRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("UseSystemEmojis", R.string.UseSystemEmojis), AdvanceGramConfig.useSystemEmoji, true);
                    } else if (position == messageTimeSwitchRow) {
                        textCheckCell.setTextAndValueAndCheck(LocaleController.getString("FormatTimeSeconds", R.string.FormatTimeSeconds), LocaleController.getString("FormatTimeSecondsDesc", R.string.FormatTimeSecondsDesc), AdvanceGramConfig.fullTime, true, true);
                    } else if (position == roundedNumberSwitchRow) {
                        textCheckCell.setTextAndValueAndCheck(LocaleController.getString("NumberRounding", R.string.NumberRounding), LocaleController.getString("NumberRoundingDesc", R.string.NumberRoundingDesc), AdvanceGramConfig.roundedNumbers, true, true);
                    } else if (position == forcePacmanRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("PacManAnimation", R.string.PacManAnimation), AdvanceGramConfig.pacmanForced, true);
                    } else if (position == smartButtonsRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("ShortcutsForAdmins", R.string.ShortcutsForAdmins), AdvanceGramConfig.smartButtons, true);
                    } else if (position == appBarShadowRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("AppBarShadow", R.string.AppBarShadow), AdvanceGramConfig.disableAppBarShadow, true);
                    } else if (position == showSantaHatRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("ChristmasHat", R.string.ChristmasHat), AdvanceGramConfig.showSantaHat, true);
                    } else if (position == showFallingSnowRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("FallingSnow", R.string.FallingSnow), AdvanceGramConfig.showSnowFalling, true);
                    } else if (position == slidingTitleRow) {
                        textCheckCell.setTextAndValueAndCheck(LocaleController.getString("SlidingTitle", R.string.SlidingTitle), LocaleController.getString("SlidingTitleDesc", R.string.SlidingTitleDesc), AdvanceGramConfig.slidingChatTitle, true, true);
                    } else if (position == searchIconInActionBarRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("SearchIconTitleBar", R.string.SearchIconTitleBar), AdvanceGramConfig.searchIconInActionBar, true);
                    } else if (position == showPencilIconRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("ShowPencilIcon", R.string.ShowPencilIcon), AdvanceGramConfig.showPencilIcon, true);
                    } else if (position == showInActionBarRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("AccountNameTitleBar", R.string.AccountNameTitleBar), AdvanceGramConfig.showNameInActionBar, true);
                    }
                    break;
                case PROFILE_PREVIEW:
                    DrawerProfilePreviewCell cell = (DrawerProfilePreviewCell) holder.itemView;
                    cell.setUser(getUserConfig().getCurrentUser(), false);
                    break;
                case TEXT_CELL:
                    TextCell textCell = (TextCell) holder.itemView;
                    if (position == menuItemsRow) {
                        textCell.setColors(Theme.key_windowBackgroundWhiteBlueText4, Theme.key_windowBackgroundWhiteBlueText4);
                        textCell.setTextAndIcon(LocaleController.getString("MenuItems", R.string.MenuItems), R.drawable.msg_newfilter, false);
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
                case PROFILE_PREVIEW:
                    view = profilePreviewCell = new DrawerProfilePreviewCell(context);
                    view.setBackground(Theme.getThemedDrawable(context, R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
                    break;
                case BLUR_INTENSITY:
                    view = new BlurIntensityCell(context) {
                        @Override
                        protected void onBlurIntensityChange(int percentage, boolean layout) {
                            super.onBlurIntensityChange(percentage, layout);
                            AdvanceGramConfig.saveBlurIntensity(percentage);
                            RecyclerView.ViewHolder holder = listView.findViewHolderForAdapterPosition(editBlurRow);
                            if (holder != null && holder.itemView instanceof BlurIntensityCell) {
                                BlurIntensityCell cell = (BlurIntensityCell) holder.itemView;
                                if (layout) {
                                    cell.requestLayout();
                                } else {
                                    cell.invalidate();
                                }
                            }
                            reloadMainInfo();
                            listAdapter.notifyItemChanged(drawerRow, PARTIAL);
                        }
                    };
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                case THEME_SELECTOR:
                    view = new ThemeSelectorDrawerCell(context, AdvanceGramConfig.eventType) {
                        @Override
                        protected void onSelectedEvent(int eventSelected) {
                            super.onSelectedEvent(eventSelected);
                            int previousEvent = AdvanceGramConfig.eventType;
                            AdvanceGramConfig.saveEventType(eventSelected);
                            if (previousEvent == 1 && eventSelected != 1) {
                                listAdapter.notifyItemRangeRemoved(forcePacmanRow + 1, 2);
                            } else if (previousEvent != 1 && eventSelected == 1) {
                                listAdapter.notifyItemRangeInserted(forcePacmanRow + 1, 2);
                            }
                            listAdapter.notifyItemChanged(drawerRow, PARTIAL);
                            Theme.lastHolidayCheckTime = 0;
                            Theme.dialogs_holidayDrawable = null;
                            reloadMainInfo();
                            updateRowsId();
                        }
                    };
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                case DYNAMIC_BUTTON_SELECTOR:
                    view = new DynamicButtonSelector(context) {
                        @Override
                        protected void onSelectionChange() {
                            super.onSelectionChange();
                            reloadInterface();
                        }
                    };
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
            }
            return view;
        }

        @Override
        public ViewType getViewType(int position) {
            if (position == drawerDividerRow || position == editBlurDividerRow || position == themeDrawerDividerRow ||
                    position == dynamicDividerRow || position == fontsAndEmojiDividerRow || position == appearanceDividerRow) {
                return ViewType.SHADOW;
            } else if (position == editBlurHeaderRow || position == themeDrawerHeader || position == dynamicButtonHeaderRow ||
                    position == fontsAndEmojiHeaderRow || position == appearanceHeaderRow) {
                return ViewType.HEADER;
            } else if (position == roundedNumberSwitchRow || position == messageTimeSwitchRow ||
                    position == useSystemFontRow || position == useSystemEmojiRow || position == drawerAvatarAsBackgroundRow ||
                    position == drawerDarkenBackgroundRow || position == drawerBlurBackgroundRow || position == showGradientRow ||
                    position == showAvatarRow || position == forcePacmanRow || position == smartButtonsRow ||
                    position == appBarShadowRow || position == showSantaHatRow || position == showFallingSnowRow ||
                    position == slidingTitleRow || position == searchIconInActionBarRow || position == showPencilIconRow ||
                    position == showInActionBarRow) {
                return ViewType.SWITCH;
            } else if (position == drawerRow) {
                return ViewType.PROFILE_PREVIEW;
            } else if (position == editBlurRow) {
                return ViewType.BLUR_INTENSITY;
            } else if (position == menuItemsRow) {
                return ViewType.TEXT_CELL;
            } else if (position == themeDrawerRow) {
                return ViewType.THEME_SELECTOR;
            } else if (position == dynamicButtonRow) {
                return ViewType.DYNAMIC_BUTTON_SELECTOR;
            }
            throw new IllegalArgumentException("Invalid position");
        }
    }
}
