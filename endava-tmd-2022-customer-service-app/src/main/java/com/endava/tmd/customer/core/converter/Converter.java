package com.endava.tmd.customer.core.converter;

import java.util.List;

public interface Converter<S, D> {

    D convert(S source);

    default List<D> convert(final List<S> sources) {
        return sources == null ? List.of() : sources.stream().map(this::convert).toList();
    }

}
