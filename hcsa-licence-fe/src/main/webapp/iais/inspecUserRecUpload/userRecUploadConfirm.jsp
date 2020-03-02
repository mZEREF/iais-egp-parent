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
      <br>
      <br>
      <br>
      <br>
      <br>
      <input type="hidden" name="inspecUserRecUploadType" value="">
      <input type="hidden" id="actionValue" name="actionValue" value="">
      <input type="hidden" id="fileId" name="fileId" value="">
      <iais:body >
        <div class="container">
          <div class="col-xs-12">
            <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
              <h3>
                <span>Attachments</span>
              </h3>
              <div class="panel panel-default">
                <div class="panel-collapse collapse in" id="collapseOne" role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" style="">
                  <div class="panel-body">
                    <div class="panel-main-content">
                      <iais:section title="" id = "upload_Rectification">
                        <c:if test="${inspecUserRecUploadDto != null}">
                          <iais:row>
                            <iais:field value="NC Clause"/>
                            <iais:value width="300">
                              <label><c:out value = "${inspecUserRecUploadDto.checkClause}"></c:out></label>
                            </iais:value>
                          </iais:row>
                          <iais:row>
                            <iais:field value="Checklist Question"/>
                            <iais:value width="7">
                              <label><c:out value = "${inspecUserRecUploadDto.checkQuestion}"/></label>
                            </iais:value>
                          </iais:row>
                          <iais:row>
                            <iais:field value="Choose a file to attach"/>
                            <iais:value width="7">
                              <input class="selectedFile premDoc" id="recFileUpload" name = "selectedFile" type="file" onchange="javascript:doUserRecUploadConfirmFile(this.value)" style="display: none;" aria-label="selectedFile1"/>
                              <button type="button" class="btn btn-default btn-sm" onclick="javascript:doUserRecUploadConfirmUpload()">Upload</button>
                              <br><span class="error-msg" name="iaisErrorMsg" id="error_recFile"></span>
                            </iais:value>
                          </iais:row>
                          <iais:row>
                            <iais:field value="Current File Attachments"/>
                            <iais:value width="300">
                              <c:if test="${inspecUserRecUploadDto.fileRepoDtos != null}">
                                <c:forEach items="${inspecUserRecUploadDto.fileRepoDtos}" var="recFile">
                                  <label><c:out value="${recFile.fileName}"></c:out></label><button type="button" class="btn btn-default btn-sm" onclick="javascript:doUserRecUploadConfirmDel('<iais:mask name="fileId" value="${recFile.id}"/>')">Delete</button><br>
                                </c:forEach>
                              </c:if>
                              <label id="recFileName"></label>
                            </iais:value>
                          </iais:row>
                          <iais:row>
                            <iais:field value="Remarks"/>
                            <iais:value width="300">
                              <textarea id="uploadRemarks" name="uploadRemarks" cols="70" rows="7"><c:out value="${inspecUserRecUploadDto.uploadRemarks}"></c:out></textarea>
                            </iais:value>
                          </iais:row>
                        </c:if>
                        <iais:action >
                          <button class="btn btn-lg btn-login-back" style="float:right" type="button" onclick="javascript:doUserRecUploadConfirmCancel()">Cancel</button>
                          <span style="float:right">&nbsp;</span>
                          <button class="btn btn-lg btn-login-next" style="float:right" type="button" onclick="javascript:doUserRecUploadConfirmSave()">Save</button>
                        </iais:action>
                      </iais:section>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </iais:body>
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
