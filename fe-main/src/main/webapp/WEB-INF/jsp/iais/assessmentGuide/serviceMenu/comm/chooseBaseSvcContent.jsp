<div class="row">
    <div class="col-xs-12 col-md-3">
    </div>
    <div class="col-xs-12 col-md-7">
        <c:if test="${!empty chooseBaseErr}">
            <span class="error-msg">${chooseBaseErr}</span>
        </c:if>
    </div>
</div>

<div class="row">
    <div class="col-xs-12 col-md-7">
        <div class="row">
            <div class="col-xs12 col-md-12" style="font-size: 16px;">
                Below are your existing base service licences. Select an existing base service prescribed as underlying to the special licensable service if applicable. Else, please add a new base service accordingly.
            </div>
        </div>
    </div>
</div>
<div class="row">
    <div class="col-xs-12 col-md-7">
        <div class="self-assessment-checkbox-gp gradient-light-grey">
            <c:if test="${reloadSvcInfo.size()>0}">
                <c:set var="reloadSvcInfo" value="${appSvcRelatedInfoList.get(0)}"/>
                <c:set var="reloadIndexNo" value="${reloadSvcInfo.serviceCode}${reloadSvcInfo.baseServiceName}${reloadSvcInfo.hciCode}"/>
            </c:if>


            <c:forEach var="specSvc" items="${appSelectSvc.speSvcDtoList}" varStatus="status">
                <c:set var="speSvcCode" value="${specSvc.svcCode}"/>
                <c:set var="corr" value="${baseAndSpcSvcMap.get(speSvcCode)}"/>
                <c:set var="baseSvcSel" value="${reloadBaseSvcSelected.get(speSvcCode)}"/>
                <c:set var="selIndexNo" value="${specSvc.svcCode}${baseSvcSel.serviceCode}${baseSvcSel.licPremisesId}"/>



                <c:set var="newLic" value="false"/>
                <c:if test="${''==baseSvcSel.licPremisesId}">
                    <c:set var="newLic" value="true"/>
                </c:if>
                <div class="speSvcContent remark-point">
                    <p class="assessment-title">Base Services for ${specSvc.svcName}</p>
                    <c:if test="${noExistBaseLic}">
                        <div class="row">
                            <div class="col-xs-12 col-md-12">
                                <div class="form-check">
                                    <label class="form-check-label">
                                        <strong>Clinical Support Services</strong>
                                    </label>
                                </div>
                            </div>
                        </div>
                    </c:if>
                    <c:forEach var="baseSvc" items="${corr}" varStatus="stat">
                        <c:set var="indexNo" value="${specSvc.svcCode}${baseSvc.svcCode}${stat.index}"/>
                        <c:choose>
                            <c:when test="${noExistBaseLic}">
                                <div class="row base-svc-content">
                                    <input type="hidden" name="svcName" value="${baseSvc.svcName}"/>
                                    <div class="col-xs-12 col-md-1">
                                    </div>
                                    <div class="col-xs-12 col-md-11 exist-base-lic-content">
                                        <div class="form-check">
                                            <input type="hidden" name="${indexNo}-new" value="${baseSvc.svcCode}" />
                                            <input class="form-check-input firstStep" type="radio" name="${specSvc.svcCode}-base" value="${indexNo}-new" aria-invalid="false" <c:if test="${newLic && baseSvcSel.serviceCode == baseSvc.svcCode}">checked="checked"</c:if> >
                                            <label class="form-check-label"><span class="check-circle"></span>${baseSvc.svcName}</label>
                                        </div>
                                    </div>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="base-svc-content">
                                    <input type="hidden" name="svcName" value="${baseSvc.svcName}"/>
                                    <c:set var="baseLics" value="${baseSvcPremisesMap.get(baseSvc.svcName)}"/>
                                    <c:if test="${!empty baseLics}">
                                        <div class="exist-base-lic-content">
                                            <div class="row">
                                                <div class="col-xs-12 col-md-1">
                                                </div>
                                                <div class="col-xs-12 col-md-11">
                                                    <div class="form-check">
                                                        <input type="hidden" name="base${indexNo}" value="${baseSvc.svcCode}"/>
                                                        <input class="form-check-input firstStep existing-base" type="radio" name="${specSvc.svcCode}-base" value="base${indexNo}" aria-invalid="false" <c:if test="${!newLic && baseSvcSel.serviceCode == baseSvc.svcCode}">checked="checked"</c:if> >
                                                        <label class="form-check-label"><span class="check-circle"></span>Existing ${baseSvc.svcName} licences</label>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="existing-base-content">
                                                <c:forEach var="baseLic" items="${baseLics}" varStatus="sts">
                                                    <!--spe svc code + base svc code + -->
                                                    <c:set var="premIndexNo" value="${specSvc.svcCode}${baseSvc.svcCode}${sts.index}"/>
                                                    <div class="row">
                                                        <div class="col-xs-12 col-md-2">
                                                        </div>
                                                        <div class="col-xs-12 col-md-10">
                                                            <div class="form-check">
                                                                <input type="hidden" name="premHci" value="${baseLic.premisesHci}"/>
                                                                <input type="hidden" name="${premIndexNo}-hciCode" value="<iais:mask name="${premIndexNo}-hciCode" value="${baseLic.hciCode}"/>"/>
                                                                <input class="form-check-input secondStep" type="radio" name="${specSvc.svcCode}" value="${premIndexNo}" aria-invalid="false" <c:if test="${!newLic && baseSvcSel.serviceCode == baseSvc.svcCode && baseLic.hciCode == baseSvcSel.hciCode}">checked="checked"</c:if> >
                                                                <label class="form-check-label"><span class="check-circle"></span>${baseLic.address}</label>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </c:forEach>
                                            </div>
                                        </div>
                                    </c:if>
                                    <div class="row new-base">
                                        <div class="col-xs-12 col-md-1">
                                        </div>
                                        <div class="col-xs-12 col-md-11">
                                            <div class="form-check">
                                                    <%--<input type="hidden" name="baseCode${indexNo}" value="<iais:mask name="baseCode${indexNo}" value="${specSvc.svcCode}"/>" />--%>
                                                <input type="hidden" name="${indexNo}-new" value="${baseSvc.svcCode}"/>
                                                <input class="form-check-input firstStep diff-base" type="radio" name="${specSvc.svcCode}-base" value="${indexNo}-new" aria-invalid="false" <c:if test="${newLic && baseSvcSel.serviceCode == baseSvc.svcCode}">checked="checked"</c:if> >
                                                <label class="form-check-label"><span class="check-circle"></span>${baseSvc.svcName} at a different premises</label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </div>
            </c:forEach>
            <div class="row">
                <div class="col-xs-12 col-md-12">
                    Note: In the absence of a pre-existing base service, you may only apply for 1 premises at a time. <a href="<iais:code code="URL001"/> ">Learn more</a>
                </div>
            </div>
        </div>
    </div>
</div>

