package com.ecquaria.cloud.moh.iais.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * AuditLogDetailView
 *
 * @author Jinhua
 * @date 2021/3/9 12:53
 */
@Getter
@Setter
public class AuditLogDetailView implements Serializable {
    private static final long serialVersionUID = 1L;

    private ArrayList<AuditLogRecView> searchParam;
    private ArrayList<AuditLogRecView> beforeChange;
    private ArrayList<AuditLogRecView> response;
    private ArrayList<AuditLogRecView> request;
    private ArrayList<AuditLogRecView> afterChange;
    private ArrayList<AuditLogRecView> errorMsg;
}
