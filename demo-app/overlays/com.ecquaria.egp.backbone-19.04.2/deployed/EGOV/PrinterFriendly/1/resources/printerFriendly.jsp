<%@page import="com.ecquaria.cloud.helper.EngineHelper"%>
<%@page import="com.ecquaria.cloud.helper.JsonHelper"%>
<%@page import="org.apache.commons.codec.binary.Base64"%>
<%@page import="ecq.commons.helper.StringHelper"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="sop.util.SysteminfoUtil"%>
<%@ page import="sop.i18n.MultiLangUtil" %>
<%@ page import="com.ecquaria.egov.core.common.constants.AppConstants" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="ecquaria/sop/sop-core" prefix="sop-core"%>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov"%>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc"%>

<webui:setLayout name="printerfriendly"/>
<%
    response.setContentType("text/html;charset=UTF-8");
	String content = request.getParameter("content");
	Map contentMap;
	if (content != null) {
		String decodeContent = new String(Base64.decodeBase64(content.getBytes()),"UTF-8");
		contentMap = JsonHelper.parseJson(decodeContent); 
	}else {
		contentMap = new HashMap();
	}
	 
	String title = (String)contentMap.get("title");
	if (StringHelper.isEmpty(title)) {
		title = "";
	}
	

%>



<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset="UTF-8" />
<link rel="shortcut icon" href="<%=EngineHelper.getResourcePath() %>/_statics/images/ecq.ico" type="image/x-icon" />
<link href="<%=EngineHelper.getResourcePath() %>/sample/printer/bootstrap.min.css" rel="stylesheet">
<link href="<%=EngineHelper.getResourcePath() %>/sample/printer/custom.css" rel="stylesheet">
<link href="<%=EngineHelper.getResourcePath() %>/sample/printer/ecit-style.css" rel="stylesheet">
<link href="<%=EngineHelper.getResourcePath() %>/sample/printer/print.css" rel="stylesheet"/>
    <script type="text/javascript" src="<%=EngineHelper.getResourcePath()%>/javascripts/egov/jquery-3.3.1.min.js"></script>
    <script type="text/javascript" src="<%=EngineHelper.getResourcePath()%>/javascripts/egov/jquery-migrate.1.4.1.js"></script>
    <script type="text/javascript" src="<%=EngineHelper.getResourcePath()%>/javascripts/egov/jquery-migrate.3.0.0.js"></script>


<title><egov-smc:commonLabel>Ecquaria Government Platform</egov-smc:commonLabel></title>
<script type="text/javascript">
	$(function(){
		window.print();
	});
</script>
    <style>
        .navbar-default{
            height: unset !important;
        }
        .navbar-brand{
            height: unset !important;
            margin-bottom: 10px;
         }
    </style>
</head>
<body>
<div class="navbar navbar-default" role="navigation">
  <div class="container">
    <div class="navbar-header">
      <a class="navbar-brand" href="#"><img src="<%=EngineHelper.getResourcePath() %>/sample/saas/assets/img/ecq-logo.png" alt="Welcome"><egov-smc:commonLabel>Ecquaria Government Platform</egov-smc:commonLabel></a>
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
              <h4><%=title%></h4>
            </div>
          <!-- form content -->
          <div class="form-area">
          <!--  -->
            <article>
              <table class="table table-striped table-condensed">
              
              <%
              Map detailMap = (Map)contentMap.get("content");
              if (detailMap != null) {
            	  for (Object key :  detailMap.keySet()) {
            		  Object value = detailMap.get(key);
            		  %>
	           		    <tr>
		                  <td><%=key %></td>
		                  <td><%=value %></td>
		                </tr>
            		  <%
            	  }
              }
              %>
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
      <p class="copyright text-center"><egov:commonLabel><%=SysteminfoUtil.getCopyright() %></egov:commonLabel></p>
    </div>
  </div>
<!-- // Footer -->



</body>
</html>



