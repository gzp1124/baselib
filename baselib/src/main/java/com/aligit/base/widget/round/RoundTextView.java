package com.aligit.base.widget.round;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;


/**
 * <pre>
 * author : gzp1124
 *
 * @version : 1.0
 * @date : 2020/9/11
 * @description : 用于需要圆角矩形框背景的TextView的情况,减少直接使用TextView时引入的shape资源文件
 * </pre>
 */
public class RoundTextView extends AppCompatTextView {
    private RoundViewDelegate delegate;

    public RoundTextView(Context context) {
        super(context);
        setIncludeFontPadding(false);
        delegate = new RoundViewDelegate(this, context, null);
    }

    public RoundTextView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        setIncludeFontPadding(false);
        delegate = new RoundViewDelegate(this, context, attrs);
    }

    public RoundTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setIncludeFontPadding(false);
        delegate = new RoundViewDelegate(this, context, attrs);
    }

    public RoundViewDelegate getDelegate() {
        return delegate;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (delegate.isWidthHeightEqual() && getWidth() > 0 && getHeight() > 0) {
            int max = Math.max(getWidth(), getHeight());
            int measureSpec = MeasureSpec.makeMeasureSpec(max, MeasureSpec.EXACTLY);
            super.onMeasure(measureSpec, measureSpec);
            return;
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void dispatchSetSelected(boolean selected) {
        super.dispatchSetSelected(selected);
        if (selChangeListener != null) selChangeListener.onSelectedChanged(delegate,selected);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (delegate.isRadiusHalfHeight()) {
            delegate.setCornerRadius(getHeight() / 2);
        } else {
            delegate.setBgSelector();
        }
    }

    private  TextViewSelectedChangeListener selChangeListener;
    public void setSelChangeListener(TextViewSelectedChangeListener listener){
        selChangeListener = listener;
    }
}
