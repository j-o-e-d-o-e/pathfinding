package net.joedoe.helpers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import net.joedoe.GameMain;

public class MainHud extends Hud {
    private Image healthImg, moneyImg;
    private Label health, money;

    public MainHud(GameMain game) {
        super(game);
        createTable();
    }

    void createTable() {
        healthImg = new Image(new Texture("images/health.png"));
        health = new Label(player.getHealth() + " %", skin);
        moneyImg = new Image(new Texture("images/money.png"));
        money = new Label(player.getMoney() + " $", skin);
        table = new Table(skin);
        // table.debug();
        table.top().left();
        table.setFillParent(true);
        table.add(healthImg).padLeft(10).padTop(10);
        table.add(health).padLeft(5).left().bottom();
        table.row();
        table.add(moneyImg).padLeft(10);
        table.add(money).padLeft(5).left();
        stage.addActor(table);
    }

    public void update() {
        health.setText(player.getHealth() + " %");
        money.setText(player.getMoney() + " $");
    }
}