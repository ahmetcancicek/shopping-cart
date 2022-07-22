package com.shopping;

import org.junit.jupiter.api.Tag;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Tag("integration-test")
@Retention(RetentionPolicy.RUNTIME)
public @interface IT {
}
