<div class="row form-horizontal">
    <c:forEach var="specialised" items="${AppSubmissionDto.appPremSpecialisedDtoList}">
        <c:if test="${specialised_svc_code == specialised.baseSvcCode}">
            <div class="specialised-content" style="padding-bottom: 1.5rem;">
                <iais:row style="padding-bottom: 1rem;">
                    <div class="col-xs-12">
                        <p class="app-title">${specialised.premName}</p>
                        <p class="font-18 bold">${specialised.premAddress}</p>
                    </div>
                </iais:row>

                <c:if test="${specialised.existCheckedScopes}">
                    <div class="">
                        <div class="font-18 bold">${specialised.categorySectionName}</div>
                    </div>
                    <iais:row>
                        <fieldset class="fieldset-content col-xs-12">
                            <legend></legend>
                            <div class="form-check-gp">
                                <c:forEach var="item" items="${specialised.allAppPremScopeDtoList}" varStatus="status">
                                    <c:if test="${item.checked}">
                                        <div class="form-check active">
                                            <div class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                                                <c:out value="${item.scopeName}" />
                                            </div>
                                        </div>
                                    </c:if>
                                </c:forEach>
                            </div>
                        </fieldset>
                    </iais:row>
                </c:if>

                <c:if test="${specialised.existCheckedRels}">
                    <div class="">
                        <div class="font-18 bold">${specialised.specialSvcSecName}</div>
                        <div><iais:message key="NEW_ACK037"/></div>
                    </div>
                    <iais:row>
                        <fieldset class="fieldset-content col-xs-12">
                            <legend></legend>
                            <div class="form-check-gp">
                                <c:forEach var="item" items="${specialised.allAppPremSubSvcRelDtoList}" varStatus="status">
                                    <c:if test="${item.checked}">
                                        <div class="form-check active">
                                            <div class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                                                <c:out value="${item.svcName}" />
                                            </div>
                                        </div>
                                    </c:if>
                                </c:forEach>
                            </div>
                        </fieldset>
                    </iais:row>
                </c:if>

                <c:if test="${!specialised.existCheckedScopes && !specialised.existCheckedRels}">
                    <iais:row>
                        <p class="font-18 bold"><iais:message key="GENERAL_ERR0071"/></p>
                    </iais:row>
                </c:if>
            </div>
        </c:if>
    </c:forEach>
</div>