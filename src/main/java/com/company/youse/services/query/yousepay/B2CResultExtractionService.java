package com.company.youse.services.query.yousepay;


import com.company.youse.platform.decorator.query.QueryBaseService;
import com.company.youse.platform.result.QueryResult;
import com.company.youse.pojo.B2CRequestResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class B2CResultExtractionService extends QueryBaseService<B2CResultExtractionQuery, B2CRequestResult> {

    @Override
    @SuppressWarnings("unchecked")
    public QueryResult<B2CRequestResult> execute(B2CResultExtractionQuery query) {

        B2CRequestResult b2CTransaction = new B2CRequestResult();

        Map<String, Object> b2cResult = query.getB2CResultCommand().getResult();

        String resultCode = String.valueOf(b2cResult.get("ResultCode"));
        String resultType = String.valueOf(b2cResult.get("ResultType"));
        String resultDesc = String.valueOf(b2cResult.get("ResultDesc"));

        String originatorConversationID = String.valueOf(b2cResult.get("OriginatorConversationID"));
        String conversationID = String.valueOf(b2cResult.get("ConversationID"));
        String transactionID = String.valueOf(b2cResult.get("TransactionID"));

        b2CTransaction.setOriginatorConversationID(originatorConversationID);
        b2CTransaction.setConversationID(conversationID);
        b2CTransaction.setTransactionID(transactionID);

        b2CTransaction.setResultCode(resultCode);
        b2CTransaction.setResultType(resultType);
        b2CTransaction.setResultDescription(resultDesc);

        if(Optional.ofNullable(b2cResult.get("ResultParameters")).isPresent()){
            Map<String, Object> resultParams = (Map<String, Object>) b2cResult.get("ResultParameters");
            List<Map<String, Object>> resultParamValue = (List<Map<String, Object>>) resultParams.get("ResultParameter");

            for (Map<String, Object> resultParamVal : resultParamValue) {

                String key = String.valueOf(resultParamVal.get("Key"));
                String value = String.valueOf(resultParamVal.get("Value"));

                switch (key) {
                    case "TransactionAmount":
                    case "Amount":
                        b2CTransaction.setTransactionAmount(new BigDecimal(value));
                        break;

                    case "TransactionReceipt":
                    case "ReceiptNo":
                        b2CTransaction.setTransactionReceipt(value);
                        break;

                    case "B2CRecipientIsRegisteredCustomer":
                        b2CTransaction.setB2CRecipientIsRegisteredCustomer(value);
                        break;

                    case "B2CChargesPaidAccountAvailableFunds":
                        b2CTransaction.setB2CChargesPaidAccountAvailableFunds(new BigDecimal(value));
                        break;

                    case "ReceiverPartyPublicName":
                        b2CTransaction.setReceiverPartyPublicName(value);
                        break;

                    case "TransactionCompletedDateTime":
                    case "FinalisedTime":
                        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

                        try {
                            Date date;

                            try{
                                date = dateFormatter.parse(value);
                            }catch (ParseException e){
                                date = new Date(Long.parseLong(value));
                            }

                            b2CTransaction.setTransactionCompletedDateTime(dateFormatter.parse(
                                    dateFormatter.format(date)));

                        } catch (Exception e) {
                            log.info("Failed to parse date string, setting current date for transaction " + b2CTransaction);

                            try {
                                b2CTransaction.setTransactionCompletedDateTime(dateFormatter.parse(dateFormatter
                                        .format(new Date())));
                            } catch (ParseException parseException) {
                                parseException.printStackTrace();
                            }
                        }
                        break;

                    case "B2CUtilityAccountAvailableFunds":
                        b2CTransaction.setB2CUtilityAccountAvailableFunds(new BigDecimal(value));
                        break;

                    case "B2CWorkingAccountAvailableFunds":
                        b2CTransaction.setB2CWorkingAccountAvailableFunds(new BigDecimal(value));
                        break;

                    case "TransactionStatus":
                        b2CTransaction.setTransactionStatus(value);
                        break;

                    case "ReasonType":
                        b2CTransaction.setReasonType(value);
                        break;

                    case "DebitPartyName":
                        b2CTransaction.setDebitPartyName(value);
                        break;

                    case "CreditPartyName":
                        b2CTransaction.setCreditPartyName(value);
                        break;

                }
            }
        }

        return new QueryResult.Builder<B2CRequestResult>().data(b2CTransaction).ok().build();
    }
}

