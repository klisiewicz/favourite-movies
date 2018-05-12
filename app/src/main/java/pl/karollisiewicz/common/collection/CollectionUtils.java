package pl.karollisiewicz.common.collection;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;

public final class CollectionUtils {
    public static <E> Collection<E> newCollection(@NonNull final Iterable<? extends E> iterable) {
        final Collection<E> list = new ArrayList<>();
        for (E item : iterable) list.add(item);
        return list;
    }
}
