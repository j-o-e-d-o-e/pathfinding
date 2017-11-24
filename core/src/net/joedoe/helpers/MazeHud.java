package net.joedoe.helpers;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import net.joedoe.GameMain;
import net.joedoe.logics.MazeLogic;

public class MazeHud extends Hud {
	private MazeLogic fight;
	private Label cheeseXY, mouse1XY, mouse2XY;

	public MazeHud(GameMain game, MazeLogic fight) {
		super(game);
		this.fight = fight;
		createTable();
	}

	void createTable() {
		cheeseXY = new Label("", skin);
		mouse1XY = new Label("", skin);
		mouse2XY = new Label("", skin);
		String controls = "Place the cheese anywhere in the hallways. Press 'p' for pause/unpause.";
		table = new Table(skin);
		// table.debug();
		table.bottom().left();
		table.pad(10);
		table.setFillParent(true);
		table.add(controls).top().width(60).left();
		table.row().padTop(5);
		table.add("CHEESE:").top().width(60).left();
		table.add(cheeseXY).padLeft(10).width(100).left();
		table.add(fight.getMice().get(0).getName().toUpperCase() + ":").padLeft(10).width(70).left();
		table.add(mouse1XY).padLeft(10).width(100).left();
		table.add(fight.getMice().get(1).getName().toUpperCase() + ":").padLeft(10).width(70).left();
		table.add(mouse2XY).padLeft(10).width(100).left();
		stage.addActor(table);
	}

	public void update() {
		cheeseXY.setText(fight.getCheeseXY());
		mouse1XY.setText(fight.getMouse1XY());
		mouse2XY.setText(fight.getMouse2XY());
	}
}
