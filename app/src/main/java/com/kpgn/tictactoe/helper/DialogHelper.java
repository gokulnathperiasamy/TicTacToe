package com.kpgn.tictactoe.helper;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.kpgn.tictactoe.R;
import com.kpgn.tictactoe.activity.GameActivity;

public class DialogHelper {

    public static void showFullScreenDialog(Context context, GameActivity activity, String message) {
        try {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.view_game_over);
            final Window window = dialog.getWindow();
            assert window != null;
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setOnDismissListener(dialogInterface -> activity.resetValues());
            dialog.setOnCancelListener(dialogInterface -> activity.resetValues());
            ((TextView) dialog.findViewById(R.id.tv_overlay_result)).setText(message);
            dialog.findViewById(R.id.view_overlay).setOnClickListener(view -> dialog.dismiss());
            dialog.show();
        } catch (Exception e) {
            activity.resetValues();
        }
    }
}
