package com.company.youse.controller.yousepay;

import com.company.youse.controller.yousepay.body.GetOrgShortCodesBody;
import com.company.youse.controller.yousepay.body.GetSysAccountBody;
import com.company.youse.controller.yousepay.body.OrgShortCodeBody;
import com.company.youse.controller.yousepay.body.SysAccountBody;
import com.company.youse.platform.contract.Response;
import com.company.youse.platform.result.ResponseConverter;
import com.company.youse.services.command.sysaccount.CreateOrgShortCodeCommand;
import com.company.youse.services.command.sysaccount.CreateOrgShortCodeService;
import com.company.youse.services.command.sysaccount.CreateSysAccountCommand;
import com.company.youse.services.command.sysaccount.CreateSysAccountService;
import com.company.youse.services.query.sysaccount.GetOrgShortCodesByMemberIdService;
import com.company.youse.services.query.sysaccount.GetShortCodesByMemberIdQuery;
import com.company.youse.services.query.sysaccount.GetSysAccountByMemberIdQuery;
import com.company.youse.services.query.sysaccount.GetSysAccountByMemberIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/org")
public class OrgController {

    private final GetSysAccountByMemberIdService getSysAccountByMemberIdService;
    private final GetOrgShortCodesByMemberIdService getOrgShortCodesByMemberIdService;
    private final CreateSysAccountService createSysAccountService;
    private final CreateOrgShortCodeService createOrgShortCodeService;

    @Autowired
    public OrgController(GetSysAccountByMemberIdService getSysAccountByMemberIdService
            , GetOrgShortCodesByMemberIdService getOrgShortCodesByMemberIdService
            , CreateSysAccountService createSysAccountService, CreateOrgShortCodeService createOrgShortCodeService) {
        this.getSysAccountByMemberIdService = getSysAccountByMemberIdService;
        this.getOrgShortCodesByMemberIdService = getOrgShortCodesByMemberIdService;
        this.createSysAccountService = createSysAccountService;
        this.createOrgShortCodeService = createOrgShortCodeService;
    }

    @PostMapping
    public ResponseEntity<Response> createOrg( @RequestBody SysAccountBody sysAccountBody){
        var command = new CreateSysAccountCommand(sysAccountBody);
        return new ResponseConverter().convert(createSysAccountService.decorate(command));
    }

    @PostMapping("/get")
    public ResponseEntity<Response> getOrg(@RequestBody GetSysAccountBody getSysAccountBody){
        var command = new GetSysAccountByMemberIdQuery(getSysAccountBody);
        return new ResponseConverter().convert(getSysAccountByMemberIdService.decorate(command));
    }

    @PostMapping("/short-code")
    public ResponseEntity<Response> createOrgShortCode(@RequestBody OrgShortCodeBody orgShortCodeBody){
        var command = new CreateOrgShortCodeCommand(orgShortCodeBody);
        return new ResponseConverter().convert(createOrgShortCodeService.decorate(command));
    }

    @PostMapping("/short-code/get")
    public ResponseEntity<Response> getOrgShortCodes(@RequestBody GetOrgShortCodesBody getOrgShortCodesBody){
        var command = new GetShortCodesByMemberIdQuery(getOrgShortCodesBody);
        return new ResponseConverter().convert(getOrgShortCodesByMemberIdService.decorate(command));
    }
}
