package com.esifit;

/**
 * Native entry point kept separate from the JavaFX Application subclass.
 * This lets classpath-based jpackage launchers load the bundled JavaFX jars
 * before JavaFX performs its application startup checks.
 */
public final class Launcher {
    private Launcher() {
    }

    public static void main(String[] args) {
        App.main(args);
    }
}
