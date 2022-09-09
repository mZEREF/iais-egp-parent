<div class="form-horizontal">
    <div class="specialised-content" style="padding-bottom: 1.5rem;">
        <iais:row style="padding-bottom: 1rem;">
            <div class="col-xs-12">
                <p class="app-title">${specialised.premName}</p>
                <p class="font-18 bold">${specialised.premAddress}</p>
            </div>
        </iais:row>

        <c:if test="${specialised.existCheckedScopes || oldSpecialised.existCheckedScopes}">
            <div class="">
                <div class="app-title">${specialised.categorySectionName}</div>
            </div>
            <iais:row>
                <fieldset class="fieldset-content col-xs-12">
                    <legend></legend>
                    <div class="form-check-gp">
                        <c:forEach var="item" items="${specialised.allAppPremScopeDtoList}" varStatus="status">
                            <c:set var="olditem" value="${oldSpecialised.allAppPremScopeDtoList[status.index]}"/>
                            <c:if test="${item.checked || olditem.checked}">
                                <div class="col-xs-6 col-md-6">
                                    <div class="newVal " attr="<c:out value="${item.checked}${item.scopeName}" />">
                                        <c:if test="${item.checked}">
                                            <div class="form-check active">
                                                <div class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                                                    <c:out value="${item.scopeName}" />
                                                </div>
                                            </div>
                                        </c:if>
                                    </div>
                                </div>
                                <div class="col-xs-6 col-md-6">
                                    <div class="oldVal " attr="${olditem.checked}<c:out value="${olditem.scopeName}" />">
                                        <c:if test="${olditem.checked}">
                                            <div class="form-check active">
                                                <div class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                                                    <c:out value="${olditem.scopeName}" />
                                                </div>
                                            </div>
                                        </c:if>
                                    </div>
                                </div>
                            </c:if>
                        </c:forEach>
                    </div>
                </fieldset>
            </iais:row>
        </c:if>

        <c:if test="${specialised.existCheckedRels || oldSpecialised.existCheckedRels}">
            <div class="">
                <div class="app-title">${specialised.specialSvcSecName}</div>
                <div><iais:message key="NEW_ACK037"/></div>
            </div>
            <iais:row>
                <fieldset class="fieldset-content col-xs-12">
                    <legend></legend>
                    <div class="form-check-gp">
                        <c:forEach var="item" items="${specialised.allAppPremSubSvcRelDtoList}" varStatus="status">
                            <c:set var="olditem" value="${oldSpecialised.allAppPremSubSvcRelDtoList[status.index]}"/>
                            <c:if test="${item.checked || olditem.checked}">
                                <div class="col-xs-6 col-md-6">
                                    <div class="newVal " attr="${item.checked}<c:out value="${item.svcName}" />">
                                        <c:if test="${item.checked}">
                                            <div class="form-check active">
                                                <div class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                                                    <c:out value="${item.svcName}" />
                                                </div>
                                            </div>
                                        </c:if>
                                    </div>
                                </div>
                                <div class="col-xs-6 col-md-6">
                                    <div class="oldVal " attr="${olditem.checked}<c:out value="${olditem.svcName}" />">
                                        <c:if test="${olditem.checked}">
                                            <div class="form-check active">
                                                <div class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                                                    <c:out value="${olditem.svcName}" />
                                                </div>
                                            </div>
                                        </c:if>
                                    </div>
                                </div>
                            </c:if>
                        </c:forEach>
                    </div>
                </fieldset>
            </iais:row>
        </c:if>

        <c:if test="${!specialised.existCheckedScopes && !specialised.existCheckedRels
                && !oldSpecialised.existCheckedScopes && !oldSpecialised.existCheckedRels}">
            <iais:row>
                <p class="font-18 bold"><iais:message key="NEW_ACK038"/></p>
            </iais:row>
        </c:if>
    </div>
</div>