package com.nuos.groups.repos;

import com.nuos.groups.domain.Student;
import org.springframework.data.repository.CrudRepository;

public interface StudentRepo extends CrudRepository<Student, Integer> {
    Student findByName(String name);
}
