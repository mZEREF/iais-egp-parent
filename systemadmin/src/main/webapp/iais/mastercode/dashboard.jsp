<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    String webroot=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.FE_CSS_ROOT;
%>
<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
    <div class="container">
        <div class="navigation-gp">
            <div class="row">
                <div class="col-xs-10 col-xs-offset-1 col-lg-offset-9 col-lg-3">
                    <div class="dropdown profile-dropdown"><a class="profile-btn btn" id="profileBtn" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" href="javascript:;">Tan Mei Ling Joyce</a>
                        <ul class="dropdown-menu" aria-labelledby="profileBtn">
                            <li class="dashboard-icon"><a href="#">Dashboard</a></li>
                            <li class="management-account"><a href="#">Manage Account</a></li>
                            <li class="logout"><a href="#">Logout</a></li>
                        </ul>
                    </div>
                </div>
                <div class="col-xs-12">
                    <div class="dashboard-page-title">
                        <h1>New Licence Application</h1>
                        <h3>You are applying for <b>Clinical Laboratory</b> | <b>Blood Banking</b></h3>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>