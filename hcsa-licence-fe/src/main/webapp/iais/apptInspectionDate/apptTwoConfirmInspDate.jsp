<%--
  Created by IntelliJ IDEA.
  User: ShiCheng_Xu
  Date: 2020/2/16
  Time: 9:46
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
    <form method="post" id="mainReConfirmForm" action=<%=process.runtime.continueURL()%>>
      <%@ include file="/include/formHidden.jsp" %>
      <br>
      <br>
      <br>
      <br>
      <br>
      <input type="hidden" name="userComfireInspDateType" value="">
      <input type="hidden" id="actionValue" name="actionValue" value="">
      <iais:body >
        <div class="container">
          <div class="col-xs-12">
            <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
              <h3>
                <span>Recomputing of Dates</span>
              </h3>
              <div class="panel panel-default">
                <div class="panel-collapse collapse in" id="collapseOne" role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" style="">
                  <div class="panel-body">
                    <div class="panel-main-content">
                      <iais:section title="" id = "ava_apptnew_date">
                        <iais:row>
                          <iais:field value="Available Appointment Dates" required="true"/>
                          <iais:value width="7">
                            <c:if test="${apptFeConfirmDateDto.inspectionNewDate != null}">
                              <c:forEach items="${apptFeConfirmDateDto.inspectionNewDate}" var="newDate">
                                <input class="form-check-input" type="radio" name="apptCheckNewDate" aria-invalid="true" value="${newDate.value}" <c:if test="${newDate.value eq apptFeConfirmDateDto.checkNewDate}">checked</c:if>>
                                <label><c:out value = "${newDate.text}"/></label>
                                <br><span class="error-msg" name="iaisErrorMsg" id="error_checkNewDate"></span>
                              </c:forEach>
                            </c:if>
                          </iais:value>
                        </iais:row>
                        <iais:action >
                          <button class="btn btn-lg btn-login-next" style="float:right" type="button" onclick="javascript:apptTwoConfirmInspDateCon()">Confirm</button>
                          <button class="btn btn-lg btn-login-next" style="float:left" type="button" onclick="javascript:apptTwoConfirmInspDateBack()">back</button>
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
    function apptTwoConfirmInspDateSubmit(action){
        $("[name='userComfireInspDateType']").val(action);
        var mainPoolForm = document.getElementById('mainReConfirmForm');
        mainPoolForm.submit();
    }

    function apptTwoConfirmInspDateCon() {
        $("#actionValue").val('confirm');
        apptTwoConfirmInspDateSubmit('confirm');
    }

    function apptTwoConfirmInspDateBack() {
        $("#actionValue").val('back');
        apptTwoConfirmInspDateSubmit('back');
    }
</script>
