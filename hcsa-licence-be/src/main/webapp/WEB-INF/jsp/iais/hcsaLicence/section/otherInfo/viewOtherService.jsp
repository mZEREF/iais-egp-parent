<c:if test="${otherInfo.existCheckedRels || oldOtherInfo.existCheckedRels}">
    <div class="col-xs-12">
        <div class="app-title">Other Service</div>
        <div style="margin-top: 10px;!important;">
            <span>Do you intend to provide the following services:</span>
        </div>
    </div>
    <c:forEach var="item" items="${otherInfo.allAppPremSubSvcRelDtoList}" varStatus="status">
        <c:set var="olditem" value="${oldOtherInfo.allAppPremSubSvcRelDtoList[status.index]}"/>
        <c:if test="${item.checked || olditem.checked}">
            <div class="col-xs-12 row">
                <div class="col-xs-12 col-md-6">
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
                <div class="col-xs-12 col-md-6">
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
            </div>
        </c:if>
    </c:forEach>
</c:if>