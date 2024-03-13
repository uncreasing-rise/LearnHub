package com.example.learnhub.Repository;

import com.example.learnhub.Entity.Video;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface VideoRepository extends JpaRepository<Video,Integer> {

//    List<Video> findAllBySectionId(int sectionId);

    @Modifying
    @Transactional
    @Query("DELETE Video i WHERE i.videoId = ?1")
    int deleteVideo(int id);

    @Modifying
    @Transactional
    @Query("delete from Video v where v.section.sectionId = ?1")
    int deleteBySection_SectionId(int sectionID);

}
