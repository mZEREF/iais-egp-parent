package com.ecquaria.cloud.moh.iais.dto.memorypage;

import java.io.Serializable;
import java.security.SecureRandom;
import lombok.Getter;
import lombok.Setter;

/**
 * PageRecords
 *
 * @author Jinhua
 * @date 2020/7/10 9:08
 */
public class PageRecords<T extends Serializable> implements Serializable {
    private static final long serialVersionUID = -8977843999854545095L;

    @Getter @Setter
    private boolean checked;
    @Getter @Setter
    private long id;
    @Getter
    private T record;

    PageRecords(T obj) {
        SecureRandom number = new SecureRandom();
        id = number.nextLong();
        this.record = obj;
    }
}
