package net.joedoe.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import net.joedoe.GameInfo;
import net.joedoe.GameMain;

public class Casino extends Place {

    public Casino(GameMain game) {
        super(game);
        title.setColor(GameInfo.RED[0], GameInfo.RED[1], GameInfo.RED[2], 1);
        createTable();
    }

    public void createTable() {
        message = new Label(name + " enters the casino.", skin);
        button1 = new TextButton(" 5 $ ", skin);
        button2 = new TextButton(" 10 $ ", skin);
        button3 = new TextButton(" 15 $ ", skin);
        buttonLeave = new TextButton(" Leave ", skin);
        button1.addListener(listener);
        button2.addListener(listener);
        button3.addListener(listener);
        buttonLeave.addListener(listener);
        table = new Table(skin);
        // table.debug();
        table.setPosition(-25, 0);
        table.setFillParent(true);
        table.add(message).width(160).left();
        table.row().padTop(10);
        table.add("Play blackjack:").left();
        table.add(button1).left().width(50);
        table.row().padTop(10);
        table.add("Play roulette:").left();
        table.add(button2).left().width(50);
        table.row().padTop(10);
        table.add("Play poker:").left();
        table.add(button3).left().width(50);
        table.row().padTop(10);
        table.row().padTop(10);
        table.add(buttonLeave).padLeft(60);
        stage.addActor(table);
    }

    public void handleInput(String choice) {
        if (choice.contains("Leave")) {
            dispose();
            game.setScreen(new City(game));
        } else {
            if (choice.contains("15")) {
                playPoker();
            } else if (choice.contains("10")) {
                if (hud.player.getMoney() >= 10) {
                    int rand = ((int) (Math.random() * (10 - 1))) + 1;
                    if (rand > 7) {
                        hud.player.changeMoney(20);
                        message.setText(name + " has won!");
                    } else {
                        hud.player.changeMoney(-10);
                        message.setText(name + " has lost!");
                    }
                } else {
                    message.setText(name + " doesn't have enough money!");
                }
                hud.update();
            } else if (choice.contains("5")) {
                if (hud.player.getMoney() >= 5) {
                    int rand = ((int) (Math.random() * (10 - 1))) + 1;
                    if (rand > 6) {
                        hud.player.changeMoney(+10);
                        message.setText(name + " has won!");
                    } else {
                        hud.player.changeMoney(-5);
                        message.setText(name + " has lost!");
                    }
                } else {
                    message.setText(name + " doesn't have enough money!");
                }
                hud.update();
            }
        }
    }

    void playPoker() {
        if (hud.player.getMoney() >= 15) {
            int rand = ((int) (Math.random() * (10 - 1))) + 1;
            if (rand > 8) {
                hud.player.changeMoney(30);
                message.setText(name + " has won!");
            } else {
                hud.player.changeMoney(-15);
                message.setText(name + " has lost!");
            }
        } else {
            message.setText(name + " doesn't have enough money!");
        }
        hud.update();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(GameInfo.GREEN[0], GameInfo.GREEN[1], GameInfo.GREEN[2], 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.getBatch().begin();
        title.draw(game.getBatch(), "CASINO", GameInfo.WIDTH / 2f - 200, GameInfo.HEIGHT / 2f + 200);
        game.getBatch().end();
        game.getBatch().setProjectionMatrix(hud.getStage().getCamera().combined);
        stage.draw();
        stage.act();
        hud.getStage().draw();
        hud.getStage().act();
    }

    @Override
    public void dispose() {
        stage.dispose();
        title.dispose();
        skin.dispose();
    }
}
