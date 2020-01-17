package com.nuos.groups.repos;

import com.nuos.groups.domain.Group;
import org.springframework.data.repository.CrudRepository;

public interface GroupRepo extends CrudRepository<Group, Integer> {
    Group findByName(String name);
}