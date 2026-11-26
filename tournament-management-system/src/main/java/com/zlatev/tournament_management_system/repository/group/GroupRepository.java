package com.zlatev.tournament_management_system.repository.group;

import com.zlatev.tournament_management_system.entity.model.group.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GroupRepository extends JpaRepository<Group, UUID> {
}
