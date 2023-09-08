package com.company.youse.platform.contract;

import com.company.youse.platform.result.Result;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
public class BodyResponse<R>  implements Response{

    @ToString.Exclude
    private R data;

    private List<ResponseMessage> messages;

    public BodyResponse(R data, Result base) {
        super();
        this.data = data;
        this.messages = base.toMessages();
    }
}
