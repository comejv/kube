package kube.model.action;

public enum ActionType {
    // --- Model ---
    SHOW_ALL,
    SHOW_MOUNTAIN,
    SWAP,
    SHUFFLE,
    VALIDATE,
    MOVE,
    UNDO,
    REDO,
    PRINT_AI,
    PRINT_COMMAND_ERROR,
    PRINT_WAIT_COORDINATES,
    PRINT_GOODBYE,
    PRINT_HELP,
    PRINT_LIST_MOVES,
    PRINT_NEXT_PLAYER,
    PRINT_PLAYER,
    PRINT_PLAYER_NAME,
    PRINT_START,
    PRINT_STATE,
    PRINT_WELCOME,
    PRINT_WIN_MESSAGE,
    PRINT_FORBIDDEN_ACTION,
    PRINT_ASK_NB_PLAYERS,
    PRINT_ASK_PLAYER_NAME,
    PRINT_ASK_GAME_MODE,

    // --- NETWORK ---
    PRINT_ASK_HOST_OR_JOIN,
    PRINT_ASK_IP,
    INIT_K3,
    OTHER_PLAYER,
    PRINT_WAITING_FOR_CONNECTION,
    PRINT_CONNECTION_ETABLISHED,
    PRINT_CONNECTION_ERROR,
    PRINT_NOT_YOUR_TURN,
    ITS_YOUR_TURN,
    MOVE_FROM_NETWORK,
    PLAYER_DATA,

    // --- GUI ---
    // Global
    SET_BUTTON_PRESSED,
    SET_BUTTON_HOVERED,
    SET_BUTTON_RELEASED,
    SET_BUTTON_CLICKED,
    SET_BUTTON_DEFAULT,
    SETTINGS,

    // Menu
    PLAY_LOCAL,
    PLAY_ONLINE,
    RULES,
    QUIT,
    VOLUME,
    PARAMETERS,
    NEXT_RULE,

    // Phase 1
    GRAB_HEX,

    // Phase 2

    // Overlay
    ADD_GLASS,
    REMOVE_GLASS,
}
