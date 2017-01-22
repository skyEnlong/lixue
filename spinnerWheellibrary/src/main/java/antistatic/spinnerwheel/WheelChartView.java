package antistatic.spinnerwheel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.view.View;

public class WheelChartView extends View {

    private static final int COLOR_DARK_GREEN = Color.parseColor("#2dcc70");
    private ChartType mChartType = ChartType.Columnar;
    private boolean isCurrentItem = false;
    private float mChartHeight = 0;
    private float mChartWidth = 0;
    private final float mMaxSpeed = 40.0f;
    private float mMaxDistance = 50.0f;
    private float mTopMargin = 0.0f;
    private float mCurrentValue = 0.0f;
    private float mPreviousValue = 0.0f;
    private float mNextValue = 0.0f;
    private final float X_RADIUS = 0, Y_RADIUS = 0;
    private int[] mColumnarColor = new int[]{Color.parseColor("#78FFFFFF"),
            Color.parseColor("#78FFFFFF")};
    private int[] mColumnarFocusColor = new int[]{
            Color.parseColor("#FFFFFF"), Color.parseColor("#FFFFFF")};
    private float mColumnarMore = 40.0f;
    private int[] mColumnarMoreColor = new int[]{Color.parseColor("#FFFFFF"),
            Color.parseColor("#ffb171")};

    private int mLineWidth = 4;
    private boolean isRealData;
    private boolean isTodayCircle;

    private int[] curvePointIds;

    private float textSize = 12;

    private float curY;

    public enum ChartType {
        Columnar, Curve
    }

    public void setChartType(ChartType chartType) {
        mChartType = chartType;
    }

    public void setIsCurrentItem(boolean isCurrent) {
        isCurrentItem = isCurrent;
    }

    private boolean isCurrentItem() {
        return isCurrentItem;
    }

    public WheelChartView(Context context) {
        super(context);
        float scale = getResources().getDisplayMetrics().widthPixels / 480.0f;
        mTopMargin = 60 * scale;
        mLineWidth *= scale;
        textSize *= scale;
    }

    public WheelChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        float scale = getResources().getDisplayMetrics().widthPixels / 480.0f;
        mTopMargin = 60 * scale;
        mLineWidth *= scale;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setChartValues(float curValue, float preValue, float nextValue) {
        mCurrentValue = curValue;
        mPreviousValue = preValue;
        mNextValue = nextValue;
        invalidate();
    }

    public void setMaxValue(float max) {
        mMaxDistance = 0.8f * max;
    }

    public void setIsRealData(boolean isReal) {
        this.isRealData = isReal;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);

        mChartHeight = this.getHeight() - mTopMargin;
        mChartWidth = this.getWidth();
        if (mChartType == ChartType.Columnar) {
            drawColumnar(canvas);
        }

