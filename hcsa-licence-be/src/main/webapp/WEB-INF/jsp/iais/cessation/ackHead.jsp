<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%
    String webroot1=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.FE_CSS_ROOT;
%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<div class="dashboard" id="dashboard" style="background-image:url('<%=webroot1%>img/Masthead-banner.jpg')">
    <div style="margin-left: 60px">
        <div class="navigation-gp">
            <div class="row">
                <webui:setLayout name="iais-intranet"/>
                <div class="col-xs-12">
                    <div class="dashboard-page-title">
                        <div class="col-xs-12">
                            <br/><br/>
                            <h1 class="font-weight 0">Acknowledgement</h1>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<style>
    .mandatory{
        color: rgb(255,0,0);
    }
    .app-font-size-16{
        font-size: 16px;
    }
</style>