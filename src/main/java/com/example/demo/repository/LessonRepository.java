package com.example.demo.repository;

import com.example.demo.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LessonRepository extends JpaRepository<Lesson, Long> {

    @Query("SELECT MAX(l.lessonOrder) FROM Lesson l WHERE l.chapter.id = :chapterId")
    Integer findMaxLessonOrderByChapterId(@Param("chapterId") Long chapterId);

}


