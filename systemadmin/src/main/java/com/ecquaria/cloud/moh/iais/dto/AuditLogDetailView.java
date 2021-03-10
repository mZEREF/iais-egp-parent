package com.ecquaria.cloud.moh.iais.dto;

import java.io.Serializable;
import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

/**
 * AuditLogDetailView
 *
 * @author Jinhua
 * @date 2021/3/9 12:53
 */
@Getter
@Setter
public class AuditLogDetailView implements Serializable {
    private static final long serialVersionUID = -3363570425650129550L;

    private ArrayList<AuditLogRecView> searchParam;
    private ArrayList<AuditLogRecView> beforeChange;
    private ArrayList<AuditLogRecView> afterChange;
    private ArrayList<AuditLogRecView> errorMsg;
}
