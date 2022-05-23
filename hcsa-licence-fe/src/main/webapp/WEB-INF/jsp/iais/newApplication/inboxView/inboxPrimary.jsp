<div class="panel panel-default">
    <div class="panel-heading" id="headingOne" role="tab">
        <h4 class="panel-title"><a class="collapsed" role="button" data-toggle="collapse" href="#collapseOne" aria-expanded="true" aria-controls="collapseOne">Primary Documents</a></h4>
    </div>
    <div class="panel-collapse collapse " id="collapseOne" role="tabpanel" aria-labelledby="headingOne">
        <div class="panel-body">
            <div class="elemClass-1561088919456">
                <div id="control--runtime--34" class="page section control  container-s-1" style="margin: 10px 0px">
                    <div class="control-set-font control-font-header section-header">
                        <p class="svc-title">Uploaded Documents</p>
                    </div>
                    <div class="pop-up">
                        <div class="pop-up-body">

                            <c:set var="reloadMap" value="${AppSubmissionDto.multipleGrpPrimaryDoc}"/>
                            <c:forEach var="config" items="${primaryDocConfig}" varStatus="configStat">
                                <c:choose>
                                    <c:when test="${'0' == config.dupForPrem}">
                                        <c:set var="fileList" value="${reloadMap[config.id]}"/>
                                        <%@include file="../../common/previewPrimaryContent.jsp"%>
                                    </c:when>
                                    <c:when test="${'1' == config.dupForPrem}">
                                        <c:forEach var="prem" items="${AppSubmissionDto.appGrpPremisesDtoList}" varStatus="premStat">
                                            <c:set var="mapKey" value="${prem.premisesIndexNo}${config.id}"/>
                                            <c:set var="fileList" value="${reloadMap[mapKey]}"/>
                                            <%@include file="../../common/previewPrimaryContent.jsp"%>
                                        </c:forEach>
                                    </c:when>
                                </c:choose>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
