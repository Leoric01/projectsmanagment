package com.leoric.repositories;

import com.leoric.models.Project;
import com.leoric.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByNameContainingAndTeamContains(String partialName, User user);

//    List<Project> findByOwner(User user);


//    @Query("SELECT p FROM Project p JOIN p.team t WHERE t=:user")
//    List<Project> findProjectByTeam(@Param("user") User user);

    List<Project> findByTeamContainingOrOwner(User user, User oner);
}
