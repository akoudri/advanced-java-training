package com.akfc.training.misc;

import java.util.Optional;

public class SoundCard {

    private Optional<Usb> usb;

    public SoundCard(Optional<Usb> usb) {
        this.usb = usb;
    }

    public Optional<Usb> getUsb() {
        return usb;
    }

    public void setUsb(Optional<Usb> usb) {
        this.usb = usb;
    }
}
