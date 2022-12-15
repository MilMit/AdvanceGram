package milmit.advancegram.messenger.components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.RLottieDrawable;
import org.telegram.ui.Components.RLottieImageView;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import milmit.advancegram.messenger.components.dynamic.BaseButtonCell;
import milmit.advancegram.messenger.components.dynamic.ButtonCell;
import milmit.advancegram.messenger.components.dynamic.ThemeInfo;

@SuppressLint("ViewConstructor")
public class ActionPanelCell extends LinearLayout {
    private final Theme.ResourcesProvider resourcesProvider;
    private OnItemClickListener onItemClickListener;

    public ActionPanelCell(Context context, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        this.resourcesProvider = resourcesProvider;
        setPadding(AndroidUtilities.dp(8), AndroidUtilities.dp(8), AndroidUtilities.dp(8), 0);
        setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        setGravity(Gravity.CENTER_HORIZONTAL);
    }

    @Override
    public void invalidate() {
        super.invalidate();
        getButtons().forEach(BaseButtonCell::updateColors);
    }

    public void addItem(String text, int icon, String color) {
        addView(getButton(getContext(), text, icon, color));
    }

    public void clear() {
        removeAllViews();
    }

    public BaseButtonCell getButton(Context context, String text, int iconId, String color) {
        BaseButtonCell buttonCell = ButtonCell.getCurrentButtonCell(context, resourcesProvider, text, iconId, color);
        int buttonId = (int) getButtons().count();
        buttonCell.setOnClickDelegate(() -> {
            if (onItemClickListener != null) onItemClickListener.onItemClick(buttonId);
        });
        return buttonCell;
    }

    private Stream<BaseButtonCell> getButtons() {
        return IntStream.iterate(0, i -> i + 1)
                .limit(getChildCount())
                .mapToObj(this::getChildAt)
                .filter(child -> child instanceof BaseButtonCell)
                .map(child -> (BaseButtonCell) child);
    }

    public RLottieDrawable getPhotoAnimationDrawable() {
        return getButtons().findFirst().map(BaseButtonCell::getAnimatedDrawable).orElse(null);
    }

    public RLottieImageView getPhotoImageView() {
        return getButtons().findFirst().map(BaseButtonCell::getLottieImageView).orElse(null);
    }

    public ThemeInfo getTheme() {
        return getButtons().findFirst().map(BaseButtonCell::getTheme).orElse(null);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int id);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.EXACTLY), heightMeasureSpec);
    }
}
