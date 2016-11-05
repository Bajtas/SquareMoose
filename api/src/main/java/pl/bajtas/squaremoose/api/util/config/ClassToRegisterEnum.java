package pl.bajtas.squaremoose.api.util.config;

public enum ClassToRegisterEnum {
    CONTROLLER_CLASS_PACKAGE("pl.bajtas.squaremoose.api.controller."),
    SERVICE_CLASS_PACKAGE("pl.bajtas.squaremoose.api.service.");

    private final String value;

    ClassToRegisterEnum(String s) {
        value = s;
    }

    public boolean equalsName(String otherName) {
        return otherName != null && value.equals(otherName);
    }

    public boolean contains(String str) {
        return value.contains(str);

    }

    @Override
    public String toString() {
        return this.value;
    }
}
