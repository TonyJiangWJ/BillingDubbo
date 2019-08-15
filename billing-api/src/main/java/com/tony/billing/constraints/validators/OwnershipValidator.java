package com.tony.billing.constraints.validators;

import com.tony.billing.constraints.OwnershipCheck;
import com.tony.billing.constraints.enums.EnumOwnershipCheckTables;
import com.tony.billing.constraints.helper.BeanHelper;
import com.tony.billing.service.api.CommonValidateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author jiangwenjie 2019-03-20
 */
public class OwnershipValidator implements ConstraintValidator<OwnershipCheck, Object> {

    private EnumOwnershipCheckTables table;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void initialize(OwnershipCheck constraintAnnotation) {
        this.table = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        CommonValidateService commonValidateService = BeanHelper.getBean(CommonValidateService.class);
        if (commonValidateService != null) {
            boolean result = commonValidateService.validateOwnership(table, value);
            logger.info("针对数据表：{} 数据所有权校验结果：{}", table.getTableName(), result ? "通过" : "不通过");
            return result;
        } else {
            logger.error("校验service注入失败");
            return true;
        }
    }

}
