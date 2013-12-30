package com.persesgames.jogl;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;

/**
 * Date: 12/30/13
 * Time: 9:25 PM
 */
public class Keyboard implements KeyListener {

    private boolean [] pressed = new boolean[127];

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() < pressed.length) {
            pressed[keyEvent.getKeyCode()] = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() < pressed.length) {
            pressed[keyEvent.getKeyCode()] = false;
        }
    }

    public boolean isPressed(short keyCode) {
        return keyCode < pressed.length && pressed[keyCode];
    }
}
