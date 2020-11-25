<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://www.ecquaria.com/menu" prefix="menu" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<%@include file="./dashboard.jsp" %>
<div class="main-content">
    <form method="post" id="mainForm" enctype="multipart/form-data" action=<%=process.runtime.continueURL()%>>
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="center-content">
                    <h2>Acknowledgement</h2>
                    <br>
                    <span style="font-size:2rem;">${WITHDRAW_ACKMSG}</span>
                </div>
            </div>
            <div class="col-lg-12 col-xs-12">
                <div class="center-content " style="padding-top: 10px">
                    <a href="/main-web/eservice/INTERNET/MohInternetInbox?initPage=initApp"><em
                            class="fa fa-angle-left"></em> Back</a>
                </div>
            </div>
        </div>
    </form>
</div>



