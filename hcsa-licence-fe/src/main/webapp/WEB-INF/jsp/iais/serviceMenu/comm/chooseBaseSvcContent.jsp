<c:if test="${!noExistBaseLic}">
    <div class="row">
        <div class="col-xs-12 col-md-3">
        </div>
        <div class="col-xs-12 col-md-6">
            <c:forEach var="lackSvc" items="${notContainedSvcList}" varStatus="statu">
                <c:set var="indexNo" value="${statu.index}"/>
                ${indexNo}
                <div class="self-assessment-checkbox-gp gradient-light-grey">
                    <c:if test="${appSvcRelatedInfoList.size()>0}">
                        <c:set var="reloadSvcInfo" value="${appSvcRelatedInfoList.get(0)}"/>
                        <c:set var="reloadIndexNo" value="${reloadSvcInfo.serviceCode}${reloadSvcInfo.baseServiceName}${reloadSvcInfo.hciCode}"/>
                    </c:if>
                    <c:set var="lackSvcCode" value="${lackSvc.svcCode}"/>
                    <c:set var="baseSvcSel" value="${reloadBaseSvcSelected.get(lackSvcCode)}"/>
                    <c:set var="selIndexNo" value="${lackSvc.svcCode}${baseSvcSel.serviceCode}${baseSvcSel.licPremisesId}"/>
                    <c:set var="newLic" value="false"/>
                    <c:if test="${''==baseSvcSel.licPremisesId}">
                        <c:set var="newLic" value="true"/>
                    </c:if>
                    <div class="speSvcContent remark-point">
                        <p class="assessment-title"><iais:code code="CDN002"/> for ${lackSvc.svcName}</p>
                        <div class="base-svc-content">
                            <input type="hidden" name="svcName" value="${lackSvc.svcName}"/>
                            <c:set var="baseLics" value="${baseSvcPremisesMap.get(lackSvc.svcName)}"/>
                            <c:if test="${!empty baseLics}">
                                <div class="exist-base-lic-content">
                                    <div class="row">
                                        <div class="col-xs-12 col-md-1">
                                        </div>
                                        <div class="col-xs-12 col-md-11">
                                            <div class="form-check">
                                                <input type="hidden" name="base${status.index}" value="${lackSvc.svcCode}"/>
                                                <input class="form-check-input firstStep existing-base" type="radio" name="${lackSvc.svcCode}-base" value="base${indexNo}" aria-invalid="false"
                                                       <c:if test="${!newLic && baseSvcSel.serviceCode == lackSvc.svcCode}">checked="checked"</c:if> >
                                                <label class="form-check-label"><span class="check-circle"></span>Existing ${lackSvc.svcName} licences</label>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="existing-base-content">
                                        <c:forEach var="baseLic" items="${baseLics}" varStatus="sts">
                                            <c:set var="premIndexNo" value="${lackSvc.svcCode}${sts.index}"/>
                                            <div class="row">
                                                <div class="col-xs-12 col-md-2">
                                                </div>
                                                <div class="col-xs-12 col-md-10">
                                                    <div class="form-check">
                                                        <input type="hidden" name="premHci" value="${baseLic.premisesHci}"/>
                                                        <input type="hidden" name="${premIndexNo}-hciCode" value="<iais:mask name="${premIndexNo}-hciCode" value="${baseLic.hciCode}"/>"/>
                                                        <input type="hidden" name="${premIndexNo}-postCode" value="<iais:mask name="${premIndexNo}-postCode" value="${baseLic.postalCode}"/>"/>
                                                        <input class="form-check-input secondStep" type="radio" name="${lackSvc.svcCode}" value="${premIndexNo}" aria-invalid="false"
                                                               <c:if test="${!newLic && baseSvcSel.serviceCode == lackSvc.svcCode && baseLic.premisesId == baseSvcSel.premisesId}">checked="checked"</c:if> >
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
                                        <input type="hidden" name="${indexNo}-new" value="${baseSvc.svcCode}"/>
                                        <input class="form-check-input firstStep diff-base" type="radio" name="${lackSvc.svcCode}-base" value="${indexNo}-new" aria-invalid="false" <c:if test="${newLic && baseSvcSel.serviceCode == baseSvc.svcCode}">checked="checked"</c:if> >
                                        <label class="form-check-label"><span class="check-circle"></span>${baseSvc.svcName} at a different mode of service delivery</label>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</c:if>

