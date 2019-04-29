package net.bridgeint.app.dialogs;


import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import net.bridgeint.app.R;
import net.bridgeint.app.utils.AnimationUtility;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationDialog extends DialogFragment {
    private static MyClickListener myClickListener;

    public interface MyClickListener {
        void gotoChat();
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        NotificationDialog.myClickListener = myClickListener;
    }

    View view;
    Activity activity;
    Handler handler;
    TextView tvChat;
    TextView tvCancel;
    ImageButton ibClose;

    public NotificationDialog() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        try {
            view = inflater.inflate(R.layout.dialog_notification, container, false);
            handler = new Handler();

            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme);

            tvChat = view.findViewById(R.id.tvChat);
            tvCancel = view.findViewById(R.id.tvCancel);

            tvChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    myClickListener.gotoChat();
                }
            });
            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismissWithAnimation();
                }
            });


            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.findViewById(R.id.root).setVisibility(View.VISIBLE);
                    AnimationUtility.slideInDown(view.findViewById(R.id.root));
                }
            }, 300);
        } catch (Exception e) {
            Log.e("oncreate ", e.toString());
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new Dialog(getActivity(), getTheme()) {
            @Override
            public void onBackPressed() {
                dismissWithAnimation();
            }
        };
    }

    private void dismissWithAnimation() {
        try {
            AnimationUtility.slideOutUp(view.findViewById(R.id.root));
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getDialog().dismiss();
                }
            }, 500);
        } catch (Exception e) {
            Log.e("oncreate ", e.toString());
            e.printStackTrace();
        }
    }

}
