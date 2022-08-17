<%@tag description="acknowledge page" pageEncoding="UTF-8" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<webui:setLayout name="iais-intranet"/>


<%@attribute name="task" required="true" type="java.lang.String" %>
<%@attribute name="nextTask" required="true" type="java.lang.String" %>
<%@attribute name="nextRole" required="true" type="java.lang.String" %>
<%@attribute name="resultMsg" required="false" type="java.lang.String" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<div class="main-content">
    <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
        <div class="col-xs-12">
            <div class="col-lg-12 col-xs-12">
                <div class="center-content">
                    <div class="intranet-content">
                        <div class="bg-title">
                            <c:choose>
                                <c:when test="${resultMsg ne null && resultMsg ne ''}">
                                    <h2><c:out value="${resultMsg}"/></h2>
                                </c:when>
                                <c:otherwise>
                                    <h2 style="border-bottom: 0px">
                                        <span>You have successfully completed your task -- <c:out value="${task}"/></span>
                                    </h2>
                                    <c:choose>
                                        <c:when test="${nextTask ne null && nextTask ne ''}">
                                            <h3>Next stage is <c:out value="${nextTask}"/>, handled by ${nextRole}</h3>
                                        </c:when>
                                        <c:otherwise>
                                            <h3>this is final stage</h3>
                                        </c:otherwise>
                                    </c:choose>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <div class="row">
                            <div class="col-xs-12">
                                <div style="float:left">
                                    <a class="back" id="back" href="/bsb-web/eservicecontinue/INTRANET/MohBsbTaskList"><em class="fa fa-angle-left"></em> Back</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>


