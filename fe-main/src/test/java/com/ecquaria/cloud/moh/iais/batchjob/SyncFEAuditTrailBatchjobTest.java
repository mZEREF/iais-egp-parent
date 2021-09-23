package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.moh.iais.common.dto.audit.AuditTrailEntityDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.service.client.AuditTrailMainClient;
import com.ecquaria.cloud.moh.iais.service.impl.SyncAuditTrailRecordsServiceImpl;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.api.mockito.mockpolicies.Slf4jMockPolicy;
import org.powermock.core.classloader.annotations.MockPolicy;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import sop.webflow.rt.api.BaseProcessClass;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * SyncFEAuditTrailBatchjobTest
 *
 * @author Jinhua
 * @date 2020/5/12 15:07
 */
@RunWith(PowerMockRunner.class)
@MockPolicy(Slf4jMockPolicy.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({SyncFEAuditTrailBatchjob.class, SyncAuditTrailRecordsServiceImpl.class, BaseProcessClass.class,
        AuditTrailMainClient.class, FeignResponseEntity.class, MiscUtil.class, FileOutputStream.class,
        FileOutputStream.class, CheckedOutputStream.class, ZipOutputStream.class})
public class SyncFEAuditTrailBatchjobTest {
    @Spy
    private SyncFEAuditTrailBatchjob job = new SyncFEAuditTrailBatchjob();
    @Spy
    private SyncAuditTrailRecordsServiceImpl service = new SyncAuditTrailRecordsServiceImpl();
    @Mock
    private BaseProcessClass bpc;
    @Mock
    private AuditTrailMainClient auditTrailMainClient;
    @Mock
    private FeignResponseEntity feignResponseEntity;
    @Mock
    private File file;
    @Mock
    private FileOutputStream fileOutputStream;
    @Mock
    private CheckedOutputStream checkedOutputStream;
    @Mock
    private ZipOutputStream zipOutputStream;
    @Mock
    private BufferedInputStream bufferedInputStream;
    @Mock
    private ZipEntry zipEntry;

    private List<AuditTrailEntityDto> auditList;

    //@Before
    public void setup() {
        Whitebox.setInternalState(job, "syncAuditTrailRecordsService", service);
        auditList = IaisCommonUtils.genNewArrayList();
        AuditTrailEntityDto dto = new AuditTrailEntityDto();
        auditList.add(dto);
        Whitebox.setInternalState(service, "sharedPath", "sharedPath");
        Whitebox.setInternalState(service, "auditTrailClient", auditTrailMainClient);
        Whitebox.setInternalState(job, "auditTrailMainClient", auditTrailMainClient);
    }

    //@Test
    public void testStart() {
        job.start(bpc);
        assertNotNull(bpc);
    }

    //@Test
    public void testPreDate() throws Exception {
        when(auditTrailMainClient.getAuditTrailsByMigrated1()).thenReturn(feignResponseEntity);
        when(feignResponseEntity.getEntity()).thenReturn(auditList);
        PowerMockito.mockStatic(MiscUtil.class);
        when(MiscUtil.generateFile(anyString(), anyString())).thenReturn(file);
        doNothing().when(MiscUtil.class, "checkDirs", file);
        PowerMockito.whenNew(FileOutputStream.class).withArguments(file).thenReturn(fileOutputStream);
        PowerMockito.whenNew(FileOutputStream.class).withArguments(anyString()).thenReturn(fileOutputStream);
        PowerMockito.whenNew(File.class).withArguments(anyString()).thenReturn(file);
        PowerMockito.whenNew(CheckedOutputStream.class).withArguments(fileOutputStream, new CRC32()).thenReturn(checkedOutputStream);
        PowerMockito.whenNew(ZipOutputStream.class).withArguments(checkedOutputStream).thenReturn(zipOutputStream);
        PowerMockito.whenNew(BufferedInputStream.class).withArguments(anyObject()).thenReturn(bufferedInputStream);
        PowerMockito.whenNew(ZipEntry.class).withArguments("aaaaa/").thenReturn(zipEntry);
        doNothing().when(fileOutputStream).write(anyObject());
        doNothing().when(zipOutputStream).putNextEntry(zipEntry);
        when(file.isDirectory()).thenReturn(false);
        when(file.getPath()).thenReturn("aaaaa/userRecFile");
        when(bufferedInputStream.read()).thenReturn(-1);
        File content = PowerMockito.mock(File.class);
        File[] files = new File[] {content};
        when(file.listFiles((FilenameFilter) anyObject())).thenReturn(files);
        when(content.listFiles()).thenReturn(null);
        when(content.getName()).thenReturn(".text");
        doNothing().when(zipOutputStream).closeEntry();
        when(auditTrailMainClient.syucUpdateAuditTrail(auditList)).thenReturn(null);
        job.preDate(bpc);
        assertNotNull(bpc);
    }
}
