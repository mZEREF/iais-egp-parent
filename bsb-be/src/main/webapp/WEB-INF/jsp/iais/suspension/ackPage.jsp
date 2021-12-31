<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<div>
    <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
        <div class="main-content">
            <div class="row">
                <div class="col-lg-12 col-xs-12">
                    <div class="center-content">
                        <div class="intranet-content">
                            <div class="bg-title">
                                <h2>${ackMsg}</h2>
                            </div>
                        </div>
                        <div style="text-align: left">
                            <c:if test="${back eq 'fac'}">
                                <a class="back" href="/bsb-be/eservice/INTRANET/FacilityList"><em class="fa fa-angle-left"></em>Back</a>
                            </c:if>
                            <c:if test="${back eq 'app'}">
                                <a class="back" href="/bsb-be/eservice/INTRANET/MohBsbTaskList"><em class="fa fa-angle-left"></em>Back</a>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>