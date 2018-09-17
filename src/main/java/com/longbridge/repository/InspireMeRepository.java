package com.longbridge.repository;

import com.longbridge.models.InspireMe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InspireMeRepository extends JpaRepository<InspireMe, Long> {

    @Query(value = "select i from InspireMe i where i.gender=:gender and i.events like %:event%")
    Page<InspireMe> findByGenderAndEventsLike(@Param("gender") String gender, @Param("event") String event, Pageable p);

    @Query(value = "select i from InspireMe i where i.gender=:gender and i.events like %:event% and i.hashTag like %:hashTag%")
    Page<InspireMe> findByGenderAndEventsLikeAndHashTagLike(@Param("gender") String gender, @Param("event") String event, @Param("hashTag") String hashTag, Pageable p);

    @Query(value = "select i.hashTag from InspireMe i where i.gender=:gender and i.events like %:event%")
    List<String> getHashtagsByGenderAndEvent(@Param("gender") String gender, @Param("event") String event);
}
