package com.qzero.bt.message.data.notice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataNoticeDao extends JpaRepository<DataNotice,String> {

    List<DataNotice> findByTargetUserName(String targetUserName);

    boolean existsByNoticeIdAndTargetUserName(String noticeId,String targetUserName);

}
