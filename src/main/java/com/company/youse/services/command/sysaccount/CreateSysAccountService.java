package com.company.youse.services.command.sysaccount;

import com.company.youse.models.yousepay.SysAccount;
import com.company.youse.platform.InternalMessage;
import com.company.youse.platform.decorator.command.CommandBaseService;
import com.company.youse.platform.result.CommandResult;
import com.company.youse.pojo.SysAccountDTO;
import com.company.youse.repositrories.yousepay.SysAccountRepository;
import com.company.youse.services.query.sysaccount.GetSysAccountByMemberIdQuery;
import com.company.youse.services.query.sysaccount.GetSysAccountByMemberIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateSysAccountService extends CommandBaseService<CreateSysAccountCommand, SysAccountDTO> {

    private final SysAccountRepository sysAccountRepository;
    private final GetSysAccountByMemberIdService getSysAccountByMemberIdService;

    @Autowired
    public CreateSysAccountService(GetSysAccountByMemberIdService getSysAccountByMemberIdService
    ,SysAccountRepository sysAccountRepository) {
        this.sysAccountRepository = sysAccountRepository;
        this.getSysAccountByMemberIdService = getSysAccountByMemberIdService;
    }

    @Override
    public CommandResult<SysAccountDTO> execute(CreateSysAccountCommand command) {

        var sysAccountResult = getSysAccountByMemberIdService.decorate(
                new GetSysAccountByMemberIdQuery(command.getMemberId()));

        if (!sysAccountResult.isNoContent()){
            InternalMessage internalMessage = InternalMessage.builder()
                    .httpStatus(404)
                    .message("Duplicate transaction with same memberId found").build();

            return new CommandResult.Builder<SysAccountDTO>().g(sysAccountResult.getData().toDTO())
                    .badRequest(internalMessage).build();
        }

        var sysAccount = new SysAccount();
        sysAccount.setContactEmail(command.getContactEmail());
        sysAccount.setContactPhone(command.getContactPhone());
        sysAccount.setMemberId(command.getMemberId());
        sysAccount.setOrgName(command.getOrgName());

        sysAccount = sysAccountRepository.save(sysAccount);

        return new CommandResult.Builder<SysAccountDTO>().g(sysAccount.toDTO())
                .id(sysAccount.getMemberId())
                .ok()
                .build();
    }
}
