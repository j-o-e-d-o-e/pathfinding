package net.joedoe.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;

import net.joedoe.GameInfo;
import net.joedoe.GameMain;
import net.joedoe.entities.weapons.MachineGun;
import net.joedoe.entities.weapons.Rifle;
import net.joedoe.entities.weapons.Weapon;

public class WeaponStore extends Place {
    private Label pistolAmmo, rifleAmmo, machineAmmo;
    private TextButton button4, button5;

    public WeaponStore(GameMain game) {
        super(game);
        title.setColor(GameInfo.BLUE[0], GameInfo.BLUE[1], GameInfo.BLUE[2], 1);
        createTable();
    }

    public void createTable() {
        String weapons = "pistol";
        if (hud.player.getWeapons()[2] != null) {
            weapons = weapons + ", " + hud.player.getWeapons()[2].getName().toLowerCase();
        }
        if (hud.player.getWeapons()[3] != null) {
            weapons = weapons + ", " + hud.player.getWeapons()[3].getName().toLowerCase();
        }
        message = new Label(name + " currently owns: " + weapons, skin);
        pistolAmmo = new Label(Integer.toString(
                hud.player.getWeapons()[1].getAmmoStock() + hud.player.getWeapons()[1].getLoadedAmmo()), skin);
        if (hud.player.getWeapons()[2] == null && hud.player.getWeapons()[3] == null) {
            rifleAmmo = new Label("0", skin);
            machineAmmo = new Label("0", skin);
            button4 = new TextButton(" 100 $ ", skin);
            button5 = new TextButton(" 150 $ ", skin);
            button4.addListener(listener);
            button5.addListener(listener);
        } else if (hud.player.getWeapons()[2] == null) { // NO RIFLE
            rifleAmmo = new Label("0", skin);
            int machineAmmo = hud.player.getWeapons()[3].getAmmoStock() + hud.player.getWeapons()[3].getLoadedAmmo();
            this.machineAmmo = new Label(Integer.toString(machineAmmo), skin);
            button4 = new TextButton(" 100 $ ", skin);
            button4.addListener(listener);
        } else if (hud.player.getWeapons()[3] == null) { // NO MACHINE GUN
            int rifleAmmo = hud.player.getWeapons()[2].getAmmoStock() + hud.player.getWeapons()[2].getLoadedAmmo();
            this.rifleAmmo = new Label(Integer.toString(rifleAmmo), skin);
            machineAmmo = new Label("0", skin);
            button5 = new TextButton(" 150 $ ", skin);
            button5.addListener(listener);
        } else {
            int rifleAmmo = hud.player.getWeapons()[2].getAmmoStock() + hud.player.getWeapons()[2].getLoadedAmmo();
            int machineAmmo = hud.player.getWeapons()[3].getAmmoStock() + hud.player.getWeapons()[3].getLoadedAmmo();
            this.rifleAmmo = new Label(Integer.toString(rifleAmmo), skin);
            this.machineAmmo = new Label(Integer.toString(machineAmmo), skin);
        }
        button1 = new TextButton(" 20 $ ", skin);
        button2 = new TextButton(" 30 $ ", skin);
        button3 = new TextButton(" 40 $ ", skin);
        buttonLeave = new TextButton(" Leave ", skin);
        button1.addListener(listener);
        button2.addListener(listener);
        button3.addListener(listener);
        buttonLeave.addListener(listener);
        table = new Table(skin);
        table.setPosition(-30, 0);
        // table.debug();
        table.setFillParent(true);
        table.add(message).left().width(100);
        table.row();
        table.add("");
        table.row();
        table.add("Weapons").left().width(100);
        table.add("Ammo").padLeft(10).width(60);
        table.add("Buy ammo").padLeft(10).width(100);
        table.add("Buy weapon").padLeft(10).width(100);
        table.row().padTop(10);
        table.add("Pistol:").left();
        table.add(pistolAmmo);
        table.add(button1).width(50);
        table.row().padTop(10);
        table.add("Rifle:").left();
        table.add(rifleAmmo);
        table.add(button2).width(50);
        if (hud.player.getWeapons()[2] == null) {
            table.add(button4).width(50);
        }
        table.row().padTop(10);
        table.add("Machine gun:").left();
        table.add(machineAmmo);
        table.add(button3).width(50);
        if (hud.player.getWeapons()[3] == null) {
            table.add(button5).width(50);
        }
        table.row().padTop(10);
        table.add("");
        table.row();
        table.add("");
        table.add(buttonLeave).right();
        stage.addActor(table);
    }

