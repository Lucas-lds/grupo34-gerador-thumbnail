package com.fiap.geradorThumbnail.infrastructure.adapter.out.repository;

import com.fiap.geradorThumbnail.infrastructure.adapter.out.entity.VideoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VideoRepository extends JpaRepository<VideoEntity, UUID> {
}
