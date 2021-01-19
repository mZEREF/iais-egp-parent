package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;

/**
 * MsgTemplateService
 *
 * @author suocheng
 * @date 3/12/2020
 */

public interface MsgTemplateService {
   public MsgTemplateDto getMessageById(String id);
}
