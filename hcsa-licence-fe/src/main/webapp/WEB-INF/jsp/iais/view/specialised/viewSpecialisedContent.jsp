<div class="form-horizontal">
    <div class="specialised-content" style="padding-bottom: 1.5rem;">
        <iais:row style="padding-bottom: 1rem;">
            <div class="col-xs-12">
                <p class="app-title">${specialised.premName}</p>
                <p class="font-18 bold">Address: ${specialised.premAddress}</p>
            </div>
        </iais:row>

        <c:if test="${specialised.existCheckedScopes}">
            <div class="">
                <div class="app-title">${specialised.categorySectionName}</div>
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
                <div class="app-title">${specialised.specialSvcSecName}</div>
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
                <span class="font-18 bold error-msg col-xs-12"><iais:message key="NEW_ACK038"/></span>
            </iais:row>
        </c:if>
    </div>
</div>