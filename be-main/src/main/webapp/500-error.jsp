<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.Formatter" %>
<%@ page import="java.util.Date" %>
<!-- MOH-IAIS -->

<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<webui:setLayout name="iais-intranet-blank"/>
<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<style type="text/css">
  .error-box {
    height: 100%;
   /* position: fixed;*/
    background:#fff;
    width: 100%;
  }
  .error-body {
    padding-top: 5%;
  }
  .error-body h1 {
    font-size: 190px;
    font-weight: 900;
    text-shadow: 4px 4px 0 #fff, 6px 6px 0 #f5333f;
    line-height: 210px;
    color: #a2d9e7;
  }
  .error-body h3 {
    border: none;
  }

</style>

    <div class="main-content">
      <div class="container">
        <div class="row">
          <div class="col-xs-12">
            <!------------------------->
            <div class="error-box">
              <div class="error-body text-center">
                 <h3 class="text-uppercase">Sorry! Internal Server Error !</h3>
                  <h1>500</h1>

                  <p class="text-muted"><strong>Error Ticket ID:</strong> ST<%=System.currentTimeMillis()%></p>
                  <p class="text-muted"><strong>Date/Time:</strong> <%=Formatter.formatDateTime(new Date(),Formatter.DETAIL_DATE_REPORT)%></p>
                  <p>For system support and assistance, please screenshot this page and email to <a href="mailto:helpdesk@equaria.gov.sg">helpdesk@equaria.gov.sg</a>.</p>
              </div>
            </div>
            <!------------------------->
          </div>
        </div>
      </div>
    </div>


