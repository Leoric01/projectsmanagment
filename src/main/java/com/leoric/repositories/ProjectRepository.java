package com.leoric.repositories;

import com.leoric.models.Project;
import com.leoric.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("SELECT p FROM Project p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :partialName, '%')) AND :user MEMBER OF p.teamMembers")
    List<Project> findByNameContainingAndTeamMembersContains(@Param("partialName") String partialName, @Param("user") User user);

    List<Project> findByTeamMembersContainingOrOwner(User user, User oner);

}
