package net.joedoe.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

import net.joedoe.GameInfo;

public class Bullet extends MovingEntity {
    private int range;
    private int damage;
    private String weaponName;
    private Sound shot;
    public boolean remove;

    public Bullet(float x, float y, int direction, int range, int damage, String weaponName) {
        if (direction == 1 || direction == 3) {
            texture = new Texture("entities/bullet_vertical.png");
        } else if (direction == 2 || direction == 4) {
            texture = new Texture("entities/bullet_horizontal.png");
        }
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.range = range;
        this.damage = damage;
        this.weaponName = weaponName;
        shot = Gdx.audio.newSound(Gdx.files.internal("sounds/shot.wav"));
        shot.play();
    }

    public void move() {
        range -= 1;
        switch (direction) {
        case 1:
            y += GameInfo.ONE_TILE;
            break;
        case 2:
            x -= GameInfo.ONE_TILE;
            break;
        case 3:
            y -= GameInfo.ONE_TILE;
            break;
        case 4:
            x += GameInfo.ONE_TILE;
            break;
        }
        if (x > GameInfo.WIDTH || y > GameInfo.HEIGHT || x < 0 || y < 0 || range == 0) {
            remove = true;
        }
    }

    public int getDamage() {
        return damage;
    }

    public int getRandomizedDamage() {
        int rand = (int) (Math.random() * 2) + 1; // 1 OR 2
        return damage / rand;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public String getWeaponName() {
        return weaponName;
    }

    public Sound getShot() {
        return shot;
    }
}
