package com.square.acube.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;

import com.square.acube.R;

public class OTPComponent extends AppCompatEditText {
    private float mSpace = 24; //24 dp by default, space between the lines
    private float mNumChars = 6;
    private float mLineSpacing = 8; //8dp by default, height of the text from our lines
    private int mMaxLength = 6;
    private float mLineStroke = 2;
    private Paint mLinesPaint;
    private OnClickListener mClickListener;

    private OnOtpCompleted onOtpCompleted;
    public String sentText = "";

    public OTPComponent(Context context) {
        super(context);
    }

    public OTPComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public OTPComponent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        float multi = context.getResources().getDisplayMetrics().density;
        mLineStroke = multi * mLineStroke;
        mLinesPaint = new Paint(getPaint());
        mLinesPaint.setStyle(Paint.Style.STROKE);
        mLinesPaint.setStrokeWidth(mLineStroke);
        mLinesPaint.setColor(getResources().getColor(R.color.green));
        setBackgroundResource(0);
        mSpace = multi * mSpace; //convert to pixels for our density
        mLineSpacing = multi * mLineSpacing; //convert to pixels for our density
        mNumChars = mMaxLength;

        super.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // When tapped, move cursor to end of text.
                setSelection(getText().length());
                if (mClickListener != null) {
                    mClickListener.onClick(v);
                }
            }
        });
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        mClickListener = l;
    }

    @Override
    public void setCustomSelectionActionModeCallback(ActionMode.Callback actionModeCallback) {
        throw new RuntimeException("setCustomSelectionActionModeCallback() not supported.");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int availableWidth = getWidth() - getPaddingRight() - getPaddingLeft();
        float mCharSize;
        if (mSpace < 0) {
            mCharSize = (availableWidth / (mNumChars * 2 - 1));
        } else {
            mCharSize = (availableWidth - (mSpace * (mNumChars - 1))) / mNumChars;
        }

        int startX = getPaddingLeft();
        int bottom = getHeight() - getPaddingBottom();

        //Text Width
        Editable text = getText();
        int textLength = text.length();
        float[] textWidths = new float[textLength];
        getPaint().getTextWidths(getText(), 0, textLength, textWidths);

        if (textLength == 6 && onOtpCompleted != null && !sentText.equals(getText().toString())) {
            onOtpCompleted.getOtp(getText().toString());
            sentText = getText().toString();
        }
        setSelection(getText().length());

        for (int i = 0; i < mNumChars; i++) {
            canvas.drawRect(startX, bottom - 80, startX + mCharSize, bottom, mLinesPaint);
            if (getText().length() > i) {
                float middle = startX + mCharSize / 2;
                canvas.drawText(text, i, i + 1, middle - textWidths[0] / 2, bottom - mLineSpacing, getPaint());
            }
            if (mSpace < 0) {
                startX += mCharSize * 2;
            } else {
                startX += mCharSize + mSpace;
            }
        }
    }

    public void setOnOtpCompleted(OnOtpCompleted onOtpCompleted) {
        this.onOtpCompleted = onOtpCompleted;
    }

    public interface OnOtpCompleted {
        public void getOtp(String otp);
    }
}