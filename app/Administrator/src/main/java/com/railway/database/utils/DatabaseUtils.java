package com.railway.database.utils;

import java.io.IOException;

public class DatabaseUtils {

    public static boolean isTrue(String value) throws IOException {
        final String TRUE = "Y";
        final String FALSE = "N";
        switch (value) {
            case TRUE: return true;
            case FALSE: return false;
            default: throw new IOException("Wrong value");
        }
    }

    public static Character toChar(boolean b) {
        return (b) ? 'Y' : 'N';
    }
}
