package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;

/**
 * FeMainFileRepoClientFallback
 *
 * @author Jinhua
 * @date 2020/9/23 15:40
 */
public class FeMainFileRepoClientFallback {
    public FeignResponseEntity<Void> fetchFileContent() {
        return IaisEGPHelper.getFeignResponseEntity("fetchFileContent");
    }
}
