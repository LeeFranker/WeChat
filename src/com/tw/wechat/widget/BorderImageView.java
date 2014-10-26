package com.tw.wechat.widget;

import com.tw.wechat.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;


/**
 * 
 * 显示带有边框的图像
 * 
 * @author Lee
 * 
 */
public class BorderImageView extends ImageView {

	private static final ScaleType SCALE_TYPE = ScaleType.FIT_XY;// 缩放类型

	private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
	private static final int COLORDRAWABLE_DIMENSION = 1;

	private static final int DEFAULT_BORDER_WIDTH = 0;// 默认边框的宽度
	private static final int DEFAULT_BORDER_COLOR = Color.BLACK;// 默认边框的颜色

	private final RectF mDrawableRect = new RectF();// 图片画笔
	private final RectF mBorderRect = new RectF();// 边框画笔

	private final Matrix mShaderMatrix = new Matrix();// 矩阵
	private final Paint mBitmapPaint = new Paint();// 图片画笔
	private final Paint mBorderPaint = new Paint();// 边框画笔

	private int mBorderColor = DEFAULT_BORDER_COLOR;// 边框宽度
	private int mBorderWidth = DEFAULT_BORDER_WIDTH;// 边框高度

	private Bitmap mBitmap;// 图片对象
	private BitmapShader mBitmapShader;// 图片渲染器
	private int mBitmapWidth;// 图片宽度
	private int mBitmapHeight;// 图片高度

	private boolean mReady;
	private boolean mSetupPending;

	public BorderImageView(Context context) {
		this(context, null);
	}

	public BorderImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public BorderImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		super.setScaleType(SCALE_TYPE);

		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BorderImageView, defStyle, 0);

		mBorderWidth = a.getDimensionPixelSize(R.styleable.BorderImageView_border_width, DEFAULT_BORDER_WIDTH);
		mBorderColor = a.getColor(R.styleable.BorderImageView_border_color, DEFAULT_BORDER_COLOR);

		a.recycle();

		mReady = true;

		if (mSetupPending) {
			setup();
			mSetupPending = false;
		}
	}

	@Override
	public ScaleType getScaleType() {
		return SCALE_TYPE;
	}

	@Override
	public void setScaleType(ScaleType scaleType) {
		if (scaleType != SCALE_TYPE) {
			throw new IllegalArgumentException(String.format("ScaleType %s not supported.", scaleType));
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (getDrawable() == null) {
			return;
		}
		canvas.drawRect(mDrawableRect, mBitmapPaint);
		if (mBorderWidth != 0) {
			canvas.drawRect(mDrawableRect, mBorderPaint);
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		setup();
	}

	/**
	 * 获取边框颜色
	 * 
	 * @return
	 */
	public int getBorderColor() {
		return mBorderColor;
	}

	/**
	 * 设置边框颜色
	 * 
	 * @param borderColor
	 */
	public void setBorderColor(int borderColor) {
		if (borderColor == mBorderColor) {
			return;
		}
		mBorderColor = borderColor;
		mBorderPaint.setColor(mBorderColor);
		invalidate();
	}

	/**
	 * 获取边框宽度
	 * 
	 * @return
	 */
	public int getBorderWidth() {
		return mBorderWidth;
	}

	/**
	 * 设置边框宽度
	 * 
	 * @param borderWidth
	 */
	public void setBorderWidth(int borderWidth) {
		if (borderWidth == mBorderWidth) {
			return;
		}
		mBorderWidth = borderWidth;
		setup();
	}

	/**
	 * 设置图片
	 * 
	 * @param bm
	 */
	@Override
	public void setImageBitmap(Bitmap bm) {
		super.setImageBitmap(bm);
		mBitmap = bm;
		setup();
	}

	/**
	 * 设置图片
	 * 
	 * @param drawable
	 */
	@Override
	public void setImageDrawable(Drawable drawable) {
		super.setImageDrawable(drawable);
		mBitmap = getBitmapFromDrawable(drawable);
		setup();
	}

	/**
	 * 设置图片
	 * 
	 * @param resId
	 */
	@Override
	public void setImageResource(int resId) {
		super.setImageResource(resId);
		mBitmap = getBitmapFromDrawable(getDrawable());
		setup();
	}

	/**
	 * drawable对象转换成bitmap对象
	 * 
	 * @param drawable
	 * @return bitmap
	 */
	private Bitmap getBitmapFromDrawable(Drawable drawable) {
		if (drawable == null) {
			return null;
		}

		if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) drawable).getBitmap();
		}

		try {
			Bitmap bitmap;

			if (drawable instanceof ColorDrawable) {
				bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION, COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);
			} else {
				bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);
			}

			Canvas canvas = new Canvas(bitmap);
			drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
			drawable.draw(canvas);
			return bitmap;
		} catch (OutOfMemoryError e) {
			return null;
		}
	}

	/**
	 * 设置图片
	 */
	private void setup() {
		if (!mReady) {
			mSetupPending = true;
			return;
		}

		if (mBitmap == null) {
			return;
		}

		// 渲染器
		mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

		mBitmapPaint.setAntiAlias(true);
		mBitmapPaint.setShader(mBitmapShader);

		// 设置画笔风格
		mBorderPaint.setStyle(Paint.Style.STROKE);
		// 设置画笔锯齿效果
		mBorderPaint.setAntiAlias(true);
		// 设置画笔颜色
		mBorderPaint.setColor(mBorderColor);
		// 设置空心的边框宽度
		mBorderPaint.setStrokeWidth(mBorderWidth);

		// 获取图片的宽高
		mBitmapHeight = mBitmap.getHeight();
		mBitmapWidth = mBitmap.getWidth();

		// 设置矩形显示大小
		mBorderRect.set(0, 0, getWidth(), getHeight());

		mDrawableRect.set(mBorderWidth, mBorderWidth, mBorderRect.width() - mBorderWidth, mBorderRect.height() - mBorderWidth);

		updateShaderMatrix();
		invalidate();
	}

	private void updateShaderMatrix() {
		float scale;
		float dx = 0;
		float dy = 0;

		mShaderMatrix.set(null);

		if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width() * mBitmapHeight) {
			scale = mDrawableRect.height() / (float) mBitmapHeight;// 高度缩放比例
			dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f;// 坐标x值
		} else {
			scale = mDrawableRect.width() / (float) mBitmapWidth;// 宽度缩放比例
			dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f;// 坐标y值
		}
		// 缩放比例
		mShaderMatrix.setScale(scale, scale);
		// 平移操作
		mShaderMatrix.postTranslate((int) (dx + 0.5f) + mBorderWidth, (int) (dy + 0.5f) + mBorderWidth);

		mBitmapShader.setLocalMatrix(mShaderMatrix);
	}
}
