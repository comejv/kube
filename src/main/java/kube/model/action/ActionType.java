package kube.model.action;

public enum ActionType {
    // Global
    SET_BUTTON_PRESSED,
    SET_BUTTON_HOVERED,
    SET_BUTTON_RELEASED,
    SET_BUTTON_CLICKED,
    SET_BUTTON_DEFAULT,
    SETTINGS,

    // Menu
    PLAY_ONLINE,
    PLAY_LOCAL,
    RULES,
    VOLUME,

    // Phase 1
    GRAB_HEX,
    VALIDATE,

    // Phase 2

    // Overlay
    ADD_GLASS,
    REMOVE_GLASS,
}
