package com.example.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.entity.Message;

/**
 * Repository interface for accessing and managing Message entities in the database.
 * This interface extends JpaRepository to provide CRUD operations for Message entities.
 *
 * @author Micah Hogan
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    
    /**
     * Finds all messages posted by a specific user.
     *
     * @param postedBy the ID of the user who posted the messages
     * @return a list of messages posted by the specified user
     */
    List<Message> findByPostedBy(Integer postedBy);
}
