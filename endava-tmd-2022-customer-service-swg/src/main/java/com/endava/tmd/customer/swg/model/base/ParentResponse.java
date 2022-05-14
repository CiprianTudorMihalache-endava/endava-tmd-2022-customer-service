package com.endava.tmd.customer.swg.model.base;

import java.util.ArrayList;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public abstract class ParentResponse<T, R> {
    private String buildVersion;
    private String traceId;
    private List<AdditionalInfo> additionalInfo = new ArrayList<>();
    private List<R> results = new ArrayList<>();

    public T setBuildVersion(final String buildVersion) {
        this.buildVersion = buildVersion;
        return realType();
    }

    public T setTraceId(final String traceId) {
        this.traceId = traceId;
        return realType();
    }

    public T setAdditionalInfo(@NonNull final List<AdditionalInfo> additionalInfo) {
        this.additionalInfo.clear();
        this.additionalInfo.addAll(additionalInfo);
        return realType();
    }

    public T addAdditionalInfo(@NonNull final AdditionalInfo additionalInfo) {
        this.additionalInfo.add(additionalInfo);
        return realType();
    }

    public T setResults(@NonNull final List<R> results) {
        this.results.clear();
        this.results.addAll(results);
        return realType();
    }

    public T addResult(@NonNull final R result) {
        this.results.add(result);
        return realType();
    }

    @SuppressWarnings("unchecked")
    private T realType() {
        return (T) this;
    }
}
