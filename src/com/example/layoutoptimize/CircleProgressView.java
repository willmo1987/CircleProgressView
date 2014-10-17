package com.example.layoutoptimize;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

public class CircleProgressView extends View {

	private float strokeWidth;
	private float borderWidth;
	private float radius;
	private float internalRadius;
	private float externalRadius;
	private int borderColor;
	private int strokeColor;
	private int textColor;
	private int backgroundColor;
	private float textSize;
	private CirclePrgressMode mode;
	private int progress;
	private int currentProgress;
	private boolean active;
	private static final long PROGRESS_INTERVAL = 20L;
	private static final int TOTAL_PROGRESS = 100;
	private static final int START_DEGREE = -90;
	private static final int TOTAL_DEGREE = 360;
	
	public enum CirclePrgressMode {
		CIRCLE_PROGRESS_MODE_STATIC,
		CIRCLE_PROGRESS_MODE_DYNAMIC
	}
	
	public CircleProgressView(Context context) {
		super(context);
	}
	
	public CircleProgressView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StrokeCircleProgress);
		strokeWidth = typedArray.getDimension(R.styleable.StrokeCircleProgress_strokeWidth, 0);
		borderWidth = typedArray.getDimension(R.styleable.StrokeCircleProgress_borderWidth, 0);
		strokeColor = typedArray.getColor(R.styleable.StrokeCircleProgress_strokeColor, 0);
		borderColor = typedArray.getColor(R.styleable.StrokeCircleProgress_borderColor, 0);
		textColor = typedArray.getColor(R.styleable.StrokeCircleProgress_textColor, 0);
		backgroundColor = typedArray.getColor(R.styleable.StrokeCircleProgress_backgroundColor, 0);
		textSize = typedArray.getDimension(R.styleable.StrokeCircleProgress_textSize, 0);
		typedArray.recycle();
		if (borderWidth == 0) {
			borderWidth = getResources().getDimension(R.dimen.stroke_circle_progress_default_border_width);
		}
		if (textSize == 0) {
			textSize = getResources().getDimension(R.dimen.stroke_circle_progress_default_text_size);
		}
		if (strokeColor == 0) {
			strokeColor = getResources().getColor(R.color.red);
		}
		if (borderColor == 0) {
			borderColor = getResources().getColor(R.color.black);
		}
		if (textColor == 0) {
			textColor = getResources().getColor(R.color.gray);
		}
		if (backgroundColor == 0) {
			backgroundColor = getResources().getColor(R.color.white);
		}
		active = false;
	}
	
	public void startDrawProgress(CirclePrgressMode mode, int progress) {
		this.mode = mode;
		this.progress = progress;
		active = true;
		currentProgress = 0;
		if (mode == CirclePrgressMode.CIRCLE_PROGRESS_MODE_STATIC) {
			currentProgress = progress;
		}
		invalidate();
	}
	
	private void prepareParams(int width, int height) {
		int minSize = Math.min(width, height);
		if (strokeWidth == 0) {
			strokeWidth = minSize / 4;
		}
		externalRadius = (minSize - borderWidth) / 2;
		internalRadius = externalRadius - strokeWidth - borderWidth;
		radius = internalRadius + (strokeWidth + borderWidth) / 2;
		
		//for anti-alias
		externalRadius -= 1;
		internalRadius += 1;
	}
	
	private void drawText(Canvas canvas, int width, int height) {
		Paint textPaint = new Paint();
		textPaint.setAntiAlias(true);
		textPaint.setTypeface(Typeface.DEFAULT);
		textPaint.setTextSize(textSize);
		textPaint.setColor(textColor);
		
		String text = String.valueOf(currentProgress) + "%";
		Rect bounds = new Rect();
		textPaint.getTextBounds(text, 0, text.length(), bounds);
		float textWidth = bounds.right - bounds.left;
		float textHeight = bounds.bottom - bounds.top;
		float x = (width - textWidth) / 2;
		float y = (height + textHeight) / 2;
		canvas.drawText(text, x, y, textPaint);
	}
	
	private void drawBorders(Canvas canvas, int width, int height) {
		Paint borderPaint = new Paint();
		borderPaint.setAntiAlias(true);
		borderPaint.setStyle(Style.STROKE);
		borderPaint.setStrokeWidth(borderWidth);
		borderPaint.setColor(borderColor);
		canvas.drawCircle(width / 2, height / 2, externalRadius, borderPaint);
		canvas.drawCircle(width / 2, height / 2, internalRadius, borderPaint);
	}
	
	private void drawStrokeCircle(Canvas canvas, int width, int height) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(strokeWidth);
		paint.setColor(strokeColor);
		canvas.drawCircle(width / 2, height / 2, radius, paint);
	}
	
	private void drawSectorCircle(Canvas canvas, int width, int height) {
		Paint paint = new Paint();
		paint.setColor(backgroundColor);
		paint.setAntiAlias(true);
		paint.setStyle(Style.FILL);
		
		RectF bounds = new RectF(borderWidth, borderWidth, width - borderWidth, height - borderWidth);
		int angle = TOTAL_DEGREE * currentProgress / TOTAL_PROGRESS;
		canvas.drawArc(bounds, START_DEGREE + angle, TOTAL_DEGREE - angle, true, paint);
		if (mode == CirclePrgressMode.CIRCLE_PROGRESS_MODE_DYNAMIC && currentProgress != progress) {
			currentProgress ++;
			postInvalidateDelayed(PROGRESS_INTERVAL);
		}
	}
	
	private void drawInternalCircle(Canvas canvas, int width, int height) {
		Paint paint = new Paint();
		paint.setColor(backgroundColor);
		paint.setAntiAlias(true);
		paint.setStyle(Style.FILL);
		canvas.drawCircle(width / 2, height / 2, internalRadius - borderWidth / 2, paint);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		int width = getWidth();
		int height = getHeight();
		prepareParams(width, height);
		if (active) {
			drawStrokeCircle(canvas, width, height);
			drawSectorCircle(canvas, width, height);
		}
		drawInternalCircle(canvas, width, height);
		drawBorders(canvas, width, height);
		drawText(canvas, width, height);
	}

}
