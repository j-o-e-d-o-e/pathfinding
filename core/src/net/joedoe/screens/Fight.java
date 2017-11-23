package net.joedoe.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import net.joedoe.GameInfo;
import net.joedoe.GameMain;
import net.joedoe.entities.Bullet;
import net.joedoe.entities.Enemy;
import net.joedoe.helpers.FightHud;
import net.joedoe.helpers.GameManager;
import net.joedoe.logics.FightLogic;
import net.joedoe.logics.pathfinding.Node;

public class Fight extends MyScreen {
    private OrthogonalTiledMapRenderer renderer;
    private FightLogic fight;
    private FightHud hud;
    private boolean endMessage, exit;
    private float enemyTimer, bulletTimer;
    private Texture theGhost;
    private boolean displayTheGhost = true;
    private boolean animateTheGhost;
    private float ghostTimer;
    private float theGhostX;

    public Fight(GameMain game) {
        super(game);
        initializeFight();
        renderer = new OrthogonalTiledMapRenderer(fight.getMap(), GameInfo.SCALE);
    }

    void initializeFight() {
        if (GameManager.storyStage == 2) { // SNEAKY PETE
            fight = new FightLogic("maps/fight1.tmx");
        } else if (GameManager.storyStage == 4) { // CARLA
            fight = new FightLogic("maps/fight2.tmx");
        } else if (GameManager.storyStage == 8) { // BODYGUARDS
            fight = new FightLogic("maps/fight3.tmx");
            theGhost = new Texture("entities/enemy.png");
            theGhostX = GameInfo.WIDTH / 2f + 5 * GameInfo.ONE_TILE;
        } else if (GameManager.storyStage == 10) { // THE GHOST
            fight = new FightLogic("maps/fight4.tmx"); // fight4.tmx
        }
        hud = new FightHud(this.game, fight);
        hud.showStory(GameManager.storyStage);
    }

