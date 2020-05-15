package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.withdrawn.WithdrawnDto;

import java.util.List;

public interface WithdrawalService {
    void saveWithdrawn(List<WithdrawnDto> withdrawnDto);
}
