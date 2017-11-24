package net.joedoe.helpers;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import net.joedoe.GameMain;
import net.joedoe.logics.FightLogic;

public class FightHud extends Hud {
    private FightLogic fight;
    private Label message, name, ap;

    public FightHud(GameMain game, FightLogic fight) {
        super(game);
        this.fight = fight;
        createTable();
    }

    void createTable() {
        message = new Label("", skin);
//        table = new Table(skin);
//        table.setDebug(true);
//        table.bottom().left();
//        table.pad(10);
//        table.setFillParent(true);
//        table.add(message).left().width(200);
//        table.row().padTop(12);
//        table.add(name).left();
//        table.row().padTop(5).expandX();
//        table.add(ap).left().padLeft(20);
//        stage.addActor(table);
        
        
        table = new Table(skin);
        table.debug();
        table.bottom().left();
        table.pad(10);
        table.setFillParent(true);
        table.add("PLAYER:").top().left().width(100);
        table.add("XXX").padLeft(10).width(100).left();
        table.add("ENEMY1:").padLeft(10).width(100).left();
        table.add("XXX").left().padLeft(10).width(100).left();
        table.add("ENEMY2:").padLeft(10).width(100).left();
        table.add("YYY").padLeft(10).width(100).left();
        stage.addActor(table);
    }

    public void update() {
        message.setText(fight.getMessage());
    }
}
