package com.aligit.base.widget.progress;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.ColorInt;
import androidx.annotation.IntDef;
import androidx.annotation.Nullable;

import com.aligit.base.R;
import com.elvishew.xlog.XLog;

import static me.jessyan.autosize.utils.AutoSizeUtils.dp2px;
import static me.jessyan.autosize.utils.AutoSizeUtils.sp2px;

/**
 * <pre>
 * author : gzp1124
 * time   : 2020/6/19
 * desc   :
 *
 | Attribute              | Description                                    |
 | ---------------------- | ---------------------------------------------- |
 | start_progress         | 起始进度                                       |
 | end_progress           | 终止进度                                       |
 | start_color            | 渐变效果的起始颜色                             |
 | end_color              | 渐变效果的终止颜色                             |
 | isTracked              | 是否显示轨迹背景                               |
 | track_width            | 进度条的宽度（边界宽度）                       |
 | trackColor             | 轨迹背景的颜色                                 |
 | progressTextVisibility | 是否显示进度值文本                             |
 | progressTextColor      | 进度值的颜色                                   |
 | progressTextSize       | 进度值的文本字体大小                           |
 | progressDuration       | 动画时长                                       |
 | animateType            | 动画类型（可以参考属性动画的TimeInterpolator） |

 | Attribute           | Description                    |
 | ------------------- | ------------------------------ |
 | corner_radius       | 圆角半径                       |
 | text_padding_bottom | 文字距离view的padding          |
 | textMovedEnable     | 设置进度值是否跟随控件动画移动 |

 * </pre>
 */
public class HorizontalProgressView extends View {
    @IntDef({ACCELERATE_DECELERATE_INTERPOLATOR, LINEAR_INTERPOLATOR, ACCELERATE_INTERPOLATOR, DECELERATE_INTERPOLATOR, OVERSHOOT_INTERPOLATOR})
    private @interface AnimateType {

    }

    /**
     * animation types supported
     */
    public static final int ACCELERATE_DECELERATE_INTERPOLATOR = 0;
    public static final int LINEAR_INTERPOLATOR = 1;
    public static final int ACCELERATE_INTERPOLATOR = 2;
    public static final int DECELERATE_INTERPOLATOR = 3;
    public static final int OVERSHOOT_INTERPOLATOR = 4;

    private static final String TAG = "Moos-Progress-View";

    /**
     * properties needed
     */
    private Context mContext;
    /**
     * the type of animation
     */
    private int mAnimateType = 0;
    /**
     * the progress of start point
     */
    private float mStartProgress = 0;
    /**
     * the progress of end point
     */
    private float mEndProgress = 60;
    /**
     * the color of start progress
     */
    private int mStartColor = getResources().getColor(R.color.light_orange);
    /**
     * the color of end progress
     */
    private int mEndColor = getResources().getColor(R.color.dark_orange);
    /**
     * has track of moving or not
     */
    private boolean trackEnabled = false;
    /**
     * the stroke width of progress
     */
    private int mTrackWidth = 6;
    /**
     * the size of inner text
     */
    private int mProgressTextSize = 48;
    /**
     * the color of inner text
     */
    private int mProgressTextColor = getResources().getColor(R.color.colorAccent);
    /**
     * the color of progress track
     */
    private int mTrackColor = getResources().getColor(R.color.default_track_color);
    /**
     * the duration of progress moving
     */
    private int mProgressDuration = 1200;
    /**
     * show the inner text or not
     */
    private boolean textVisibility = true;
    /**
     * the round rect corner radius
     */
    private int mCornerRadius = 30;
    /**
     * the offset of text padding bottom
     */
    private int mTextPaddingBottomOffset = 5;
    /**
     * moving the text with progress or not
     */
    private boolean isTextMoved = true;

    /**
     * the animator of progress moving
     */
    private ObjectAnimator progressAnimator;
    /**
     * the progress of moving
     */
    private float moveProgress = 0;
    /**
     * the paint of drawing progress
     */
    private Paint progressPaint;
    /**
     * the gradient of color
     */
    private LinearGradient mShader;
    /**
     * the oval's rect shape
     */
    private RectF mRect;
    private RectF mTrackRect;
    private Paint mTextPaint;
    private Interpolator mInterpolator;
    private HorizontalProgressView.HorizontalProgressUpdateListener animatorUpdateListener;


