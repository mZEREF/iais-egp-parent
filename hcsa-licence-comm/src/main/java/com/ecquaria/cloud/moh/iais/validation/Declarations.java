package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppDeclarationMessageDto;

import java.util.Map;

/**
 * @author Wenkang
 * @date 2021/5/15 9:42
 */
public interface Declarations {

    void validateDeclarations(Map<String, String> map, AppDeclarationMessageDto appDeclarationMessageDto);

}
