package sg.gov.moh.iais.egp.bsb.client;

import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;

//  fallback for feign has not be set now, will add in future
public class CommonFallback {
    protected <T> ResponseDto<T> unexpectedError() {
        ResponseDto<T> r = new ResponseDto<>();
        r.setStatus("ERROR");
        r.setErrorCode("UNEXPECTED");
        r.setErrorDesc("Unexpected error happened");
        r.setEntity(null);
        return r;
    }
}
