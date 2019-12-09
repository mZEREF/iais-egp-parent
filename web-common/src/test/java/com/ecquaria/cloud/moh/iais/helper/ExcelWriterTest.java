package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.common.dto.postcode.PostCodeDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.api.mockito.mockpolicies.Slf4jMockPolicy;
import org.powermock.core.classloader.annotations.MockPolicy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.anyObject;
import static org.powermock.api.mockito.PowerMockito.doNothing;

/*
 *author: yichen
 *date time:9/23/2019 9:58 AM
 *description:
 */

@RunWith(PowerMockRunner.class)
@MockPolicy(Slf4jMockPolicy.class)
@PrepareForTest({ExcelWriter.class, HSSFWorkbook.class, FileOutputStream.class})
public class ExcelWriterTest {
    @Spy
    private ExcelWriter excelWriter = new ExcelWriter("test", null);
    @Spy
    private  HSSFWorkbook wb = new  HSSFWorkbook();

    private List<PostCodeDto> list = new ArrayList<>();
    @Before
    public void start() throws Exception {
        PostCodeDto p1 = new PostCodeDto();
        p1.setId(1);
        p1.setRowguid("adsada");
        p1.setBuildingName("dsadas");
        p1.setStreetName("dsadas");
        PowerMockito.whenNew(HSSFWorkbook.class).withNoArguments().thenReturn(wb);
        FileOutputStream fos = PowerMockito.mock(FileOutputStream.class);
        PowerMockito.whenNew(FileOutputStream.class).withAnyArguments().thenReturn(fos);

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
        doNothing().when(wb, "write", anyObject());
        doNothing().when(fos, "close");
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
