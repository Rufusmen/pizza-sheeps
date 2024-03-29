package com.codingame.game;

import com.codingame.game.actions.AbstractAction;
import com.codingame.game.actions.InvalidAction;
import com.codingame.gameengine.core.AbstractPlayer.TimeoutException;
import com.codingame.gameengine.core.AbstractReferee;
import com.codingame.gameengine.core.GameManager;
import com.codingame.gameengine.core.MultiplayerGameManager;
import com.codingame.gameengine.module.endscreen.EndScreenModule;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Sprite;
import com.codingame.gameengine.module.entities.Text;
import com.codingame.gameengine.module.tooltip.TooltipModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


/**
 * Main class of the game.
 */
public class Referee extends AbstractReferee {

    @Inject
    private MultiplayerGameManager<Player> gameManager;
    @Inject
    private GraphicEntityModule graphicEntityModule;
    @Inject
    private Provider<GameState> gameStateProvider;
    @Inject
    private TooltipModule tooltips;
    @Inject EndScreenModule endScreenModule;

    private GameState state;
    private List<AbstractAction> validActions;

    private final Map<Integer,Text> texts = new HashMap<>();

    @Override
    public void init() {
        Random random = new Random(gameManager.getSeed());
        state = gameStateProvider.get();
        gameManager.setMaxTurns(state.init(random));
        drawBackground();
        drawHud();
        drawGrids();

        gameManager.setFrameDuration(100);

        sendInitInputs();


    }

    private void sendInitInputs() {
        for (Player p : gameManager.getActivePlayers()) {
            p.setPawns(state.playerPawnsAmount());
            p.sendInputLine(String.format("%d", p.getIndex()));
            p.sendInputLine(state.getConstsString());
            p.sendInputLine(state.getCounts());
        }
    }

    private void drawBackground() {
        graphicEntityModule.createSprite()
            .setImage("old-wood-plank-background.jpg")
            .setAnchor(0);
        graphicEntityModule.createSprite()
            .setImage("sheep_logo.png")
            .setX(1530)
            .setY(40)
            .setAnchor(0);
    }

    private void drawGrids() {
        int cellSize = 100;
        state.drawInit(40, 10, cellSize);
    }

    private void drawHud() {
        for (Player player : gameManager.getPlayers()) {
            int x = 1920 - 280;
            int y = player.getIndex() == 0 ? 620 : 1080 - 220;

            graphicEntityModule
                .createRectangle()
                .setWidth(140)
                .setHeight(140)
                .setX(x - 70)
                .setY(y - 70)
                .setLineWidth(0)
                .setFillColor(player.getColorToken());

            graphicEntityModule
                .createRectangle()
                .setWidth(120)
                .setHeight(120)
                .setX(x - 60)
                .setY(y - 60)
                .setLineWidth(0)
                .setFillColor(0xffffff);

            Text text = graphicEntityModule.createText(player.getNicknameToken())
                .setX(x+50)
                .setY(y + 120)
                .setZIndex(20)
                .setFontSize(40)
                .setFillColor(0xffffff)
                .setAnchor(0.5);
            texts.put(player.getIndex(),text);

            Sprite avatar = graphicEntityModule.createSprite()
                .setX(x)
                .setY(y)
                .setZIndex(20)
                .setImage(player.getAvatarToken())
                .setAnchor(0.5)
                .setBaseHeight(116)
                .setBaseWidth(116);

            player.hud = graphicEntityModule.createGroup(text, avatar);
        }
    }

    private void sendPlayerInputs() {
        List<String> sheeps = state.getSheepsInput();
        List<String> sheds = state.getShedsInput();
        for (Player p : gameManager.getActivePlayers()) {
            sheeps.forEach(p::sendInputLine);
            state.getShepherdsInput(p).forEach(p::sendInputLine);
            state.getDogsInput().forEach(p::sendInputLine);
            sheds.forEach(p::sendInputLine);
            p.execute();
        }
    }


    private void setWinner(Player player) {
        gameManager.addToGameSummary(GameManager.formatSuccessMessage(player.getNicknameToken() + " won!"));
    }



    @Override
    public void gameTurn(int turn) {

        sendPlayerInputs();

        // Read inputs
        List<AbstractAction> actions = new ArrayList<>();
        for (Player player : gameManager.getActivePlayers()) {
            try {
                actions.addAll(player.getActions());
            } catch (NumberFormatException e) {
                player.deactivate("Wrong output!");
                player.setScore(-1);
                endGame();
            } catch (TimeoutException e) {
                gameManager.addToGameSummary(GameManager.formatErrorMessage(player.getNicknameToken() + " timeout!"));
                player.deactivate(player.getNicknameToken() + " timeout!");
                player.setScore(-1);
                endGame();
            } catch (InvalidAction e) {
                gameManager.addToGameSummary(GameManager.formatErrorMessage(player.getNicknameToken() + " " + e.getMessage()));
                player.deactivate(player.getNicknameToken() + " invalid action!");
                player.setScore(-1);
                endGame();
            }
        }
        state.resolveActions(actions);
        state.onTurnEnd();
        state.draw();
        state.updateTooltip(tooltips);
        updateScore();
    }

    private void updateScore() {
        for (Player p : gameManager.getActivePlayers()) {
            p.setScore(state.getScore(p.getIndex()));
        };
        Player p0 = gameManager.getPlayers().get(0);
        Player p1 = gameManager.getPlayers().get(1);
        texts.get(0).setText(p0.getNicknameToken() + " score: " + p0.getScore());
        texts.get(1).setText(p1.getNicknameToken() + " score: " + p1.getScore());
    }

    @Override
    public void onEnd() {
        endGame();
        endScreenModule.setTitleRankingsSprite("logoend.png");
        endScreenModule.setScores(gameManager.getPlayers().stream().mapToInt(p -> p.getScore()).toArray());
    }

    private void endGame() {
        gameManager.endGame();
        Player p0 = gameManager.getPlayers().get(0);
        Player p1 = gameManager.getPlayers().get(1);
        if (p0.getScore() > p1.getScore()) {
            p1.hud.setAlpha(0.3);
            setWinner(p0);
        }
        if (p0.getScore() < p1.getScore()) {
            p0.hud.setAlpha(0.3);
            setWinner(p1);
        }
    }
}
