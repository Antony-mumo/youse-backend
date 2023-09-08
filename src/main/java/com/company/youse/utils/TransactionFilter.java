package com.company.youse.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * @author Austin Oyugi
 * @since  17/1/2022
 * @email austinoyugi@gmail.com
 */

@Getter
@Setter
@NoArgsConstructor
public class TransactionFilter {

    private int start;
    private int length;
    private int draw;
    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;
    private Map<String, ?> reqParam;
    private boolean isSearch;

    public TransactionFilter(Map<String, ?> reqMap){
        this.reqParam = reqMap;

        Object isSearch = reqParam.get("search[value]");

        if (!Objects.isNull(isSearch) && String.valueOf(isSearch).length() > 0){
            this.setSearch(true);
        }

        setTransactionFilter();

        start = (Objects.isNull(reqMap.get("start"))) ? 0 : Integer.parseInt(String.valueOf(reqMap.get("start")));
        length = (Objects.isNull(reqMap.get("length"))) ? 0 : (Integer.parseInt(String.valueOf(reqMap.get("length"))));
        draw = (Objects.isNull(reqMap.get("draw"))) ? 0 :  Integer.parseInt(String.valueOf(reqMap.get("draw")));
    }

    private void setTransactionFilter(){

        if (reqParam.get("start") != null && reqParam.get("length") != null){
            this.setStart((Integer.parseInt(String.valueOf(reqParam.get("start")))));
            this.setLength(Integer.parseInt(String.valueOf(reqParam.get("length"))));
            this.setDraw(Integer.parseInt(String.valueOf(reqParam.get("draw"))));
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        Date from;
        Date to;

        try{

            Calendar calendar = Calendar.getInstance();

            String dateFromString = String.valueOf(reqParam.get("dateFrom"));
            String dateToString = String.valueOf(reqParam.get("dateTo"));

            if (!Objects.isNull(dateFromString) || !Objects.isNull(dateToString)){
                if (dateFromString.equals("null")) dateFromString = null;
                if (dateToString.equals("null")) dateToString = null;
            }

            Date dateFrom = Objects.isNull(dateFromString) ? new Date() : simpleDateFormat.parse(dateToString);
            Date dateTo = Objects.isNull(dateToString) ? new Date() : simpleDateFormat.parse(dateToString);

            calendar.setTime(dateFrom);
            calendar.add(Calendar.DATE, -1);
            from = calendar.getTime();
            this.setDateFrom(convertToLocalDateTimeViaInstant(from));

            calendar.setTime(dateTo);
            calendar.add(Calendar.DATE, 1);
            to = calendar.getTime();
            this.setDateTo(convertToLocalDateTimeViaInstant(to));


        }catch (Exception exception){
            exception.printStackTrace();
        }
    }

    private LocalDateTime convertToLocalDateTimeViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    private Date convertToDateViaInstant(LocalDateTime dateToConvert) {
        return Date
                .from(dateToConvert.atZone(ZoneId.systemDefault())
                        .toInstant());
    }
}
