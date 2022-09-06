package com.aligit.base.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import androidx.core.view.ViewCompat;

/**
 * 有拖拽能力的 ViewGroup
 * https://juejin.im/entry/594fd0276fb9a06bbc4acc5b
 */
public class DragViewGroup extends FrameLayout {
    private static final String TAG = "DragViewGroup";

    //是否移动，用来区分点击事件
    private boolean haveMove = false;

    // 记录手指上次触摸的坐标
    private float mLastPointX;
    private float mLastPointY;

    //用于识别最小的滑动距离
    private int mSlop;
    // 用于标识正在被拖拽的 child，为 null 时表明没有 child 被拖拽
    private View mDragView;

    // 状态分别空闲、拖拽两种
    enum State {
        IDLE,
        DRAGGING
    }

    State mCurrentState;

    public DragViewGroup(Context context) {
        this(context,null);
    }

    public DragViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }


    public DragViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mSlop = ViewConfiguration.getWindowTouchSlop();
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        switch (action){
            case MotionEvent.ACTION_DOWN:
                if (myOnClickListener != null)myOnClickListener.onTouchDown();
                if ( isPointOnViews(event)) {
                    //标记状态为拖拽，并记录上次触摸坐标
                    mCurrentState = State.DRAGGING;
                    mLastPointX = event.getX();
                    mLastPointY = event.getY();
                }else {
                    return false;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                int deltaX = (int) (event.getX() - mLastPointX);
                int deltaY = (int) (event.getY() - mLastPointY);
                if (mCurrentState == State.DRAGGING && mDragView != null
                        && (Math.abs(deltaX) > mSlop || Math.abs(deltaY) > mSlop)
                        && (event.getX() < getWidth()-20 && event.getX() > 20 && event.getY() < getHeight()-100 && event.getY() > 200)
                ) {
                    haveMove = true;
                    //如果符合条件则对被拖拽的 child 进行位置移动
                    ViewCompat.offsetLeftAndRight(mDragView,deltaX);
                    ViewCompat.offsetTopAndBottom(mDragView,deltaY);
                    mLastPointX = event.getX();
                    mLastPointY = event.getY();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if ( mCurrentState == State.DRAGGING ){
                    // 标记状态为空闲，并将 mDragView 变量置为 null
                    mCurrentState = State.IDLE;
                    if (mDragView.getX() >= getWidth() / 2) {
                        //靠右吸附
                        mDragView.animate().setInterpolator(new DecelerateInterpolator())
                                .setDuration(500)
                                .xBy(getWidth() - mDragView.getWidth() - mDragView.getX())
                                .start();
                    } else {
                        //靠左吸附
                        ObjectAnimator oa = ObjectAnimator.ofFloat(mDragView, "x", mDragView.getX(), 0);
                        oa.setInterpolator(new DecelerateInterpolator());
                        oa.setDuration(500);
                        oa.start();
                    }
                }
                if (!haveMove){
                    if (myOnClickListener != null)myOnClickListener.onClick(mDragView);
                }
                if (myOnClickListener != null)myOnClickListener.onTouchUp();
                haveMove = false;
                mDragView = null;
                break;
        }
        return true;
    }

    /**
     * 判断触摸的位置是否落在 child 身上
     *
     * */
    private boolean isPointOnViews(MotionEvent ev) {
        boolean result = false;
        Rect rect = new Rect();
        for (int i = getChildCount() - 1;i >= 0;i--) {
            View view = getChildAt(i);
            rect.set((int)view.getX(),(int)view.getY(),(int)view.getX()+(int)view.getWidth()
                    ,(int)view.getY()+view.getHeight());

            if (rect.contains((int)ev.getX(),(int)ev.getY())){
                //标记被拖拽的child
                mDragView = view;
                result = true;
                break;
            }
        }

        return  result && mCurrentState != State.DRAGGING;
    }

    public void setMyOnClickListener(MyOnClickListener myOnClickListener) {
        this.myOnClickListener = myOnClickListener;
    }

    MyOnClickListener myOnClickListener;
    public interface MyOnClickListener{
        void onClick(View mDragView);
        void onTouchDown();
        void onTouchUp();
    }
}