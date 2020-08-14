package com.kpgn.tictactoe.activity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.kpgn.tictactoe.R;
import com.kpgn.tictactoe.entity.GameState;
import com.kpgn.tictactoe.entity.Player;
import com.kpgn.tictactoe.processor.GameProcessor;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GameActivity extends AppCompatActivity {

    @BindView(R.id.game_board)
    View mGameBoard;

    @BindViews({R.id.image_00, R.id.image_01, R.id.image_02})
    ImageView[] imageViewListRow1;

    @BindViews({R.id.image_10, R.id.image_11, R.id.image_12})
    ImageView[] imageViewListRow2;

    @BindViews({R.id.image_20, R.id.image_21, R.id.image_22})
    ImageView[] imageViewListRow3;

    @BindViews({R.id.container_00, R.id.container_01, R.id.container_02})
    View[] containerListRow1;

    @BindViews({R.id.container_10, R.id.container_11, R.id.container_12})
    View[] containerListRow2;

    @BindViews({R.id.container_20, R.id.container_21, R.id.container_22})
    View[] containerListRow3;

    @BindViews({
            R.id.image_00, R.id.image_01, R.id.image_02,
            R.id.image_10, R.id.image_11, R.id.image_12,
            R.id.image_20, R.id.image_21, R.id.image_22
    })
    ImageView[] tintedImageList;

    @BindView(R.id.tv_x_score)
    TextView mPlayerXScore;

    @BindView(R.id.tv_y_score)
    TextView mPlayerYScore;

    @BindView(R.id.tv_result)
    TextView mResult;

    private ImageView[][] imageViewList = new ImageView[3][3];
    private View[][] containerList = new View[3][3];
    private static Player currentPlayer = Player.FIRST;

    private static int totalMovesCompleted = 0;
    private static int playerXWinCounter = 0;
    private static int playerYWinCounter = 0;

    private static char[][] currentGameBoard = {
            {'-', '-', '-'},
            {'-', '-', '-'},
            {'-', '-', '-'}
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        ButterKnife.bind(this);
        initValues();
        resetValues();
    }

    @OnClick({R.id.container_00, R.id.container_01, R.id.container_02,
            R.id.container_10, R.id.container_11, R.id.container_12,
            R.id.container_20, R.id.container_21, R.id.container_22})
    public void ctaContainerClicked(View view) {
        updateState(view.getTag().toString());
        togglePlayer();
    }

    private void initValues() {
        imageViewList[0] = imageViewListRow1;
        imageViewList[1] = imageViewListRow2;
        imageViewList[2] = imageViewListRow3;

        containerList[0] = containerListRow1;
        containerList[1] = containerListRow2;
        containerList[2] = containerListRow3;
    }

    private void resetValues() {
        for (View[] parentView : containerList) {
            for (View childView : parentView) {
                childView.setClickable(true);
            }
        }

        for (ImageView[] parentView : imageViewList) {
            for (ImageView childView : parentView) {
                childView.setImageDrawable(null);
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                currentGameBoard[i][j] = '-';
            }
        }

        for (ImageView imageView : tintedImageList) {
            imageView.setColorFilter(null);
        }

        mResult.setText("");
        totalMovesCompleted = 0;
        currentPlayer = Player.FIRST;
    }

    private void updateState(String selectedTag) {
        char[] xyIndex = selectedTag.toCharArray();
        int xIndex = Integer.parseInt(String.valueOf(xyIndex[0]));
        int yIndex = Integer.parseInt(String.valueOf(xyIndex[1]));

        if (currentPlayer == Player.FIRST) {
            imageViewList[xIndex][yIndex].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_x));
            currentGameBoard[xIndex][yIndex] = Player.FIRST.getPlayerId();
        } else {
            imageViewList[xIndex][yIndex].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_o));
            currentGameBoard[xIndex][yIndex] = Player.SECOND.getPlayerId();
        }

        if (++totalMovesCompleted > 4) {
            updateResult(GameProcessor.checkEndState(currentGameBoard, currentPlayer));
        }

        containerList[xIndex][yIndex].setClickable(false);
    }

    private void togglePlayer() {
        if (currentPlayer == Player.FIRST) {
            currentPlayer = Player.SECOND;
        } else {
            currentPlayer = Player.FIRST;
        }
    }

    private void updateResult(GameState gameState) {
        boolean isGameOver = gameState.currentWinningState != -1;
        if (isGameOver) {
            if (currentPlayer.getPlayerId() == Player.FIRST.getPlayerId()) {
                mPlayerXScore.setText(String.valueOf(++playerXWinCounter));
            } else {
                mPlayerYScore.setText(String.valueOf(++playerYWinCounter));
            }
            updatedTileTint(gameState);
            showFullScreenDialog(String.format(getResources().getString(R.string.game_over_text), currentPlayer.getPlayerId()));
        } else if (totalMovesCompleted == 9) {
            showFullScreenDialog(getResources().getString(R.string.game_draw_text));
        }
    }

    private void updatedTileTint(GameState gameState) {
        for (int index : gameState.selectedWinningState) {
            tintedImageList[index].setColorFilter(ContextCompat.getColor(this, R.color.colorWinner), android.graphics.PorterDuff.Mode.MULTIPLY);
        }
    }

    private void showFullScreenDialog(String message) {
        try {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.view_game_over);
            final Window window = dialog.getWindow();
            assert window != null;
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setOnDismissListener(dialogInterface -> resetValues());
            dialog.setOnCancelListener(dialogInterface -> resetValues());
            ((TextView) dialog.findViewById(R.id.tv_overlay_result)).setText(message);
            dialog.findViewById(R.id.view_overlay).setOnClickListener(view -> dialog.dismiss());
            dialog.show();
        } catch (Exception e) {
            resetValues();
        }
    }
}