package com.akhilsharma.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.TextureView;

/**
 * change the view size with keeping the specified aspect ratio.
 * if you set this view with in a FrameLayout and set property "android:layout_gravity="center",
 * you can show this view in the center of screen and keep the aspect ratio of content
 * XXX it is better that can set the aspect raton a a xml property
 */
public class SimpleUVCCameraTextureView extends TextureView	// API >= 14
	implements AspectRatioViewInterface {

    private double mRequestedAspect = -1.0;

	public SimpleUVCCameraTextureView(final Context context) {
		this(context, null, 0);
	}

	public SimpleUVCCameraTextureView(final Context context, final AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SimpleUVCCameraTextureView(final Context context, final AttributeSet attrs, final int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onResume() {
	}

	@Override
	public void onPause() {
	}

	@Override
    public void setAspectRatio(final double aspectRatio) {
        if (aspectRatio < 0) {
            throw new IllegalArgumentException();
        }
        if (mRequestedAspect != aspectRatio) {
            mRequestedAspect = aspectRatio;
            requestLayout();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		if (mRequestedAspect > 0) {
			int initialWidth = MeasureSpec.getSize(widthMeasureSpec);
			int initialHeight = MeasureSpec.getSize(heightMeasureSpec);

			final int horizPadding = getPaddingLeft() + getPaddingRight();
			final int vertPadding = getPaddingTop() + getPaddingBottom();
			initialWidth -= horizPadding;
			initialHeight -= vertPadding;

			final double viewAspectRatio = (double)initialWidth / initialHeight;
			final double aspectDiff = mRequestedAspect / viewAspectRatio - 1;

			if (Math.abs(aspectDiff) > 0.01) {
				if (aspectDiff > 0) {
					// width priority decision
					initialHeight = (int) (initialWidth / mRequestedAspect);
				} else {
					// height priority decison
					initialWidth = (int) (initialHeight * mRequestedAspect);
				}
				initialWidth += horizPadding;
				initialHeight += vertPadding;
				widthMeasureSpec = MeasureSpec.makeMeasureSpec(initialWidth, MeasureSpec.EXACTLY);
				heightMeasureSpec = MeasureSpec.makeMeasureSpec(initialHeight, MeasureSpec.EXACTLY);
			}
		}

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}