package com.ecquaria.cloud.moh.iais.service;

import java.io.File;
import java.util.List;

public interface DataSubmissionElisInterfaceService {
    void processLicence(List<File> sortList);
    void processUsers(List<File> sortList);
    void processDoctor(List<File> sortList);
}
