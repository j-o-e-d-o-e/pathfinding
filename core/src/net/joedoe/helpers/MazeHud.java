package net.joedoe.helpers;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import net.joedoe.GameInfo;
import net.joedoe.GameMain;
import net.joedoe.logics.MazeLogic;

public class MazeHud extends Hud {
	private MazeLogic mazeLogic;
	private Label cheese, mouse1, mouse2;

	public MazeHud(GameMain game, MazeLogic mazeLogic) {
		super(game);
		this.mazeLogic = mazeLogic;
		createTable();
	}

	void createTable() {
		cheese = new Label("", skin);
		mouse1 = new Label("", skin);
		mouse2 = new Label("", skin);
		String controls = "Place cheese anywhere in the hallways. Press 'p' to pause/unpause while mice run.";
		table = new Table(skin);
		table.bottom().left();
		table.pad(10);
		table.add(cheese).padLeft(10).width(200).left();
		table.add(mouse1).padLeft(10).width(200).left();
		table.add(mouse2).padLeft(10).width(200).left();
		table.row().padTop(10);
		table.add(controls).padLeft(10).width(200).left();
		stage.addActor(table);
	}

	public void update() {
		if (GameInfo.cheeseIsSet) {
			String cheeseXY = "CHEESE: " + (int) mazeLogic.getCheese()[0] / GameInfo.ONE_TILE + "/"
					+ (int) mazeLogic.getCheese()[1] / GameInfo.ONE_TILE;
			cheese.setText(cheeseXY);
		} else {
			cheese.setText("CHEESE: ??/??");
		}
		String mouse1XY = mazeLogic.getMice().get(0).getName().toUpperCase() + ": "
				+ (int) mazeLogic.getMice().get(0).getX() / GameInfo.ONE_TILE + "/"
				+ (int) mazeLogic.getMice().get(0).getY() / GameInfo.ONE_TILE;
		mouse1.setText(mouse1XY);
		String mouse2XY = mazeLogic.getMice().get(1).getName().toUpperCase() + ": "
				+ (int) mazeLogic.getMice().get(1).getX() / GameInfo.ONE_TILE + "/"
				+ (int) mazeLogic.getMice().get(1).getY() / GameInfo.ONE_TILE;
		mouse2.setText(mouse2XY);
	}
}
