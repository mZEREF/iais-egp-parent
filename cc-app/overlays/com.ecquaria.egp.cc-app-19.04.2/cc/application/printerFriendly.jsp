<%@page import="com.ecquaria.cloud.mc.api.MCPageTitleHelper"%>
<%@page import="com.ecquaria.cloud.mc.application.Application"%>
<%@page import="com.ecquaria.cloud.mc.common.constants.AppConstants"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.util.Locale" %>
<%@ page import="org.springframework.web.servlet.i18n.SessionLocaleResolver" %>
<%@ page import="com.ecquaria.cloud.mc.api.ConsistencyHelper" %>
<%@ page import="com.ecquaria.cloud.helper.ConfigHelper" %>
<%@ page import="sop.i18n.MultiLangUtil" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="ecquaria/sop/sop-smc" prefix="sop-smc"%>
<%@page import="ecq.commons.util.EgpcloudPortFactory"%>


<%
  response.setContentType("text/html;charset=UTF-8");
  String title = MCPageTitleHelper.getMCPageTitle();
  pageContext.setAttribute("transTitle", title);
%>
<webui:setAttribute name="title">
  <c:out value="${transTitle }"></c:out>
</webui:setAttribute>

<script type="text/javascript">
	$(function(){
		window.print();
	});
	
</script>
<webui:setLayout name="printerfriendly"/>
<%
	Application app = (Application)request.getAttribute("entity");
  Locale locale= MultiLangUtil.getSiteLocale();
  if (locale==null)
    locale=AppConstants.DEFAULT_LOCAL;
  String submittedDate = ConsistencyHelper.formatDateTime(app.getSubmittedDate());

%>
<style>
  .emailfix {
    word-break: break-all;
  }
</style>

<div class="navbar navbar-default" role="navigation">
  <div class="container">
    <div class="navbar-header">
      <a class="navbar-brand" href="#"><img src="<%=EgpcloudPortFactory.webContext%>/_themes/cc/img/bn-citilogo.png" alt="<egov-smc:commonLabel>Welcome</egov-smc:commonLabel>"><egov-smc:commonLabel>Citizen Centre</egov-smc:commonLabel></a>
    </div>
  </div>
</div>
<!-- // Fixed navbar -->

<div class="container">

<!-- begin Nav Tabs -->
<div >

        <!-- Form Section -->
        <section class="form-wrap">
          <!-- form header -->
            <div class="formtitle">
              <h4><egov-smc:commonLabel><c:out value="${entity.serviceName }"></c:out></egov-smc:commonLabel></h4>
            </div>
          <!-- form content -->
          <div class="form-area">
          <!--  -->
            <article>
              <table class="table table-striped table-condensed">
                <tr>
                  <td><egov-smc:commonLabel>Application No.</egov-smc:commonLabel>:</td>
                  <td class="emailfix"><c:out value="${entity.no }"/></td>
                </tr>
                <tr>
                  <td><egov-smc:commonLabel>Submitted Date/Time</egov-smc:commonLabel>:</td>
                  <td><%=submittedDate %></td>
                </tr>
                <tr>
                  <td><egov-smc:commonLabel>Status</egov-smc:commonLabel>:</td>
                  <td><egov-smc:commonLabel><c:out value="${entity.status }"/></egov-smc:commonLabel></td>
                </tr>
                <tr>
                  <td><egov-smc:commonLabel>Service</egov-smc:commonLabel>:</td>
                  <td class="emailfix"><egov-smc:commonLabel><c:out value="${entity.serviceName }"/></egov-smc:commonLabel></td>
                </tr>
                <tr>
                  <td><egov-smc:commonLabel>Agency</egov-smc:commonLabel>:</td>
                  <td><c:out value="${entity.agencyName }"/></td>
                </tr>
              </table>  
            </article>
          <!-- Applicant -->
            <article>
              <h3><egov-smc:commonLabel>Applicant Particular</egov-smc:commonLabel></h3>
              <table class="table table-striped table-condensed">
                <tr>
                  <td><egov-smc:commonLabel>Applicant ID</egov-smc:commonLabel>:</td>
                  <td><c:out value="${entity.applicantId }"/></td>
                </tr>
                <tr>
                  <td><egov-smc:commonLabel>Name</egov-smc:commonLabel>:</td>
                  <td><c:out value="${entity.applicantName }"/></td>
                </tr>
                <tr>
                  <td><egov-smc:commonLabel>Address</egov-smc:commonLabel>:</td>
                  <td><c:out value="${entity.applicantAddress }"/></td>
                </tr>
                <tr>
                  <td><egov-smc:commonLabel>Mailing Address</egov-smc:commonLabel>:</td>
                  <td><c:out value="${entity.applicantMailAddress }"/></td>
                </tr>
                <tr>
                  <td><egov-smc:commonLabel>Email</egov-smc:commonLabel>:</td>
                  <td class="emailfix"><c:out value="${entity.applicantEmail }"/></td>
                </tr>
                <tr>
                  <td><egov-smc:commonLabel>Mobile No.</egov-smc:commonLabel>:</td>
                  <td><c:out value="${entity.applicantMobile}"/></td>
                </tr>
                <tr>
                  <td><egov-smc:commonLabel>Telephone No.</egov-smc:commonLabel>:</td>
                  <td><c:out value="${entity.applicantTel}"/></td>
                </tr>
              </table>  
            </article>

            <!-- Submitter -->
              <article>
              <h3><egov-smc:commonLabel>Submitter Particular</egov-smc:commonLabel></h3>
              <table class="table table-striped table-condensed">
                <tr>
                  <td><egov-smc:commonLabel>Submitter ID</egov-smc:commonLabel>:</td>
                  <td><c:out value="${entity.submitterId }"/></td>
                </tr>
                <tr>
                  <td><egov-smc:commonLabel>Name</egov-smc:commonLabel>:</td>
                  <td><c:out value="${entity.submitterName }"/></td>
                </tr>
                <tr>
                  <td><egov-smc:commonLabel>Address</egov-smc:commonLabel>:</td>
                  <td><c:out value="${entity.submitterAddress }"/></td>
                </tr>
                <tr>
                  <td><egov-smc:commonLabel>Mailing Address</egov-smc:commonLabel>:</td>
                  <td><c:out value="${entity.submitterMailAddress }"/></td>
                </tr>
                <tr>
                  <td><egov-smc:commonLabel>Email</egov-smc:commonLabel>:</td>
                  <td class="emailfix"><c:out value="${entity.submitterEmail }"/></td>
                </tr>
                <tr>
                  <td><egov-smc:commonLabel>Mobile No.</egov-smc:commonLabel>:</td>
                  <td><c:out value="${entity.submitterMobile }"/></td>
                </tr>
                <tr>
                  <td><egov-smc:commonLabel>Telephone No.</egov-smc:commonLabel>:</td>
                  <td><c:out value="${entity.submitterTel }"/></td>
                </tr>
              </table>  
            </article>


          </div>
          
        </section>
        <!-- // Form Section -->

  </div>
<!--  -->

</div>
<!-- // container -->


<!-- Footer -->
  <div id="footer">
      <div class="container">
          <%
              Calendar ca = Calendar.getInstance();
              int year = ca.get(Calendar.YEAR);
          %>
          <p class="copyright text-center"><egov-smc:commonLabel>Copyright</egov-smc:commonLabel> &copy; <%=year%> <egov-smc:commonLabel>Ecquaria Technologies Pte Ltd.</egov-smc:commonLabel> <egov-smc:commonLabel>All Rights Reserved.</egov-smc:commonLabel></p>
      </div>
  </div>
<!-- // Footer -->