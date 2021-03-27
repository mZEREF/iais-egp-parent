<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.UrlConfig" %>
<%@ page import="com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
    String webrooth=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.BE_CSS_ROOT;
    String intranetWebSite = UrlConfig.getInstance().getIntranetWebSite();
    String intranetInbox = UrlConfig.getInstance().getIntranetInbox();
//      if(!IaisEGPHelper.isLogin()){
//          intranetInbox= intranetWebSite;
//      }
%>
<header>

    <div class="container">
        <div class="row">
            <div class="col-xs-12 col-lg-6">
                <div class="logo-img">
                    <button type="button" id="sidebarCollapse" class="btn btn-info navbar-btn">
                        <em class="fa fa-bars"></em>
                    </button>
                    <a href="<%=intranetWebSite%>"><img src="<%=webrooth%>img/moh-logo.svg" alt="Ministry of Health" width="235" height="64"></a>
                        <a href="<%=intranetInbox%>"><p class="logo-img"><img src="<%=webrooth%>img/HALP-log.png" alt="Healthcare Application and Licensing Portal" width="235" height="64"></p></a>

                </div>
            </div>
            <div class="col-xs-2 col-lg-6 text-right hidden-sm hidden-xs">
                <span>You are here:</span>
                <div class="page-breadcrumb">
                    <ol class="breadcrumb">
                       <!-- <li><a href="/main-web/eservice/INTRANET/MohBackendInbox">Home</a></li> -->
                        <li><a href="#"><c:out value="${iais_Audit_Trail_dto_Attr.functionName}"/></a></li>
                        <li class="active">Processing Page</li>
                    </ol>
                </div>
            </div>
        </div>
    </div>
</header>
<script type="text/javascript">
    $(document).ready(function () {
        $('#sidebarCollapse').on('click', function () {
            $('#sidebar').toggleClass('active');
        });
    });
</script>