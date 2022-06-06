package io.gg.arcade.global.utils;

public class EloRating {
    public static Integer pppChange(Integer myPPP, Integer opponentPPP, Boolean isWin) {
        Double we = 1 / Math.pow(10, (myPPP - opponentPPP) / 400) + 1;
        Double change = 40 * (isWin ? 1 : 0 - we);

        return change.intValue();
    }
}
