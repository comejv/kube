package kube.model.action;

public enum ActionType {

    /**********
     * GAME
     **********/

    // --- PRINT ---

    PRINT_AI,
    PRINT_COMMAND_ERROR,
    PRINT_WAIT_COORDINATES,
    PRINT_GOODBYE,
    PRINT_HELP,
    PRINT_LIST_MOVES,
    PRINT_NEXT_PLAYER,
    PRINT_PLAYER,
    PRINT_START,
    PRINT_STATE,
    PRINT_WELCOME,
    PRINT_WIN_MESSAGE,
    PRINT_FORBIDDEN_ACTION,
    PRINT_ASK_NB_PLAYERS,
    PRINT_ASK_GAME_MODE,
    PRINT_ASK_SAVE_FILE_NAME,
    PRINT_ASK_LOAD_FILE_NAME,
    PRINT_LOADED,
    PRINT_SAVED,
    PRINT_WRONG_FILE_NAME,

    // --- MODEL ---

    START,
    RESET,
    SWAP,
    BUILD,
    SHUFFLE,
    VALIDATE,
    MOVE,
    MOVE_NUMBER,
    UNDO,
    REDO,
    REMOVE,
    SAVE,
    LOAD,
    AI_MOVE,

    /**********
     * SERVICES
     **********/

    // --- PRINT ---

    PRINT_WAITING_FOR_CONNECTION,
    PRINT_ASK_HOST_OR_JOIN,
    PRINT_CONNECTION_ETABLISHED,
    PRINT_CONNECTION_ERROR,
    PRINT_NOT_YOUR_TURN,

    // --- MODEL ---

    PRINT_ASK_IP,
    INIT_K3,
    ITS_YOUR_TURN,
    PLAYER_DATA,
    ACKNOWLEDGEMENT,
    PRINT_WAITING_RESPONSE,
    CONNECTION_CLOSED,

    /**********
     * GUI
     **********/

    // --- GLOBAL ---

    SET_BUTTON_PRESSED,
    SET_BUTTON_HOVERED,
    SET_BUTTON_RELEASED,
    SET_BUTTON_CLICKED,
    SET_BUTTON_DEFAULT,
    SETTINGS,
    RETURN_TO_MENU,

    // --- Menu ---

    PLAY_LOCAL,
    PLAY_ONLINE,
    RULES,
    QUIT,
    VOLUME,
    PARAMETERS,
    NEXT_RULE,
    END_RULE,

    /**********
     * UNUSED
     **********/

    // TODO : remove unused actions
    GRAB_HEX,
    ADD_GLASS,
    REMOVE_GLASS,
    MOVE_FROM_NETWORK,
    OTHER_PLAYER,
    PRINT_PLAYER_NAME,
    PRINT_ASK_PLAYER_NAME,
    SHOW_ALL,
    SHOW_MOUNTAIN,
}
