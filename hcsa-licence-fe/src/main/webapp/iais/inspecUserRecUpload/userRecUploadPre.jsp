<%--
  Created by IntelliJ IDEA.
  User: ShiCheng_Xu
  Date: 2019/12/23
  Time: 14:37
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
    <form method="post" id="mainReviewForm" action=<%=process.runtime.continueURL()%>>
      <%@ include file="/include/formHidden.jsp" %>
      <br>
      <br>
      <br>
      <br>
      <br>
      <input type="hidden" name="inspecUserRecUploadType" value="">
      <input type="hidden" id="actionValue" name="actionValue" value="">
      <iais:body >
        <div class="container">
          <div class="col-xs-12">
            <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
              <h3>
                <span>User Rectification Upload</span>
              </h3>
              <div class="panel panel-default">
                <div class="panel-collapse collapse in" id="collapseOne" role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" style="">
                  <div class="panel-body">
                    <div class="panel-main-content">
                      <iais:section title="" id = "upload_Rectification">
                        <c:if test="${inspecUserRecUploadDtos != null}">
                          <c:forEach var="recCheck" items="${inspecUserRecUploadDtos}">
                            <c:set var="delFlag" value="delFlag${recCheck.index}" />
                            <c:set var="selectedFile" value="selectedFile${recCheck.index}"/>
                            <c:set var="uploadRemarks" value="uploadRemarks${recCheck.index}"/>
                            <iais:row>
                              <iais:field value="Checklist Question"/>
                              <iais:value width="7">
                                <label><c:out value="${recCheck.checklistItem}"></c:out></label>
                              </iais:value>
                            </iais:row>
                            <iais:row>
                              <iais:field value="NC Clause"/>
                              <iais:value width="300">
                                <label><c:out value="${recCheck.regulationClause}"></c:out></label>
                              </iais:value>
                            </iais:row>
                            <iais:row>
                              <iais:field value="Attachment"/>
                              <iais:value width="7">
                                <div class="document-upload-list">
                                  <div class="file-upload-gp">
                                    <input class="hidden delFlag" type="hidden" name="${delFlag}" value="N"/>
                                    <span>${recCheck.fileName}</span>
                                    <c:choose>
                                      <c:when test="${recCheck.fileName == '' || recCheck.fileName == null }">
                                        <span class="hidden delBtn">
                                    &nbsp;&nbsp;  <button type="button" class="">Delete</button>
                                        </span>
                                      </c:when>
                                      <c:otherwise>
                                        <span class="delBtn">
                                      &nbsp;&nbsp;<button type="button" class="">Delete</button>
                                        </span>
                                      </c:otherwise>
                                    </c:choose>
                                    <br/>
                                    <input class="selectedFile premDoc" name = "${selectedFile}" type="file" style="display: none;" aria-label="selectedFile1" />
                                    <a class="btn btn-file-upload btn-secondary" >Upload</a>
                                    <br><span class="error-msg" name="iaisErrorMsg" id="error_recFile${recCheck.index}"></span>
                                  </div>
                                </div>
                              </iais:value>
                            </iais:row>
                            <iais:row>
                              <iais:field value="Remarks"/>
                              <iais:value width="300">
                                <textarea id="${uploadRemarks}" name="${uploadRemarks}" cols="70" rows="7"><c:out value="${recCheck.uploadRemarks}"></c:out></textarea>
                              </iais:value>
                            </iais:row>
                          </c:forEach>
                        </c:if>
                        <iais:action >
                          <button class="btn btn-lg btn-login-back" style="float:left" type="button" onclick="javascript:doUserRecUploadBack()">Back</button>
                          <button class="btn btn-lg btn-login-next" style="float:right" type="button" onclick="javascript:doUserRecUploadNext()">Next</button>
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
  function userRecUploadSubmit(action){
      $("[name='inspecUserRecUploadType']").val(action);
      var mainPoolForm = document.getElementById('mainReviewForm');
      mainPoolForm.submit();
  }

  function doUserRecUploadBack() {
      $("#actionValue").val('back');
      userRecUploadSubmit('back');
  }

  function doUserRecUploadNext() {
      $("#actionValue").val('confirm');
      userRecUploadSubmit('confirm');
  }

  function getFileName(o) {
      var pos = o.lastIndexOf("\\");
      return o.substring(pos + 1);
  }

  $('.selectedFile').change(function () {
      var file = $(this).val();
      $(this).parent().children('span:eq(0)').html(getFileName(file));
      $(this).parent().children('span:eq(0)').next().removeClass("hidden");
      $(this).parent().children('input delFlag').val('N');
  });

  $('.delBtn').click(function () {
      $(this).parent().children('span:eq(0)').html('');
      $(this).parent().children('span:eq(0)').next().addClass("hidden");
      $(this).parent().children('input.selectedFile').val('');
      $(this).parent().children('input.delFlag').val('Y');

  });

</script>
