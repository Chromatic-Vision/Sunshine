package nl.chromaticvision.sunshine.impl.util.system;

import com.google.gson.*;
import nl.chromaticvision.sunshine.Main;
import nl.chromaticvision.sunshine.impl.module.Module;
import nl.chromaticvision.sunshine.impl.module.settings.Bind;
import nl.chromaticvision.sunshine.impl.module.settings.Setting;
import nl.chromaticvision.sunshine.impl.module.settings.converter.EnumConverter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class FileUtils {

    public static final File sunshine = new File("sunshine");
    public static final File config = new File("sunshine/config");

    public static void saveConfig() {

        if (!sunshine.exists()) sunshine.mkdir();
        if (!config.exists()) config.mkdir();

        saveActiveModuleConfig();
        saveModuleSettings();
    }

    public static void saveActiveModuleConfig()  {
        File file = new File(sunshine + "/activemodules.stc");

        try {
            if (!file.exists()) file.createNewFile();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        try (FileWriter fileWriter = new FileWriter(file)) {

            for (Module module : Main.moduleManager.getModules()) {
                fileWriter.write(module.getName() + ">" + module.isEnabled() + "\n");
            }

            fileWriter.flush();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void saveModuleSettings() {
        for (Module module : Main.moduleManager.getModules()) {

            JsonObject jsonObject = new JsonObject();
            JsonParser jsonParser = new JsonParser();

            for (Setting setting : module.getSettings()) {

                if (setting.isEnumSetting()) {
                    EnumConverter converter = new EnumConverter(((Enum) setting.getValue()).getClass());
                    jsonObject.add(setting.getName(), converter.doForward((Enum) setting.getValue()));
                    continue;
                }

                jsonObject.add(setting.getName(), jsonParser.parse(setting.getValue().toString()));
            }

            try (FileWriter fileWriter = new FileWriter(config + "/" + module.getName() + ".json")) {

                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                JsonElement jsonElement = jsonParser.parse(jsonObject.toString());
                String prettyJsonString = gson.toJson(jsonElement);

                fileWriter.write(prettyJsonString);
                fileWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void loadConfig() {

        if (!sunshine.exists()) {
            sunshine.mkdir();

            if (!config.exists()) {
                config.mkdir();
                return;
            }
        }

        if (!config.exists()) {
            config.mkdir();
            return;
        }

        loadActiveModuleConfig();
        loadModuleSettings();
    }

    public static void loadActiveModuleConfig() {
        File file = new File(sunshine + "/activemodules.stc");

        try {

            if (!file.exists()) {
                file.createNewFile();
                return;
            }

            Scanner scanner = new Scanner(file);
            List<String> lines = Files.readAllLines(file.toPath());

            for (String line : lines) {

                String[] regex = line.split(">");
                Module module = Main.moduleManager.getModuleByName(regex[0]);

                if (module == null) continue;

                module.setEnabled(Boolean.parseBoolean(regex[1]), false);
            }

            scanner.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public static void loadModuleSettings() {
        for (Module module : Main.moduleManager.getModules()) {

            Path path = Paths.get(config + "/" + module.getName() + ".json");
            File file = path.toFile();

            if (!file.exists()) continue;

            try {

                //set module settings from json
                JsonObject jsonObject = (new JsonParser()).parse(new InputStreamReader(Files.newInputStream(path))).getAsJsonObject();

                for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {

                    Setting setting = module.getSettingByName(entry.getKey());
                    JsonElement element = entry.getValue();

                    if (setting == null) continue;

                    switch (setting.getType()) {
                        case "Boolean":
                            setting.setValue(element.getAsBoolean());
                            continue;
                        case "Integer":
                            setting.setValue(element.getAsInt());
                            continue;
                        case "Double":
                            setting.setValue(element.getAsDouble());
                            continue;
                        case "Float":
                            setting.setValue(element.getAsFloat());
                            continue;
                        case "Long":
                            setting.setValue(element.getAsLong());
                            continue;
                        case "Byte":
                            setting.setValue(element.getAsByte());
                            continue;
                        case "Short":
                            setting.setValue(element.getAsShort());
                            continue;
                        case "String":
                            setting.setValue(element.getAsString());
                            continue;
                        case "Character":
                            setting.setValue(element.getAsCharacter());
                            continue;
                        case "Bind":
                            setting.setValue((new Bind.BindConverter()).doBackward(element));
                            continue;
                        case "Enum":
                            try {
                                EnumConverter converter = new EnumConverter(((Enum) setting.getValue()).getClass());
                                Enum value = converter.doBackward(element);
                                setting.setValue((value == null) ? setting.getDefaultValue() : value);
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                    }
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
