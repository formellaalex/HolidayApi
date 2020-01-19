package com.bluestone.holidayservice.validation;

import com.bluestone.holidayservice.model.CommonHolidayRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CommonHolidayValidator implements ConstraintValidator<NotEqualCountriesConstraint, CommonHolidayRequest> {

    @Override
    public void initialize(NotEqualCountriesConstraint contactNumber){
    }

    @Override
    public boolean isValid(CommonHolidayRequest request, ConstraintValidatorContext cxt) {
            return !request.getFirstCountryCode().equals(request.getSecondCountryCode());
    }
}