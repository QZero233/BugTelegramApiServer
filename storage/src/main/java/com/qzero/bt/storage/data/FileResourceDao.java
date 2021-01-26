package com.qzero.bt.storage.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileResourceDao extends JpaRepository<FileResource,String> {
}
