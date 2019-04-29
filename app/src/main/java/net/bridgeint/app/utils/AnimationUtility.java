package net.bridgeint.app.utils;

import android.view.View;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;


public class AnimationUtility {

    public AnimationUtility() {
        //
    }

    public static void dropOut(View view){
        YoYo.with(Techniques.DropOut)
                .duration(700)
                .playOn(view);
    }

    public static void fadeIn(View view){
        YoYo.with(Techniques.FadeIn)
                .duration(500)
                .playOn(view);
    }

    public static void fadeOut(View view){
        YoYo.with(Techniques.FadeOut)
                .duration(300)
                .playOn(view);
    }

    public static void slideUp(View view){
        YoYo.with(Techniques.SlideInUp)
                .duration(500)
                .playOn(view);
    }

    public static void slideInDown(View view){
        YoYo.with(Techniques.SlideInDown)
                .duration(300)
                .playOn(view);
    }



    public static void slideOutDown(View view){
        YoYo.with(Techniques.SlideOutDown)
                .duration(500)
                .playOn(view);
    }

    public static void slideOutUp(View view){
        YoYo.with(Techniques.SlideOutUp)
                .duration(300)
                .playOn(view);
    }

    public static void pluse(View view){
        YoYo.with(Techniques.Pulse)
                .duration(500)
                .playOn(view);
    }

    public static void tada(View view){
        YoYo.with(Techniques.Tada)
                .duration(500)
                .playOn(view);
    }

    public static void bounceIn(View view){
        YoYo.with(Techniques.BounceIn)
                .duration(500)
                .playOn(view);
    }

    public static void shake(View view){
        YoYo.with(Techniques.Shake)
                .duration(500)
                .playOn(view);
    }

    public static void rubber(View view){
        YoYo.with(Techniques.RubberBand)
                .duration(500)
                .playOn(view);
    }

    public static void slideInLeft(View view){
        YoYo.with(Techniques.SlideInLeft)
                .duration(500)
                .playOn(view);
    }

    public static void slideInRight(View view){
        YoYo.with(Techniques.SlideInRight)
                .duration(500)
                .playOn(view);
    }

    public static void bounce(View view){
        YoYo.with(Techniques.Bounce)
                .duration(500)
                .playOn(view);
    }

    public static void flash(View view){
        YoYo.with(Techniques.Flash)
                .duration(500)
                .playOn(view);
    }

}
