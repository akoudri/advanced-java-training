package com.akfc.training.misc;

public enum Day {
    MONDAY(false), TUESDAY(false), WEDNESDAY(false),
    THURSDAY(false), FRIDAY(false),
    SATURDAY(true), SUNDAY(true);

    private boolean isWeekend;

    Day(boolean isWeekend) {
        this.isWeekend = isWeekend;
    }
}
