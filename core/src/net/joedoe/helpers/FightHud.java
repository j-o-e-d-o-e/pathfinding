package net.joedoe.helpers;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import net.joedoe.GameMain;
import net.joedoe.logics.FightLogic;

public class FightHud extends Hud {
    private FightLogic fight;
    private Label message, name, health, ap, stats, ammoStock, weapon, ammo;

    public FightHud(GameMain game, FightLogic fight) {
        super(game);
        this.fight = fight;
        createTable();
    }

    void createTable() {
        message = new Label("", skin);
        name = new Label(player.getName().toUpperCase(), skin);
        health = new Label("", skin);
        ap = new Label("", skin);
        weapon = new Label("", skin);
        stats = new Label("", skin);
        stats.setText("S/D/I: " + player.getStrength() + "/" + player.getDexterity() + "/" + player.getIntelligence());
        ammoStock = new Label("", skin);
        weapon = new Label("", skin);
        ammo = new Label("", skin);
        table = new Table(skin);
        // table.setDebug(true);
        table.bottom().left();
        table.pad(10);
        table.setFillParent(true);
        table.add(message).left().width(200);
        table.row().padTop(12);
        table.add(name).left();
        table.row().padTop(5).expandX();
        table.add(health).left();
        table.add(ap).left().padLeft(20);
        table.add(weapon).left().padLeft(20).width(200);
        table.row().padTop(5);
        table.add(stats).left();
        table.add(ammoStock).left().padLeft(20).width(200);
        table.add(ammo).left().padLeft(20);
        stage.addActor(table);
    }

    public void update() {
        message.setText(fight.getMessage());
        health.setText("Health: " + player.getHealth() + " %");
        ap.setText("Action points: " + player.getActionPoints() + "/" + player.getStrength() / 5);
    }
}
