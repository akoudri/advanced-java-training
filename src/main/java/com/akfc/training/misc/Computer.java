package com.akfc.training.misc;

import java.util.Optional;

public class Computer {

    private Optional<SoundCard> soundCard;

    public Computer(Optional<SoundCard> soundCard) {
        this.soundCard = soundCard;
    }

    public Optional<SoundCard> getSoundCard() {
        return soundCard;
    }

    public void setSoundCard(Optional<SoundCard> soundCard) {
        this.soundCard = soundCard;
    }

    public static void main(String[] args) throws Exception {
        Usb usb = new Usb("v0.1");
        SoundCard sc = new SoundCard(Optional.of(usb));
        Computer c = new Computer(Optional.empty());
        String version = c.getSoundCard()
                .flatMap(SoundCard::getUsb)
                .map(Usb::getVersion)
                .orElse("No version");
        System.out.println(version);
    }


}
