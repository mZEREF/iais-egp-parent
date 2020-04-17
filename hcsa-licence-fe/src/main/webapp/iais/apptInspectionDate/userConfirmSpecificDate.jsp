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
      <input type="hidden" name="userConfirmSpecificDateType" value="">
      <input type="hidden" id="actionValue" name="actionValue" value="">
      <div class="main-content">
        <div class="row">
          <div class="col-lg-12 col-xs-12">
            <div class="center-content">
              <div class="intranet-content">
                <iais:body>
                  <c:if test="${'SUCCESS' eq apptInspFlag}">
                    <iais:section title="" id = "rec_ack_page">
                      <div class="bg-title">
                        <h3 style="border-bottom: 0px solid">Submission successful</h3>
                      </div>
                      <iais:row>
                        <iais:value width="7">
                          <p><label>Congratulations, you have successfully submitted to MOH.</label></p>
                        </iais:value>
                      </iais:row>
                      <iais:action >
                        <p class="print">
                          <a href="#" id="print-ack"> <em class="fa fa-print"></em>Print</a>
                          <a class="btn btn-primary" style="float:right" href="/main-web/eservice/INTERNET/MohInternetInbox" >Go to Dashboard</a>
                        </p>
                      </iais:action>
                    </iais:section>
                  </c:if>
                  <c:if test="${'SUCCESS' ne apptInspFlag}">
                    <iais:section title="" id = "ava_appt_date">
                      <iais:row>
                        <iais:field value="Application No"/>
                        <iais:value width="7">
                          <c:forEach items="${apptFeConfirmDateDto.applicationDtos}" var="app">
                            <label><c:out value = "${app.applicationNo}"/></label><br>
                          </c:forEach>
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
                          <c:forEach items="${apptFeConfirmDateDto.licencePeriods}" var="lic">
                            <label><c:out value = "${lic}"/></label><br>
                          </c:forEach>
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
                  </c:if>
                </iais:body>
              </div>
            </div>
          </div>
        </div>
      </div>
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
        showWaiting();
        $("#actionValue").val('reject');
        alert("We note that the date assigned for inspection is unsuitable. Please contact an MOH officer @ <Tel. No.>* to arrange a suitable appointment date.");
        userConfirmSpecificDateSubmit('reject');
    }

    function userConfirmSpecificDateAccept() {
        showWaiting();
        $("#actionValue").val('confirm');
        userConfirmSpecificDateSubmit('confirm');
    }
</script>