    void handleInput() {
        if (!GameManager.isPaused && !fight.isOver()) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.LEFT)
                    || Gdx.input.isKeyJustPressed(Input.Keys.DOWN) || Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                    fight.getPlayer().setDirection(1);
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
                    fight.getPlayer().setDirection(2);
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
                    fight.getPlayer().setDirection(3);
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
                    fight.getPlayer().setDirection(4);
                }
                if (fight.nextTileIsAccessible(fight.getPlayer()) && !fight.collidesWithActor(fight.getPlayer())) {
                    fight.getPlayer().move();
                }
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.W) || Gdx.input.isKeyJustPressed(Input.Keys.A)
                    || Gdx.input.isKeyJustPressed(Input.Keys.S) || Gdx.input.isKeyJustPressed(Input.Keys.D)) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
                    fight.getPlayer().setDirection(1);
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
                    fight.getPlayer().setDirection(2);
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
                    fight.getPlayer().setDirection(3);
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
                    fight.getPlayer().setDirection(4);
                }
                fight.getPlayer().attack(fight);
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
                fight.getPlayer().changeWeapon(0);
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
                fight.getPlayer().changeWeapon(1);
            } else if (fight.getPlayer().getWeapons()[2] != null && Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
                fight.getPlayer().changeWeapon(2);
            } else if (fight.getPlayer().getWeapons()[3] != null && Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
                fight.getPlayer().changeWeapon(3);
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
                fight.getPlayer().reload();
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                fight.getPlayer().endTurn();
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

    void handleStoryInput() {
        if (fight.getPlayer().isDead && Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
            exit = true;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
            if (GameManager.storyStage == 2 || GameManager.storyStage == 4 || GameManager.storyStage == 8
                    || GameManager.storyStage == 10) {
                hud.removeStory();
                if (theGhost != null) {
                    animateTheGhost = true;
                }
                GameManager.storyStage++;
            } else if (GameManager.storyStage == 3 || GameManager.storyStage == 5 || GameManager.storyStage == 9
                    || GameManager.storyStage == 11) {
                hud.removeStory();
                GameManager.storyStage++;
                exit = true;
            }
        }
    }

    void update() {
        if (bulletTimer >= 0.1) {
            fight.updateBullets();
            bulletTimer = 0;
        }
        if (exit) {
            dispose();
            if (fight.getPlayer().isDead) {
                game.setScreen(new GameOver(game));
            } else if (GameManager.storyStage == 10) {
                game.setScreen(new Fight(game));
            } else if (GameManager.storyStage == 12) {
                game.setScreen(new Win(game));
            } else {
                game.setScreen(new City(game));
            }
        } else if (fight.isOver() && !fight.getPlayer().isDead && !endMessage) {
            hud.showStory(GameManager.storyStage);
            endMessage = true;
        } else if (fight.getBullets().isEmpty()) {
            if (!fight.getPlayer().hasMoved) {
                handleInput();
                for (Enemy enemy : fight.getEnemies()) {
                    enemy.hasMoved = false;
                }
            } else if (enemyTimer >= 0.2 && !fight.getPlayer().isDead) {
                fight.updateEnemies();
                enemyTimer = 0;
            }
        }
    }

    @Override
    public void render(float delta) {
        enemyTimer += delta;
        bulletTimer += delta;
        update();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.render();
        renderer.setView(camera);
        game.getBatch().begin();
        showTheGhost(delta);
        fight.getPlayer().render(game.getBatch());
        for (Enemy enemy : fight.getEnemies()) {
            enemy.render(game.getBatch());
        }
        for (Bullet bullet : fight.getBullets()) {
            bullet.render(game.getBatch());
        }
        game.getBatch().end();
        game.getBatch().setProjectionMatrix(hud.getStage().getCamera().combined);
        // debug();
        hud.update();
        hud.getStage().draw();
        hud.getStage().act();
        fight.setActorTexturesToDefault();
        if (GameManager.storyMode) {
            handleStoryInput();
        }
    }

    void showTheGhost(float delta) {
        if (theGhost != null && displayTheGhost) {
            ghostTimer += delta;
            if (animateTheGhost) {
                if (ghostTimer >= 0.2) {
                    theGhostX += GameInfo.ONE_TILE;
                    ghostTimer = 0;
                }
            }
            game.getBatch().draw(theGhost, theGhostX, GameInfo.HEIGHT - 10 * GameInfo.ONE_TILE, GameInfo.ONE_TILE,
                    GameInfo.ONE_TILE);
            if (theGhostX >= GameInfo.WIDTH) {
                animateTheGhost = false;
                displayTheGhost = false;
            }
        }
    }

    void debug() {
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        // PATH OF ENEMY_1 IN RED
        if (fight.getEnemies().size() >= 1) {
            for (Node node : fight.getEnemies().get(0).path) {
                node.render(shapeRenderer, Color.RED);
            }
            // PATH OF ENEMY_2 IN YELLOW
            if (fight.getEnemies().size() >= 2) {
                for (Node node : fight.getEnemies().get(1).path) {
                    node.render(shapeRenderer, Color.YELLOW);
                }
            }
            // PATH OF ENEMY_3 IN BLUE
            if (fight.getEnemies().size() >= 3) {
                for (Node node : fight.getEnemies().get(2).path) {
                    node.render(shapeRenderer, Color.BLUE);
                }
            }
            if (fight.getEnemies().get(0).path.nodes.size != 0) {
                Enemy enemy = fight.getEnemies().get(0);
                // CURRENT NODE OF ENEMY_1 IN SKYBLUE
                Node currentNode = enemy.getCurrentNode();
                currentNode.render(shapeRenderer, Color.SKY);
                // NEXT NODE OF ENEMY_1 IN GREEN
                Node nextNode = enemy.getNextNode();
                nextNode.render(shapeRenderer, Color.GREEN);
                // AFTER NEXT NODE OF ENEMY_1 IN ORANGE
                Node afterNextNode = enemy.getAfterNextNode();
                if (afterNextNode != null) {
                    afterNextNode.render(shapeRenderer, Color.ORANGE);
                }
            }
        }
        shapeRenderer.end();
    }

    @Override
    public void dispose() {
        fight.dispose();
        hud.dispose();
    }
}
