package net.joedoe.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import net.joedoe.GameMain;
import net.joedoe.helpers.GameManager;
import net.joedoe.helpers.MainHud;

public abstract class Place extends MyScreen {
    protected MainHud hud;
    protected Skin skin;
    protected Stage stage;
    protected BitmapFont title;
    protected Table table;
    protected Label message;
    protected TextButton button1, button2, button3, buttonLeave;
    protected ClickListener listener;
    protected String name;

    public Place(GameMain game) {
        super(game);
        hud = new MainHud(this.game);
        name = GameManager.getInstance().gameData.getName();
        skin = new Skin(Gdx.files.internal("fonts/uiskin.json"));
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        title = new BitmapFont(Gdx.files.internal("fonts/font.fnt"));
        title.getData().setScale(2.0f);
        listener = new ClickListener() {
            @Override
            public boolean touchDown(InputEvent e, float x, float y, int point, int btn) {
                handleInput(e.getTarget().toString());
                return false;
            }
        };
    }

    public abstract void createTable();

    public abstract void handleInput(String choice);
}
