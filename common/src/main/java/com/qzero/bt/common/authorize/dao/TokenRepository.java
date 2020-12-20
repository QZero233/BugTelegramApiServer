package com.qzero.bt.common.authorize.dao;

import com.qzero.bt.common.authorize.data.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TokenRepository extends JpaRepository<TokenEntity,String> {

    void deleteByOwnerUserName(String ownerUserName);

    List<TokenEntity> getAllByOwnerUserName(String ownerUserName);

}
