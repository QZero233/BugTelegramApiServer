package com.qzero.bt.message.data.session;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatSessionParameterDao extends JpaRepository<ChatSessionParameter,Long> {

    ChatSessionParameter findBySessionIdAndAndParameterName(String sessionId,String parameterName);

    boolean existsBySessionIdAndAndParameterName(String sessionId,String parameterName);

}
