package com.company.youse.services.query.sysaccount;

import com.company.youse.models.yousepay.SysAccount;
import com.company.youse.platform.InternalMessage;
import com.company.youse.platform.decorator.query.QueryBaseService;
import com.company.youse.platform.result.QueryResult;
import com.company.youse.repositrories.yousepay.SysAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetSysAccountByMemberIdService extends QueryBaseService<GetSysAccountByMemberIdQuery, SysAccount> {

    private final SysAccountRepository sysAccountRepository;

    @Autowired
    public GetSysAccountByMemberIdService(SysAccountRepository sysAccountRepository) {
        this.sysAccountRepository = sysAccountRepository;
    }

    @Override
    public QueryResult<SysAccount> execute(GetSysAccountByMemberIdQuery query) {

        Optional<SysAccount> sysAccount = sysAccountRepository.findByMemberId(query.getMemberId());

        if (!sysAccount.isPresent()){

            InternalMessage internalMessage = InternalMessage.builder()
                    .message("Account with memberId: ".concat(query.getMemberId()).concat(" does not exist."))
                    .build();

            return new QueryResult
                    .Builder<SysAccount>()
                    .noContent()
                    .message(internalMessage)
                    .build();
        }

        return new QueryResult
                .Builder<SysAccount>()
                .data(sysAccount.get())
                .ok()
                .build();
    }
}
