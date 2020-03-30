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
      <%@ include file="/include/formHidden.jsp" %>
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
                      <h3 style="border-bottom: 0px solid">Attachments</h3>
                    </div>
                    <div style="padding:10px">
                      <c:if test="${inspecUserRecUploadDto != null}">
                        <div style="padding:10px">
                          <iais:row>
                            <iais:field value="Choose a file to attach"/>
                          </iais:row>
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
                        <iais:row>
                          <iais:field value="Current File Attachments"/>
                        </iais:row>
                        <iais:row>
                          <iais:value width="300">
                            <c:if test="${inspecUserRecUploadDto.fileRepoDtos != null}">
                              <c:forEach items="${inspecUserRecUploadDto.fileRepoDtos}" var="recFile" varStatus="status">
                                <a href="${pageContext.request.contextPath}/file-repo-popup?filerepo=fileRo${status.index}&fileRo${status.index}=<iais:mask name="fileRo${status.index}" value="${recFile.id}"/>&fileRepoName=${recFile.fileName}" title="Download" class="downloadFile">
                                    ${recFile.fileName}
                                </a>
                                &nbsp;<button type="button" class="btn btn-danger btn-sm" onclick="javascript:doUserRecUploadConfirmDel('<iais:mask name="fileId" value="${recFile.id}"/>')"><i class="fa fa-times"></i></button>
                                <br><br>
                              </c:forEach>
                            </c:if>
                            <label id="recFileName"></label>
                          </iais:value>
                        </iais:row>
                      </c:if>
                      <div class="bg-title">
                        <iais:row>
                          <h3 style="border-bottom: 0px solid">Remarks</h3>
                        </iais:row>
                        <iais:row>
                          <iais:value width="300" style="padding-left:0px">
                            <textarea id="uploadRemarks" name="uploadRemarks" cols="70" rows="7" maxlength="300"><c:out value="${inspecUserRecUploadDto.uploadRemarks}"></c:out></textarea>
                            <br><span class="error-msg" name="iaisErrorMsg" id="error_remarks"></span>
                          </iais:value>
                        </iais:row>
                      </div>
                    </div>
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
<%@ include file="/include/validation.jsp" %>
<script type="text/javascript">
    function userRecUploadConfirmSubmit(action){
        $("[name='inspecUserRecUploadType']").val(action);
        var mainPoolForm = document.getElementById('mainReviewForm');
        mainPoolForm.submit();
    }

    function doUserRecUploadConfirmCancel() {
        $("#actionValue").val('review');
        userRecUploadConfirmSubmit('review');
    }

    function doUserRecUploadConfirmSave() {
        $("#actionValue").val('success');
        userRecUploadConfirmSubmit('success');
    }

    function doUserRecUploadConfirmDel(fileId) {
        $("#actionValue").val('delete');
        $("#fileId").val(fileId);
        userRecUploadConfirmSubmit('delete');
    }

    function doUserRecUploadConfirmUpload() {
        $("#recFileUpload").trigger('click');
    }

    function doUserRecUploadConfirmFile(value) {
        $("#actionValue").val('add');
        userRecUploadConfirmSubmit('add');
    }

    function getFileName(o) {
        var pos = o.lastIndexOf("\\");
        return o.substring(pos + 1);
    }


</script>
