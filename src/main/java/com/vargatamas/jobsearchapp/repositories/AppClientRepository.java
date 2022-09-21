package com.vargatamas.jobsearchapp.repositories;

import com.vargatamas.jobsearchapp.models.AppClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppClientRepository extends JpaRepository<AppClient, String> {
}
