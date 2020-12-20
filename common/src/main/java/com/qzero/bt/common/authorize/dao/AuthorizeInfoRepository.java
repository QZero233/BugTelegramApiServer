package com.qzero.bt.common.authorize.dao;

import com.qzero.bt.common.authorize.data.AuthorizeInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorizeInfoRepository extends JpaRepository<AuthorizeInfoEntity,String> {

    boolean existsByUserName(String userName);

}
