package com.endava.tmd.customer.swg.model.base;

import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public abstract class ParentResponse<T, R> {
    @Schema(description = "The application build version", example = "1.2.3-SNAPSHOT")
    private String buildVersion;

    @Schema(description = "The identifier of the request", example = "9e517fa3682c82ed")
    private String traceId;

    @Schema(description = "Any info the server returns, additional to the result(s)")
    private List<AdditionalInfo> additionalInfo = new ArrayList<>();

    @Schema(description = "The result(s) from the operation")
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
