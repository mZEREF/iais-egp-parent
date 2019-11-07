/*
package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.common.dto.postcode.PostCodeDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.mockpolicies.Slf4jMockPolicy;
import org.powermock.core.classloader.annotations.MockPolicy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

*/
/*
 *author: yichen
 *date time:9/23/2019 9:58 AM
 *description:
 *//*

@RunWith(PowerMockRunner.class)
@MockPolicy(Slf4jMockPolicy.class)
@PrepareForTest({ExcelWriter.class})
public class ExcelWriterTest {

    private List<PostCodeDto> list = new ArrayList<>();
    @Before
    public void start(){
        PostCodeDto p1 = new PostCodeDto();
        p1.setId(1);
        p1.setRowguid("adsada");
        p1.setBuildingName("dsadas");
        p1.setStreetName("dsadas");


        PostCodeDto p2 = new PostCodeDto();
        p2.setId(2);
        p2.setRowguid("adsada");
        p2.setBuildingName("dsadas");
        p2.setStreetName("dsadas");

        PostCodeDto p3 = new PostCodeDto();
        p3.setId(3);
        p3.setRowguid("adsada");
        p3.setBuildingName("dsadas");
        p3.setStreetName("dsadas");

        list.add(p1);
        list.add(p3);
        list.add(p2);
        Assert.assertTrue(true);
    }


    @Test
    public void testExportXlsToSuccess(){
        ExcelWriter excelWriter = new ExcelWriter("test", null);
        excelWriter.exportXls(list);
        Assert.assertTrue(true);
    }


    @Test(expected = IaisRuntimeException.class)
    public void testExportXlsToError(){
        ExcelWriter excelWriter = new ExcelWriter("test", null);
        excelWriter.exportXls(null);
        Assert.assertTrue(true);
    }

    @Test
    public void testGetterSetter(){
        ExcelWriter excelWriter = new ExcelWriter("test", null);
        excelWriter.setFileName("aa");
        excelWriter.setExportPath("/dsa");
        excelWriter.setSheetName("test");
        excelWriter.exportXls(list);


        excelWriter.getExportPath();
        excelWriter.getFileName();
        excelWriter.getSheetName();
        Assert.assertTrue(true);
    }
}
*/