        if (mChartType == ChartType.Curve) {
            drawCurve(canvas);
        }
    }

    public void setIsTodayCircle(boolean isToday) {
        isTodayCircle = isToday;
    }

    private void drawColumnar(Canvas canvas) {

        float average = mChartHeight * 1.0f / mMaxDistance;
        float height = mCurrentValue * average;
        int[] colors;
        if (isCurrentItem()) {
            // RectF tmpRectF = new RectF(mChartWidth / 4,
            //
            // 0, mChartWidth * 3 / 4, getHeight());
            //
            // colors = mColumnarFocusColor;
            //
            // drawColumnar(canvas, tmpRectF, // viewHeight 2012-1-11
            // colors);
            colors = mColumnarFocusColor;
        } else {
            colors = mColumnarColor;
        }

        if (mCurrentValue <= mMaxDistance) {
            RectF tmpRectF = new RectF(mChartWidth / 4,

                    (float) (mChartHeight - height + mTopMargin), mChartWidth * 3 / 4,
                    getHeight());

            drawColumnar(canvas, tmpRectF, // viewHeight 2012-1-11
                    colors);
        } else {
            if (isCurrentItem()) {
                RectF tmpRectF = new RectF(mChartWidth / 4,

                        (float) (mColumnarMore + mTopMargin), mChartWidth * 3 / 4,
                        getHeight());

                drawColumnar(canvas, tmpRectF, // viewHeight 2012-1-11
                        colors);
                RectF moreRectF = new RectF(mChartWidth / 4,

                        mTopMargin, mChartWidth * 3 / 4, mColumnarMore + mTopMargin);

                colors = mColumnarMoreColor;

                drawColumnar(canvas, moreRectF, colors);
            } else {
                RectF tmpRectF = new RectF(mChartWidth / 4,

                        (float) mTopMargin, mChartWidth * 3 / 4, getHeight());

                drawColumnar(canvas, tmpRectF, // viewHeight 2012-1-11
                        colors);
            }
        }
    }

    public int getCurY() {
        
        return (int)curY;
    }

    private void drawCurve(Canvas canvas) {
        if (mCurrentValue == 0)
            return;
        float average = mChartHeight * 0.8f * 0.6f / mMaxDistance;
        float prePoint_X = 0.0f;
        float prePoint_Y = 0.0f;
        float nextPoint_X = 0.0f;
        float nextPoint_Y = 0.0f;
        float startPoint_X = mChartWidth / 2;
        float startPoint_Y = mChartHeight - mCurrentValue * average;
        if (startPoint_Y < 0) {
            startPoint_Y = 0;
        }

        CurvePoint prePoint = null;
        CurvePoint nextPoint = null;
        CurvePoint curPoint = new CurvePoint(startPoint_X, startPoint_Y);
        mPreviousValue = mPreviousValue == -1 ? 0 : mPreviousValue;
        if (mPreviousValue > 0) {
            prePoint_X = -mChartWidth / 2;
            prePoint_Y = mChartHeight - mPreviousValue * average;
            if (prePoint_Y < 0) {
                prePoint_Y = 0;
            }
            prePoint = new CurvePoint(prePoint_X, prePoint_Y);

        }
        mNextValue = mNextValue == -1 ? 0 : mNextValue;

        if (mNextValue > 0) {
            nextPoint_X = mChartWidth;
            nextPoint_Y = mChartHeight - (mNextValue + mCurrentValue) / 2
                    * average;
            if (nextPoint_Y < 0) {
                nextPoint_Y = 0;
            }

            nextPoint = new CurvePoint(nextPoint_X, nextPoint_Y);

        }

        drawPath(canvas, prePoint, curPoint, nextPoint);
        if (null != prePoint) {
            drawLine(canvas, prePoint, curPoint);
        }

        if (null != nextPoint) {
            drawLine(canvas, curPoint, nextPoint);
        }
        if (isRealData) {

            drawDot(canvas, curPoint);
        }

    }


    private void drawValue(CurvePoint point, Canvas canvas) {
        if (isTodayCircle && null != curvePointIds && curvePointIds.length >= 4) {
            Paint paint = new Paint();

            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL);
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), curvePointIds[3]);
            canvas.drawBitmap(bitmap,
                    (mChartWidth - bitmap.getWidth()) / 2, point.Y - bitmap.getHeight(), paint);
            canvas.restore();
            canvas.save();

            String value = "" + mCurrentValue;
            float length = (float) paint.measureText(value);
            paint.setTextSize(14);
            canvas.drawText(value, (mChartWidth - length) / 2, point.Y - bitmap.getHeight() / 2, paint);

        }

    }


    private void drawDot(Canvas canvas, CurvePoint curPoint) {
        // TODO Auto-generated method stub
        Paint paint = new Paint();

        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(COLOR_DARK_GREEN);

        if (null == curvePointIds || curvePointIds.length < 2 || curvePointIds[0] == 0 || curvePointIds[1] == 0) {

            if (isTodayCircle) {
                canvas.drawCircle(curPoint.X, curPoint.Y, mLineWidth * 2.1f, paint);
                paint.setColor(Color.WHITE);
                canvas.drawCircle(curPoint.X, curPoint.Y, mLineWidth * 1.5f, paint);
            } else {
                canvas.drawCircle(curPoint.X, curPoint.Y, mLineWidth * 1.8f, paint);
            }
        } else {
            Bitmap bitmap = null;
            if (isTodayCircle) {
                bitmap = BitmapFactory.decodeResource(getResources(), curvePointIds[0]);
            } else {
                bitmap = BitmapFactory.decodeResource(getResources(), curvePointIds[1]);
            }

            float left = curPoint.X - bitmap.getWidth() / 2;
            float top = curPoint.Y - bitmap.getHeight() / 2;

            curY = top;
            canvas.drawBitmap(bitmap, left, top, null);
        }

        canvas.restore();
    }

    private void drawColumnar(Canvas canvas, RectF rectF, int[] colors) {
        Paint paint = new Paint();

        LinearGradient shader = new LinearGradient(rectF.left, rectF.bottom,
                rectF.left, rectF.top, colors[0], colors[1], TileMode.MIRROR);
        paint.setShader(shader);
        paint.setAntiAlias(true);
        canvas.drawRoundRect(rectF, X_RADIUS, Y_RADIUS, paint);//
    }

    public void drawLine(Canvas canvas, CurvePoint pointA, CurvePoint pointB) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Style.FILL_AND_STROKE);
        paint.setColor(Color.parseColor("#cccccc"));

        paint.setStrokeWidth(mLineWidth);
        paint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawLine(pointA.X, pointA.Y, pointB.X, pointB.Y, paint);
        // float controlValue = 0;
        // if (pointA.X < pointB.X) {
        // controlValue = -20;
        // } else {
        // controlValue = 20;
        // }
        // Path path2 = new Path();
        // path2.moveTo(pointA.X, pointA.Y);// ����Path�����
        // // path2.quadTo((pointA.X + pointB.X) / 2 + controlValue,
        // // (pointA.Y + pointB.Y) / 2 + controlValue, pointB.X, pointB.Y);
        //
        // path2.lineTo( pointB.X, pointB.Y);// ���ñ�������ߵĿ��Ƶ������յ����
        // canvas.drawPath(path2, paint);

    }

    public void drawPath(Canvas canvas, CurvePoint start, CurvePoint control,
                         CurvePoint end) {


        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Style.FILL);
        paint.setStrokeCap(Cap.SQUARE);
