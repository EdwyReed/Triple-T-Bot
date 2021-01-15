package com.reed.telegram.repository;

import com.reed.telegram.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaMessagesRepository extends JpaRepository<Message, Long> {

}
