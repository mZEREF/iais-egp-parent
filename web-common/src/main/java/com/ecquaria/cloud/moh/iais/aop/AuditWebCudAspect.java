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

package com.ecquaria.cloud.moh.iais.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import sg.gov.moh.iais.web.logging.aop.AuditCudAspect;

/**
 * AuditWebCudAspect
 *
 * @author Jinhua
 * @date 2019/7/26 18:10
 */
@Aspect
@Component
@Slf4j
public class AuditWebCudAspect {
    @Pointcut("execution(public * sg.gov.moh.iais..dao.*.save*(..)) " +
            "|| execution(public * sg.gov.moh.iais..dao.*.delete*(..)) " +
            "|| execution(public * com.ecquaria.cloud.moh.iais..dao.*.delete*(..)) " +
            "|| execution(public * com.ecquaria.cloud.moh.iais..dao.*.save*(..))")
    public void daoTrail() {
        //No need to implement
    }

    @Around(value = "daoTrail()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        AuditCudAspect asp = new AuditCudAspect();
        return asp.doAround(joinPoint);
    }
}
