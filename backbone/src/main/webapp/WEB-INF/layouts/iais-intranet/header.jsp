
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    String webrooth=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.BE_CSS_ROOT;
%>
<header>
    <div class="container">
        <div class="row">
            <div class="col-xs-12 col-lg-6">
                <div class="logo-img">
                    <button type="button" id="sidebarCollapse" class="btn btn-info navbar-btn">
                        <em class="fa fa-bars"></em>
                    </button>
                    <a href="#"><img src="<%=webrooth%>img/moh-logo.svg" alt="Ministry of Health" width="235" height="64">
                        <p class="logo-txt">Integrated Application and Inspection System</p></a>

                </div>
            </div>
            <div class="col-xs-2 col-lg-6 text-right hidden-sm hidden-xs">
                <span>You are here:</span>
                <div class="page-breadcrumb">
                    <ol class="breadcrumb">
                        <li><a href="#">Home</a></li>
                        <li><a href="#">Layouts</a></li>
                        <li class="active">Blank Page</li>
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