<%--
  Created by IntelliJ IDEA.
  User: ShiCheng_Xu
  Date: 2019/12/18
  Time: 14:37
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>

<%
  String webroot=IaisEGPConstant.BE_CSS_ROOT;
%>
<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
  <form method="post" id="mainReviewForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/include/formHidden.jsp" %>
    <br>
    <br>
    <br>
    <br>
    <br>
    <input type="hidden" name="InspectorProRectificationType" value="">
    <input type="hidden" id="actionValue" name="actionValue" value="">
    <input type="hidden" id="processDec" name="processDec" value="">

    <iais:body >
      <div class="container">
        <div class="col-xs-12">
          <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
            <h3>
              <span>Process Rectification</span>
            </h3>
            <div class="panel panel-default">
              <div class="panel-collapse collapse in" id="collapseOne" role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" style="">
                <div class="panel-body">
                  <div class="panel-main-content">
                    <iais:section title="" id = "process_Rectification">
                      <iais:row>
                        <iais:field value="Current Status"/>
                        <iais:value width="7">
                          <label><iais:code code="${inspectionPreTaskDto.appStatus}"/></label>
                        </iais:value>
                      </iais:row>
                      <iais:row>
                        <iais:field value="Remarks"/>
                        <iais:value width="300">
                          <label><c:out value="${inspectionPreTaskDto.reMarks}"></c:out></label>
                        </iais:value>
                      </iais:row>
                      <iais:row>
                        <iais:field value="Processing Decision"/>
                        <iais:value width="7">
                          <iais:select name="selectValue" options="processDecOption" firstOption="Please select" value="${inspectionPreTaskDto.selectValue}" onchange="javascript:doInspectorProRecChange(this.value)"></iais:select>
                        </iais:value>
                      </iais:row>
                      <iais:row>
                        <iais:field value="Internal Remarks"/>
                        <iais:value width="4000">
                          <textarea id="internalRemarks" name="internalRemarks" cols="70" rows="7"><c:out value="${inspectionPreTaskDto.internalMarks}"></c:out></textarea>
                          <br><span class="error-msg" name="iaisErrorMsg" id="error_internalMarks"></span>
                        </iais:value>
                      </iais:row>
                      <iais:row id="indicateCondRemarks">
                        <iais:field value="Please indicate Licensing Terms and Conditions"/>
                        <iais:value width="4000">
                          <textarea id="condRemarks" name="condRemarks" cols="70" rows="7"><c:out value="${inspectionPreTaskDto.accCondMarks}"></c:out></textarea>
                          <br><span class="error-msg" name="iaisErrorMsg" id="error_accCondMarks"></span>
                        </iais:value>
                      </iais:row>
                      <iais:action >
                        <button class="btn btn-lg btn-login-back" style="float:left" type="button" onclick="javascript:doInspectorProRecBack()">Back</button>
                        <button class="btn btn-lg btn-login-submit" style="float:right" type="button" onclick="javascript:doInspectorProRecSubmit()">Submit</button>
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
<%@ include file="/include/validation.jsp" %>
<script type="text/javascript">
    $(document).ready(function() {
        $("#indicateCondRemarks").hidden;
    })

    function submit(action){
        $("[name='InspectorProRectificationType']").val(action);
        var mainPoolForm = document.getElementById('mainReviewForm');
        mainPoolForm.submit();
    }

    function doInspectorProRecBack() {
        $("#actionValue").val('back');
        submit('back');
    }

    function doInspectorProRecChange(value) {
        $("#processDec").val(value);
        if("REDECI007" == value){
            $("#indicateCondRemarks").show();
        } else if ("REDECI007" != value){
            $("#indicateCondRemarks").hidden;
        }
    }

    function doInspectorProRecSubmit() {
        var processDec = $("#processDec").val();
        if("REDECI006" == processDec){
            $("#actionValue").val('accept');
            submit("accept");
        } else if ("REDECI001" == processDec){
            $("#actionValue").val('request');
            submit("request");
        } else if("REDECI007" == processDec) {
            $("#actionValue").val('acccond');
            submit("acccond");
        } else {
            var errMsg = 'The field is mandatory.';
            $("#error_selectValue").text(errMsg);
            dismissWaiting();
        }
    }
</script>
