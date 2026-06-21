package com.safety.ticket.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.io.ByteArrayOutputStream;

public class SignatureView extends View {
    private Paint paint;
    private Path path;
    private Canvas canvas;
    private Bitmap bitmap;
    private boolean isSigned = false;

    private static final float STROKE_WIDTH = 5f;
    private static final int PAINT_COLOR = Color.BLACK;

    public SignatureView(Context context) {
        super(context);
        init();
    }

    public SignatureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SignatureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(PAINT_COLOR);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(STROKE_WIDTH);
        path = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmap, 0, 0, null);
        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(x, y);
                isSigned = true;
                break;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(x, y);
                break;
            case MotionEvent.ACTION_UP:
                canvas.drawPath(path, paint);
                path.reset();
                break;
        }
        invalidate();
        return true;
    }

    public void clear() {
        path.reset();
        if (canvas != null && bitmap != null) {
            canvas.drawColor(Color.WHITE);
        }
        isSigned = false;
        invalidate();
    }

    public boolean isSigned() {
        return isSigned;
    }

    public Bitmap getSignatureBitmap() {
        if (!isSigned) return null;
        Bitmap result = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(result);
        c.drawColor(Color.WHITE);
        c.drawBitmap(bitmap, 0, 0, null);
        return result;
    }

    public byte[] getSignatureBytes() {
        Bitmap bmp = getSignatureBitmap();
        if (bmp == null) return null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public void setSignatureBitmap(Bitmap bmp) {
        if (bmp == null) return;
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bmp, 0, 0, null);
        isSigned = true;
        invalidate();
    }
}
