<c:if test="${empty printView}">
    <c:choose>
        <c:when test="${!FirstView}">
            <c:set var="headingSign" value="${coMap.premises == 'premises' ? 'completed' : 'incompleted'}"/>
        </c:when>
        <c:when test="${needShowErr}">
            <c:set var="headingSign" value="${not empty svcSecMap.premiss ? 'incompleted' : 'completed'}" />
        </c:when>
    </c:choose>
</c:if>
<div class="panel panel-default">
    <div class="panel-heading ${headingSign}" id="headingPremise" role="tab">
        <h4 class="panel-title"><a role="button" class="collapsed" style="text-decoration: none;" data-toggle="collapse" href="#collapsePremise${documentIndex}" aria-expanded="true" aria-controls="collapsePremise"  name="printControlNameForApp">Mode of Service Delivery</a></h4>
    </div>
    <div class="panel-collapse collapse <c:if test="${!empty printFlag}">in</c:if>" id="collapsePremise${documentIndex}" role="tabpanel" aria-labelledby="headingPremise">
        <div class="panel-body">
            <c:if test="${(AppSubmissionDto.appEditSelectDto==null||AppSubmissionDto.appEditSelectDto.premisesEdit) && empty printView && (empty isSingle || isSingle == 'Y')}">
                <p><div class="text-right app-font-size-16"><a href="#" id="premisesEdit"><em class="fa fa-pencil-square-o"></em>Edit</a></div></p>
            </c:if>
            <c:forEach var="appGrpPremDto" items="${AppSubmissionDto.appGrpPremisesDtoList}"
                       varStatus="status">
                <div class="panel-main-content form-horizontal min-row">
                    <div class="row col-xs-12">
                        <p class="app-title">Mode of Service Delivery ${status.index+1}</p>
                    </div>
                    <%@include file="premises/viewPremisesContent.jsp"%>
                </div>
            </c:forEach>
            <c:if test="${empty retriggerGiro && FirstView && !('APTY004' == AppSubmissionDto.appType || 'APTY005' == AppSubmissionDto.appType)}">
                <br/>
                <p class="font-size-14">Please note that you will not be able to add  or remove any mode of service delivery here.</p>
                <p class="font-size-14">If you wish to do so, please click <a href="#">here</a>.</p>
            </c:if>
        </div>
    </div>
</div>