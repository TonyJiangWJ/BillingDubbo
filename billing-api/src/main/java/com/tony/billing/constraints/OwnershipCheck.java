package com.tony.billing.constraints;

import com.tony.billing.constraints.enums.EnumOwnershipCheckTables;
import com.tony.billing.constraints.validators.OwnershipValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author jiangwenjie 2019-03-20
 */
@Constraint(validatedBy = OwnershipValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OwnershipCheck {

    /**
     * 需要校验的表
     *
     * @return
     */
    EnumOwnershipCheckTables value();

    String message() default "所有权校验失败";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
