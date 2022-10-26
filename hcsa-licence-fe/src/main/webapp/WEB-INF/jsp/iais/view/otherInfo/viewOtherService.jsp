<div class="form-horizontal">
    <div class="specialised-content" style="padding-bottom: 1.5rem;">
        <c:if test="${appSvcOtherInfoDto.existCheckedRels}">
            <div class="">
                <div class="app-title">Other Services</div>
            </div>
            <div style="margin-top: 10px;!important;">
                <span>Do you intend to provide the following services:</span>
            </div>
            <iais:row>
                <fieldset class="fieldset-content col-xs-12">
                    <div class="form-check-gp">
                        <c:forEach var="item" items="${appSvcOtherInfoDto.allAppPremSubSvcRelDtoList}" varStatus="status">
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
    </div>
</div>