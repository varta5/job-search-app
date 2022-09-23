package com.vargatamas.jobsearchapp.repositories;

import com.vargatamas.jobsearchapp.models.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {

    List<Position> findByNameContainsOrJobLocationContains(String subStringOfName, String subStringOfJobLocation);

}
