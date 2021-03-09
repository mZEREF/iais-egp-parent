package com.ecquaria.cloud.moh.iais.method;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Wenkang
 * @date 2021/3/9 16:06
 */
public interface RfiCanCheck {
   default List<AppEditSelectDto> getAppEditSelectDtoSForRfi( String appNo){return new ArrayList<>();};
}
