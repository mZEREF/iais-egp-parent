package com.ecquaria.cloud.moh.iais;

import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import org.junit.Test;

/**
 * @Description Test
 * @Auther chenlei on 12/21/2021.
 */
public class BaseTest {

    @Test
    public static void testIdNo(String[] args) {
        System.out.println(IaisEGPHelper.checkIdentityNoType("G6212222L"));
    }
}
