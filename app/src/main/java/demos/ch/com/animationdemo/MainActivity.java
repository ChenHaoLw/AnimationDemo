package demos.ch.com.animationdemo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

  FloatingActionButton fab;
  CirclePic ivCenter; //中间大图片
  CirclePic cpLeft, cpCenter, cpRight; // 左中右依次是:系统默认 ;属性动画 ; 属性动画+自定义控件
  ViewTreeObserver.OnPreDrawListener listener;
  int centerX, centerY;
  float startRadiu, endRadiu;
  private static final int ANIMA_DURATION = 1 * 1000; //默认动画时间

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    initView();
  }

  private void initView() {
    fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(this);
    ivCenter = (CirclePic) findViewById(R.id.iv);
    listener = new ViewTreeObserver.OnPreDrawListener() {
      @Override public boolean onPreDraw() {
        //这里一定要remove 避免执行多次
        ivCenter.getViewTreeObserver().removeOnPreDrawListener(listener);
        centerX = (ivCenter.getLeft() + ivCenter.getRight()) / 2;
        //开始裁剪的中心点Y坐标
        centerY = (ivCenter.getTop() + ivCenter.getBottom()) / 2;
        //结束半径对角线
        endRadiu = (float) Math.hypot(centerX, centerY);
        //开始半径
        startRadiu = 0;

        anim(centerX, centerY, endRadiu, 0, ivCenter,
            ivCenter.getVisibility() == View.INVISIBLE ? true : false);
        return false;
      }
    };

    ivCenter.getViewTreeObserver().addOnPreDrawListener(listener);
    cpCenter = (CirclePic) findViewById(R.id.circlePic);
    cpCenter.setOnClickListener(this);
    cpLeft = (CirclePic) findViewById(R.id.circlePicLeft);
    cpLeft.setOnClickListener(this);
    cpRight = (CirclePic) findViewById(R.id.circlePicRight);
    cpRight.setOnClickListener(this);
  }

  /**
   * @param isShow 动画结束是否显示View
   */
  private void anim(int centerX, int centerY, float endRadiu, float startRadiu, final ImageView iv,
      final boolean isShow) {
    // fab的动画
    //fab.setPivotY((iv.getHeight() + iv.getBottom()) / 2);
    //final Animation rotationAnima = new RotateAnimation(isShow ? 0 : 360, isShow ? 360 : 0);
    //fab.setAnimation(rotationAnima);
    //rotationAnima.setDuration(ANIMA_DURATION);
    //rotationAnima.setInterpolator(new AccelerateDecelerateInterpolator());
    //rotationAnima.start();
    //rotationAnima.setAnimationListener(new Animation.AnimationListener() {
    //  @Override public void onAnimationStart(Animation animation) {
    //
    //  }
    //
    //  @Override public void onAnimationEnd(Animation animation) {
    //    rotationAnima.cancel();
    //    rotationAnima.setAnimationListener(null);
    //  }
    //
    //  @Override public void onAnimationRepeat(Animation animation) {
    //
    //  }
    //});

    //ivCenter的动画
    final Animator animator = ViewAnimationUtils.createCircularReveal(iv, centerX, centerY,
        isShow ? startRadiu : endRadiu, isShow ? endRadiu : startRadiu);

    animator.setInterpolator(new AccelerateDecelerateInterpolator());
    animator.setDuration(ANIMA_DURATION);
    animator.setStartDelay(0);
    iv.setVisibility(View.VISIBLE);
    animator.start();
    animator.addListener(new AnimatorListenerAdapter() {
      @Override public void onAnimationEnd(Animator animation) {
        if (!isShow) {
          iv.setVisibility(View.INVISIBLE);
        }
      }
    });
  }

  @Override public void onClick(View view) {
    switch (view.getId()) {
      case R.id.fab:
        anim(centerX, centerY, endRadiu, startRadiu, ivCenter,
            ivCenter.getVisibility() == View.INVISIBLE ? true : false);
        break;

      case R.id.circlePic:
        toSecondActivity(Constant.CENTER);
        break;

      case R.id.circlePicLeft:
        toSecondActivity(Constant.LEFT);
        break;

      case R.id.circlePicRight:
        toSecondActivity(Constant.RIGHT);
        break;
    }
  }

  /**
   * 跳转secondActivity
   */
  private void toSecondActivity(int whichAnima) {
    Intent intent = new Intent(MainActivity.this, SecondActivity.class);
    intent.putExtra("param", whichAnima);
    startActivity(intent);
    //取消默认的activity切换动画
    overridePendingTransition(0, 0);
  }
}
