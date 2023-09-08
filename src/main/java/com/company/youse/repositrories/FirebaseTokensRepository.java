package com.company.youse.repositrories;

import com.company.youse.models.FirebaseTokens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FirebaseTokensRepository extends JpaRepository<FirebaseTokens, Long> {

}
