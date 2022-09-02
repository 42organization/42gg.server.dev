package io.pp.arcade.v1.global.trace.service;

import io.pp.arcade.v1.global.trace.domain.TraceStatus;

public interface LogTrace {
    TraceStatus begin(String message);
    void end(TraceStatus status);
    void exception(TraceStatus status, Exception e);
}
