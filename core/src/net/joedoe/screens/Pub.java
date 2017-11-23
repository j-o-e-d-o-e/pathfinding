package net.joedoe.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import net.joedoe.GameInfo;
import net.joedoe.GameMain;
import net.joedoe.helpers.GameManager;

public class Pub extends Place {
    protected TextButton button4;
    protected int beerCount, mealCount, storyStage;
    protected boolean askedForJob;

    public Pub(GameMain game) {
        super(game);
        title.setColor(GameInfo.RED[0], GameInfo.RED[1], GameInfo.RED[2], 1);
        title.getData().setScale(1.7f);
        createTable();
    }

    public void createTable() {
        message = new Label(name + " enters the pub.", skin);
        button1 = new TextButton(" 5 $ ", skin);
        button2 = new TextButton(" 10 $ ", skin);
        button3 = new TextButton(" Chat ", skin);
        button4 = new TextButton(" Ask ", skin);
        buttonLeave = new TextButton(" Leave ", skin);
        button1.addListener(listener);
        button2.addListener(listener);
        button3.addListener(listener);
        button4.addListener(listener);
        buttonLeave.addListener(listener);
        table = new Table(skin);
        // table.debug();
        table.setPosition(-25, 0);
        table.setFillParent(true);
        table.add(message).width(160).left();
        table.row().padTop(10);
        table.add("Drink a beer:").left();
        table.add(button1).left().width(50);
        table.row().padTop(10);
        table.add("Eat fish n' chips:").left();
        table.add(button2).left().width(50);
        table.row().padTop(10);
        table.add("Chat with a guest:").left();
        table.add(button3).left().width(50);
        table.row().padTop(10);
        table.add("Ask for a job:").left();
        table.add(button4).left().width(50);
        table.row().padTop(10);
        table.add(buttonLeave).padLeft(60);
        stage.addActor(table);
    }

    public void handleInput(String choice) {
        if (choice.contains("Leave")) {
            dispose();
            game.setScreen(new City(game));
        } else {
            if (choice.contains("5")) {
                if (beerCount > 3 && hud.player.getHealth() <= 10) {
                    message.setText(name + " is drunk. The Bartender refuses to serve him another drink.");
                } else if (hud.player.getMoney() >= 5) {
                    hud.player.changeMoney(-5);
                    beerCount++;
                    if (beerCount > 3) {
                        hud.player.changeHealth(-5);
                        message.setText(name + " gets drunk: -5 health.");
                    } else {
                        message.setText(name + " drinks a beer.");
                    }
                } else {
                    message.setText(name + " doesn't have enough money!");
                }
                hud.update();
            } else if (choice.contains("10")) {
                if (hud.player.getMoney() >= 10) {
                    hud.player.changeMoney(-10);
                    hud.player.changeHealth(5);
                    mealCount++;
                    if (mealCount > 2) {
                        hud.player.changeMoney(-10);
                        message.setText(name + " can't eat more!");
                    } else {
                        message.setText(name + " eats a delicious meal.");
                    }
                } else {
                    message.setText(name + " doesn't have enough money!");
                }
                hud.update();
            } else if (choice.contains("Chat")) {
                chatWithAGuest();
            } else if (choice.contains("Ask")) {
                if (!askedForJob) {
                    askedForJob = true;
                    int rand = ((int) (Math.random() * (10 - 1))) + 1;
                    if (rand > 6) {
                        askedForJob = false;
                        rand = ((int) (Math.random() * (10 - 1))) + 1;
                        if (rand > 6) {
                            message.setText("");
                            storyStage = 89;
                            hud.showStory(storyStage);
                        } else {
                            message.setText("");
                            storyStage = 88;
                            hud.showStory(storyStage);
                        }
                    } else {
                        message.setText("The barkeeper has currently no job for " + name + ".");
                    }
                } else {
                    message.setText("The barkeeper already told " + name + " that he has no jobs.");
                }
            }
        }
    }

    void handleStoryInput() {
        if (storyStage == 88) {
            if (Gdx.input.isKeyJustPressed(Keys.Y)) {
                hud.player.changeMoney(25);
                hud.update();
                message.setText(name + " does the dishwashing.");
                storyStage = -1;
                hud.removeStory();
            } else if (Gdx.input.isKeyJustPressed(Keys.N)) {
                message.setText(name + " declines the job offer.");
                hud.removeStory();
            }
        } else if (storyStage == 89) {
            if (Gdx.input.isKeyJustPressed(Keys.Y)) {
                hud.player.changeMoney(50);
                hud.update();
                message.setText(name + " hands out the flyers successfully.");
                storyStage = -1;
                hud.removeStory();
            } else if (Gdx.input.isKeyJustPressed(Keys.N)) {
                message.setText(name + " declines the job offer.");
                hud.removeStory();
            }
        }
    }

    void chatWithAGuest() {
        if (beerCount == 0 && mealCount == 0) {
            message.setText(name + " needs to order something, first.");
        } else if (beerCount < 5) {
            message.setText(name + " has a pleasant chat with a stranger.");
        } else {
            message.setText("Nobody wants to talk to " + name + ".");
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(GameInfo.BLUE[0], GameInfo.BLUE[1], GameInfo.BLUE[2], 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.getBatch().begin();
        title.draw(game.getBatch(), "RAILWAY TAVERN", GameInfo.WIDTH / 2f - 397, GameInfo.HEIGHT / 2f + 190);
        game.getBatch().end();
        game.getBatch().setProjectionMatrix(hud.getStage().getCamera().combined);
        stage.draw();
        stage.act();
        hud.getStage().draw();
        hud.getStage().act();
        if (GameManager.storyMode) {
            handleStoryInput();
        }
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        title.dispose();
    }
}
