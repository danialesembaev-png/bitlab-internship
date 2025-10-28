package com.example.demo.service.impl;

import com.example.demo.dto.CourseRequestDto;
import com.example.demo.dto.CourseResponseDto;
import com.example.demo.mapper.CourseMapper;
import com.example.demo.model.Course;
import com.example.demo.repository.CourseRepository;
import com.example.demo.service.CourseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {
    private final CourseRepository repository;
    private final CourseMapper mapper;

    public CourseServiceImpl(CourseRepository repository, CourseMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<CourseResponseDto> getAllCourses() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CourseResponseDto getCourseById(Long id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElse(null);
    }

    @Override
    public CourseResponseDto createCourse(CourseRequestDto dto) {
        Course course = mapper.toEntity(dto);
        Course saved = repository.save(course);
        return mapper.toDto(saved);
    }

    @Override
    public CourseResponseDto updateCourse(Long id, CourseRequestDto dto) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setName(dto.getName());
                    existing.setDescription(dto.getDescription());
                    Course updated = repository.save(existing);
                    return mapper.toDto(updated);

                })
                .orElse(null);
    }

    @Override
    public boolean deleteCourse(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
