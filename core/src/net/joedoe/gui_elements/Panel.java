package net.joedoe.gui_elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import net.joedoe.GameInfo;
import net.joedoe.GameMain;
import net.joedoe.entities.Mouse;

import java.util.ArrayList;

public class Panel {
    private Stage stage;
    private Label cheese, mouse1, mouse2;

    public Panel(GameMain game) {
        Viewport viewport = new FitViewport(GameInfo.MAP_WIDTH_PX, GameInfo.MAP_HEIGHT_PX, new OrthographicCamera());
        stage = new Stage(viewport, game.getBatch());
        Gdx.input.setInputProcessor(stage);
        createTable();
    }

    private void createTable() {
        Skin skin = new Skin(Gdx.files.internal("fonts/uiskin.json"));
        skin.getFont("default-font").getData().setScale(1.33f, 1.33f);
        cheese = new Label("", skin);
        mouse1 = new Label("", skin);
        mouse2 = new Label("", skin);
        String controls = "Place cheese anywhere in the hallways. Press 'p' to pause/resume while mice run.";
        Table table = new Table(skin);
        table.bottom().left();
        table.pad(10);
        table.add(cheese).padLeft(10).width(200).left();
        table.add(mouse1).padLeft(10).width(200).left();
        table.add(mouse2).padLeft(10).width(200).left();
        table.row().padTop(10);
        table.add(controls).padLeft(10).width(200).left();
        stage.addActor(table);
    }

    public void update(ArrayList<Mouse> mice, float[] cheese) {
        if (GameInfo.cheeseIsSet) {
            int cheeseX = (int) cheese[0] / GameInfo.ONE_TILE;
            int cheeseY = (int) cheese[1] / GameInfo.ONE_TILE;
            this.cheese.setText("Cheese: " + cheeseX + "/" + cheeseY);
        } else {
            this.cheese.setText("Cheese: ??/??");
        }
        Mouse mouse1 = mice.get(0);
        int x1 = (int) mouse1.getX() / GameInfo.ONE_TILE;
        int y1 = (int) mouse1.getY() / GameInfo.ONE_TILE;
        String mouse1XY = mouse1.getName() + ": " + x1 + "/" + y1;
        this.mouse1.setText(mouse1XY);

        Mouse mouse2 = mice.get(1);
        int x2 = (int) mouse2.getX() / GameInfo.ONE_TILE;
        int y2 = (int) mouse2.getY() / GameInfo.ONE_TILE;
        String mouse2XY = mouse2.getName() + ": " + x2 + "/" + y2;
        this.mouse2.setText(mouse2XY);
    }

    public Stage getStage() {
        return stage;
    }

    public void dispose() {
        stage.dispose();
    }
}