<div class="row">
    <div class="col-xs-12 col-md-3">
    </div>
    <div class="col-xs-12 col-md-6">
        <c:if test="${!empty chooseBaseErr}">
            <span class="error-msg">${chooseBaseErr}</span>
        </c:if>
    </div>
</div>

<div class="row">
    <div class="col-xs-12 col-md-3">
    </div>
    <div class="col-xs-12 col-md-6">
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
                <div class="speSvcContent disable-point">
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
                                    <div class="col-xs-12 col-md-1">
                                    </div>
                                    <div class="col-xs-12 col-md-11">
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
                                            <c:forEach var="baseLic" items="${retainLicPremisesList}" varStatus="sts">
                                                <!--spe svc code + base svc code + -->
                                                <c:set var="premIndexNo" value="${specSvc.svcCode}${baseSvc.svcCode}${sts.index}"/>
                                                <div class="row">
                                                    <div class="col-xs-12 col-md-2">
                                                    </div>
                                                    <div class="col-xs-12 col-md-10">
                                                        <div class="form-check">
                                                            <input type="hidden" name="${premIndexNo}-hciCode" value="<iais:mask name="${premIndexNo}-hciCode" value="${baseLic.hciCode}"/>"/>
                                                            <input class="form-check-input" type="radio" name="${specSvc.svcCode}" value="${premIndexNo}" aria-invalid="false" <c:if test="${!newLic && baseSvcSel.serviceCode == baseSvc.svcCode && baseLic.hciCode == baseSvcSel.hciCode}">checked="checked"</c:if> >
                                                            <label class="form-check-label"><span class="check-circle"></span>${baseLic.address}</label>
                                                        </div>
                                                    </div>
                                                </div>
                                            </c:forEach>
                                        </div>
                                    </div>
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
        </div>
    </div>
</div>

