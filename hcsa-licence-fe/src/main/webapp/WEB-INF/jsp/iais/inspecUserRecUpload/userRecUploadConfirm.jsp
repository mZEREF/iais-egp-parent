<%--
  Created by IntelliJ IDEA.
  User: ShiCheng_Xu
  Date: 2019/12/23
  Time: 14:38
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>

<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<div class="container">
  <div class="component-gp">
    <br>
    <form method="post" id="mainReviewForm" enctype="multipart/form-data" action=<%=process.runtime.continueURL()%>>
      <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
      <input type="hidden" name="inspecUserRecUploadType" value="">
      <input type="hidden" id="actionValue" name="actionValue" value="">
      <input type="hidden" id="fileId" name="fileId" value="">
      <div class="main-content">
        <div class="row">
          <div class="col-lg-12 col-xs-12">
            <div class="center-content">
              <div class="intranet-content">
                <iais:body >
                  <iais:section title="" id = "upload_Rectification">
                    <div class="table-gp">
                      <table class="table">
                        <thead>
                        <tr align="center">
                          <th>NC Clause</th>
                          <th>Checklist Question</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                          <td><c:out value="${inspecUserRecUploadDto.checkClause}"/></td>
                          <td><c:out value="${inspecUserRecUploadDto.checkQuestion}"/></td>
                        </tr>
                        </tbody>
                      </table>
                    </div>
                    <div class="bg-title">
                      <h3 style="margin-bottom: 0px;border-bottom: 0px solid;padding-bottom: 0px;">Attachments</h3>
                    </div>
                    <c:if test="${inspecUserRecUploadDto != null}">
                      <div>
                        <div class="row">
                          <label class="col-xs-0 col-md-6 control-label">Choose a file to attach<span style="color: red"> *</span></label>
                        </div>
                        <ul>
                          <li><span>The maximum file size for each upload is <c:out value="${inspSetMaskValueDto.sqlFileSize}"/>MB.</span></li>
                          <li><span>Acceptable file formats are <c:out value="${recFileTypeHint}"/>.</span></li>
                        </ul>
                        <iais:row>
                          <iais:value width="7">
                            <input class="selectedFile premDoc" id="recFileUpload" name = "selectedFile" type="file" onchange="javascript:doUserRecUploadConfirmFile(this.value)" style="display: none;" aria-label="selectedFile1"/>
                            <button type="button" class="btn btn-file-upload btn-secondary" onclick="javascript:doUserRecUploadConfirmUpload()">Upload</button>
                            <c:if test="${empty inspecUserRecUploadDto.fileRepoDtos}">
                              &nbsp;<label>No files selected.</label>
                            </c:if>
                            <br><span class="error-msg" name="iaisErrorMsg" id="error_recFile"></span>
                          </iais:value>
                        </iais:row>
                      </div>
                      <div class="row">
                        <div class="col-md-4">
                          <label style="margin-bottom: 0px;border-bottom: 0px solid;font-size: 1.6rem;padding-bottom: 10px;">Current File Attachments</label>
                        </div>
                      </div>
                      <iais:row>
                        <iais:value width="300">
                          <c:if test="${inspecUserRecUploadDto.fileRepoDtos != null}">
                            <c:forEach items="${inspecUserRecUploadDto.fileRepoDtos}" var="recFile" varStatus="status">
                              <a href="${pageContext.request.contextPath}/file-repo-popup?filerepo=fileRo${status.index}&fileRo${status.index}=<iais:mask name="fileRo${status.index}" value="${recFile.id}"/>&fileRepoName=${recFile.fileName}" title="Download" class="downloadFile">
                                  ${recFile.fileName}
                              </a>
                              &nbsp;<button type="button" class="btn btn-danger btn-sm" onclick="javascript:doUserRecUploadConfirmDel('<iais:mask name="fileId" value="${recFile.id}"/>')"><em class="fa fa-times"></em></button>
                              <br><br>
                            </c:forEach>
                          </c:if>
                        </iais:value>
                      </iais:row>
                    </c:if>
                    <div class="row">
                      <div class="col-md-4">
                        <h3 style="margin-bottom: 0px;border-bottom: 0px solid;">Remarks</h3>
                      </div>
                    </div>
                    <iais:row>
                      <div class="col-sm-7 col-md-6 col-xs-10" style="">
                        <textarea id="uploadRemarks" name="uploadRemarks" cols="70" rows="7" maxlength="300"><c:out value="${inspecUserRecUploadDto.uploadRemarks}"></c:out></textarea>
                        <br><span class="error-msg" name="iaisErrorMsg" id="error_remarks"></span>
                      </div>
                    </iais:row>
                    <iais:action >
                      <button class="btn btn-primary" style="float:right" type="button" onclick="javascript:doUserRecUploadConfirmSave()">Save</button>
                      <span style="float:right">&nbsp;</span>
                      <button class="btn btn-secondary" style="float:right" type="button" onclick="javascript:doUserRecUploadConfirmCancel()">Cancel</button>
                    </iais:action>
                  </iais:section>
                </iais:body>
              </div>
            </div>
          </div>
        </div>
      </div>
    </form>
  </div>
</div>
<%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
<script type="text/javascript">
    function userRecUploadConfirmSubmit(action){
        $("[name='inspecUserRecUploadType']").val(action);
        var mainPoolForm = document.getElementById('mainReviewForm');
        mainPoolForm.submit();
    }

    function doUserRecUploadConfirmCancel() {
        showWaiting();
        $("#actionValue").val('cancel');
        userRecUploadConfirmSubmit('review');
    }

    function doUserRecUploadConfirmSave() {
        showWaiting();
        $("#actionValue").val('success');
        userRecUploadConfirmSubmit('success');
    }

    function doUserRecUploadConfirmDel(fileId) {
        showWaiting();
        $("#actionValue").val('delete');
        $("#fileId").val(fileId);
        userRecUploadConfirmSubmit('delete');
    }

    function doUserRecUploadConfirmUpload() {
        showWaiting();
        $("#recFileUpload").trigger('click');
        dismissWaiting();
    }

    function doUserRecUploadConfirmFile(value) {
        showWaiting();
        $("#actionValue").val('add');
        userRecUploadConfirmSubmit('add');
    }

    function getFileName(o) {
        var pos = o.lastIndexOf("\\");
        return o.substring(pos + 1);
    }
</script>
