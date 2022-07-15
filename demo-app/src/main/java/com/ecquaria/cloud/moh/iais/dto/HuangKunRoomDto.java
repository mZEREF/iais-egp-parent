package com.ecquaria.cloud.moh.iais.dto;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @ClassName: HuangKunRoomDto
 * @author: haungkun
 * @date: 2022/7/14 16:28
 */
@Data
@Entity
public class HuangKunRoomDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    private String roomType;

    private String roomNO;

}
