<c:if test="${taskList=='APPRTY002'}">
    <div class="document-upload-list">
        <h3>Biosafety Committee Approval<span class="mandatory"> *</span></h3>
        <div class="file-upload-gp">
            <input type="hidden" name="configIndex" value="${configIndex}"/>
            <a class="btn file-upload btn-secondary" href="javascript:void(0);">Upload</a>
            <br/>
        </div>
    </div>
</c:if>
<c:if test="${taskList=='APPRTY001'}">
    <div class="document-upload-list">
        <h3>Biosafety Committee Approval<span class="mandatory"> *</span></h3>
        <div class="file-upload-gp">
            <input type="hidden" name="configIndex" value="${configIndex}"/>
            <a class="btn file-upload btn-secondary" href="javascript:void(0);">Upload</a>
            <br/>
        </div>
    </div>
    <div class="document-upload-list">
        <h3>Risk Assessment<span class="mandatory"> *</span></h3>
        <div class="file-upload-gp">
            <input type="hidden" name="configIndex" value="${configIndex}"/>
            <a class="btn file-upload btn-secondary" href="javascript:void(0);">Upload</a>
            <br/>
        </div>
    </div>
    <div class="document-upload-list">
        <h3>Standard Operating Procedure (SOP)<span class="mandatory"> *</span></h3>
        <div class="file-upload-gp">
            <input type="hidden" name="configIndex" value="${configIndex}"/>
            <a class="btn file-upload btn-secondary" href="javascript:void(0);">Upload</a>
            <br/>
        </div>
    </div>
    <div class="document-upload-list">
        <h3>Emergency Response Plan<span class="mandatory"> *</span></h3>
        <div class="file-upload-gp">
            <input type="hidden" name="configIndex" value="${configIndex}"/>
            <a class="btn file-upload btn-secondary" href="javascript:void(0);">Upload</a>
            <br/>
        </div>
    </div>
</c:if>
<c:if test="${taskList=='APPRTY003'}">
    <div class="document-upload-list">
        <h3>Biosafety Committee Approval<span class="mandatory"> *</span></h3>
        <div class="file-upload-gp">
            <input type="hidden" name="configIndex" value="${configIndex}"/>
            <a class="btn file-upload btn-secondary" href="javascript:void(0);">Upload</a>
            <br/>
        </div>
    </div>
    <div class="document-upload-list">
        <h3>Risk Assessment and Risk Management<span class="mandatory"> *</span></h3>
        <div class="file-upload-gp">
            <input type="hidden" name="configIndex" value="${configIndex}"/>
            <a class="btn file-upload btn-secondary" href="javascript:void(0);">Upload</a>
            <br/>
        </div>
    </div>
    <div class="document-upload-list">
        <h3>In-Principal Approval from MOH<span class="mandatory"> *</span></h3>
        <div class="file-upload-gp">
            <input type="hidden" name="configIndex" value="${configIndex}"/>
            <a class="btn file-upload btn-secondary" href="javascript:void(0);">Upload</a>
            <br/>
        </div>
    </div>
    <div class="document-upload-list">
        <h3>Emergency Response Plan<span class="mandatory"> *</span></h3>
        <div class="file-upload-gp">
            <input type="hidden" name="configIndex" value="${configIndex}"/>
            <a class="btn file-upload btn-secondary" href="javascript:void(0);">Upload</a>
            <br/>
        </div>
    </div>
    <div class="document-upload-list">
        <h3>Letter of declaration<span class="mandatory"> *</span></h3>
        <div class="file-upload-gp">
            <input type="hidden" name="configIndex" value="${configIndex}"/>
            <a class="btn file-upload btn-secondary" href="javascript:void(0);">Upload</a>
            <br/>
        </div>
    </div>
</c:if>
<%--<div class="document-upload-list">--%>
<%--    <h3>--%>
<%--        <c:if test="${'1' == config.dupForPrem}">Mode of Service Delivery&nbsp;${premStat.index+1}:&nbsp;</c:if>--%>
<%--        <c:if test="${'svcDoc' == docType}">--%>
<%--            <c:choose>--%>
<%--                <c:when test="${'1' == config.dupForPerson}">--%>
<%--                    Clinical Governance Officer:&nbsp;--%>
<%--                </c:when>--%>
<%--                <c:when test="${'2' == config.dupForPerson}">--%>
<%--                    Principal Officer:&nbsp;--%>
<%--                </c:when>--%>
<%--                <c:when test="${'4' == config.dupForPerson}">--%>
<%--                    Nominee:&nbsp;--%>
<%--                </c:when>--%>
<%--                <c:when test="${'8' == config.dupForPerson}">--%>
<%--                    MedAlert Person:&nbsp;--%>
<%--                </c:when>--%>
<%--                <c:when test="${'16' == config.dupForPerson}">--%>
<%--                    Service Personnel:&nbsp;--%>
<%--                </c:when>--%>
<%--                <c:when test="${'32' == config.dupForPerson}">--%>
<%--                    Clinical Director:&nbsp;--%>
<%--                </c:when>--%>
<%--            </c:choose>--%>
<%--        </c:if>--%>
<%--        ${config.docTitle}<c:if test="${config.isMandatory}"><span class="mandatory">  *</span></c:if>--%>
<%--    </h3>--%>
<%--    <div class="file-upload-gp">--%>
<%--        <input type="hidden" name="configIndex" value="${configIndex}"/>--%>
<%--        <span name="${configIndex}ShowId" id="${configIndex}ShowId">--%>
<%--            <c:forEach var="file" items="${fileList}" varStatus="fileStat">--%>
<%--                <div id="${configIndex}Div${file.seqNum}">--%>
<%--                    <c:choose>--%>
<%--                        <c:when test="${!file.passValidate}">--%>
<%--                            <c:out value="${file.docName}"/>--%>
<%--                        </c:when>--%>
<%--                        <c:otherwise>--%>
<%--                            <a class="<c:if test="${!isClickEdit}">disabled</c:if>" href="${pageContext.request.contextPath}/file-repo?filerepo=fileRo${v.index}&fileRo${v.index}=<iais:mask name="fileRo${v.index}" value="${file.fileRepoId}"/>&fileRepoName=${file.docName}">${file.docName}</a>--%>
<%--                        </c:otherwise>--%>
<%--                    </c:choose>--%>
<%--                    <button type="button" class="btn btn-secondary btn-sm delFileBtn" onclick="javascript:deleteFileFeAjax('${configIndex}',${file.seqNum});">--%>
<%--                        Delete--%>
<%--                    </button>--%>
<%--                    <button type="button" class="btn btn-secondary btn-sm reUploadFileBtn" onclick="javascript:reUploadFileFeAjax('${configIndex}',${file.seqNum},'mainForm');">--%>
<%--                        ReUpload--%>
<%--                    </button>--%>
<%--                </div>--%>
<%--            </c:forEach>--%>
<%--        </span>--%>
<%--        <span name="iaisErrorMsg" class="error-msg" id="error_${configIndex}Error"></span>--%>
<%--        <br/>--%>
<%--        <a class="btn file-upload btn-secondary" href="javascript:void(0);">Upload</a>--%>
<%--        <br/>--%>
<%--    </div>--%>
<%--</div>--%>