package net.joedoe.helpers;

import com.badlogic.gdx.Screen;

import net.joedoe.GameInfo;
import net.joedoe.entities.Player;
import net.joedoe.screens.City;

public class GameManager {
    private static GameManager gameManager = new GameManager();
    public GameData gameData;
    public static Player player;
    public static boolean isPaused, storyMode;
    public static int storyStage;

    public void initializeGameData(String name, int gender) {
        storyStage = 0;
        gameData = new GameData();
        gameData.setName(name.toUpperCase());
        gameData.setGender(gender);
        gameData.setX(GameInfo.WIDTH / 2f + 8 * GameInfo.ONE_TILE);
        gameData.setY(GameInfo.HEIGHT - 3 * GameInfo.ONE_TILE);
        gameData.setHealth(100);
        gameData.setStrength(30);
        gameData.setDexterity(20);
        gameData.setIntelligence(15);
        gameData.setActionPoints(gameData.getStrength() / 5);
        gameData.setMoney(100);
        gameData.setExperience(0);

    }

    public void saveData(Screen screen) {
        if (gameData != null) {
            if (screen instanceof City) {
                gameData.setX(player.getX());
                gameData.setY(player.getY());
            }
            gameData.setName(player.getName());
            gameData.setHealth(player.getHealth());
            gameData.setStrength(player.getStrength());
            gameData.setDexterity(player.getDexterity());
            gameData.setIntelligence(player.getIntelligence());
            gameData.setActionPoints(player.getActionPoints());
            gameData.setMoney(player.getMoney());
            gameData.setExperience(player.getExperience());
        }
    }

    public static GameManager getInstance() {
        return gameManager;
    }

}
