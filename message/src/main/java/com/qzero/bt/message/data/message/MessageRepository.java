package com.qzero.bt.message.data.message;

import com.qzero.bt.message.data.message.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<ChatMessage,String> {
}
