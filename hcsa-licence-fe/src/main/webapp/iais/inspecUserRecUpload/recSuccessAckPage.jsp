<%--
  Created by IntelliJ IDEA.
  User: ShiCheng_Xu
  Date: 2020/4/3
  Time: 13:31
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
<%@include file="../common/dashboard.jsp"%>
<form method="post" id="mainRecAckForm" action=<%=process.runtime.continueURL()%>>
  <div class="main-content">
    <div class="row">
      <div class="col-lg-12 col-xs-12">
        <div class="center-content">
          <div class="intranet-content">
            <iais:body >
              <iais:section title="" id = "rec_ack_page">
                <div class="bg-title">
                  <h3 style="border-bottom: 0px solid">Submission successful</h3>
                </div>
                <iais:row>
                  <iais:value width="7">
                    <p><label>Congratulations, you have successfully submitted all your NC Rectifications to MOH.</label></p>
                  </iais:value>
                </iais:row>
                <iais:action >
                  <p class="print">
                    <a href="#" id="print-ack"> <em class="fa fa-print"></em>Print</a>
                    <a class="btn btn-primary" style="float:right" href="/main-web/eservice/INTERNET/MohInternetInbox" >Go to Dashboard</a>
                  </p>
                </iais:action>
              </iais:section>
            </iais:body>
          </div>
        </div>
      </div>
    </div>
  </div>
</form>
<script type="text/javascript">
    $("#print-ack").click(function () {
        window.print();
    })
</script>
