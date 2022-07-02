package io.pp.arcade.global.util;

public class EloRating {
    public static Integer pppChange(Integer myPPP, Integer opponentPPP, Boolean isWin, Boolean isOneSide) {
        Double we = 1.0 / (Math.pow(10.0, (opponentPPP - myPPP) / 400.0) + 1.0);
        Double change = 40 * ((isWin ? 1 : 0) - we);
        if (isOneSide) {
            change = change * 1.2;
        }
        return change.intValue();
    }
}