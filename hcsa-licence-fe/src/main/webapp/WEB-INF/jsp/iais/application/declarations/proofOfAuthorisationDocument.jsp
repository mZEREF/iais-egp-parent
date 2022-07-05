<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    String webroot2 = IaisEGPConstant.CSS_ROOT + IaisEGPConstant.COMMON_CSS_ROOT;
%>
<script type="text/javascript" src="<%=webroot2%>js/file-upload.js"></script>
<div class="panel-body">
    <div class="row">
        <P>If you have selected that <span style="font-style: italic">'I am duly authorised by the Applicant to make this application on its behalf and
          the Applicant will be the licensee if the application is granted'</span>, please attach proof of your authorisation
            below:</P>
        <br>
        <div class="document-upload-gp">
            <div class="document-upload-list Proof-Authorisation">
                <h3>Proof of Authorisation <c:if test="${AppSubmissionDto.appDeclarationMessageDto.preliminaryQuestionKindly=='0'}"><strong style=color:#ff0000;>*</strong></c:if></h3>
                <div class="file-upload-gp">
                    <span name="selected${sec}FileShowId" id="selected${sec}FileShowId">
                    <c:forEach items="${pageShowFileDtos}" var="pageShowFileDto" varStatus="ind">
                        <div id="${pageShowFileDto.fileMapId}">
                          <span name="fileName" style="font-size: 14px;color: #2199E8;text-align: center">
                            <iais:downloadLink fileRepoIdName="fileRo0" fileRepoId="${pageShowFileDto.fileUploadUrl}" docName="${pageShowFileDto.fileName}"/>
                          </span>
                          <span class="error-msg" name="iaisErrorMsg" id="file${ind.index}"></span>
                          <span class="error-msg" name="iaisErrorMsg" id="error_${configIndex}error"></span>
                          <button type="button"<c:if test="${!empty declaration_page_confirm}">disabled</c:if>  class="btn btn-secondary btn-sm" onclick="javascript:deleteFileFeAjax('selected${sec}File',${pageShowFileDto.index});">Delete</button>
                          <button type="button" <c:if test="${!empty declaration_page_confirm}">disabled</c:if> class="btn btn-secondary btn-sm" onclick="javascript:reUploadFileFeAjax('selected${sec}File',${pageShowFileDto.index},'mainForm');">ReUpload</button>
                      </div>
                    </c:forEach>
                    </span>
                    <br/>
                    <input id="selectedFile" name="selectedFile"
                       class="selectedFile commDoc"
                       type="file" <c:if test="${!empty declaration_page_confirm}">disabled</c:if> style="display: none;"
                       aria-label="selectedFile1"
                       onclick="fileClicked(event)"
                       onchange="ajaxCallUploadForMax('mainForm','selected${sec}File', true);"/>
                    <a class="btn btn-file-upload btn-secondary" onclick="clearFlagValueFEFile()" <c:if test="${!empty declaration_page_confirm}">disabled</c:if>>Upload</a>
                </div>
                <span class="error-msg" id="error_litterFile_Show" name="error_litterFile_Show" style="color: #D22727; font-size: 1.6rem"></span>
                <span id="error_selected${sec}FileError" name="iaisErrorMsg" class="error-msg"></span>
            </div>
        </div>
    </div>
</div>

