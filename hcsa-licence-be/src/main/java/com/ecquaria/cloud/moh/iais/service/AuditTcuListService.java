package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.inspection.AuditTaskDataFillterDto;
import java.util.List;

/**
 * @Author: jiahao
 * @Date: 2020/2/20 15:09
 */
public interface AuditTcuListService {
    List<AuditTaskDataFillterDto> getAuditTcuList();
}
