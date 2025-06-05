package com.planprostructure.planpro.payload.trips;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DestinationRequest {
    @NotNull
    private String id;
    @NotNull
    private String destinationName;
    @NotNull
    private Integer days;
    private List<String> activities;

    @Builder
    public DestinationRequest(String id, String destinationName, Integer days, List<String> activities) {
        this.id = id;
        this.destinationName = destinationName;
        this.days = days;
        this.activities = activities;
    }
}
