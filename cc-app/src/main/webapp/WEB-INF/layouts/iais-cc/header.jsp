<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisCCConstant" %>
<%
    String webrooth=IaisCCConstant.FE_CSS_ROOT;
%>
<header>
    <div class="container">
        <div class="row">
            <div class="col-xs-10 col-lg-6">
                <div class="logo-img"><a href="#"><img src="<%=webrooth%>img/moh-logo.svg" alt="Ministry of Health" width="235" height="64">
                    <p class="logo-txt">Integrated Application and Inspection System</p></a></div>
            </div>
            <div class="col-xs-2 col-lg-6">
                <ul class="list-inline hidden-xs hidden-sm">
                    <li class="site-fontsizer-cont"><a class="decrease-font fontsizer">A-</a></li>
                    <li class="site-fontsizer-cont"><a class="decrease-font fontsizer">A+</a></li>
                </ul>
                <div class="sg-gov-logo hidden-xs hidden-sm"><a href="https://www.gov.sg/"> <img src="<%=webrooth%>img/singapore-gov-logo.svg" alt="Singapore Government" width="270" height="42"></a></div>
            </div>
        </div>
    </div>
</header>