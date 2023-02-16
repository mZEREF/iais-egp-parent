<div class="form-horizontal">
    <div class="specialised-content" style="padding-bottom: 1.5rem;">
        <iais:row style="padding-bottom: 1rem;">
            <div class="col-xs-12">
                <div class="newVal "
                     attr="${specialised.premName}<c:out value="${specialised.premAddress}"/>">
                    <c:if test="${not empty specialised.premAddress}">
                        <div class="app-title"><c:out value="${specialised.premName}"/></div>
                        <div class="font-18 bold">Address: <c:out
                                value="${specialised.premAddress}"/></div>
                    </c:if>
                </div>
            </div>
            <div class="col-xs-12">
                <div class="oldVal"
                     attr="${oldSpecialised.premName}<c:out value="${oldSpecialised.premAddress}"/>">
                    <c:if test="${not empty oldSpecialised.premAddress}">
                        <div class="app-title"><c:out value="${oldSpecialised.premName}"/></div>
                        <div class="font-18 bold">Address: <c:out
                                value="${oldSpecialised.premAddress}"/></div>
                    </c:if>
                </div>
            </div>
        </iais:row>
        <c:if test="${specialised.existCheckedScopes || oldSpecialised.existCheckedScopes}">
            <div class="">
                <div class="app-title">${specialised.categorySectionName}</div>
            </div>
            <iais:row>
                <fieldset class="fieldset-content category">
                    <legend></legend>
                    <div class="form-check-gp">
                        <c:forEach var="item" items="${specialised.allAppPremScopeDtoList}" varStatus="status">
                            <c:set var="olditem" value="${oldSpecialised.allAppPremScopeDtoList[status.index]}"/>
                            <c:if test="${item.checked || olditem.checked}">
                                <div class="col-xs-12 row form-check">
                                    <div class="col-xs-12 col-md-6">
                                        <div class="newVal" attr="<c:out value="${item.checked}${item.scopeName}" />">
                                            <c:if test="${item.checked}">
                                                <input style="cursor: default;" class="form-check-input" checked type="checkbox" disabled>
                                                <span class="form-check-label" style="color: #212529;">
                                                    <span class="check-square"></span>
                                                    <c:out value="${item.scopeName}" />
                                                </span>
                                            </c:if>
                                        </div>
                                    </div>
                                    <div class="col-xs-12 col-md-6">
                                        <div class="oldVal" attr="${olditem.checked}<c:out value="${olditem.scopeName}" />">
                                            <c:if test="${olditem.checked}">
                                                <input style="cursor: default;" class="form-check-input" checked type="checkbox" disabled>
                                                <span class="form-check-label" style="color: #212529;">
                                                    <span class="check-square"></span>
                                                    <c:out value="${olditem.scopeName}" />
                                                </span>
                                            </c:if>
                                        </div>
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
            </div>
            <iais:row>
                <fieldset class="fieldset-content ss">
                    <legend></legend>
                    <div class="form-check-gp">
                        <c:forEach var="item" items="${specialised.allAppPremSubSvcRelDtoList}" varStatus="status">
                            <c:set var="olditem" value="${oldSpecialised.allAppPremSubSvcRelDtoList[status.index]}"/>
                            <c:if test="${item.checked || olditem.checked}">
                                <div class="col-xs-12 row form-check">
                                    <div class="col-xs-12 col-md-6">
                                        <div class="newVal" attr="${item.checked}<c:out value="${item.svcName}" />">
                                            <c:if test="${item.checked}">
                                                <input style="cursor: default;" class="form-check-input" checked type="checkbox" disabled>
                                                <span class="form-check-label" style="color: #212529;">
                                                    <span class="check-square"></span>
                                                    <c:out value="${item.svcName}" />
                                                </span>
                                            </c:if>
                                        </div>
                                    </div>
                                    <div class="col-xs-12 col-md-6">
                                        <div class="oldVal " attr="${olditem.checked}<c:out value="${olditem.svcName}" />">
                                            <c:if test="${olditem.checked}">
                                                <input style="cursor: default;" class="form-check-input" checked type="checkbox" disabled>
                                                <span class="form-check-label" style="color: #212529;">
                                                    <span class="check-square"></span>
                                                    <c:out value="${olditem.svcName}" />
                                                </span>
                                            </c:if>
                                        </div>
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
                <span class="color-red bold col-xs-12"><iais:message key="NEW_ACK038"/></span>
            </iais:row>
        </c:if>
    </div>
</div>