//        paint.setColor(mColumnarColor[0]);
//        paint.setAlpha(60);

        Path path = new Path();
        // path.quadTo(control.X, control.Y, end.X, end.Y); //
        int mChartHeight = getHeight();
        float start_x = 0;
        float start_y = 0;

        CurvePoint shaderPoint = null;

        if (null != start) {
            shaderPoint = start;
            start_x = 0;
            start_y = mChartHeight;
            path.moveTo(0, mChartHeight);
            path.lineTo(start.X, start.Y);

        } else {
            start_x = mChartWidth / 2;
            start_y = mChartHeight;
            path.moveTo(mChartWidth / 2, mChartHeight);
        }
        if (null != control) {

            path.lineTo(control.X, control.Y);

            if(null != shaderPoint){
                shaderPoint = (shaderPoint.Y < control.Y) ? shaderPoint : control;
            }else {
                shaderPoint = control;
            }
        }

        if (null != end) {

            path.lineTo(end.X, end.Y);
            path.lineTo(mChartWidth, mChartHeight);
            if(null != shaderPoint){
                shaderPoint = (shaderPoint.Y < end.Y) ? shaderPoint : end;
            }else {
                shaderPoint = end;
            }

        } else {
            path.lineTo(mChartWidth / 2, mChartHeight);
        }
        path.lineTo(start_x, start_y);
        path.close();


        paint.setShader(new LinearGradient(shaderPoint.X, mTopMargin, shaderPoint.X, mChartHeight,
                Color.parseColor("#802dcc70"), Color.parseColor("#052dcc70"), TileMode.CLAMP));
        canvas.drawPath(path, paint);
    }


    public void setCurvePointIds(int[] curvePointIds) {
        this.curvePointIds = curvePointIds;
    }


    public class CurvePoint {
        public float X;
        public float Y;

        public CurvePoint(float x, float y) {
            X = x;
            Y = y;
        }
    }

}
