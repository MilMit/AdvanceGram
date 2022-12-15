package milmit.advancegram.messenger.components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.TextStyleSpan;

import java.util.Locale;
import java.util.Objects;

import milmit.advancegram.messenger.helpers.DCHelper;

public class DatacenterStatusCell extends LinearLayout {

    private final RadialProgressView radialProgressView;
    private final TextView textView;
    private final TextView ipTextView;
    private final TextView statusTextView;
    private final ImageView imageView;
    private final ShimmerFrameLayout shimmerFrameLayout;
    private final LinearLayout linearLayout;
    private boolean needDivider = false;

    @SuppressLint("SetTextI18n")
    public DatacenterStatusCell(Context context) {
        super(context);
        setGravity(Gravity.CENTER_VERTICAL);
        setPadding(AndroidUtilities.dp(13), AndroidUtilities.dp(5), AndroidUtilities.dp(13), AndroidUtilities.dp(5));
        RelativeLayout relativeLayout = new RelativeLayout(context);
        radialProgressView = new RadialProgressView(
                context,
                Color.TRANSPARENT
        );
        imageView = new ImageView(context);
        relativeLayout.addView(radialProgressView, LayoutHelper.createRelative(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        relativeLayout.addView(imageView, LayoutHelper.createRelative(25, 25, RelativeLayout.CENTER_IN_PARENT));
        addView(relativeLayout, LayoutHelper.createLinear(65, 65));

        linearLayout = new LinearLayout(context);
        linearLayout.setGravity(Gravity.CENTER_VERTICAL);
        linearLayout.setOrientation(VERTICAL);
        linearLayout.setVisibility(GONE);
        addView(linearLayout, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        textView = new TextView(context);
        textView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
        textView.setTextSize(16);
        linearLayout.addView(textView, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, LocaleController.isRTL ? Gravity.RIGHT : Gravity.LEFT, 10, 0, 0, 0));

        ipTextView = new TextView(context);
        ipTextView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText2));
        ipTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
        ipTextView.setGravity(LocaleController.isRTL ? Gravity.RIGHT : Gravity.LEFT);
        ipTextView.setImportantForAccessibility(IMPORTANT_FOR_ACCESSIBILITY_NO);
        linearLayout.addView(ipTextView, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, LocaleController.isRTL ? Gravity.RIGHT : Gravity.LEFT, 10, 0, 0, 0));

        statusTextView = new TextView(context);
        statusTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
        statusTextView.setGravity(LocaleController.isRTL ? Gravity.RIGHT : Gravity.LEFT);
        statusTextView.setImportantForAccessibility(IMPORTANT_FOR_ACCESSIBILITY_NO);
        linearLayout.addView(statusTextView, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, LocaleController.isRTL ? Gravity.RIGHT : Gravity.LEFT, 10, 0, 0, 0));

        shimmerFrameLayout = new ShimmerFrameLayout(context);
        Shimmer.AlphaHighlightBuilder shimmer = new Shimmer.AlphaHighlightBuilder();
        shimmer.setBaseAlpha(0.05f);
        shimmer.setHighlightAlpha(0.1f);
        shimmer.setDuration(1500);
        shimmerFrameLayout.setShimmer(shimmer.build());
        addView(shimmerFrameLayout, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        LinearLayout linearLayoutLoading = new LinearLayout(context);
        linearLayoutLoading.setGravity(Gravity.CENTER_VERTICAL);
        linearLayoutLoading.setOrientation(VERTICAL);
        shimmerFrameLayout.addView(linearLayoutLoading, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        for (int i = 0; i < 3; i++) {
            int radius = i == 0 ? 7 : 5;
            int w;
            switch (i) {
                case 0:
                    w = 160;
                    break;
                case 1:
                    w = 90;
                    break;
                default:
                    w = 110;
                    break;
            }
            CardView cardView = new CardView(context);
            cardView.setCardElevation(0);
            cardView.setRadius(AndroidUtilities.dp(radius));
            cardView.setCardBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
            linearLayoutLoading.addView(cardView, LayoutHelper.createLinear(w, radius * 2, 10, i != 0 ? 9 : 0, 0, 0));
        }
    }

    public void setData(int dcId, int ping, int status, boolean needDivider) {
        this.needDivider = needDivider;
        Drawable d = ContextCompat.getDrawable(getContext(), DCHelper.getDcIcon(dcId));
        Objects.requireNonNull(d).setColorFilter(Theme.getColor(Theme.key_windowBackgroundWhite), PorterDuff.Mode.SRC_ATOP);
        imageView.setImageBitmap(drawableToBitmap(d));
        imageView.setBackgroundResource(DCHelper.getDcIcon(dcId));
        radialProgressView.setColor(DCHelper.getDCColor(dcId));
        if (status == -1) {
            shimmerFrameLayout.setVisibility(VISIBLE);
            linearLayout.setVisibility(GONE);
        } else {
            shimmerFrameLayout.setVisibility(GONE);
            linearLayout.setVisibility(VISIBLE);
            TextStyleSpan.TextStyleRun run = new TextStyleSpan.TextStyleRun();
            run.flags |= TextStyleSpan.FLAG_STYLE_BOLD;
            TextStyleSpan mSpan = new TextStyleSpan(run);
            String DC_NAME = DCHelper.getDcName(dcId);
            if (dcId != -1) {
                DC_NAME = String.format(Locale.ENGLISH, "%s - DC%d", DC_NAME, dcId);
            }
            SpannableString spannableString = new SpannableString(DC_NAME);
            spannableString.setSpan(mSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setGravity(LocaleController.isRTL ? Gravity.RIGHT : Gravity.LEFT);
            textView.setImportantForAccessibility(IMPORTANT_FOR_ACCESSIBILITY_NO);
            textView.setText(spannableString);
            ipTextView.setText(DCHelper.getDCIp(dcId));
            String statusText;
            String colorKey;
            if (status == 0) {
                statusText = LocaleController.getString("Unavailable", R.string.Unavailable);
                colorKey = Theme.key_windowBackgroundWhiteRedText4;
            } else if (status == 1) {
                statusText = LocaleController.getString("Available", R.string.Available);
                colorKey = Theme.key_windowBackgroundWhiteGreenText;
            } else {
                statusText = LocaleController.getString("SpeedSlow", R.string.SpeedSlow);
                colorKey = Theme.key_statisticChartLine_orange;
            }
            statusText += ", " + LocaleController.formatString("Ping", R.string.Ping, ping);
            statusTextView.setText(statusText);
            statusTextView.setTextColor(Theme.getColor(colorKey));
        }
    }

    public static Bitmap drawableToBitmap(@NonNull Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (needDivider) {
            canvas.drawLine(
                    LocaleController.isRTL ? 0 : AndroidUtilities.dp(20),
                    getMeasuredHeight() - 1,
                    getMeasuredWidth() - (LocaleController.isRTL ? AndroidUtilities.dp(20) : 0),
                    getMeasuredHeight() - 1,
                    Theme.dividerPaint
            );
        }
    }
}
