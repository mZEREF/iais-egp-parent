<%--
  Created by IntelliJ IDEA.
  User: ShiCheng_Xu
  Date: 2019/12/18
  Time: 14:37
  To change this template use File | Settings | File Templates.
--%>
<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
  <form method="post" id="mainReviewForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/include/formHidden.jsp" %>
    <br>
    <br>
    <br>
    <br>
    <br>
    <input type="hidden" name="inspectorPreType" value="">
    <input type="hidden" id="taskId" name="taskId" value="">
    <input type="hidden" id="actionValue" name="actionValue" value="">
    <input type="hidden" id="processDec" name="processDec" value="">

    <iais:body >
      <div class="container">
        <div class="col-xs-12">
          <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
            <h3>
              <span>Review Task</span>
            </h3>
            <div class="panel panel-default">
              <div class="panel-collapse collapse in" id="collapseOne" role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" style="">
                <div class="panel-body">
                  <div class="panel-main-content">
                    <iais:section title="" id = "review_Task">
                      <iais:row>
                        <iais:field value="Current Status"/>
                        <iais:value width="7">
                          <label><iais:code code="${inspectionPreTaskDto.appStatus}"/></label>
                        </iais:value>
                      </iais:row>
                      <iais:row>
                        <iais:field value="Remarks"/>
                        <iais:value width="300">
                          <textarea id="preInspecRemarks" name="preInspecRemarks" cols="70" rows="7" ><c:out value="${inspectionPreTaskDto.reMarks}"></c:out></textarea>
                          <br><span class="error-msg" name="iaisErrorMsg" id="error_reMarks"></span>
                        </iais:value>
                      </iais:row>
                      <iais:row>
                        <iais:field value="Processing Decision"/>
                        <iais:value width="7">
                          <iais:select name="selectValue" options="processDecOption" firstOption="Please select" value="${inspectionPreTaskDto.selectValue}" onchange="javascript:doInspectionPreTaskChange(this.value)"></iais:select>
                        </iais:value>
                      </iais:row>
                      <iais:action >
                        <button class="btn btn-lg btn-login-back" style="float:left" type="button" onclick="javascript:doInspectionPreTaskBack()">Back</button>
                        <button class="btn btn-lg btn-login-submit" style="float:right" type="button" onclick="javascript:doInspectionPreTaskSubmit()">Submit</button>
                        <span style="float:right">&nbsp;</span>
                        <button class="btn btn-lg btn-login-edit" style="float:right" type="button" onclick="javascript:doInspectionPreTaskEdit('<iais:mask name="taskId" value="${taskDto.id}"/>');">Edit</button>
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
    function submit(action){
        $("[name='inspectorPreType']").val(action);
        var mainPoolForm = document.getElementById('mainReviewForm');
        mainPoolForm.submit();
    }

    function doInspectionPreTaskEdit(taskId) {
        $("#taskId").val(taskId);
        $("#actionValue").val('edit');
        submit('edit');
    }

    function doInspectionPreTaskBack() {
        $("#actionValue").val('back');
        submit('back');
    }

    function doInspectionPreTaskChange(value) {
        $("#processDec").val(value);
    }

    function doInspectionPreTaskSubmit() {
        var actionValue = $("#processDec").val();
        if("REDECI002" == actionValue){
            $("#actionValue").val('approve');
            submit("approve");
        } else if ("REDECI001" == actionValue){
            $("#actionValue").val('routeB');
            submit("routeB");
        } else {
            var errMsg = 'The field is mandatory.';
            $("#error_selectValue").text(errMsg);
            dismissWaiting();
        }
    }
</script>
