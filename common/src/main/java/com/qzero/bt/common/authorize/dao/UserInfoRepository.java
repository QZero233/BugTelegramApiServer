package com.qzero.bt.common.authorize.dao;

import com.qzero.bt.common.authorize.data.UserInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfoEntity,String> {
}
