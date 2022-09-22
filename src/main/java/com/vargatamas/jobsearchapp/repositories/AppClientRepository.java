package com.vargatamas.jobsearchapp.repositories;

import com.vargatamas.jobsearchapp.models.AppClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppClientRepository extends JpaRepository<AppClient, String> {

    Optional<AppClient> findByEmailAddress(String emailAddress);

}
