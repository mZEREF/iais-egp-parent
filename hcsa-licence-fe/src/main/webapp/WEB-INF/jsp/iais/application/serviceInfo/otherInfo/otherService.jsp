<c:if test="${not empty appSvcOtherInfoDto.allAppPremSubSvcRelDtoList}">
    <%-- <div class="">
         <div class="app-title">${appSvcOtherInfoDto.specialSvcSecName}</div>
         <div><iais:message key="NEW_ACK037"/></div>
     </div>--%>
    <iais:row>
        <fieldset class="fieldset-content col-xs-12">
            <legend></legend>
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