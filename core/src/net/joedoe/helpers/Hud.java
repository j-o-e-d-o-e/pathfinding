package net.joedoe.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import net.joedoe.GameInfo;
import net.joedoe.GameMain;

public abstract class Hud {
	protected Stage stage;
	protected Viewport viewport;
	protected Table table;
	protected Skin skin;

	public Hud(GameMain game) {
		viewport = new FitViewport(GameInfo.WIDTH, GameInfo.HEIGHT, new OrthographicCamera());
		stage = new Stage(viewport, game.getBatch());
		Gdx.input.setInputProcessor(stage);
		skin = new Skin(Gdx.files.internal("fonts/uiskin.json"));
		skin.getFont("default-font").getData().setScale(1.33f, 1.33f);
	}

	public Stage getStage() {
		return this.stage;
	}

	public void dispose() {
		skin.dispose();
		stage.dispose();
	}
}
