package com.planprostructure.planpro.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.planprostructure.planpro.components.AbstractEnumConverter;
import com.planprostructure.planpro.components.GenericEnum;

public enum CurrencyEnum implements GenericEnum<CurrencyEnum, String> {

    USD("1"),
    RIEL("2"),
    ;
    private final String value;

    CurrencyEnum(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getLabel() {
        switch (this) {
            case USD:
                return "USD";
            case RIEL:
                return "RIEL";
            default:
                return "RIEL";
        }
    }
    @JsonCreator
    public static CurrencyEnum fromValue(String value) {
        for (CurrencyEnum currency : CurrencyEnum.values()) {
            if (currency.value.equals(value)) {
                return currency;
            }
        }
        return null;
    }

    public static class Converter extends AbstractEnumConverter<CurrencyEnum, String> {
        public Converter() {
            super(CurrencyEnum.class);
        }
    }
}
