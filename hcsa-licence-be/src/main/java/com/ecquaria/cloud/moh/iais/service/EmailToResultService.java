package com.ecquaria.cloud.moh.iais.service;

import freemarker.template.TemplateException;

import java.io.IOException;

/**
 * @author weilu
 * @date 2020/1/16 19:39
 */
public interface EmailToResultService {
    void sendRenewResultEmail() throws Exception;

    void sendEfcResultEmail() throws Exception;

    void sendCessationFurtherDateEmail() throws Exception;

    void sendCessationPresentDateEmail() throws Exception;

    void sendCessationEffectiveDateEmail() throws Exception;

    void sendCessationLicenceEndDateEmail() throws Exception;



}
