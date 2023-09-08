package com.company.youse.services.command.sysaccount;

import com.company.youse.models.yousepay.OrgShortCode;
import com.company.youse.platform.InternalMessage;
import com.company.youse.platform.decorator.command.CommandBaseService;
import com.company.youse.platform.result.CommandResult;
import com.company.youse.pojo.OrgShortCodeDTO;
import com.company.youse.repositrories.yousepay.OrgShortCodeRepository;
import com.company.youse.services.query.sysaccount.GetSysAccountByMemberIdQuery;
import com.company.youse.services.query.sysaccount.GetSysAccountByMemberIdService;
import com.company.youse.services.query.yousepay.GetOrgCodeByCodeQuery;
import com.company.youse.services.query.yousepay.GetOrgCodeByCodeService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CreateOrgShortCodeService extends CommandBaseService<CreateOrgShortCodeCommand, OrgShortCodeDTO> {

    private final GetSysAccountByMemberIdService getSysAccountByMemberIdService;
    private final GetOrgCodeByCodeService getOrgCodeByCodeService;
    private final OrgShortCodeRepository orgShortCodeRepository;


    public CreateOrgShortCodeService(GetSysAccountByMemberIdService getSysAccountByMemberIdService
            , GetOrgCodeByCodeService getOrgCodeByCodeService, OrgShortCodeRepository orgShortCodeRepository) {
        this.getSysAccountByMemberIdService = getSysAccountByMemberIdService;
        this.getOrgCodeByCodeService = getOrgCodeByCodeService;
        this.orgShortCodeRepository = orgShortCodeRepository;
    }

    @Override
    public CommandResult<OrgShortCodeDTO> execute(CreateOrgShortCodeCommand command) {

        var sysAccountResult = getSysAccountByMemberIdService.decorate(
                new GetSysAccountByMemberIdQuery(command.getMemberId()));

        if (sysAccountResult.isNoContent()){
            InternalMessage internalMessage = InternalMessage.builder()
                    .httpStatus(404)
                    .message("Org with the memberId not found").build();

            return new CommandResult.Builder<OrgShortCodeDTO>()
                    .notFound().message(internalMessage).build();
        }

        var orgShortCodeQueryResult = getOrgCodeByCodeService.decorate(
                new GetOrgCodeByCodeQuery(command.getShortCodeType(), command.getShortCode()));

        if (!orgShortCodeQueryResult.isNoContent()){

            if (orgShortCodeQueryResult.getData().getSysAccount().getMemberId().equals(command.getMemberId())){

                InternalMessage internalMessage = InternalMessage.builder()
                        .httpStatus(404)
                        .message("Org short code already exists").build();

                return new CommandResult.Builder<OrgShortCodeDTO>().g(orgShortCodeQueryResult.getData().toDTO())
                        .badRequest(internalMessage).build();
            }
        }

        var orgShortCode = new OrgShortCode();
        orgShortCode.setShortCodeType(command.getShortCodeType());
        orgShortCode.setShortCode(command.getShortCode());
        orgShortCode.setTotalTransactedAmount(BigDecimal.ZERO);
        orgShortCode.setC2bReceiptUrl(command.getC2bReceiptUrl());
        orgShortCode.setTimeoutUrl(command.getTimeoutUrl());
        orgShortCode.setStkPushResultUrl(command.getStkPushResultUrl());
        orgShortCode.setApiSecret(command.getApiSecret());
        orgShortCode.setApiKey(command.getApiKey());
        orgShortCode.setSysAccount(sysAccountResult.getData());

        orgShortCode = orgShortCodeRepository.save(orgShortCode);

        return new CommandResult.Builder<OrgShortCodeDTO>().g(orgShortCode.toDTO())
                .id(String.valueOf(orgShortCode.getId())).ok().build();
    }
}
