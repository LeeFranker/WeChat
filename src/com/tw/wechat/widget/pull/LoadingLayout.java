package com.tw.wechat.widget.pull;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tw.wechat.R;

public class LoadingLayout extends FrameLayout {
	static final int DEFAULT_ROTATION_ANIMATION_DURATION = 150;
	private final ImageView headerImage;
	private final ProgressBar headerProgress;
	private final TextView headerText;
	private final TextView subHeaderText;
	private String pullLabel;
	private String refreshingLabel;
	private String releaseLabel;
	private CharSequence subTextLabel;
	private final Animation rotateAnimation;
	private final Animation resetRotateAnimation;

	public LoadingLayout(Context context) {
		this(context, null);
	}

	public LoadingLayout(Context context, AttributeSet attrs) {
		this(context, 1, "", "", "");
	}

	public LoadingLayout(Context context, int mode, String releaseLabel, String pullLabel, String refreshingLabel) {
		super(context);
		ViewGroup header = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.pull_to_refresh_header, this);
		this.headerText = ((TextView) header.findViewById(R.id.pull_to_refresh_text));
		this.subHeaderText = ((TextView) header.findViewById(R.id.pull_to_refresh_sub_text));
		this.headerImage = ((ImageView) header.findViewById(R.id.pull_to_refresh_image));
		this.headerProgress = ((ProgressBar) header.findViewById(R.id.pull_to_refresh_progress));

		Interpolator interpolator = new LinearInterpolator();
		this.rotateAnimation = new RotateAnimation(0.0F, -180.0F, 1, 0.5F, 1, 0.5F);
		this.rotateAnimation.setInterpolator(interpolator);
		this.rotateAnimation.setDuration(150L);
		this.rotateAnimation.setFillAfter(true);

		this.resetRotateAnimation = new RotateAnimation(-180.0F, 0.0F, 1, 0.5F, 1, 0.5F);
		this.resetRotateAnimation.setInterpolator(interpolator);
		this.resetRotateAnimation.setDuration(150L);
		this.resetRotateAnimation.setFillAfter(true);

		this.releaseLabel = releaseLabel;
		this.pullLabel = pullLabel;
		this.refreshingLabel = refreshingLabel;

		switch (mode) {
		case 2:
			this.headerImage.setImageResource(R.drawable.pulltorefresh_up_arrow);
			break;
		case 1:
		default:
			this.headerImage.setImageResource(R.drawable.ic_refresh_down);
		}
	}

	public void reset() {
		this.headerText.setText(this.pullLabel);
		this.headerImage.setVisibility(View.VISIBLE);
		this.headerProgress.setVisibility(View.GONE);
	}

	public void releaseToRefresh() {
		this.headerText.setText(this.releaseLabel);
		this.headerImage.clearAnimation();
		this.headerImage.startAnimation(this.rotateAnimation);
	}

	public void setPullLabel(String pullLabel) {
		this.pullLabel = pullLabel;
	}

	public void refreshing() {
		this.headerText.setText(this.refreshingLabel);
		this.headerImage.clearAnimation();
		this.headerImage.setVisibility(View.INVISIBLE);
		this.headerProgress.setVisibility(View.VISIBLE);
	}

	public void setRefreshingLabel(String refreshingLabel) {
		this.refreshingLabel = refreshingLabel;
	}

	public void setReleaseLabel(String releaseLabel) {
		this.releaseLabel = releaseLabel;
	}

	public void pullToRefresh() {
		this.headerText.setText(this.pullLabel);
		this.headerImage.clearAnimation();
		this.headerImage.startAnimation(this.resetRotateAnimation);
	}

	public CharSequence getSubHeaderText() {
		return this.subTextLabel;
	}

	public void setSubHeaderText(CharSequence label) {
		if (TextUtils.isEmpty(label)) {
			this.subHeaderText.setVisibility(View.GONE);
		} else {
			this.subTextLabel = label;
			this.subHeaderText.setText(label);
			this.subHeaderText.setVisibility(View.VISIBLE);
		}
	}
}