package com.planprostructure.planpro.domain.trips;

import com.planprostructure.planpro.payload.trips.IGetTrips;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DestinationRepository extends JpaRepository<Destination, Long> {

    @Query(value = """
            WITH activities_per_destination AS (
                SELECT da.destination_id,
                       json_agg(da.activity ORDER BY da.activity) AS activities
                FROM destination_activities da
                GROUP BY da.destination_id
            )
            SELECT td.trip_id,
                   json_agg(
                       json_build_object(
                           'id', d.id,
                           'destination_id', d.destination_id,
                           'destination_name', d.destination_name,
                           'days', d.days,
                           'activities', COALESCE(apd.activities, '[]'::json)
                       )
                       ORDER BY d.destination_id
                   ) AS destinations
            FROM tb_destinations td
            JOIN tb_destinations d ON td.destination_id = d.destination_id
            LEFT JOIN activities_per_destination apd ON d.destination_id = apd.destination_id
            WHERE td.trip_id = ?1
            GROUP BY td.trip_id
            ORDER BY td.trip_id
            """, nativeQuery = true)
    List<IGetTrips> findAllByUserIdAndFilters(Long tripId);
}
