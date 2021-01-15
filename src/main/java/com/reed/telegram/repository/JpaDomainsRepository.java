package com.reed.telegram.repository;

import com.reed.telegram.entity.Domain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaDomainsRepository extends JpaRepository<Domain, Long> {

}
