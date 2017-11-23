package net.joedoe.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import net.joedoe.GameInfo;
import net.joedoe.GameMain;
import net.joedoe.helpers.GameManager;
import net.joedoe.helpers.MainHud;
import net.joedoe.logics.MapLogic;

public class City extends MyScreen {
    private OrthogonalTiledMapRenderer renderer;
    private MapLogic map;
    private MainHud hud;

    public City(GameMain game) {
        super(game);
        this.game = game;
        // System.out.println("Story stage: " + GameManager.storyStage);
        if (GameManager.storyStage == 2) {
            map = new MapLogic("maps/city2.tmx"); // SNEAKY PETE
        } else if (GameManager.storyStage == 4) {
            map = new MapLogic("maps/city3.tmx"); // CARLA MONTEPULCIANO
        } else if (GameManager.storyStage == 8 || GameManager.storyStage == 10) {
            map = new MapLogic("maps/city4.tmx");// THE GHOST
        } else {
            map = new MapLogic("maps/city1.tmx");
        }
        initializePlayer();
        renderer = new OrthogonalTiledMapRenderer(map.getMap(), GameInfo.SCALE);
        hud = new MainHud(this.game);
        if (GameManager.storyStage == 0) {
            hud.showStory(GameManager.storyStage);
            GameManager.storyStage++;
        }
    }

    void initializePlayer() {
        map.setPlayer(GameManager.player);
        map.getPlayer().setX(GameManager.getInstance().gameData.getX());
        map.getPlayer().setY(GameManager.getInstance().gameData.getY());
        map.getPlayer().setTexture("entities/player.png");
    }

    void handleInput() {
        if (GameManager.storyMode) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
                hud.removeStory();
            }
        } else if (!GameManager.isPaused) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.LEFT)
                    || Gdx.input.isKeyJustPressed(Input.Keys.DOWN) || Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)
                    || Gdx.input.isKeyJustPressed(Input.Keys.W) || Gdx.input.isKeyJustPressed(Input.Keys.A)
                    || Gdx.input.isKeyJustPressed(Input.Keys.S) || Gdx.input.isKeyJustPressed(Input.Keys.D)) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.W)) {
                    map.getPlayer().setDirection(1);
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) || Gdx.input.isKeyJustPressed(Input.Keys.A)) {
                    map.getPlayer().setDirection(2);
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) || Gdx.input.isKeyJustPressed(Input.Keys.S)) {
                    map.getPlayer().setDirection(3);
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) || Gdx.input.isKeyJustPressed(Input.Keys.D)) {
                    map.getPlayer().setDirection(4);
                }
                if (map.nextTileIsAccessible(map.getPlayer())) {
                    String tileName = map.getTileName();
                    if (tileName != null) {
                        goTo(tileName);
                    } else {
                        map.getPlayer().move();
                    }
                }
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
                GameManager.isPaused = true;
                hud.createPausePanel();
            }
        } else if (GameManager.isPaused) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
                GameManager.isPaused = false;
                hud.removePausePanel();
            }
        }
    }

    void goTo(String tileName) {
        GameManager.getInstance().saveData(this);
        if (tileName != null) {
            if (tileName.equals("Casino1")) {
                dispose();
                game.setScreen(new CasinoRailway(game));
            } else if (tileName.equals("Casino2")) {
                dispose();
                game.setScreen(new Casino(game));
            } else if (tileName.equals("WeaponStore1") || tileName.equals("WeaponStore2")) {
                dispose();
                game.setScreen(new WeaponStore(game));
            } else if (tileName.equals("Pub1")) {
                dispose();
                game.setScreen(new Pub(game));
            } else if (tileName.equals("Pub2")) {
                dispose();
                game.setScreen(new PubBeach(game));
            } else if (tileName.equals("Pub3")) {
                dispose();
                game.setScreen(new PubWestern(game));
            } else if (tileName.equals("Hospital1") || tileName.equals("Hospital2")) {
                dispose();
                game.setScreen(new Hospital(game));
            } else if (tileName.equals("Fight1") || tileName.equals("Fight2") || tileName.equals("Fight3")) {
                dispose();
                game.setScreen(new Fight(game));
            }
        }
    }

    @Override
    public void render(float delta) {
        handleInput();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.render();
        game.getBatch().begin();
        map.getPlayer().render(game.getBatch());
        game.getBatch().end();
        renderer.setView(camera);
        game.getBatch().setProjectionMatrix(hud.getStage().getCamera().combined);
        hud.getStage().draw();
        hud.getStage().act();
    }

    @Override
    public void dispose() {
        map.getMap().dispose();
        hud.getStage().dispose();
        hud.getSkin().dispose();
    }
}
