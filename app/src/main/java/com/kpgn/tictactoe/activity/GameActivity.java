package com.kpgn.tictactoe.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kpgn.tictactoe.BuildConfig;
import com.kpgn.tictactoe.R;
import com.kpgn.tictactoe.entity.GameState;
import com.kpgn.tictactoe.entity.Player;
import com.kpgn.tictactoe.helper.DialogHelper;
import com.kpgn.tictactoe.helper.SharedPreferenceHelper;
import com.kpgn.tictactoe.processor.AIMoveProcessor;
import com.kpgn.tictactoe.processor.GameStateProcessor;

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

    @BindView(R.id.tv_o_score)
    TextView mPlayerOScore;

    @BindView(R.id.tv_result)
    TextView mResult;

    @BindView(R.id.tv_app_version)
    TextView mAppVersion;

    private ImageView[][] imageViewList = new ImageView[3][3];
    private View[][] containerList = new View[3][3];
    private char[][] currentGameBoard = new char[3][3];
    private Player currentPlayer = Player.FIRST;
    private int totalMovesCompleted = 0;
    private int playerXWinCounter = 0;
    private int playerOWinCounter = 0;

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
        if (currentPlayer == Player.FIRST) {
            updateState(view.getTag().toString());
        }
    }

    private void initValues() {
        mAppVersion.setText(String.format(getResources().getString(R.string.app_version_text), BuildConfig.VERSION_NAME));

        imageViewList[0] = imageViewListRow1;
        imageViewList[1] = imageViewListRow2;
        imageViewList[2] = imageViewListRow3;

        containerList[0] = containerListRow1;
        containerList[1] = containerListRow2;
        containerList[2] = containerListRow3;

        getLocalScore();

        mPlayerXScore.setText(String.valueOf(playerXWinCounter));
        mPlayerOScore.setText(String.valueOf(playerOWinCounter));
    }

    private void getLocalScore() {
        playerXWinCounter = SharedPreferenceHelper.getXScore();
        playerOWinCounter = SharedPreferenceHelper.getOScore();
    }

    private void setLocalScore() {
        SharedPreferenceHelper.setXScore(playerXWinCounter);
        SharedPreferenceHelper.setOScore(playerOWinCounter);
    }

    public void resetValues() {
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
                currentGameBoard[i][j] = Player.EMPTY.getPlayerId();
            }
        }

        for (ImageView imageView : tintedImageList) {
            imageView.setColorFilter(null);
        }

        mResult.setText(R.string.players_turn);
        mResult.setVisibility(View.VISIBLE);
        totalMovesCompleted = 0;
        currentPlayer = Player.FIRST;
    }

    private void updateState(String selectedTag) {
        if (selectedTag == null) {
            DialogHelper.showFullScreenDialog(this, this, getResources().getString(R.string.game_draw_text));
            mResult.setText("");
            return;
        }
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
        updateResult(GameStateProcessor.checkEndState(currentGameBoard, currentPlayer));
        containerList[xIndex][yIndex].setClickable(false);
    }

    private void togglePlayer() {
        if (currentPlayer == Player.FIRST) {
            currentPlayer = Player.SECOND;
            mResult.setText(R.string.computers_turn);
            (new Handler()).postDelayed(this::makeAIMove, 500);
        } else {
            currentPlayer = Player.FIRST;
            mResult.setText(R.string.players_turn);
        }
    }

    private void makeAIMove() {
        updateState(AIMoveProcessor.getAIMove(currentGameBoard));
    }

    private void updateResult(GameState gameState) {
        boolean isGameOver = gameState.currentWinningState != -1;
        if (isGameOver) {
            mResult.setText("");
            mResult.setVisibility(View.GONE);
            if (currentPlayer.getPlayerId() == Player.FIRST.getPlayerId()) {
                mPlayerXScore.setText(String.valueOf(++playerXWinCounter));
            } else {
                mPlayerOScore.setText(String.valueOf(++playerOWinCounter));
            }
            DialogHelper.showFullScreenDialog(this, this, String.format(getResources().getString(R.string.game_over_text), Player.toString(currentPlayer.getPlayerId())));
            updatedTileTint(gameState);
            setLocalScore();
        } else if (totalMovesCompleted == 9) {
            mResult.setText("");
            mResult.setVisibility(View.GONE);
            DialogHelper.showFullScreenDialog(this, this, getResources().getString(R.string.game_draw_text));
        } else {
            togglePlayer();
        }
    }

    private void updatedTileTint(GameState gameState) {
        for (int index : gameState.selectedWinningState) {
            tintedImageList[index].setColorFilter(ContextCompat.getColor(this, R.color.colorWinner), android.graphics.PorterDuff.Mode.MULTIPLY);
        }
    }
}