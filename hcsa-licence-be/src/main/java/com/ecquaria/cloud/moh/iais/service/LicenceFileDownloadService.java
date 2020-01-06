package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.ProcessFileTrackDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import java.util.List;

/**
 * @author Wenkang
 * @date 2019/11/9 16:08
 */
public interface LicenceFileDownloadService {
    void delete();
    Boolean  download(ProcessFileTrackDto processFileTrackDto,List<ApplicationDto> listApplicationDto,List<ApplicationDto> requestForInfList,String fileName);
    void compress(List<ApplicationDto> list,List<ApplicationDto> requestForInfList);
    List<ApplicationDto> listApplication();
    void requestForInfList(List<ApplicationDto> list);
    Boolean changeFeApplicationStatus();
    public List<TaskDto> getTasksByRefNo(String refNo);
}