    public HorizontalProgressView(Context context) {
        super(context);
        this.mContext = context;
        obtainAttrs(context, null);
        init();
    }

    public HorizontalProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        obtainAttrs(context, attrs);
        init();
    }

    public HorizontalProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        obtainAttrs(context, attrs);
        init();
    }

    private void obtainAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HorizontalProgressView);

        mStartProgress = typedArray.getInt(R.styleable.HorizontalProgressView_start_progress, 0);
        mEndProgress = typedArray.getInt(R.styleable.HorizontalProgressView_end_progress, 60);
        mStartColor = typedArray.getColor(R.styleable.HorizontalProgressView_start_color, getResources().getColor(R.color.light_orange));
        mEndColor = typedArray.getColor(R.styleable.HorizontalProgressView_end_color, getResources().getColor(R.color.dark_orange));
        trackEnabled = typedArray.getBoolean(R.styleable.HorizontalProgressView_isTracked, false);
        mProgressTextColor = typedArray.getColor(R.styleable.HorizontalProgressView_progressTextColor, getResources().getColor(R.color.colorAccent));
        mProgressTextSize = typedArray.getDimensionPixelSize(R.styleable.HorizontalProgressView_progressTextSize, getResources().getDimensionPixelSize(R.dimen.default_horizontal_text_size));
        mTrackWidth = typedArray.getDimensionPixelSize(R.styleable.HorizontalProgressView_track_width, getResources().getDimensionPixelSize(R.dimen.default_trace_width));
        mAnimateType = typedArray.getInt(R.styleable.HorizontalProgressView_animateType, ACCELERATE_DECELERATE_INTERPOLATOR);
        mTrackColor = typedArray.getColor(R.styleable.HorizontalProgressView_trackColor, getResources().getColor(R.color.default_track_color));
        textVisibility = typedArray.getBoolean(R.styleable.HorizontalProgressView_progressTextVisibility, true);
        mProgressDuration = typedArray.getInt(R.styleable.HorizontalProgressView_progressDuration, 1200);
        mCornerRadius = typedArray.getDimensionPixelSize(R.styleable.HorizontalProgressView_corner_radius, getResources().getDimensionPixelSize(R.dimen.default_corner_radius));
        mTextPaddingBottomOffset = typedArray.getDimensionPixelSize(R.styleable.HorizontalProgressView_text_padding_bottom, getResources().getDimensionPixelSize(R.dimen.default_corner_radius));
        isTextMoved = typedArray.getBoolean(R.styleable.HorizontalProgressView_textMovedEnable, true);
        XLog.e(TAG, "progressDuration: " + mProgressDuration);

        typedArray.recycle();
        moveProgress = mStartProgress;
    }

    private void init() {
        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressPaint.setStyle(Paint.Style.FILL);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        /**
         *
         * 1.adjust the draw location when has no text,
         * 2.add a progress label when progress moving.
         */
        //canvas.drawLine(getPaddingLeft()+50, getHeight()/2 + getPaddingTop() + mTrackWidth, (getWidth() - getPaddingRight()-50)*(moveProgress/100), getHeight()/2 + getPaddingTop() + mTrackWidth, progressPaint );

        updateTheTrack();
        drawTrack(canvas);
        progressPaint.setShader(mShader);
        canvas.drawRoundRect(mRect, mCornerRadius, mCornerRadius, progressPaint);

        drawProgressText(canvas);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mShader = new LinearGradient(getPaddingLeft() - 50, (getHeight() - getPaddingTop()) - 50, getWidth() - getPaddingRight(), getHeight() / 2 + getPaddingTop() + mTrackWidth,
                mStartColor, mEndColor, Shader.TileMode.CLAMP);


    }

    /**
     * draw the track(moving background)
     *
     * @param canvas mCanvas
     */
    private void drawTrack(Canvas canvas) {
        if (trackEnabled) {
            progressPaint.setShader(null);
            progressPaint.setColor(mTrackColor);
            canvas.drawRoundRect(mTrackRect, mCornerRadius, mCornerRadius, progressPaint);

        }
    }

    /**
     * draw the progress text
     *
     * @param canvas mCanvas
     */
    private void drawProgressText(Canvas canvas) {

        if (textVisibility) {
            mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mTextPaint.setStyle(Paint.Style.FILL);
            mTextPaint.setTextSize(mProgressTextSize);
            mTextPaint.setColor(mProgressTextColor);
            mTextPaint.setTextAlign(Paint.Align.CENTER);

            String progressText = ((int) moveProgress) + "%";
            if (isTextMoved) {
                /**
                 * draw the animated text of progress, should think about the offsets, or text will be covered.
                 */
                canvas.drawText(progressText,
                        (getWidth() - getPaddingLeft() - getPaddingRight() - dp2px(mContext, 28)) * (moveProgress / 100) + dp2px(mContext, 10),
                        getHeight() / 2 - getPaddingTop() - mTextPaddingBottomOffset, mTextPaint);
            } else {
                canvas.drawText(progressText, (getWidth() - getPaddingLeft()) / 2, getHeight() / 2 - getPaddingTop() - mTextPaddingBottomOffset, mTextPaint);
            }


        }

    }


    /**
     * set progress animate type
     *
     * @param type anim type
     */
    public void setAnimateType(@AnimateType int type) {
        this.mAnimateType = type;
        setObjectAnimatorType(type);
    }

    /**
     * set object animation type by received
     *
     * @param animatorType object anim type
     */
    private void setObjectAnimatorType(int animatorType) {
        XLog.e(TAG, "AnimatorType>>>>>>" + animatorType);
        switch (animatorType) {
            case ACCELERATE_DECELERATE_INTERPOLATOR:

                if (mInterpolator != null) {
                    mInterpolator = null;
                }

                mInterpolator = new AccelerateDecelerateInterpolator();
                break;

            case LINEAR_INTERPOLATOR:

                if (mInterpolator != null) {
                    mInterpolator = null;
                }
                mInterpolator = new LinearInterpolator();

                break;

            case ACCELERATE_INTERPOLATOR:

                if (mInterpolator != null) {
                    mInterpolator = null;
                    mInterpolator = new AccelerateInterpolator();
                }

                break;

            case DECELERATE_INTERPOLATOR:

                if (mInterpolator != null) {
                    mInterpolator = null;
                }
                mInterpolator = new DecelerateInterpolator();

                break;

            case OVERSHOOT_INTERPOLATOR:

                if (mInterpolator != null) {
                    mInterpolator = null;
                }
                mInterpolator = new OvershootInterpolator();

                break;
        }
    }

    /**
     * set move progress
     *
     * @param progress progress of moving
     */
    public void setProgress(float progress) {
        this.moveProgress = progress;
        refreshTheView();
    }

    public float getProgress() {
        return this.moveProgress;
    }

    /**
     * set start progress
     *
     * @param startProgress start progress
     */
    public void setStartProgress(float startProgress) {
        if (startProgress < 0 || startProgress > 100) {
            throw new IllegalArgumentException("Illegal progress value, please change it!");
        }
        this.mStartProgress = startProgress;
        this.moveProgress = mStartProgress;
        refreshTheView();
    }

    /**
     * set end progress
     *
     * @param endProgress end progress
     */
    public void setEndProgress(float endProgress) {
        if (endProgress < 0 || endProgress > 100) {
            throw new IllegalArgumentException("Illegal progress value, please change it!");
        }
        this.mEndProgress = endProgress;
        refreshTheView();

    }

    /**
     * set start color
     *
     * @param startColor start point color
     */
    public void setStartColor(@ColorInt int startColor) {
        this.mStartColor = startColor;
        mShader = new LinearGradient(getPaddingLeft() - 50, (getHeight() - getPaddingTop()) - 50, getWidth() - getPaddingRight(), getHeight() / 2 + getPaddingTop() + mTrackWidth,
                mStartColor, mEndColor, Shader.TileMode.CLAMP);
        refreshTheView();
    }

    /**
     * set end color
     *
     * @param endColor end point color
     */
    public void setEndColor(@ColorInt int endColor) {
        this.mEndColor = endColor;
        mShader = new LinearGradient(getPaddingLeft() - 50, (getHeight() - getPaddingTop()) - 50, getWidth() - getPaddingRight(), getHeight() / 2 + getPaddingTop() + mTrackWidth,
                mStartColor, mEndColor, Shader.TileMode.CLAMP);
        refreshTheView();
    }

    /**
     * set the width of progress stroke
     *
     * @param width stroke
     */
    public void setTrackWidth(int width) {
        this.mTrackWidth = dp2px(mContext, width);
        refreshTheView();
    }

    /**
     * set track color for progress background
     *
     * @param color bg color
     */
    public void setTrackColor(@ColorInt int color) {
        this.mTrackColor = color;
        refreshTheView();
    }

    /**
     * set text color for progress text
     *
     * @param textColor
     */
    public void setProgressTextColor(@ColorInt int textColor) {
        this.mProgressTextColor = textColor;
    }

    /**
     * set text size for inner text
     *
     * @param size text size
     */
    public void setProgressTextSize(int size) {
        mProgressTextSize = sp2px(mContext, size);
        refreshTheView();
    }

    /**
     * set duration of progress moving
     *
     * @param duration
     */
    public void setProgressDuration(int duration) {
        this.mProgressDuration = duration;
    }

    /**
     * set track for progress
     *
     * @param trackAble whether track or not
     */
    public void setTrackEnabled(boolean trackAble) {
        this.trackEnabled = trackAble;
        refreshTheView();
    }

    /**
     * set the visibility for progress inner text
     *
     * @param visibility text visible or not
     */
    public void setProgressTextVisibility(boolean visibility) {
        this.textVisibility = visibility;
        refreshTheView();
    }

    /**
     * set progress text moving with progress view or not
     *
     * @param moved
     */
    public void setProgressTextMoved(boolean moved) {
        this.isTextMoved = moved;
    }

    /**
     * start the progress's moving
     */
    public void startProgressAnimation() {
        progressAnimator = ObjectAnimator.ofFloat(this, "progress", mStartProgress, mEndProgress);
        XLog.e(TAG, "progressDuration: " + mProgressDuration);
        progressAnimator.setInterpolator(mInterpolator);
        progressAnimator.setDuration(mProgressDuration);
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float progress = (float) animation.getAnimatedValue("progress");
                if (animatorUpdateListener != null) {
                    animatorUpdateListener.onHorizontalProgressUpdate(HorizontalProgressView.this, progress);
                }

            }

        });
        progressAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                if (animatorUpdateListener != null) {
                    animatorUpdateListener.onHorizontalProgressStart(HorizontalProgressView.this);
                }
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (animatorUpdateListener != null) {
                    animatorUpdateListener.onHorizontalProgressFinished(HorizontalProgressView.this);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        progressAnimator.start();
    }

    /**
     * stop the progress moving
     */
    public void stopProgressAnimation() {
        if (progressAnimator != null) {
            progressAnimator.cancel();
            progressAnimator = null;
        }
    }

    /**
     * set the corner radius for the rect of progress
     *
     * @param radius the corner radius
     */
    public void setProgressCornerRadius(int radius) {
        this.mCornerRadius = dp2px(mContext, radius);
        refreshTheView();
    }

    /**
     * set the text padding bottom offset
     *
     * @param offset the value of padding bottom
     */
    public void setProgressTextPaddingBottom(int offset) {
        this.mTextPaddingBottomOffset = dp2px(mContext, offset);
    }


    /**
     * refresh the layout
     */
    private void refreshTheView() {
        invalidate();
        //requestLayout();
    }

    /**
     * update the oval progress track
     */
    private void updateTheTrack() {
        mRect = new RectF(getPaddingLeft() + mStartProgress * (getWidth() - getPaddingLeft() - getPaddingRight() + 60) / 100, getHeight() / 2 - getPaddingTop(),
                (getWidth() - getPaddingRight() - 20) * ((moveProgress) / 100),
                getHeight() / 2 + getPaddingTop() + mTrackWidth);
        mTrackRect = new RectF(getPaddingLeft(), getHeight() / 2 - getPaddingTop(),
                (getWidth() - getPaddingRight() - 20),
                getHeight() / 2 + getPaddingTop() + mTrackWidth);
    }

    /**
     * the interface to help get the value of progress moving
     */
    public interface HorizontalProgressUpdateListener {
        void onHorizontalProgressStart(View view);

        void onHorizontalProgressUpdate(View view, float progress);

        void onHorizontalProgressFinished(View view);

    }

    /**
     * set the progress update listener for progress view
     *
     * @param listener update listener
     */
    public void setProgressViewUpdateListener(HorizontalProgressView.HorizontalProgressUpdateListener listener) {
        this.animatorUpdateListener = listener;
    }


}