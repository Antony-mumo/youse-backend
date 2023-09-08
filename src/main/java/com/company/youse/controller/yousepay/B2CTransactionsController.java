package com.company.youse.controller.yousepay;

import com.company.youse.controller.yousepay.body.B2CRequestBody;
import com.company.youse.controller.yousepay.body.B2CResultBody;
import com.company.youse.platform.api.Controller;
import com.company.youse.platform.contract.Response;
import com.company.youse.platform.result.ResponseConverter;
import com.company.youse.services.command.yousepay.B2CRequestCommand;
import com.company.youse.services.command.yousepay.B2CResultCommand;
import com.company.youse.services.command.yousepay.ProcessB2CRequestFlowService;
import com.company.youse.services.command.yousepay.ProcessB2CTransactionFlowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/b2c")
public class B2CTransactionsController implements Controller {

    private final ProcessB2CRequestFlowService b2CRequestService;
    private final ProcessB2CTransactionFlowService b2CTransactionService;

    public B2CTransactionsController(ProcessB2CRequestFlowService b2CRequestService
            , ProcessB2CTransactionFlowService b2CTransactionService) {
        this.b2CRequestService = b2CRequestService;
        this.b2CTransactionService = b2CTransactionService;
    }

    @PostMapping
    public ResponseEntity<Response> receiveRequest(@RequestBody B2CRequestBody b2CRequestBody){
        var command = new B2CRequestCommand(b2CRequestBody);
        return new ResponseConverter().convert(b2CRequestService.decorate(command));
    }

    @PostMapping("result")
    public ResponseEntity<Response> receiveResponse(@RequestBody B2CResultBody b2CResultBody){
        var command = new B2CResultCommand(b2CResultBody);
        return new ResponseConverter().convert(b2CTransactionService.decorate(command));
    }
}
