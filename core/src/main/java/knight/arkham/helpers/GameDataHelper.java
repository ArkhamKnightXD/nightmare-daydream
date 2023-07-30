package knight.arkham.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.Vector2;

public class GameDataHelper {

    public static void saveGameData(String filename, GameData gameData){

// This method creates a xml file if the file doesn't exist it will be created, if the file does exist it will
// be overwritten, default path user/.pref/filename.xml
        Preferences preferences = Gdx.app.getPreferences(filename);

        preferences.putString("screenName", gameData.screenName);

        preferences.putFloat("positionX", gameData.position.x);
        preferences.putFloat("positionY", gameData.position.y);

        preferences.flush();
    }

    public static GameData loadGameData(String filename){

        Preferences preferences = Gdx.app.getPreferences(filename);

        float positionX = preferences.getFloat("positionX");
        float positionY = preferences.getFloat("positionY");

        String screenName = preferences.getString("screenName");

        return new GameData(screenName, new Vector2(positionX, positionY));
    }
}
