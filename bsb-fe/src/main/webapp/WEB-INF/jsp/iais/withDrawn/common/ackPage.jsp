<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
<%@include file="dashboard.jsp" %>
<div class="main-content">
    <div class="container">
        <br/>
        <div class="row">
            <div class="col-lg-6 col-xs-12">
                <h2><iais:message key="${resultMsg}" escape="false"/></h2>
                <p><span>Withdrawal of the following submission.</span></p>
                <div class="table-responsive">
                    <table aria-describedby="" class="table">
                        <thead>
                        <tr>
                            <th scope="col">Application Number</th>
                            <th scope="col">Date & Time</th>
                        </tr>
                        </thead>
                        <tbody>
                        <%--@elvariable id="withdrawnDtoListAck" type="sg.gov.moh.iais.egp.bsb.dto.withdrawn.WithdrawnAckDto"--%>
                        <c:forEach items="${withdrawnDtoListAck.applicationNos}" var="appNo">
                            <tr>
                                <td>
                                    <p><c:out value="${appNo}"/></p>
                                </td>
                                <td>
                                    <p><fmt:formatDate value="${withdrawnDtoListAck.withdrawnDate}" pattern="dd/MM/yyyy HH:mm:ss"/></p>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="col-lg-7 col-xs-12">
                <div style="padding-top: 10px;text-align: right">
                    <a href="#" class="btn btn-secondary">PRINT</a>
                    <a href="${backUrl}" class="btn btn-secondary">HOME</a>
                </div>
            </div>
        </div>
        <br/>
    </div>
</div>




