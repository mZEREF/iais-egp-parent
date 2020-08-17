package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeView;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;

/**
 * MasterCodeClientFallback
 *
 * @author Jinhua
 * @date 2019/11/25 21:42
 */
public class MasterCodeClientFallback implements MasterCodeClient{

    @Override
    public FeignResponseEntity<SearchResult<MasterCodeView>> retrieveMasterCodes(SearchParam param) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<String> messageID() {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}
