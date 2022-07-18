<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts" %>
<div class="document-upload-list">
    <h3>
        <c:if test="${'1' == config.dupForPrem}">Mode of Service Delivery&nbsp;${premStat.index+1}:&nbsp;</c:if>
        <c:if test="${'svcDoc' == docType}">
            <c:choose>
                <c:when test="${'1' == config.dupForPerson}">
                    <%=HcsaConsts.CLINICAL_GOVERNANCE_OFFICER%>&nbsp;${psnStat.index+1}:&nbsp;
                </c:when>
                <c:when test="${'2' == config.dupForPerson}">
                    <%=HcsaConsts.PRINCIPAL_OFFICER%>&nbsp;${psnStat.index+1}:&nbsp;
                </c:when>
                <c:when test="${'4' == config.dupForPerson}">
                    <%=HcsaConsts.NOMINEE%>&nbsp;${psnStat.index+1}:&nbsp;
                </c:when>
                <c:when test="${'8' == config.dupForPerson}">
                    <%=HcsaConsts.MEDALERT_PERSON%>&nbsp;${psnStat.index+1}:&nbsp;
                </c:when>
                <c:when test="${'16' == config.dupForPerson}">
                    <%=HcsaConsts.SERVICE_PERSONNEL%>&nbsp;${psnStat.index+1}:&nbsp;
                </c:when>
                <c:when test="${'32' == config.dupForPerson}">
                    <%=HcsaConsts.CLINICAL_DIRECTOR%>&nbsp;${psnStat.index+1}:&nbsp;
                </c:when>
                <c:when test="${'64' == config.dupForPerson}">
                    <%=HcsaConsts.SECTION_LEADER%>&nbsp;${psnStat.index+1}:&nbsp;
                </c:when>
            </c:choose>
        </c:if>
        ${config.docTitle}<c:if test="${config.isMandatory}"><span class="mandatory"> *</span></c:if>
    </h3>
    <div class="file-upload-gp">
        <input type="hidden" name="configIndex" value="${configIndex}"/>
        <span name="${configIndex}ShowId" id="${configIndex}ShowId">
            <c:forEach var="file" items="${fileList}" varStatus="fileStat">
                <div id="${configIndex}Div${file.seqNum}">
                    <c:choose>
                        <c:when test="${!file.passValidate}">
                            <c:out value="${file.docName}"/>
                        </c:when>
                        <c:otherwise>
                            <iais:downloadLink fileRepoIdName="fileRo${v.index}" fileRepoId="${file.fileRepoId}" docName="${file.docName}"/>
                        </c:otherwise>
                    </c:choose>
                    <button type="button" class="btn btn-secondary btn-sm delFileBtn" onclick="javascript:deleteFileFeAjax('${configIndex}',${file.seqNum});">
                        Delete
                    </button>
                    <button type="button" class="btn btn-secondary btn-sm reUploadFileBtn" onclick="javascript:reUploadFileFeAjax('${configIndex}',${file.seqNum},'mainForm');">
                        ReUpload
                    </button>
                </div>
            </c:forEach>
        </span>
        <span name="iaisErrorMsg" class="error-msg" id="error_${configIndex}Error"></span>
        <br/>
        <a class="btn file-upload btn-secondary" href="javascript:void(0);">Upload</a>
        <br/>
    </div>
</div>