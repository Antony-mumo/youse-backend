package com.company.youse.repositrories.yousepay;

import com.company.youse.models.yousepay.FactAcc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FactAccRepository extends JpaRepository<FactAcc,Long> {
}
