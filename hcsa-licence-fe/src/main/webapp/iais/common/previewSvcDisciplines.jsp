<div class="amended-service-info-gp">
    <h2>LABORATORY DISCIPLINES</h2>
    <c:forEach var="appSvcLaboratoryDisciplinesDto" items="${currentPreviewSvcInfo.appSvcLaboratoryDisciplinesDtoList}" varStatus="status">
        <div class="amend-preview-info">
            <p><span class="preview-title">Premises ${status.index+1}</span>: ${appSvcLaboratoryDisciplinesDto.premiseGetAddress}</p>
            <div class="form-check-gp">
                <div class="row">
                    <div class="col-xs-12">
                        <c:forEach var="checkList" items="${appSvcLaboratoryDisciplinesDto.appSvcChckListDtoList}">
                            <c:if test="${checkList.chkLstConfId!='0B38F14D-1123-EA11-BE78-000C29D29DB0'}">
                                <div class="form-check active">
                                    <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                                        <c:choose>
                                            <c:when test="${checkList.chkLstConfId=='27D8EB5B-1123-EA11-BE78-000C29D29DB0'}">${checkList.otherScopeName}</c:when>
                                            <c:otherwise>${checkList.chkName}</c:otherwise>
                                        </c:choose>
                                    </p>
                                </div>
                            </c:if>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>
    </c:forEach>
</div>
