package com.example.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.entity.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message,Long>{

    Message findByMessageId(int messageId);

    
    Message findByMessageId(Long messageId);


    @Modifying
    @Query("UPDATE Message m set m.messageText=:text where m.messageId=:id")
    int updateMessage(@Param("text") String messageText,@Param("id") Integer messageId);

    
    List<Message> findAllByPostedBy(Integer id);

}