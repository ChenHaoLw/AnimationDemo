package demos.ch.com.animationdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Chenhao on 2016/12/8.
 */

public class CirclePic extends ImageView {

  private int defaultRadiu = 100;//默认圆形图片

  public CirclePic(Context context) {
    this(context, null);
  }

  public CirclePic(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public CirclePic(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(attrs);
  }

  private void init(AttributeSet attrs) {
    TypedArray a = getResources().obtainAttributes(attrs,R.styleable.CirclePic);
    defaultRadiu = (int) a.getDimension(R.styleable.CirclePic_radiu,defaultRadiu);
    a.recycle();
  }

  @Override protected void onDraw(Canvas canvas) {
    Drawable drawable = getDrawable();
    if (drawable != null) {
      if (drawable instanceof BitmapDrawable && drawable.getIntrinsicWidth() > 0
          && drawable.getIntrinsicHeight() > 0) {
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        Bitmap circleBitmap = getCircleBitamp(bitmap);

        canvas.drawBitmap(circleBitmap, 0, 0, null);
      }
    } else {
      super.onDraw(canvas);
    }
  }

  /**
   * 生成圆形bitmap
   */
  private Bitmap getCircleBitamp(Bitmap bitmap) {

    Bitmap circleBitmap = null;
    if (bitmap.getWidth() != defaultRadiu || bitmap.getHeight() != defaultRadiu) {
      circleBitmap = Bitmap.createScaledBitmap(bitmap, defaultRadiu, defaultRadiu, false);
    } else {
      circleBitmap = bitmap;
    }

    Bitmap output = Bitmap.createBitmap(circleBitmap.getWidth(), circleBitmap.getHeight(),
        Bitmap.Config.ARGB_8888);

    Rect rect = new Rect(0, 0, circleBitmap.getWidth(), circleBitmap.getHeight());

    Canvas canvas = new Canvas(output);

    Paint paint = new Paint();
    paint.setAntiAlias(true);
    paint.setDither(true);

    canvas.drawCircle(circleBitmap.getWidth() / 2, circleBitmap.getHeight() / 2,
        circleBitmap.getWidth() / 2, paint);
    //取2层交集部分,只显示上层
    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

    canvas.drawBitmap(circleBitmap, rect, rect, paint);

    return output;
  }
}
