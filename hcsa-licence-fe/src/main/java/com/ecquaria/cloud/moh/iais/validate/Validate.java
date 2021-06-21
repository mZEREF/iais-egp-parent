package com.ecquaria.cloud.moh.iais.validate;

import java.util.Map;

/**
 * @author Wenkang
 * @date 2021/4/26 13:56
 */
public interface Validate {
    Map<String,String> validate(Object o,Integer index);
}
