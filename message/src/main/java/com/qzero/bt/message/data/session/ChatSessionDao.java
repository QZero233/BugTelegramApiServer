package com.qzero.bt.message.data.session;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatSessionDao extends JpaRepository<ChatSession,String> {



}
