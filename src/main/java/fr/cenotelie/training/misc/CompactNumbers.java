package fr.cenotelie.training.misc;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class CompactNumbers {

    public static void main(String[] args) {
        NumberFormat fmt = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.LONG);
        fmt.setMinimumFractionDigits(3);
        System.out.println(fmt.format(1_200_000));
        try {
            System.out.println(fmt.parse("10 thousand"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
