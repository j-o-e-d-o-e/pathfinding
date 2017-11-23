package net.joedoe.entities;

import net.joedoe.GameInfo;
import net.joedoe.logics.FightLogic;

public abstract class Actor extends MovingEntity {
    protected String name;
    protected int health;
    protected int strength, dexterity, intelligence;
    protected int actionPoints;

    public boolean hasMoved, isHit, isSeverelyInjured, isDead;

    public void move() {
        decreaseActionPointsBy(1);
        switch (direction) {
        case 1: // N
            y += GameInfo.ONE_TILE;
            if (y > GameInfo.HEIGHT - GameInfo.ONE_TILE) {
                y = GameInfo.HEIGHT - GameInfo.ONE_TILE;
            }
            break;
        case 2: // W
            x -= GameInfo.ONE_TILE;
            if (x < 0) {
                x = 0;
            }
            break;
        case 3: // S
            y -= GameInfo.ONE_TILE;
            if (y < 0) {
                y = 0;
            }
            break;
        case 4: // O
            x += GameInfo.ONE_TILE;
            if (x > GameInfo.WIDTH - GameInfo.ONE_TILE) {
                x = GameInfo.WIDTH - GameInfo.ONE_TILE;
            }
            break;
        }
    }

    public void endTurn() {
        decreaseActionPointsBy(actionPoints);
    }

    public boolean decreaseActionPointsBy(int amount) {
        if (actionPoints >= amount) {
            actionPoints -= amount;
            if (actionPoints == 0) {
                hasMoved = true;
                if (this instanceof Enemy) {
                    setActionPointsToDefault();
                }
            }
            return true;
        }
        return false;
    }

    public void changeHealth(int health) {
        this.health += health;
        if (this.health > 100) {
            this.health = 100;
        } else if (this.health <= 0) {
            this.health = 0;
            isDead = true;
        }
    }

    public void isHit(int damage) {
        isHit = true;
        changeHealth(damage);
        setTexture("entities/actorIsHit.png");
    }

    public void setHasMoved(boolean hasMoved) {
        if (!hasMoved) {
            setActionPointsToDefault();
        }
        this.hasMoved = hasMoved;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getDexterity() {
        return dexterity;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public int getActionPoints() {
        return actionPoints;
    }

    public void setActionPointsToDefault() {
        this.actionPoints = strength / 5;
    }
}
