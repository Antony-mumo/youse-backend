package com.company.youse.repositrories;


import com.company.youse.enums.RoleEnum;
import com.company.youse.models.Notification;
import com.company.youse.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findAllByAccountTypeAndUserOrderByCreatedAtDesc(RoleEnum type, User user);

}
