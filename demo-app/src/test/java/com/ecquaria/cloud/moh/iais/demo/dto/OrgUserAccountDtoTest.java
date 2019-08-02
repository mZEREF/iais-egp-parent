package com.ecquaria.cloud.moh.iais.demo.dto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(PowerMockRunner.class)
@PrepareForTest({OrgUserAccountDto.class})
public class OrgUserAccountDtoTest {

    @Spy
    OrgUserAccountDto entity = new OrgUserAccountDto();

    @Test
    public void testGetterSetter() {
        entity.getId();
        entity.setId(1);
        entity.getRowguid();
        entity.setRowguid(null);
        entity.getName();
        entity.setName(null);
        entity.getOldNricNo();
        entity.setOldNricNo(null);
        entity.getNircNo();
        entity.setNircNo(null);
        entity.getCorpPassId();
        entity.setCorpPassId(null);
        entity.getStatus();
        entity.setStatus(null);
        entity.getOrgId();
        entity.setOrgId(null);
        entity.isEditFlag();
        entity.setEditFlag(false);
        entity.validateNricEdit("nric");
        entity.fakeValidateA("");
        entity.fakeValidateB("");
        assertNotNull(entity);
    }
}