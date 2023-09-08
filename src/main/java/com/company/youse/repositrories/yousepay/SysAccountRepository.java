package com.company.youse.repositrories.yousepay;

import com.company.youse.models.yousepay.SysAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SysAccountRepository extends JpaRepository<SysAccount,Long> {
    Optional<SysAccount> findByMemberId(String memberId);
}
