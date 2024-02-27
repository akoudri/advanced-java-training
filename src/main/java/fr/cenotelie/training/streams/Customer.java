package fr.cenotelie.training.streams;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record Customer(int pClass, boolean survived, String name, Sex sex, double age) {

    public String[] fullName() {
        Pattern p = Pattern.compile("(\\w+), (\\w+)\\. (.*)");
        Matcher m = p.matcher(name);
        if (m.find()) {
            return new String[] { m.group(2), m.group(1), m.group(3)};
        }
        return null;
    }

    public String category() {
        if (age < 20.0) return "20-";
        if (age < 40.0) return "20-40";
        if (age < 60.0) return "40-60";
        return "60+";
    }

    @Override
    public String toString() {
        return String.format("%d\t%b\t%s\t%s\t%f", pClass, survived, name, sex.name(), age);
    }

}
