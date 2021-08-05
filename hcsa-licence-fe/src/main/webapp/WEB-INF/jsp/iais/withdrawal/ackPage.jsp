<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://www.ecquaria.com/menu" prefix="menu" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<%
    String webroot1=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.FE_CSS_ROOT;
%>
<webui:setLayout name="iais-internet"/>
<div class="dashboard" style="background-image:url('<%=webroot1%>img/Masthead-banner.jpg')" >
    <div class="container">
        <div class="navigation-gp">
            <div class="row">
                <%@ include file="../common/dashboardDropDown.jsp" %>
            </div>
        </div>
    </div>
    <div class=" steps-tab container">
        <div class="tab-content">
            <div class="tab-pane active" id="previewTab" role="tabpanel">
                <h1 class="font-weight 0 ">Withdrawal Form</h1>
                <c:if test="${rfiServiceName!=null }">
                    <p style="font-size: 2.2rem">You are Withdrawing for <strong>${rfiServiceName }</strong></p>
                </c:if>
            </div>
        </div>
    </div>
</div>
<div class="main-content">
    <div class="container">
        <br/>
        <div class="row">
            <div class="col-lg-12 col-xs-12 cesform-box">
                <p><span style="font-size:2rem;">${WITHDRAW_ACKMSG}</span></p>
                <div class="table-responsive">
                    <table aria-describedby="" class="table">
                        <thead>
                        <tr>
                            <th scope="col" style="text-align:center">Application No.</th>
                            <th scope="col" style="text-align:center">Service Name</th>
                            <th scope="col" style="text-align:center">HCI Name</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${withdrawnDtoListAck}" var="confirm" varStatus="num">
                            <tr style="text-align: center">
                                <td>
                                    <p><c:out value="${confirm.newApplicationNo}"></c:out></p>
                                </td>
                                <td>
                                    <p><c:out value="${confirm.svcName}"></c:out></p>
                                </td>
                                <td>
                                    <p><c:out value="${(confirm.hciName == null || confirm.hciName == '') ? 'N/A' : confirm.hciName}"></c:out></p>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="col-lg-12 col-xs-12">
                <div class="center-content " style="padding-top: 10px">
                    <a href="/main-web/eservice/INTERNET/MohInternetInbox?initPage=initApp"><em
                            class="fa fa-angle-left"></em> Back</a>
                </div>
            </div>
        </div>
        <br/>
    </div>
</div>




