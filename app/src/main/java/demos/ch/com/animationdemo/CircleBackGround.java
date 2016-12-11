package demos.ch.com.animationdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Chenhao on 2016/12/11.
 */

public class CircleBackGround extends ImageView {

  private float radiu = 0; //背景圆半径 用于属性动画提供set get方法
  private int color = Color.RED; //画笔颜色

  private float cx, cy;

  public float getCx() {
    return cx;
  }

  public void setCx(float cx) {
    this.cx = cx;
  }

  public float getCy() {
    return cy;
  }

  public void setCy(float cy) {
    this.cy = cy;
  }

  public float getRadiu() {
    return radiu;
  }

  public void setRadiu(float radiu) {
    this.radiu = radiu;
    invalidate();
  }

  public CircleBackGround(Context context) {
    this(context, null);
  }

  public CircleBackGround(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public CircleBackGround(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(attrs);
  }

  private void init(AttributeSet attrs) {
    TypedArray a = getResources().obtainAttributes(attrs, R.styleable.CircleBackGround);
    radiu = (int) a.getDimension(R.styleable.CircleBackGround_radiuCbg, 0);
    color =  a.getColor(R.styleable.CircleBackGround_pColor,0xffffff);
    a.recycle();
  }

  @Override protected void onDraw(Canvas canvas) {
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    paint.setStyle(Paint.Style.FILL_AND_STROKE);
    paint.setTypeface(Typeface.DEFAULT_BOLD);
    paint.setColor(color);
    canvas.drawCircle(getCx(), getCy(), radiu, paint);
  }
}
