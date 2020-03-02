<div class="amended-service-info-gp">
    <h2>LABORATORY DISCIPLINES</h2>
    <c:forEach var="appSvcLaboratoryDisciplinesDto" items="${currentPreviewSvcInfo.appSvcLaboratoryDisciplinesDtoList}" varStatus="status">
        <div class="amend-preview-info">
            <p><span class="preview-title">Premises ${status.index+1}</span>: ${appSvcLaboratoryDisciplinesDto.premiseGetAddress}</p>
            <div class="form-check-gp">
                <div class="row">
                    <div class="col-xs-12">
                        <c:forEach var="checkList" items="${appSvcLaboratoryDisciplinesDto.appSvcChckListDtoList}">
                            <div class="form-check active">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>${checkList.chkName}</p>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>
    </c:forEach>
</div>