<c:if test="${noExistBaseLic&&!noExistBaseApp}">
    <div class="row">
        <div class="col-xs-12 col-md-3">
        </div>
        <div class="col-xs-12 col-md-6">
            <c:forEach var="lackSvc" items="${notContainedSvcList}" varStatus="status">
                <c:set var="indexNo" value="${stat.index}"/>
                <div class="self-assessment-checkbox-gp gradient-light-grey">
                    <c:if test="${appSvcRelatedInfoList.size()>0}">
                        <c:set var="reloadSvcInfo" value="${appSvcRelatedInfoList.get(0)}"/>
                        <c:set var="reloadIndexNo" value="${reloadSvcInfo.serviceCode}${reloadSvcInfo.baseServiceName}${reloadSvcInfo.hciCode}"/>
                    </c:if>
                    <c:set var="lackSvcCode" value="${lackSvc.svcCode}"/>
                    <c:set var="baseSvcSel" value="${reloadBaseSvcSelected.get(lackSvcCode)}"/>
                    <c:set var="selIndexNo" value="${lackSvc.svcCode}${baseSvcSel.serviceCode}${baseSvcSel.licPremisesId}"/>
                    <c:set var="newLic" value="false"/>
                    <c:if test="${''==baseSvcSel.licPremisesId}">
                        <c:set var="newLic" value="true"/>
                    </c:if>
                    <div class="speSvcContent remark-point">
                        <p class="assessment-title"><iais:code code="CDN002"/> for ${lackSvc.svcName}</p>
                        <div class="base-svc-content">
                            <input type="hidden" name="svcName" value="${lackSvc.svcName}"/>
                            <input type="hidden" class="isNewOrBase" name="isNewOrBase${lackSvc.svcName}" value="base"/>
                            <c:set var="baseLics" value="${baseAppPremisesMap.get(lackSvcCode)}"/>
                            <c:if test="${!empty baseLics}">
                                <div class="exist-base-lic-content">
                                    <div class="row">
                                        <div class="col-xs-12 col-md-1">
                                        </div>
                                        <div class="col-xs-12 col-md-11">
                                            <div class="form-check">
                                                <input type="hidden" name="base${indexNo}" value="${lackSvc.svcCode}"/>
                                                <input class="form-check-input firstStep existing-base" type="radio" name="${lackSvc.svcCode}-base" value="base${indexNo}" aria-invalid="false"
                                                       <c:if test="${!newLic && baseSvcSel.serviceCode == lackSvc.svcCode}">checked="checked"</c:if> >
                                                <label class="form-check-label"><span class="check-circle"></span>Existing ${lackSvc.svcName} Applications</label>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="existing-base-content">
                                        <c:forEach var="baseLic" items="${baseLics}" varStatus="sts">
                                            <c:set var="premIndexNo" value="${lackSvc.svcCode}${sts.index}"/>
                                            <div class="row">
                                                <div class="col-xs-12 col-md-2">
                                                </div>
                                                <div class="col-xs-12 col-md-10">
                                                    <div class="form-check">
                                                        <input type="hidden" name="premHci" value="${baseLic.premisesHci}"/>
                                                        <input type="hidden" name="${premIndexNo}-hciCode" value="<iais:mask name="${premIndexNo}-hciCode" value="${baseLic.hciCode}"/>"/>
                                                        <input type="hidden" name="${premIndexNo}-postCode" value="<iais:mask name="${premIndexNo}-postCode" value="${baseLic.postalCode}"/>"/>
                                                        <input class="form-check-input secondStep" type="radio" name="${lackSvc.svcCode}" value="${premIndexNo}" aria-invalid="false"
                                                               <c:if test="${!newLic && baseSvcSel.serviceCode == lackSvc.svcCode && baseLic.premisesId == baseSvcSel.licPremisesId}">checked="checked"</c:if> >
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
                                        <input type="hidden" name="${indexNo}-new" value="${baseSvc.svcCode}"/>
                                        <input class="form-check-input firstStep diff-base" type="radio" name="${lackSvc.svcCode}-base" value="${indexNo}-new" aria-invalid="false"
                                               <c:if test="${newLic && baseSvcSel.serviceCode == lackSvc.svcCode}">checked="checked"</c:if> >
                                        <label class="form-check-label"><span class="check-circle"></span>${lackSvc.svcName} at a different mode of service delivery</label>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <c:if test="${!status.last}">
                    <br>
                </c:if>
            </c:forEach>
        </div>
    </div>
</c:if>

<c:if test="${noExistBaseLic&&noExistBaseApp}">
    <div class="row">
        <div class="col-xs-12 col-md-3">
        </div>
        <div class="col-xs-12 col-md-6">
            <div class="self-assessment-checkbox-gp gradient-light-grey">
                <div class="row">
                    <div class="col-xs-12 col-md-12">
                        <iais:message key="NEW_ACK008"></iais:message>
                    </div>
                    <div class="col-xs-12 col-md-12">
                        <a target="_blank" href="<iais:code code="URL001"/>">Learn More</a>: <iais:message key="NEW_ACK009"></iais:message>
                    </div>
                </div>
            </div>
        </div>
    </div>
</c:if>
<div class="row">
    <div class="col-xs-12 col-md-3">
    </div>
    <div class="col-xs-12 col-md-6">
        <c:if test="${!empty chooseBaseErr}">
            <span class="error-msg">${chooseBaseErr}</span>
        </c:if>
    </div>
</div>
