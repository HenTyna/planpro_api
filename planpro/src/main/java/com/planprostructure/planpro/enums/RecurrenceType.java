package com.planprostructure.planpro.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.planprostructure.planpro.components.AbstractEnumConverter;
import com.planprostructure.planpro.components.GenericEnum;

public enum RecurrenceType implements GenericEnum<RecurrenceType, String> {
    DAILY("daily"),
    WEEKLY("weekly"),
    MONTHLY("monthly"),
    YEARLY("yearly");

    private final String value;

    RecurrenceType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String getLabel() {
        switch (this) {
            case DAILY:
                return "Daily";
            case WEEKLY:
                return "Weekly";
            case MONTHLY:
                return "Monthly";
            case YEARLY:
                return "Yearly";
            default:
                return "Unknown";
        }
    }


    @JsonCreator
    public static RecurrenceType fromValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Value cannot be null or empty");
        }
        for (RecurrenceType recurrenceType : RecurrenceType.values()) {
            if (recurrenceType.name().equalsIgnoreCase(value.trim())) {
                return recurrenceType;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }

    public static class Converter extends AbstractEnumConverter<RecurrenceType, String> {
        public Converter() {
            super(RecurrenceType.class);
        }
    }
}
