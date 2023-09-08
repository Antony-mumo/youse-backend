package com.company.youse.platform.result;

import com.company.youse.platform.api.rest.NoContent;
import com.company.youse.platform.contract.Response;
import org.springframework.http.ResponseEntity;

public class ResponseConverter {

    public <G> ResponseEntity<Response> convert(CommandResult<G> result) {

        if (result == null) {
            return new NoContent().getResponse(null, null);
        }
        return result.getStrategy().getResponse(result.getId(), result.getBase());
    }

    public <R> ResponseEntity<Response> convert(QueryResult<R> result) {

        if (result == null) {
            return new NoContent().getResponse(null, null);
        }

        return result.getStrategy().getResponse(result.getData(), result.getBase());
    }
}
