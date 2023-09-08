package com.company.youse.controller.yousepay;

import com.company.youse.controller.yousepay.body.C2BTransactionBody;
import com.company.youse.controller.yousepay.body.RegisterUrlBody;
import com.company.youse.controller.yousepay.body.STKPushRequestBody;
import com.company.youse.controller.yousepay.body.STKPushResultBody;
import com.company.youse.platform.api.Controller;
import com.company.youse.platform.contract.Response;
import com.company.youse.platform.result.ResponseConverter;
import com.company.youse.services.command.yousepay.ProcessSTKPushResultService;
import com.company.youse.services.command.yousepay.STKPushResultCommand;
import com.company.youse.services.command.yousepay.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/payments/c2b")
public class C2BTransactionsController implements Controller {

    private final RegisterC2BUrlService registerC2BUrlService;

    private final ProcessC2BReceiptReceivalFlowService processC2BReceiptReceivalFlowService;
    private final InitiateSTKPushRequestService initiateSTKPushRequestService;
    private final ProcessSTKPushResultService processSTKPushResultService;

    public C2BTransactionsController(RegisterC2BUrlService registerC2BUrlService
            , ProcessC2BReceiptReceivalFlowService processC2BReceiptReceivalFlowService
            , InitiateSTKPushRequestService initiateSTKPushRequestService
            , ProcessSTKPushResultService processSTKPushResultService) {
        this.registerC2BUrlService = registerC2BUrlService;
        this.processC2BReceiptReceivalFlowService = processC2BReceiptReceivalFlowService;
        this.initiateSTKPushRequestService = initiateSTKPushRequestService;
        this.processSTKPushResultService = processSTKPushResultService;
    }

    @PostMapping("/register-url")
    public ResponseEntity<Response> registerUrl(@RequestBody RegisterUrlBody registerUrlBody){
        return new ResponseConverter().convert(registerC2BUrlService
                .decorate(new RegisterUrlCommand(registerUrlBody)));
    }

    @PostMapping
    public ResponseEntity<Response> receiveC2B(@RequestHeader Map<String, String> headers
            , @RequestBody C2BTransactionBody c2BTransactionBody){
        var command = new C2BReceiptCommand(c2BTransactionBody);
        return new ResponseConverter().convert(processC2BReceiptReceivalFlowService.decorate(command));
    }

    @PostMapping("stk-push/process")
    public ResponseEntity<Response> initiateSTKPush(@RequestBody STKPushRequestBody stkPushRequestBody){
        var command = new STKPushRequestCommand(stkPushRequestBody);
        return new ResponseConverter().convert(initiateSTKPushRequestService.decorate(command));
    }

    @PostMapping("stk-push/result")
    public ResponseEntity<Response> receiveSTKResult(@RequestBody STKPushResultBody stkPushResult ){
        var command = new STKPushResultCommand(stkPushResult);
        return new ResponseConverter().convert(processSTKPushResultService.decorate(command));
    }
}
