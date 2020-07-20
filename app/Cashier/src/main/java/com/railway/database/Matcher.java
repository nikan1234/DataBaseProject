package com.railway.database;

public abstract class Matcher {
    public static final String EQUAL = "=";
    public static final String LESS = "<";
    public static final String GREATER = ">";
    public static final String NOT_LESS = ">=";
    public static final String NOT_GREATER = "<=";

    private Object value = null;
    private String cmp = EQUAL;

    public Matcher bind(Object o) {
        value = o;
        return this;
    }

    public Object getValue() {
        return value;
    }

    public Matcher comparator(String cmp) {
        this.cmp = cmp;
        return this;
    }

    public String comparator() {
        return cmp;
    }

    public abstract String getCondition();


    protected static String format(String s) {
        return '\'' + s + '\'';
    }
}