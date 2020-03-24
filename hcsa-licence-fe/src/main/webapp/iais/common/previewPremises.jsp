<div class="panel panel-default">
    <div class="panel-heading
        <c:if test="${!FirstView}">
            <c:if test="${Msg.premiss==null}">completed </c:if> <c:if test="${Msg.premiss!=null}">incompleted </c:if>
        </c:if>" id="headingPremise" role="tab">
        <h4 class="panel-title"><a role="button" class="collapse collapsed" data-toggle="collapse" href="#collapsePremise" aria-expanded="true" aria-controls="collapsePremise">Premises</a></h4>
    </div>
    <div class="panel-collapse collapse" id="collapsePremise" role="tabpanel" aria-labelledby="headingPremise">
        <div class="panel-body">
            <c:if test="${AppSubmissionDto.appEditSelectDto==null||AppSubmissionDto.appEditSelectDto.premisesEdit}">
                <p class="text-right"><a href="#" id="premisesEdit"><em class="fa fa-pencil-square-o"></em>Edit</a></p>
            </c:if>
            <c:forEach var="appGrpPremDto" items="${AppSubmissionDto.appGrpPremisesDtoList}" varStatus="status">
                <div class="panel-main-content">
                    <div class="preview-info">
                        <p><strong>Premises ${status.index+1}</strong></p>
                        <p>${appGrpPremDto.premisesType}: ${appGrpPremDto.address}</p>
                        <c:if test="${'CONVEYANCE'==appGrpPremDto.premisesType}">
                            <p>Vehicle No: ${appGrpPremDto.conveyanceVehicleNo}</p>
                        </c:if>
                    </div>
                </div>
            </c:forEach>
            <c:if test="${FirstView}">
                <br/>
                <p class="font-size-14">Please note that you will not be able to add  or remove any premises here.</p>
                <p class="font-size-14">If you wish to do so, please click <a href="#">here</a>.</p>
            </c:if>
        </div>
    </div>
</div>
