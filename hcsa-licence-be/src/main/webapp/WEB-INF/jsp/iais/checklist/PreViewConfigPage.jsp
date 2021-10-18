<%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 11/8/2019
  Time: 10:27 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<webui:setLayout name="iais-intranet"/>
<meta http-equiv="Content-Type" content="text/html charset=gb2312">

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<div class="main-content">
    <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <br><br><br>
        <div class="">
            <div class="row">
                <div class="col-xs-12">
                    <div class="center-content">
                        <c:if test="${configSessionAttr.common eq true}">
                            <div class="bg-title">
                                <h2>General Regulation</h2>
                            </div>
                        </c:if>

                        <c:if test="${configSessionAttr.common eq false}">
                            <c:choose>
                                <c:when test="${empty item.svcSubType}">
                                    <h2>${configSessionAttr.svcName}</h2>
                                </c:when>
                                <c:otherwise>
                                    <h2>${configSessionAttr.svcName} | ${configSessionAttr.svcSubType}</h2>
                                </c:otherwise>
                            </c:choose>
                        </c:if>


                        <c:forEach var="sec" items="${configSessionAttr.sectionDtos}" varStatus="status">
                            <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                                <div class="panel panel-default">
                                    <div class="panel-heading" id="headingPremise${status.index}" role="tab">
                                        <h4 class="panel-title"><a role="button" data-toggle="collapse"
                                                                   href="#collapsePremise${status.index}" aria-expanded="true"
                                                                   aria-controls="collapsePremise">${sec.section}</a>
                                        </h4>
                                    </div>
                                    <div class="panel-collapse collapse in" id="collapsePremise${status.index}" role="tabpanel"
                                         aria-labelledby="headingPremise">
                                        <div class="panel-body">
                                            <table aria-describedby="" class="table">
                                                <thead>
                                                <tr>
                                                    <th scope="col" >Regulation Clause Number</th>
                                                    <th scope="col" >Regulations</th>
                                                    <th scope="col" >Checklist Item</th>
                                                    <th scope="col" >Risk Level</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <c:forEach var="chklitem" items="${sec.checklistItemDtos}"
                                                           varStatus="status">
                                                    <tr>
                                                        <td>
                                                            <p>${chklitem.regulationClauseNo}</p>
                                                        </td>
                                                        <td>
                                                            <p>${chklitem.regulationClause}</p>
                                                        </td>
                                                        <td>
                                                            <p>${chklitem.checklistItem}</p>
                                                        </td>
                                                        <td>
                                                            <p><iais:code code="${chklitem.riskLevel}"></iais:code></p>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>

                    </div>


                        <td>
                            <div class="text-right text-center-mobile">
                                <c:choose>
                                    <c:when test="${actionBtn eq 'dataView'}">
                                        <a class="btn btn-primary next" href="javascript:void(0);"
                                           onclick="javascript: doBack();">Back</a>
                                    </c:when>
                                    <c:otherwise>
                                        <a class="btn btn-secondary" href="javascript:void(0);"
                                           onclick="javascript: doBack();">Cancel</a>
                                        <a class="btn btn-primary next" href="javascript:void(0);"
                                           onclick="javascript: submitConfig();">Submit</a>
                                    </c:otherwise>

                                </c:choose>


                            </div>

                        </td>
                </div>
            </div>
        </div>

</div>

<script type="text/javascript">
    function submitConfig() {
        SOP.Crud.cfxSubmit("mainForm", "submitConfig");
    }

    function doBack() {
        SOP.Crud.cfxSubmit("mainForm", "doBack");
    }
</script>