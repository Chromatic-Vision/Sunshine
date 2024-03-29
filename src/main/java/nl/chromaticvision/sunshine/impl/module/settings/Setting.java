package nl.chromaticvision.sunshine.impl.module.settings;

import nl.chromaticvision.sunshine.impl.module.settings.converter.EnumConverter;

import java.util.function.Predicate;

public class Setting <T> {

    public String name;
    public T value;
    public T defaultValue;
    public T min;
    public T max;
    public Predicate<T> visible;
    public boolean hasRestriction;

    public Setting(String name, T defaultValue) {
        this.name = name;
        this.value = defaultValue;
        this.defaultValue = defaultValue;
        this.hasRestriction = false;
    }

    public Setting(String name, T defaultValue, T min, T max) {
        this.name = name;
        this.value = defaultValue;
        this.defaultValue = defaultValue;
        this.min = min;
        this.max = max;
        this.hasRestriction = true;
    }
    public Setting(String name, T defaultValue, Predicate<T> visible) {
        this.name = name;
        this.value = defaultValue;
        this.defaultValue = defaultValue;
        this.visible = visible;
        this.hasRestriction = false;
    }


    public Setting(String name, T defaultValue, T min, T max, Predicate<T> visible) {
        this.name = name;
        this.value = defaultValue;
        this.defaultValue = defaultValue;
        this.min = min;
        this.max = max;
        this.visible = visible;
        this.hasRestriction = true;
    }

    public String getName() {
        return this.name;
    }

    public T getValue() {
        return this.value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public int getEnum(String input) {
        try {
            for (int i = 0; i < this.value.getClass().getEnumConstants().length; ++i) {
                Enum<?> e = (Enum<?>) this.value.getClass().getEnumConstants()[i];
                if (!e.name().equalsIgnoreCase(input)) continue;
                return i;
            }
        } catch (ClassCastException ignored) {

        }
        return -1;
    }

    public void setEnumValue(String value) {
        try {
            for (Enum<?> e :  ((Enum<?>) this.value).getClass().getEnumConstants()) {
                if (!e.name().equalsIgnoreCase(value)) continue;
                this.value = (T) e;
            }
        } catch (ClassCastException ignored) {

        }
    }

    public String currentEnumName() {
        try {
            return EnumConverter.getProperName((Enum<?>) this.value);
        } catch (ClassCastException ignored) {}
        return "";
    }

    public int currentEnum() {
        try {
            return EnumConverter.currentEnum((Enum<?>) this.value);
        } catch (ClassCastException ignored) {}
        return 0;
    }

    public void increaseEnumNoEvent() {
        try {
            this.value = (T) EnumConverter.increaseEnum((Enum<?>) this.value);
        } catch (ClassCastException ignored) {

        }
    }

    public String getType() {
        if (this.isEnumSetting()) {
            return "Enum";
        }
        return this.getClassName(this.defaultValue);
    }

    public <T> String getClassName(T value) {
        return value.getClass().getSimpleName();
    }

    public boolean isNumberSetting() {
        return this.value instanceof Number;
    }

    public boolean isEnumSetting() {
        return !this.isNumberSetting()
                && !(this.value instanceof String)
                && !(this.value instanceof Character)
                && !(this.value instanceof Boolean)
                && !(this.value instanceof Bind)
                && !this.name.equalsIgnoreCase("Keybind")
                && !this.name.equalsIgnoreCase("DisplayName");
    }

    public boolean isStringSetting() {
        return this.value instanceof String;
    }

    public T getDefaultValue() {
        return this.defaultValue;
    }

    public String getValueAsString() {
        return this.value.toString();
    }

    public boolean hasRestriction() {
        return this.hasRestriction;
    }

    public void setVisible(Predicate<T> visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        if (this.visible == null) {
            return true;
        }
        return this.visible.test(this.getValue());
    }

    public T getMin() {
        return this.min;
    }

    public void setMin(T min) {
        this.min = min;
    }

    public T getMax() {
        return this.max;
    }

    public void setMax(T max) {
        this.max = max;
    }
}