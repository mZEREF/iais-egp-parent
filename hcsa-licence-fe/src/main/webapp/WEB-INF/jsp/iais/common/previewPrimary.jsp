<div class="panel panel-default">
    <div class="panel-heading
        <c:if test="${empty printView}">
            <c:choose>
                <c:when test="${!FirstView}">
                    <c:if test="${coMap.document=='document'}">completed </c:if> <c:if test="${coMap.document==''}">incompleted </c:if>
                </c:when>
                <c:when test="${needShowErr}">
                    <c:if test="${!empty svcSecMap.document}">incompleted </c:if>
                </c:when>
            </c:choose>
         </c:if>
    " id="headingOne" role="tab">
        <h4 class="panel-title"><a class="collapsed a-panel-collapse" style="text-decoration: none;" role="button" data-toggle="collapse" href="#collapseOne${documentIndex}" aria-expanded="true" aria-controls="collapseOne" name="printControlNameForApp">Primary Documents</a></h4>
    </div>
    <div class="panel-collapse collapse <c:if test="${!empty printFlag}">in</c:if>" id="collapseOne${documentIndex}" role="tabpanel" aria-labelledby="headingOne">
        <div class="panel-body">
            <c:if test="${(AppSubmissionDto.appEditSelectDto==null||AppSubmissionDto.appEditSelectDto.docEdit) && empty printView && (empty isSingle || isSingle == 'Y')}">
                <p class="mb-0"><div class="text-right app-font-size-16"><a href="#" id="docEdit"><em class="fa fa-pencil-square-o"></em>Edit</a></div></p>
            </c:if>
            <div class="elemClass-1561088919456">
                <div id="control--runtime--34" class="page section control  container-s-1" style="margin: 10px 0px">
                    <div class="control-set-font control-font-header section-header">
                        <label style="font-size: 2.2rem">Uploaded Documents</label>

                    </div>
                    <div class="pop-up">
                        <div class="pop-up-body">
                            <%--<c:forEach var="appGrpPrimaryDocDto" items="${AppSubmissionDto.appGrpPrimaryDocDtos}" varStatus="status">
                                <c:if test="${!empty appGrpPrimaryDocDto.docName && !empty appGrpPrimaryDocDto.fileRepoId}">
                                <div class="content-body fileUploadContainer">
                                    <div class="field col-sm-4 control-label formtext"><label>  ${appGrpPrimaryDocDto.svcComDocName}</label></div>
                                    <span class="fileType" style="display:none">Docment1</span><span class="fileFilter" style="display:none">png</span><span class="fileMandatory" style="display:none">Yes</span>
                                    <div class="control col-sm-12">
                                        <div class="fileList ">
                                            <span class="filename server-site" id="130">
                                                <c:choose>
                                                    <c:when test="${!appGrpPrimaryDocDto.passValidate}">
                                                        ${appGrpPrimaryDocDto.docName}
                                                    </c:when>
                                                    <c:otherwise>
                                                        <a href="${pageContext.request.contextPath}/file-repo?filerepo=fileRo${status.index}&fileRo${status.index}=<iais:mask name="fileRo${status.index}" value="${appGrpPrimaryDocDto.fileRepoId}"/>&fileRepoName=${appGrpPrimaryDocDto.docName}" title="Download" class="downloadFile">${appGrpPrimaryDocDto.docName}</a>
                                                    </c:otherwise>
                                                </c:choose> (${appGrpPrimaryDocDto.docSize} KB)
                                            </span>
                                        </div>
                                    </div>
                                </div>
                                </c:if>
                            </c:forEach>--%>

                            <c:set var="reloadMap" value="${AppSubmissionDto.multipleGrpPrimaryDoc}"/>
                            <c:forEach var="config" items="${primaryDocConfig}" varStatus="configStat">
                                <c:choose>
                                    <c:when test="${'0' == config.dupForPrem}">
                                        <c:set var="fileList" value="${reloadMap[config.id]}"/>
                                        <%@include file="previewPrimaryContent.jsp"%>
                                    </c:when>
                                    <c:when test="${'1' == config.dupForPrem}">
                                        <c:forEach var="prem" items="${AppSubmissionDto.appGrpPremisesDtoList}" varStatus="premStat">
                                            <c:set var="mapKey" value="${prem.premisesIndexNo}${config.id}"/>
                                            <c:set var="fileList" value="${reloadMap[mapKey]}"/>
                                            <%@include file="previewPrimaryContent.jsp"%>
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
