package com.qzero.bt.message.data.session;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMemberDao extends JpaRepository<ChatMember,Integer> {

    void deleteBySessionIdAndUserName(String sessionId,String userName);

    boolean existsBySessionIdAndUserName(String sessionId,String userName);

    boolean existsBySessionIdAndUserNameAndLevelIsGreaterThanEqual(String sessionId,String userName,int level);

    List<ChatMember> findAllBySessionId(String sessionId);

    @Query(value = "SELECT userName FROM ChatMember WHERE sessionId=?1")
    List<String> findAllNameBySessionId(String sessionId);

    @Query(value = "SELECT sessionId FROM ChatMember WHERE userName=?1")
    List<String> findAllSessionIdByName(String userName);

    ChatMember findBySessionIdAndUserName(String sessionId,String userName);

}
