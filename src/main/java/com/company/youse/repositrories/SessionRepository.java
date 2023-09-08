package com.company.youse.repositrories;

import com.company.youse.models.Session;
import com.company.youse.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    /**
     * method finds if user with mentioned email already exists
     * @param refreshToken
     * @return
     */
    Session findFirstByRefreshToken(String refreshToken);

    List<Session> findAllByUser(User user);

    void deleteAllByUser(User user);

}
