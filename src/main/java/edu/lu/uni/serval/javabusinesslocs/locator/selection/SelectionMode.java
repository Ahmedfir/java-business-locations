package edu.lu.uni.serval.javabusinesslocs.locator.selection;

public enum SelectionMode {
    ORDERED("ordered"), RANDOM("random");

    private final String id;

    SelectionMode(String id) {
        this.id = id;
    }

    public static final SelectionMode forId(String id) {
        for (SelectionMode sm : SelectionMode.values()) {
            if (sm.id.equals(id)) {
                return sm;
            }
        }
        return ORDERED;
    }

    @Override
    public String toString() {
        return "SelectionMode{" +
                "id='" + id + '\'' +
                '}';
    }
}
