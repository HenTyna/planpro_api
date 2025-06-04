package com.planprostructure.planpro.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.planprostructure.planpro.components.AbstractEnumConverter;
import com.planprostructure.planpro.components.GenericEnum;

public enum TripsStatus implements GenericEnum<TripsStatus, String> {

    IN_PROGRESS("IN01"),
    HOLD("IN02"),
    COMPLETED("IN03"),
    CANCELLED("IN04"),
    INCOMING("IN05"),
    PLANNING("IN06"),
    BOOKED("IN07"),
    ;

    private final String value;

    TripsStatus(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getLabel() {
        return switch (this) {
            case IN_PROGRESS -> "In Progress";
            case HOLD -> "On Hold";
            case COMPLETED -> "Completed";
            case CANCELLED -> "Cancelled";
            case INCOMING -> "Incoming";
            case PLANNING -> "Planning";
            case BOOKED -> "Booked";
        };
    }
    @JsonCreator
    public static TripsStatus fromValue(String value) {
        for (TripsStatus status : TripsStatus.values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        return null; // or throw an exception if preferred
    }

    public static class Converter extends AbstractEnumConverter<TripsStatus, String> {
        public Converter() {
            super(TripsStatus.class);
        }
    }
}
