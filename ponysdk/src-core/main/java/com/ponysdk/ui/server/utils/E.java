
package com.ponysdk.ui.server.utils;

public class E {

    public static boolean quals(final Object s1, final Object s2) {
        if (s1 != null) {
            return s1.equals(s2);
        } else {
            return s2 == null;
        }
    }

}
