package pl.bajtas.squaremoose.api.util.config;

public enum ClassToRegisterEnum {
    CONTROLLER_CLASS_PACKAGE("pl.bajtas.squaremoose.api.controller."),
    SERVICE_CLASS_PACKAGE("pl.bajtas.squaremoose.api.service.");

    private final String value;

    private ClassToRegisterEnum(String s) {
        value = s;
    }

    public boolean equalsName(String otherName) {
        return (otherName == null) ? false : value.equals(otherName);
    }

    public String toString() {
        return this.value;
    }

    public boolean contains(String str) {
        if (value.contains(str))
            return true;

        return false;
    }

}
