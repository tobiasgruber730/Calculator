import java.io.*;
import java.util.Properties;

/**
 * A class to manage user preferences using a properties file.
 */
public class UserPreferences {
    private Properties properties;
    private final String preferencesFile = "userPreferences.properties";

    /**
     * Constructor initializes the properties and loads preferences from the file.
     */
    public UserPreferences() {
        properties = new Properties();
        loadPreferences();
    }

    /**
     * Loads user preferences from the properties file.
     * If the file does not exist, default preferences are set.
     */
    public void loadPreferences() {
        try (FileInputStream input = new FileInputStream(preferencesFile)) {
            properties.load(input);
        } catch (IOException e) {
            // If the file doesn't exist, defaults will be used
            setDefaultPreferences();
        }
    }

    /**
     * Saves the current user preferences to the properties file.
     */
    public void savePreferences() {
        try (FileOutputStream output = new FileOutputStream(preferencesFile)) {
            properties.store(output, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets default preferences.
     * This method is called when the preferences file does not exist.
     */
    private void setDefaultPreferences() {
        properties.setProperty("theme", "light");
        properties.setProperty("fontSize", "20");
    }

    /**
     * Gets the current theme preference.
     *
     * @return the theme preference, default is "light"
     */
    public String getTheme() {
        return properties.getProperty("theme", "light");
    }

    /**
     * Sets the theme preference.
     *
     * @param theme the theme to be set
     */
    public void setTheme(String theme) {
        properties.setProperty("theme", theme);
    }

    /**
     * Gets the current font size preference.
     *
     * @return the font size preference, default is 20
     */
    public int getFontSize() {
        return Integer.parseInt(properties.getProperty("fontSize", "20"));
    }

    /**
     * Sets the font size preference.
     *
     * @param fontSize the font size to be set
     */
    public void setFontSize(int fontSize) {
        properties.setProperty("fontSize", String.valueOf(fontSize));
    }
}
