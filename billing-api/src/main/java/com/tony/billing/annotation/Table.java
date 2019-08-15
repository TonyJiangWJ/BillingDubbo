package com.tony.billing.annotation;

import java.lang.annotation.*;

/**
 * @author by TonyJiang on 2017/6/10.
 */
@Inherited
@Target(ElementType.TYPE)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {
    String value();
}
