package com.eltechs.axs.widgets.actions;

public interface Action {
    String getName();

    boolean isCheckable();

    boolean isChecked();

    boolean isEnabled();

    void run();
}
