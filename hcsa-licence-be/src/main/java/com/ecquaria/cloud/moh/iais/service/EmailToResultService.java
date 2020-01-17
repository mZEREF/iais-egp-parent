package com.ecquaria.cloud.moh.iais.service;

import freemarker.template.TemplateException;

import java.io.IOException;

/**
 * @author weilu
 * @date 2020/1/16 19:39
 */
public interface EmailToResultService {
    void sendRenewResultEmail() throws IOException, TemplateException;

    void sendEfcResultEmail() throws IOException, TemplateException;
}
