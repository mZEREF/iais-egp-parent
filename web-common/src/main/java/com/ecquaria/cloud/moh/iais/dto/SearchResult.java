/*
 * This file is generated by ECQ project skeleton automatically.
 *
 *   Copyright 2019-2049, Ecquaria Technologies Pte Ltd. All rights reserved.
 *
 *   No part of this material may be copied, reproduced, transmitted,
 *   stored in a retrieval system, reverse engineered, decompiled,
 *   disassembled, localised, ported, adapted, varied, modified, reused,
 *   customised or translated into any language in any form or by any means,
 *   electronic, mechanical, photocopying, recording or otherwise,
 *   without the prior written permission of Ecquaria Technologies Pte Ltd.
 */

package com.ecquaria.cloud.moh.iais.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * SearchResult
 *
 * @author Jinhua
 * @date 2019/7/22 17:51
 */
public class SearchResult<E> implements Serializable {
    @Getter @Setter private List<E> rows;
    @Getter @Setter private int rowCount;   // total number of records

    public SearchResult(){
        rows = new ArrayList<E>();
    }

    public SearchResult(List<E> rows, int rowCount) {
        this.rows = rows;
        this.rowCount = rowCount;
    }

    public int getPageCount(int pageSize) {
        int pageCount = 0;
        if (pageSize > 0) { // calculate pageCount
            pageCount = (int) Math.ceil(((double) rowCount / pageSize));
        }
        return pageCount;
    }

    public void remove(E e){
        rows.remove(e);
        rowCount--;
    }
}
