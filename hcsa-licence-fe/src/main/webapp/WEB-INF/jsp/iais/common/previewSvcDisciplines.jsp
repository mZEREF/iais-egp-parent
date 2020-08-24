<div class="amended-service-info-gp">
    <c:forEach var="stepSchem" items="${currentPreviewSvcInfo.hcsaServiceStepSchemeDtos}">
        <c:if test="${stepSchem.stepCode == 'SVST001'}">
            <c:set var="currStepName" value="${stepSchem.stepName}"/>
        </c:if>
    </c:forEach>
    <label style="font-size: 2.2rem">${currStepName}</label>

    <c:forEach var="appSvcLaboratoryDisciplinesDto" items="${currentPreviewSvcInfo.appSvcLaboratoryDisciplinesDtoList}" varStatus="status">
        <div class="amend-preview-info">
            <p><span class="preview-title">Premises ${status.index+1}</span>: ${appSvcLaboratoryDisciplinesDto.premiseGetAddress}</p>
            <div class="form-check-gp">
                <div class="row">
                    <div class="col-xs-12">
                        <c:forEach var="checkList" items="${appSvcLaboratoryDisciplinesDto.appSvcChckListDtoList}">
                            <div class="form-check active">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                                    <c:choose>
                                        <c:when test="${checkList.chkName=='Please indicate'}">${checkList.otherScopeName}</c:when>
                                        <c:otherwise>${checkList.chkName}</c:otherwise>
                                    </c:choose>
                                </p>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>
    </c:forEach>
</div>
