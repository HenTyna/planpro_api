package com.planprostructure.planpro.service.trips;

import com.planprostructure.planpro.domain.trips.Destination;
import com.planprostructure.planpro.domain.trips.DestinationRepository;
import com.planprostructure.planpro.domain.trips.Trips;
import com.planprostructure.planpro.domain.trips.TripsRepository;
import com.planprostructure.planpro.enums.CategoryEnums;
import com.planprostructure.planpro.enums.CurrencyEnum;
import com.planprostructure.planpro.enums.TripsStatus;
import com.planprostructure.planpro.helper.AuthHelper;
import com.planprostructure.planpro.payload.trips.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TripsServiceImpl implements TripsService{
    private final TripsRepository tripsRepository;
    private final DestinationRepository destinationRepository;

    @Override
    @Transactional(readOnly = true)
    public Object getTrips(String searchValue, String filterCategory, String filterStatus) throws Throwable {
       var userId = AuthHelper.getUserId();

       var trips = tripsRepository.findByUserIdAndFilters(userId, searchValue, filterCategory, filterStatus);
        var response = trips.stream().map(
                trip -> {
                    List<Destination> destinations = destinationRepository.findAllByUserIdAndFilters(trip.getId());
                    List<DestinationResponse> destinationresponse = new ArrayList<>();
                    for (Destination destination : destinations) {
                        destinationresponse.add(DestinationResponse.builder()
                                .destinationName(destination.getDestinationName())
                                .days(destination.getDays())
                                .activities(destination.getActivities())
                                .build());
                    }
                    return TripsResponse.builder()
                            .id(trip.getId())
                            .title(trip.getTitle())
                            .description(trip.getDescription())
                            .startDate(trip.getStartDate())
                            .endDate(trip.getEndDate())
                            .category(trip.getCategory().getValue())
                            .status(trip.getStatus().getValue())
                            .budget(trip.getBudget())
                            .currency(trip.getCurrency().getValue())
                            .members(trip.getMembers())
                            .accommodation(trip.getAccommodation())
                            .transportation(trip.getTransportation())
                            .remarks(trip.getRemarks())
                            .imageUrl(trip.getImageUrl())
                            .location(trip.getLocation())
                            .destinations(destinationresponse)
                            .build();
                }

        ).toList();
        return response;
    }

    @Override
    @Transactional
    public void createTrips(TripsRequest request) throws Throwable {

        List<Destination> destinationEntities = new ArrayList<>();
        if (request.getDestinations() != null) {
            for (DestinationRequest destRequest : request.getDestinations()) {
                Destination destination = Destination.builder()
                        .destinationName(destRequest.getDestinationName())
                        .days(destRequest.getDays())
                        .activities(destRequest.getActivities())
                        .build();
                destinationEntities.add(destination);
            }
        }

        var trips = Trips.builder()
                .userId(AuthHelper.getUserId())
                .title(request.getTitle())
                .description(request.getDescription())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .category(CategoryEnums.fromValue(request.getCategory()))
                .status(TripsStatus.fromValue(request.getStatus()))
                .budget(request.getBudget())
                .currency(CurrencyEnum.fromValue(request.getCurrency()))
                .members(request.getMembers())
                .accommodation(request.getAccommodation())
                .transportation(request.getTransportation())
                .remarks(request.getRemarks())
                .imageUrl(request.getImageUrl())
                .build();
        var savedTrips = tripsRepository.save(trips);
        Long tripId = savedTrips.getId();
        for (Destination d : destinationEntities) {
            d.setTripId(tripId);
        }
        destinationRepository.saveAll(destinationEntities);
    }
}
