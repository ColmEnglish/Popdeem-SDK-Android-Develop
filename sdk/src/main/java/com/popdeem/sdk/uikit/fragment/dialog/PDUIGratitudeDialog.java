package com.popdeem.sdk.uikit.fragment.dialog;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.popdeem.sdk.R;
import com.popdeem.sdk.core.model.PDReward;
import com.popdeem.sdk.core.realm.PDRealmCustomer;
import com.popdeem.sdk.core.realm.PDRealmUserDetails;
import com.popdeem.sdk.uikit.fragment.PDUIHomeFlowFragment;
import com.popdeem.sdk.uikit.widget.PDAmbassadorView;

import io.realm.Realm;

/**
 * Created by colm on 27/02/2018.
 */

public class PDUIGratitudeDialog extends Dialog {





    public PDUIGratitudeDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    public static PDUIGratitudeDialog showGratitudeDialog(Context context, String type){
        return showGratitudeDialog(context,type, null);
    }
    public static PDUIGratitudeDialog showGratitudeDialog(final Context context, String type, PDReward reward){

        final PDUIGratitudeDialog dialog = new PDUIGratitudeDialog(context, R.style.FullScreenDialogStyle);
        dialog.setCancelable(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_pduigratitude);
//        dialog.setOnCancelListener(onCancelListener);
        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        PDAmbassadorView ambassadorView = (PDAmbassadorView) dialog.findViewById(R.id.pd_gratitude_ambassador_view);
        Button profileButton = (Button) dialog.findViewById(R.id.pd_gratitude_profile_button);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Realm  mRealm = Realm.getDefaultInstance();
        PDRealmUserDetails mUser = mRealm.where(PDRealmUserDetails.class).findFirst();

        PDRealmCustomer customerDetails = mRealm.where(PDRealmCustomer.class).findFirst();
        int increment = 0;
        if(customerDetails!=null){
            increment = customerDetails.getIncrement_advocacy_points();
        }


        if(type.equalsIgnoreCase("share")) {
            ((TextView) dialog.findViewById(R.id.pd_gratitude_title)).setText(context.getString(R.string.pd_gratitude_sweet_ribs_and_burgers));
            ((TextView) dialog.findViewById(R.id.pd_gratitude_description)).setText(context.getString(R.string.pd_gratitude_share_body_text));
            if (mUser != null) {
                if (mUser.getAdvocacyScore()+increment < 30) {
                    ambassadorView.setLevel(0, true);
                } else if (mUser.getAdvocacyScore()+increment <= 60) {
                    ambassadorView.setLevel(1, true);
                } else if (mUser.getAdvocacyScore()+increment <= 90) {
                    ambassadorView.setLevel(2, true);
                } else {
                    ambassadorView.setLevel(3, true);
                }
            }
        }else{
            ((TextView) dialog.findViewById(R.id.pd_gratitude_title)).setText(context.getString(R.string.pd_gratitude_thanks_for_connecting));
            ((TextView) dialog.findViewById(R.id.pd_gratitude_description)).setText(context.getString(R.string.pd_gratitude_connect_body_text));
            if (mUser != null) {
                if (mUser.getAdvocacyScore() < 30) {
                    ambassadorView.setLevel(0, true);
                } else if (mUser.getAdvocacyScore() <= 60) {
                    ambassadorView.setLevel(1, true);
                } else if (mUser.getAdvocacyScore() <= 90) {
                    ambassadorView.setLevel(2, true);
                } else {
                    ambassadorView.setLevel(3, true);
                }
            }
        }






        mRealm.close();
        return dialog;
    }

}