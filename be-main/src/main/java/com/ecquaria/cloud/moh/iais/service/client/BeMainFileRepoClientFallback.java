package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;

/**
 * BeMainFileRepoClientFallback
 *
 * @author Jinhua
 * @date 2020/9/23 15:34
 */
public class BeMainFileRepoClientFallback {
    public FeignResponseEntity<Void> fetchFileContent() {
        return IaisEGPHelper.getFeignResponseEntity("fetchFileContent");
    }
}
