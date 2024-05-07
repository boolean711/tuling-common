package com.tuling.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Set;

/**
 * Validator 校验框架工具
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidatorUtils {

    private static final Validator VALID = SpringUtils.getBean(Validator.class);

    public static <T> void validate(T object, Class<?>... groups) {
        Set<ConstraintViolation<T>> validate = VALID.validate(object, groups);
        if (!validate.isEmpty()) {
            throw new ConstraintViolationException("参数校验异常", validate);
        }
    }

    // Regular expression for validating Chinese mainland mobile phone numbers
    private static final String CHINA_MOBILE_REGEX = "^1[3-9]\\d{9}$";

    /**
     * Validates if the provided string is a valid Chinese mainland mobile phone number.
     *
     * @param phoneNumber the phone number to validate
     * @return true if the phone number is valid, false otherwise
     */
    public static boolean isValidChinesePhoneNumber(String phoneNumber) {
        return phoneNumber != null && phoneNumber.matches(CHINA_MOBILE_REGEX);
    }

}
