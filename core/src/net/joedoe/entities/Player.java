package net.joedoe.entities;

import com.badlogic.gdx.graphics.Texture;

import net.joedoe.helpers.GameManager;

public class Player extends Actor {
    private int money;
    private int experience;
    private int gender;

    public Player() {
        texture = new Texture("entities/player.png");
        name = GameManager.getInstance().gameData.getName();
        x = GameManager.getInstance().gameData.getX();
        y = GameManager.getInstance().gameData.getY();
        health = GameManager.getInstance().gameData.getHealth();
        strength = GameManager.getInstance().gameData.getStrength();
        dexterity = GameManager.getInstance().gameData.getDexterity();
        intelligence = GameManager.getInstance().gameData.getIntelligence();
        actionPoints = GameManager.getInstance().gameData.getActionPoints();
        money = GameManager.getInstance().gameData.getMoney();
        experience = GameManager.getInstance().gameData.getExperience();
        setGender(GameManager.getInstance().gameData.getGender());
    }

    public void increaseExperience(int experience) {
        this.experience += experience;
    }

    public void changeMoney(int money) {
        this.money += money;
        if (this.money < 0) {
            this.money = 0;
        }
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }
}
