package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.withdrawn.WithdrawnDto;

public interface WithdrawalService {
    void saveWithdrawn(WithdrawnDto withdrawnDto);
}
