package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.WithdrawApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.withdrawn.WithdrawnDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface WithdrawalService {
    List<WithdrawnDto> saveWithdrawn(List<WithdrawnDto> withdrawnDto, HttpServletRequest httpRequest);
    List<WithdrawnDto> saveRfiWithdrawn(List<WithdrawnDto> withdrawnDtoList,HttpServletRequest httpRequest);
    WithdrawnDto getWithdrawAppInfo(String appNo);
    List<WithdrawApplicationDto> getCanWithdrawAppList(List<String[]> appTandS,String licenseeId);
}
