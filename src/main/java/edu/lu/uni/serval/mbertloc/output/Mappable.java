package edu.lu.uni.serval.mbertloc.output;

import java.util.List;

public abstract class Mappable<Q, T extends Queryable<Q>> {

    public abstract List<T> getItems();

    public T getChildrenByQuery(Q query) {
        if (getItems() != null && !getItems().isEmpty()) {
            for (T item : getItems()) {
                if (query.equals(item.getQueryable()))
                    return item;
            }
        }
        return null;
    }
}
