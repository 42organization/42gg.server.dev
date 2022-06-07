package io.gg.arcade.global.utils;

public class EloRating {
    public static Integer pppChange(Integer myPPP, Integer opponentPPP, Boolean isWin) {
        Double we = 1.0 / (Math.pow(10.0, (opponentPPP - myPPP) / 400.0) + 1.0);
        Double change = 40 * ((isWin ? 1 : 0) - we);

        return change.intValue();
    }
}
