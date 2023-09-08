package com.company.youse.repositrories;

import com.company.youse.models.Avatar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvatarRepository extends JpaRepository<Avatar, Long> {


    /**
     * method finds a profilePicture/avatar by its name
     * @param name
     * @return
     */
    Avatar findTopByName(String name) ;
}
