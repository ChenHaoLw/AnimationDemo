package demos.ch.com.animationdemo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Chenhao on 2016/12/9.
 */

public class SecondActivity extends AppCompatActivity implements View.OnClickListener {
  Button button;
  TextView tv;
  CircleBackGround cgb;
  ViewTreeObserver.OnPreDrawListener listener;
  int width, height;
  int whichAnima;
  private static final int ANIMA_DURATION = 1 * 1000; //默认动画时间

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_second);

    DisplayMetrics dm = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(dm);
    width = dm.widthPixels;
    height = dm.heightPixels;

    if (null != getIntent()) {
      whichAnima = getIntent().getIntExtra("param", Constant.CENTER);
    }

    initView();
  }

  private void initView() {
    button = (Button) findViewById(R.id.btn);
    button.setOnClickListener(this);
    tv = (TextView) findViewById(R.id.tv);
    cgb = (CircleBackGround) findViewById(R.id.cbg);
    cgb.setCx(width / 2);
    cgb.setCy(height / 2);

    listener = new ViewTreeObserver.OnPreDrawListener() {
      @Override public boolean onPreDraw() {
        //这里一定要remove 避免执行多次
        startWhichAnima();
        return false;
      }
    };
    if (whichAnima == Constant.RIGHT) {
      cgb.getViewTreeObserver().addOnPreDrawListener(listener);
    } else {
      tv.getViewTreeObserver().addOnPreDrawListener(listener);
    }
  }

  /**
   * 启动动画
   */
  private void startWhichAnima() {
    switch (whichAnima) {
      case Constant.CENTER:
        //2.属性动画实现
        tv.getViewTreeObserver().removeOnPreDrawListener(listener);
        tv.setVisibility(View.VISIBLE);
        propertyAnima();
        break;
      case Constant.LEFT:
        //1.系统揭露动画实现
        tv.getViewTreeObserver().removeOnPreDrawListener(listener);
        tv.setVisibility(View.VISIBLE);
        circularAnima();
        break;
      case Constant.RIGHT:
        //3.自定义控件+属性动画实现
        cgb.getViewTreeObserver().removeOnPreDrawListener(listener);
        cgb.setVisibility(View.VISIBLE);
        CirclePropertyAnima();
        break;
    }
  }

  /**
   * 自定义控件+属性动画 圆形
   */
  private void CirclePropertyAnima() {
    //代实现
    ObjectAnimator animaRadiu =
        ObjectAnimator.ofFloat(cgb, "radiu", 0.0f, (float) Math.hypot(width / 2, height / 2));
    animaRadiu.setDuration(ANIMA_DURATION);
    animaRadiu.setInterpolator(new AccelerateDecelerateInterpolator());
    animaRadiu.addListener(new AnimatorListenerAdapter() {
      @Override public void onAnimationEnd(Animator animation) {
        animaEndAction();
      }
    });
    animaRadiu.start();
  }

  /**
   * 使用属性动画实现揭露 效果不是圆形的
   */
  public void propertyAnima() {
    final AnimatorSet animaSet = new AnimatorSet();
    ObjectAnimator animaX = ObjectAnimator.ofFloat(tv, "x", width, 0);
    ObjectAnimator animaY = ObjectAnimator.ofFloat(tv, "y", height, 0);
    //ObjectAnimator animaAlpha = ObjectAnimator.ofFloat(tv,"alpha",1.0f,0.0f);
    animaSet.setDuration(ANIMA_DURATION);
    animaSet.setInterpolator(new AccelerateDecelerateInterpolator());
    animaSet.playTogether(animaX, animaY/*,animaAlpha*/);
    animaSet.addListener(new AnimatorListenerAdapter() {
      @Override public void onAnimationEnd(Animator animation) {
        animaEndAction();
      }
    });
    animaSet.setStartDelay(0);
    animaSet.start();
  }

  /**
   * 使用createCircularReveal实现揭露效果
   */
  private void circularAnima() {
    int centerX = (tv.getLeft() + tv.getRight()) / 2;
    //开始裁剪的中心点Y坐标
    int centerY = (tv.getTop() + tv.getBottom()) / 2;
    //开始半径
    float startRadiu = (float) Math.hypot(centerX, centerY);

    //结束半径对角线
    float endRadiu = (float) Math.min(tv.getWidth(), tv.getHeight()) / 2;

    final Animator animator =
        ViewAnimationUtils.createCircularReveal(tv, centerX, centerY, startRadiu, 0);

    animator.setInterpolator(new AccelerateDecelerateInterpolator());
    animator.setDuration(ANIMA_DURATION);
    animator.setStartDelay(0);
    animator.start();
    animator.addListener(new AnimatorListenerAdapter() {
      @Override public void onAnimationEnd(Animator animation) {
        animaEndAction();
      }
    });
  }

  /**
   * 动画结束后续动作
   */
  private void animaEndAction() {
    tv.setVisibility(View.INVISIBLE);
    cgb.setVisibility(View.INVISIBLE);
    button.setVisibility(View.VISIBLE);
    //ObjectAnimator anima = ObjectAnimator.ofFloat(button,"alpha",0.0f,1.0f);
    //ObjectAnimator anima1 = ObjectAnimator.ofFloat(button,"rotation",0,360);
    //anima1.setDuration(ANIMA_DURATION);
    //anima1.setInterpolator(new AccelerateDecelerateInterpolator());
    //anima1.start();
  }

  @Override public void onClick(View view) {
    if (view.getId() == R.id.btn) {
      onBackPressed();
    }
  }
}
