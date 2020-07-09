<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.UrlConfig" %>
<%
    String webrooth=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.FE_CSS_ROOT;
    String internetWebSite = UrlConfig.getInstance().getInternetWebSite();
    String internetInbox = UrlConfig.getInstance().getInternetInbox();
%>
<header>

    <div class="container">
        <div class="row">
            <div class="col-xs-10 col-lg-6">
                <div class="logo-img"><a href="<%=internetWebSite%>"><img src="<%=webrooth%>img/moh-logo.svg" alt="Ministry of Health" width="235" height="64"></a>
                    <a href="<%=internetInbox%>"><p class="logo-txt">Integrated Application and Inspection System</p></a></div>
            </div>
            <div class="col-xs-2 col-lg-6">
                <ul class="list-inline hidden-xs hidden-sm">
                    <li class="site-fontsizer-cont"><a class="decrease-font fontsizer" onclick="zoomin();">A-</a></li>
                    <li class="site-fontsizer-cont"><a class="decrease-font fontsizer" onclick="zoomout();">A+</a></li>
                </ul>
                <div class="sg-gov-logo hidden-xs hidden-sm"><a href="https://www.gov.sg/"> <img src="<%=webrooth%>img/singapore-gov-logo.svg" alt="Singapore Government" width="270" height="42"></a></div>
            </div>
        </div>
    </div>
</header>
<script type="text/javascript">

    var size = 1.0;
    function zoomout() {
        size = size + 0.1;
        set();
    }


    function zoomin() {
        size = size - 0.1;
        set();
    }


    function set() {
        document.body.style.zoom = size;
        document.body.style.cssText += '; -moz-transform: scale(' + size + ');-moz-transform-origin: 0 0; ';     //
    }
</script>