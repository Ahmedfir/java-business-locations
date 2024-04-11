package edu.lu.uni.serval.javabusinesslocs.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @see java-n-gram-line-level/src/main/java/utils/Checker.java
 */
public final class Checker {

    private Checker() throws IllegalAccessException {
        throw new IllegalAccessException("Utility class : static access only.");
    }

    public static boolean isTrimNlOrEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }

    public static <T> boolean allItemsNull(Collection<T> c){
        List<T> tmp = new ArrayList<>(c);
        tmp.removeAll(new ArrayList<T>() {{
            add(null);
        }});
        return tmp.isEmpty();
    }

    public static <T> boolean allItemsNull(Iterable<T> c){
        for (T t : c) {
            if (t != null) {
                return false;
            }
        }
        return true;
    }

    public static boolean isTrimNlOrEmpty(Collection<?> c) {
        return c == null || c.isEmpty() || allItemsNull(c);
    }

    public static boolean isTrimNlOrEmpty(Iterable<?> c) {
        return c == null || allItemsNull(c) ;
    }

    public static boolean isNlOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    public static boolean isNlOrEmpty(Collection<?> c) {
        return c == null || c.isEmpty();
    }

}
