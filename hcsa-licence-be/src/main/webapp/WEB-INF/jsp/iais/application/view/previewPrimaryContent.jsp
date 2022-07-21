<div class="content-body fileUploadContainer">
    <c:forEach var="appGrpPrimaryDocDto" items="${fileList}" varStatus="docStat">
        <div class="field col-sm-4 control-label formtext">
            <c:if test="${docStat.first}">
                <label>  ${appGrpPrimaryDocDto.displayTitle}</label>
            </c:if>
        </div>
        <div class="control col-sm-12">
            <div class="fileList ">
            <span class="filename server-site" id="130">
                <c:choose>
                    <c:when test="${!empty printFlag}">
                        ${appGrpPrimaryDocDto.docName}
                    </c:when>
                    <c:when test="${empty appGrpPrimaryDocDto.fileRepoId}">
                        ${appGrpPrimaryDocDto.docName}
                    </c:when>
                    <c:otherwise>
                        <iais:downloadLink fileRepoIdName="fileRo${docStat.index}" fileRepoId="${appGrpPrimaryDocDto.fileRepoId}" docName="${appGrpPrimaryDocDto.docName}"/>
                    </c:otherwise>
                </c:choose> (${appGrpPrimaryDocDto.docSize} KB)
            </span>
            </div>
        </div>
    </c:forEach>
</div>