    @SuppressWarnings("rawtypes")
    public void handleInput(String choice) {
        if (choice.contains("Leave")) {
            dispose();
            game.setScreen(new City(game));
        } else if (choice.contains("150")) {
            if (hud.player.getMoney() >= 150) {
                hud.player.changeMoney(-150);
                hud.player.getWeapons()[3] = new MachineGun();
                message.setText(name + " purchased a machine gun.");
                hud.update();
                Array<Cell> cells = table.getCells();
                cells.get(16).clearActor();
            } else {
                message.setText(name + " doesn't have enough money!");
            }
        } else if (choice.contains("100")) {
            if (hud.player.getMoney() >= 100) {
                hud.player.changeMoney(-100);
                hud.player.getWeapons()[2] = new Rifle();
                message.setText(name + " purchased a rifle.");
                hud.update();
                Array<Cell> cells = table.getCells();
                cells.get(12).clearActor();
            } else {
                message.setText(name + " doesn't have enough money!");
            }
        } else if (choice.contains("40")) {
            if (hud.player.getWeapons()[3] == null) {
                message.setText(name + " needs to buy a machine gun, first.");
            } else if (hud.player.getMoney() >= 40) {
                hud.player.changeMoney(-40);
                Weapon machine = hud.player.getWeapons()[3];
                machine.changeAmmoStock(machine.getOneRound());
                machine.reload();
                int newAmmo = machine.getAmmoStock() + machine.getLoadedAmmo();
                machineAmmo.setText(Integer.toString(newAmmo));
                message.setText(name + " purchased one round for a machine gun.");
                hud.update();
            } else {
                message.setText(name + " doesn't have enough money!");
            }
        } else if (choice.contains("30")) {
            if (hud.player.getWeapons()[2] == null) {
                message.setText(name + " needs to buy a rifle, first.");
            } else if (hud.player.getMoney() >= 30) {
                hud.player.changeMoney(-30);
                Weapon rifle = hud.player.getWeapons()[2];
                rifle.changeAmmoStock(rifle.getOneRound());
                rifle.reload();
                int newAmmo = rifle.getAmmoStock() + rifle.getLoadedAmmo();
                rifleAmmo.setText(Integer.toString(newAmmo));
                message.setText(name + " purchased one round for a rifle.");
                hud.update();
            } else {
                message.setText(name + " doesn't have enough money!");
            }
        } else if (choice.contains("20")) {
            if (hud.player.getMoney() >= 20) {
                hud.player.changeMoney(-20);
                Weapon pistol = hud.player.getWeapons()[1];
                pistol.changeAmmoStock(pistol.getOneRound());
                pistol.reload();
                int newAmmo = pistol.getAmmoStock() + pistol.getLoadedAmmo();
                pistolAmmo.setText(Integer.toString(newAmmo));
                message.setText(name + " purchased one round for a pistol.");
                hud.update();
            } else {
                message.setText(name + " doesn't have enough money!");
            }
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(GameInfo.RED[0], GameInfo.RED[1], GameInfo.RED[2], 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.getBatch().begin();
        title.draw(game.getBatch(), "WEAPONS", GameInfo.WIDTH / 2f - 235, GameInfo.HEIGHT / 2f + 200);
        game.getBatch().end();
        game.getBatch().setProjectionMatrix(hud.getStage().getCamera().combined);
        hud.getStage().draw();
        hud.getStage().act();
        stage.draw();
        stage.act();
    }

    @Override
    public void dispose() {
        title.dispose();
        skin.dispose();
        stage.dispose();
    }
}
