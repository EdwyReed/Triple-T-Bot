package com.reed.telegram.repository;

import com.reed.telegram.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaUsersRepository extends JpaRepository<User, Long> {

    List<User> findByUsername(String username);
}
