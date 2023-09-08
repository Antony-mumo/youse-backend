package com.company.youse.repositrories;

import com.company.youse.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * method finds if user with mentioned email already exists
     * @param email
     * @return
     */
    User findUserByEmail(String email);

    /**
     * method finds if user with mentioned email already exists
     * @param phone
     * @return
     */
    User findUserByPhoneNumber(String phone);

    User findUserByEmailOrPhoneNumber(String userName, String phoneNumber);

}
