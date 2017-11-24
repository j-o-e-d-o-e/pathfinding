package net.joedoe.helpers;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import net.joedoe.GameMain;
import net.joedoe.logics.FightLogic;

public class FightHud extends Hud {
	private FightLogic fight;
	private Label playerAP, playerXY, enemy1XY, enemy2XY, enemy1AP, enemy2AP;

	public FightHud(GameMain game, FightLogic fight) {
		super(game);
		this.fight = fight;
		createTable();
	}

	void createTable() {
		playerAP = new Label("", skin);
		playerXY = new Label("", skin);
		enemy1XY = new Label("", skin);
		enemy2XY = new Label("", skin);
		enemy1AP = new Label("", skin);
		enemy2AP = new Label("", skin);
		table = new Table(skin);
		// table.debug();
		table.bottom().left();
		table.pad(10);
		table.setFillParent(true);
		table.add("").top().width(50).left();
		table.add(fight.getPlayer().getName().toUpperCase()).padLeft(10).width(80).left();
		table.add(fight.getEnemies().get(0).getName().toUpperCase()).padLeft(10).width(80).left();
		table.add(fight.getEnemies().get(1).getName().toUpperCase()).padLeft(10).width(80).left();
		table.row().padTop(5);
		table.add("X/Y:").top().left();
		table.add(playerXY).padLeft(10).left();
		table.add(enemy1XY).padLeft(10).left();
		table.add(enemy2XY).padLeft(10).left();
		table.row().padTop(10);
		table.add("STEPS:").top().left();
		table.add(playerAP).padLeft(10).left();
		table.add(enemy1AP).padLeft(10).left();
		table.add(enemy2AP).padLeft(10).left();
		stage.addActor(table);
	}

	public void update() {
		playerAP.setText(fight.getPlayer().getActionPoints() + "/" + fight.getPlayer().getStrength());
		enemy1AP.setText(fight.getEnemies().get(0).getActionPoints() + "/" + fight.getEnemies().get(0).getStrength());
		enemy2AP.setText(fight.getEnemies().get(1).getActionPoints() + "/" + fight.getEnemies().get(0).getStrength());
		playerXY.setText(fight.getPlayerXY());
		enemy1XY.setText(fight.getEnemy1XY());
		enemy2XY.setText(fight.getEnemy2XY());
	}
}
