package net.joedoe.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import net.joedoe.GameInfo;
import net.joedoe.GameMain;
import net.joedoe.entities.Player;

public abstract class Hud {
    protected Stage stage;
    protected Viewport viewport;
    protected Table table;
    protected Skin skin;
    protected TextureRegionDrawable panel;
    protected String name, story;
    protected int gender;
    protected Label label;
    protected String[] heShe = new String[] { "he", "she" };
    protected String[] himHer = new String[] { "him", "her" };
    protected String[] hisHer = new String[] { "his", "her" };
    public Player player;

    public Hud(GameMain game) {
        viewport = new FitViewport(GameInfo.WIDTH, GameInfo.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, game.getBatch());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("fonts/uiskin.json"));
        player = GameManager.player;
        name = player.getName();
        gender = player.getGender();
    }

    public void createPausePanel() {
        table = new Table(skin);
        panel = new TextureRegionDrawable(new TextureRegion(new Texture("images/panel.png")));
        table.setBackground(panel);
        table.setFillParent(true);
        table.top().center();
        table.add("-----------PAUSED-----------");
        table.row();
        table.add(GameInfo.CONTROLS);
        stage.addActor(table);
    }

    public void removePausePanel() {
        table.remove();
    }

    public void showStory(int storyStage) {
        GameManager.storyMode = true;
        GameManager.isPaused = true;
        table = new Table(skin);
        // table.debug();
        panel = new TextureRegionDrawable(new TextureRegion(new Texture("images/panel.png")));
        table.setBackground(panel);
        table.setFillParent(true);
        if (storyStage == 0) {
            story = name
                    + " has just arrived at the railway station in the coastal city of Newport. A mysterious client hired "
                    + himHer[gender]
                    + " to assassinate a powerful, yet even more mysterious drug lord called \"the Ghost\" who is said to hide "
                    + "somewhere in this city. As a starting point, " + hisHer[gender] + " client instructed " + name
                    + " to meet a man who goes by the nickname \"Cottonmouth\" in the local pub at the beach boulevard."
                    + "\n\nPRESS ANY KEY TO CONTINUE...";
        } else if (storyStage == 1) {
            story = "A slim guy who appears to be slightly drunk approaches " + name + " and introduces himself as "
                    + "\"Cottonmouth\". After the exchange of a few pleaseantries, he gets quickly to the point: "
                    + "he doesn't know where to find \"the Ghost\", of course, this man seems to be very mysterious. "
                    + "But he knows one of his henchmen who might be willing to talk. He shows " + name
                    + " the place where \"Sneaky Pete\" lives and marks it with a big \"X\" on the city map."
                    + "\n\nPRESS ANY KEY TO CONTINUE...";
        } else if (storyStage == 2) {
            story = "When \"Sneaky Pete\" sees " + name
                    + ", he immediately grabs his weapon and starts to shoot. Someone must have "
                    + "warned him. Get ready to fight.\n\nPRESS ANY KEY TO CONTINUE...";
        } else if (storyStage == 3) {
            story = "\"Sneaky Pete\" pleads " + name + " not to kill him and in return wants to help " + himHer[gender]
                    + " finding \"the Ghost\"." + " Unfortunately, he has no clue about his whereabouts. "
                    + "But he knows someone who certainly does. She is called \"Carla Montepulciano\" and is the girlfriend "
                    + "of the drug lord. He marks her location with a big \"X\" on the city map and thanks " + name
                    + " for sparing his life." + "\n\nPRESS ANY KEY TO CONTINUE...";
        } else if (storyStage == 4) {
            story = "When " + name + " enters the residency of \"Carla Montepulciano\", she already awaits "
                    + himHer[gender] + "." + " With a smirky, yet excited expression on her face she tells "
                    + himHer[gender] + " that " + heShe[gender] + " is going to die now. Get ready to fight."
                    + "\n\nPRESS ANY KEY TO CONTINUE...";
        } else if (storyStage == 5) {
            story = "\"Carla Montepulciano\" is dead! After a moment of rest, " + name
                    + " searches her appartement for hints about \"the Ghost\"."
                    + " Apparently, the two of them visited regularly the \"The Western Pub\", whose owner is the notorious gangster \"Fat Tony\"."
                    + " He might know where to find the drug lord. At least, it's worth a try."
                    + "\n\nPRESS ANY KEY TO CONTINUE...";
        } else if (storyStage == 6) {
            story = "Without any introduction a rather big guy sits himself next to " + name + ". "
                    + "After a short moment of silence, he cuts straight to the chase: rumours have spread across town that "
                    + name + " has killed \"Carla Montepulciano\". He would even more appreciate" + " if "
                    + heShe[gender] + " could eliminate \"the Ghost\". He knows where he hides, but first " + name
                    + " needs to prove to him that " + heShe[gender] + " is up for it. If " + heShe[gender]
                    + " beats him in a match of poker in the casino near"
                    + " the railway station, he will reveal his information. Then, he leaves. Apparently, the man "
                    + name + " was talking to is \"Fat Tony\" himself." + "\n\nPRESS ANY KEY TO CONTINUE...";
        } else if (storyStage == 7) {
            story = "Finally, " + name + " wins! \"Fat Tony\" marks the location where \"the Ghost\" resides"
                    + " with a big \"X\" and wishes " + himHer[gender] + " good luck for " + hisHer[gender]
                    + " mission." + "\n\nPRESS ANY KEY TO CONTINUE...";
        } else if (storyStage == 8) {
            story = "When " + name + " arrives at his majestic residency, \"the Ghost\" welcomes " + himHer[gender]
                    + " with a grinning smile " + " standing behind two of his bodyguards. He shouts that " + name
                    + " will never leave this place alive and takes one of the backdoors. Obviously, \"the Ghost\""
                    + " is trying to flee. Take care of his guards within 18 rounds, otherwise the drug lord will be gone."
                    + "\n\nPRESS ANY KEY TO CONTINUE...";
        } else if (storyStage == 9) {
            story = "After killing the bodyguards, " + name
                    + " quickly leaves the mansion in the search of \"the Ghost\"."
                    + "\n\nPRESS ANY KEY TO CONTINUE...";
        } else if (storyStage == 10) {
            story = "\"The Ghost\" has almost reached the railway station. But when he sees " + name
                    + " racing after him, he shouts that " + heShe[gender]
                    + " is going to pay now for the murder of his girlfriend. He turns around furiously willing to"
                    + " end this once and for all. Get ready for the final battle."
                    + "\n\nPRESS ANY KEY TO CONTINUE...";
        } else if (storyStage == 11) {
            story = "\"The Ghost\" is finally dead!" + "\n\nShortly after this epic battle, \"Fat Tony\" approaches "
                    + name + " and hands " + himHer[gender] + " out " + hisHer[gender] + " reward. It's only now that "
                    + heShe[gender] + " realizes that \"Fat Tony\" has been " + hisHer[gender]
                    + " mysterious client all along." + " After a brief farewell, the two of them part."
                    + " Since the fight in the middle of the city has drawn some attention to it, the police is most likely already on its way. "
                    + name + " heads quickly to the railway station and leaves the city of Newport for good."
                    + "\n\nPRESS ANY KEY TO CONTINUE...";
        } else if (storyStage == 88) {
            story = "The Bartender offers " + name + " to do the dishwashing for 25 $. Accept (Y/N)?";
        } else if (storyStage == 89) {
            story = "The Bartender offers " + name
                    + " to hand out flyers for the upcoming rock show. This pays 50 $. Accept (Y/N)?";
        }
        label = new Label(story, skin);
        label.setWrap(true);
        table.add(label).left().width(600);
        stage.addActor(table);
    }

    public void removeStory() {
        GameManager.storyMode = false;
        GameManager.isPaused = false;
        table.remove();
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
