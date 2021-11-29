package com.tadah.common.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

/**
 * 선언된 클래스 또는 메서드를 테스트 커버리지에서 제외한다.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({TYPE, METHOD})
public @interface Generated {
}
