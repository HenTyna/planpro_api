package com.planprostructure.planpro.domain.trips;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "tb_destinations")
public class Destination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "destination_id")
    private Long id;

    @Column(name = "id")
    private String destDate;

    @Column(name = "trip_id", nullable = false)
    private Long tripId;

    @Column(name = "destination_name")
    private String destinationName;

    @Column(name = "days")
    private Integer days;

    /**
     * Now that Destination is an Entity,
     * we CAN put an @ElementCollection of Strings here.
     * JPA will create a table named "destination_activities".
     */
    @ElementCollection
    @CollectionTable(
            name = "destination_activities",
            joinColumns = @JoinColumn(name = "destination_id")
    )
    @Column(name = "activity")
    private List<String> activities = new ArrayList<>();

    @Builder
    public Destination(Long tripId,String destDate, String destinationName, Integer days, List<String> activities) {
        this.tripId = tripId;
        this.destDate = destDate;
        this.destinationName = destinationName;
        this.days = days;
        if (activities != null) {
            this.activities = activities;
        }
    }
}
