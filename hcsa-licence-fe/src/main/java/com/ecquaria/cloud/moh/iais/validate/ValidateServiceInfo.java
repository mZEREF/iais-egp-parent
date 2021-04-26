package com.ecquaria.cloud.moh.iais.validate;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcCgoDto;

import java.util.List;
import java.util.Map;

/**
 * @author Wenkang
 * @date 2021/4/25 17:02
 */
public interface ValidateServiceInfo {
    default void doValidteCGO(Map<String,String> map, AppSvcCgoDto appSvcCgoDto,Integer index, List<String> stringList,StringBuilder stringBuilder,boolean newErr0006){};
}
