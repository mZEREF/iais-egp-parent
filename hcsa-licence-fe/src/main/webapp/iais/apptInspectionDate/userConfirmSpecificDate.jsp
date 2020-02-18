<%--
  Created by IntelliJ IDEA.
  User: ShiCheng_Xu
  Date: 2020/2/17
  Time: 16:45
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
    <form method="post" id="mainConfirmForm" action=<%=process.runtime.continueURL()%>>
      <%@ include file="/include/formHidden.jsp" %>
      <br>
      <br>
      <br>
      <br>
      <br>
      <input type="hidden" name="userConfirmSpecificDateType" value="">
      <input type="hidden" id="actionValue" name="actionValue" value="">
      <iais:body >
        <div class="container">
          <div class="col-xs-12">
            <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
              <h3>
                <span>Inspector Assigns Specific Date</span>
              </h3>
              <div class="panel panel-default">
                <div class="panel-collapse collapse in" id="collapseOne" role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" style="">
                  <div class="panel-body">
                    <div class="panel-main-content">
                      <iais:section title="" id = "ava_appt_date">
                        <iais:row>
                          <iais:field value="Application No"/>
                          <iais:value width="7">
                            <label><c:out value = "${apptFeConfirmDateDto.applicationNo}"/></label>
                          </iais:value>
                        </iais:row>
                        <iais:row>
                          <iais:field value="Application Type"/>
                          <iais:value width="7">
                            <label><iais:code code = "${apptFeConfirmDateDto.applicationType}"/></label>
                          </iais:value>
                        </iais:row>
                        <iais:row>
                          <iais:field value="Licence Period"/>
                          <iais:value width="7">
                            <label><c:out value = "${apptFeConfirmDateDto.licencePeriod}"/></label>
                          </iais:value>
                        </iais:row>
                        <iais:row>
                          <iais:field value="Date"/>
                          <iais:value width="7">
                            <label><c:out value = "${apptFeConfirmDateDto.specificDateShow}"/></label>
                          </iais:value>
                        </iais:row>
                        <iais:action >
                          <button class="btn btn-lg btn-login-next" style="float:right" type="button" onclick="javascript:userConfirmSpecificDateAccept()">Accept</button>
                          <span style="float:right">&nbsp;</span>
                          <button class="btn btn-lg btn-login-next" style="float:right" type="button" onclick="javascript:userConfirmSpecificDateReject()">Reject</button>
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
<script type="text/javascript">
    function userConfirmSpecificDateSubmit(action){
        $("[name='userConfirmSpecificDateType']").val(action);
        var mainPoolForm = document.getElementById('mainConfirmForm');
        mainPoolForm.submit();
    }

    function userConfirmSpecificDateReject() {
        $("#actionValue").val('reject');
        alert("We note that the date assigned for inspection is unsuitable. Please contact an MOH officer @ <Tel. No.>* to arrange a suitable appointment date.");
        userConfirmSpecificDateSubmit('reject');
    }

    function userConfirmSpecificDateAccept() {
        $("#actionValue").val('confirm');
        userConfirmSpecificDateSubmit('confirm');
    }
</script>