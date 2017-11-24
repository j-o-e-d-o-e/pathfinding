package net.joedoe.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import net.joedoe.GameInfo;
import net.joedoe.GameMain;

public abstract class Hud {
    protected Stage stage;
    protected Viewport viewport;
    protected Table table;
    protected Skin skin;
    protected TextureRegionDrawable panel;
    protected Label label;

    public Hud(GameMain game) {
        viewport = new FitViewport(GameInfo.WIDTH, GameInfo.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, game.getBatch());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("fonts/uiskin.json"));
    }

    public Skin getSkin() {
        return skin;
    }

    public Stage getStage() {
        return this.stage;
    }

    abstract void createTable();

    abstract void update();

    public void dispose() {
        skin.dispose();
        stage.dispose();
    }
}
