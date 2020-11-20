package com.ecquaria.cloud.moh.iais.dto;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceStepSchemeDto;
import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * ServiceStepDto
 *
 * @author suocheng
 * @date 12/21/2019
 */

@Getter
@Setter
public class ServiceStepDto implements Serializable {
    private static final long serialVersionUID = 2875642266190696635L;
    private List<HcsaServiceStepSchemeDto> hcsaServiceStepSchemeDtos;
    private HcsaServiceStepSchemeDto previousStep;
    private HcsaServiceStepSchemeDto currentStep;
    private HcsaServiceStepSchemeDto nextStep;
    private int currentNumber;
    private int serviceNumber;

    private boolean serviceFirst;
    private boolean serviceEnd;
    private boolean stepFirst;
    private boolean stepEnd;


}
