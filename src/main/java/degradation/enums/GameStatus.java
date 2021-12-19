package degradation.enums;

public enum GameStatus {
    RUNNING("running"),
    GAME_OVER("game over"),
    COMPLETED("completed");

    public final String value;

    GameStatus(String value) {
        this.value = value;
    }

}
