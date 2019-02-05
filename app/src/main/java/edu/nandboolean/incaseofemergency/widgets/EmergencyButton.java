package edu.nandboolean.incaseofemergency.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import edu.nandboolean.incaseofemergency.utils.ICEEventLogger;
import edu.nandboolean.incaseofemergency.R;

public class EmergencyButton extends android.support.v7.widget.AppCompatButton
        implements View.OnClickListener {

    private int tapsToRespond;
    private ICEEventLogger tapEventLogger;
    private MultiTapListener listener;

    public EmergencyButton(Context context) {
        super(context);
        this.tapsToRespond = 1;
        tapEventLogger = new ICEEventLogger();
        super.setOnClickListener(this);
    }

    public EmergencyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initializeTapCount(context, attrs);
        tapEventLogger = new ICEEventLogger();
        super.setOnClickListener(this);
    }

    public EmergencyButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initializeTapCount(context, attrs);
        tapEventLogger = new ICEEventLogger();
        super.setOnClickListener(this);
    }

    private void initializeTapCount(Context context, AttributeSet attrs) {
        TypedArray customAttrs = context.obtainStyledAttributes(attrs, R.styleable.EmergencyButton);
        try {
            this.tapsToRespond = customAttrs.getInteger(R.styleable.EmergencyButton_tap_count, 1);
        } finally {
            customAttrs.recycle();
        }
    }

    @Override
    public void setOnClickListener(View.OnClickListener l) {
        throw new UnsupportedOperationException("OnClickListener is not supported for this custom view. Use setOnMultiTapListener(MultiTapResponseButton.MultiTapListener listener), instead.");
    }

    public void setOnMultiTapListener(MultiTapListener multitapListener) {
        this.listener = multitapListener;
    }

    @Override
    public void onClick(View v) {
        tapEventLogger.logEvent();

        if (tapEventLogger.getCurrentEventCount() == this.tapsToRespond) {
            if (this.listener != null) {
                this.listener.onMultiTap(v);
            }
            tapEventLogger.clear();
        }
    }

    public interface MultiTapListener {
        void onMultiTap(View view);
    }
}
