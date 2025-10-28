package com.example.demo.service;

import com.example.demo.dto.CourseRequestDto;
import com.example.demo.dto.CourseResponseDto;

import java.util.List;

public interface CourseService {
    List<CourseResponseDto> getAllCourses();

    CourseResponseDto getCourseById(Long id);

    CourseResponseDto createCourse(CourseRequestDto dto);

    CourseResponseDto updateCourse(Long id, CourseRequestDto dto);

    boolean deleteCourse(Long id);
}
