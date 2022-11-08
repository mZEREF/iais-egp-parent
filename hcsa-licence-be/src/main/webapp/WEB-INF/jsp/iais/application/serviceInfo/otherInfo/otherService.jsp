<div class="otherServiceContent">
    <c:if test="${not empty appSvcOtherInfoDto.allAppPremSubSvcRelDtoList}">
        <input type="hidden" class ="isPartEditOtherService" name="isPartEditOtherService" value="0"/>
        <input type="hidden" class="otherInfoIndexNo" name="otherInfoIndexNo" value="${appSvcOtherInfoDto.premiseIndex}"/>
        <div class="col-md-12 col-xs-12">
            <div class="edit-content">
                <c:if test="${canEdit}">
                    <div class="text-right app-font-size-16">
                        <a class="edit otherServiceEdit" href="javascript:void(0);">
                            <em class="fa fa-pencil-square-o"></em><span>&nbsp;</span>Edit
                        </a>
                    </div>
                </c:if>
            </div>
        </div>
        <div class="">
            <div class="app-title">Other Services</div>
        </div>
        <div class="col-xs-12" style="margin-top: 10px;!important;">
            <span>Do you intend to provide the following services:</span>
        </div>
        <iais:row>
            <fieldset class="fieldset-content col-xs-12">
                <div class="form-check-gp">
                    <c:forEach var="item" items="${appSvcOtherInfoDto.allAppPremSubSvcRelDtoList}" varStatus="status">
                        <div class="form-check form-check-${item.level}" data-parent="${appSvcOtherInfoDto.premisesVal}-${item.parentId}">
                            <input class="form-check-input" id="${appSvcOtherInfoDto.premisesVal}-${item.svcId}"
                                   name="${appSvcOtherInfoDto.premisesVal}_${item.parentId}_service" value="${item.svcId}"
                                   type="checkbox" aria-invalid="false" data-prem="${appSvcOtherInfoDto.premisesVal}"
                                   <c:if test="${item.checked}">checked="checked"</c:if> />
                            <label class="form-check-label" for="${appSvcOtherInfoDto.premisesVal}-${item.svcId}">
                                <span class="check-square"></span><c:out value="${item.svcName}"/>
                            </label>
                        </div>
                    </c:forEach>
                    <div class="form-check">
                        <span class="error-msg" name="iaisErrorMSg" id="error_${appSvcOtherInfoDto.premisesVal}_service"></span>
                    </div>
                </div>
            </fieldset>
        </iais:row>
    </c:if>
</div>