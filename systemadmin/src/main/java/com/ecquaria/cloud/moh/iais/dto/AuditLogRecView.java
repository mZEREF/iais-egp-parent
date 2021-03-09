package com.ecquaria.cloud.moh.iais.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * AuditLogRecView
 *
 * @author Jinhua
 * @date 2021/3/9 12:58
 */
@Getter
@Setter
public class AuditLogRecView implements Serializable {
    private static final long serialVersionUID = -3804364206680566816L;

    private String colName;
    private String colDetail;
    private String longText;
}
