<%@page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<link href="<%=WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-rfi.js"></script>

<%@include file="dashboard.jsp"%>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="completedAllRfi" value="${completedAllRfi}">

    <div class="main-content">
        <div class="container">
            <div class="row" style="margin: 0 auto">
                <div class="col-xs-12">
                    <div class="form-horizontal">
                        <%--@elvariable id="rfiDisplayDto" type="sg.gov.moh.iais.egp.bsb.dto.rfi.RfiDisplayDto"--%>
                        <%--@elvariable id="applicationRfiIndicatorDtoList" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.rfi.ApplicationRfiIndicatorDto>"--%>
                        <div class="table-gp">
                            <table aria-describedby="" class="table">
                                <thead>
                                <tr style="text-align: center">
                                    <th scope="col" style="width:5%">S/N</th>
                                    <th scope="col" style="width:25%">Module Name</th>
                                    <th scope="col" style="width:25%">Action</th>
                                    <th scope="col" style="width:25%">Completed</th>
                                </tr>
                                </thead>
                                <c:choose>
                                <c:when test="${empty applicationRfiIndicatorDtoList}">
                                    <iais:message key="GENERAL_ACK018" escape="true"/>
                                </c:when>
                                <c:otherwise>
                                    <tbody>
                                    <c:forEach var="dto" items="${applicationRfiIndicatorDtoList}" varStatus="status">
                                        <tr style="text-align: center">
                                            <td><c:out value="${status.index+1}"/></td>
                                            <td><c:out value="${dto.moduleName}"/></td>
                                            <td><a class="btn btn-default btn-sm" href="${dto.internetProcessUrl}?appId=<iais:mask name='rfiAppId' value='${rfiDisplayDto.appId}'/>">Handle</a></td>
                                            <td>
                                                <c:if test="${dto.status eq false}">
                                                    <h4 class="text-danger"><em class="fa fa-times-circle del-size-36 cursorPointer removeBtn"></em></h4>
                                                </c:if>
                                                <c:if test="${dto.status eq true}">
                                                    <h4 class="text-success"><em class="fa fa-check-circle del-size-36"></em></h4>
                                                </c:if>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </c:otherwise>
                                </c:choose>
                            </table>
                        </div>
                    </div>
                    <div class="row" style="border-top: 1px solid #D1D1D1;margin-top: 100px;padding: 20px 0">
                        <div class="col-xs-12 col-sm-6 ">
                            <a class="back" id="back" href="/bsb-web/eservice/INTERNET/MohBSBInboxMsg"><em class="fa fa-angle-left"></em> Previous</a>
                        </div>
                        <div class="col-xs-12 col-sm-6">
                            <div class="button-group" style="float: right">
                                <a class="btn btn-primary" id="submitBtn" >Submit</a>
                            </div>
                        </div>
                    </div>

                    <div class="modal fade" id="submitModal" role="dialog" aria-labelledby="myModalLabel">
                        <div class="modal-dialog modal-dialog-centered" role="document">
                            <div class="modal-content">
                                <div class="modal-body">
                                    <div class="row">
                                        <div class="col-md-12"><span style="font-size: 2rem">Make sure you have handled all rfi.</span></div>
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary btn-md" data-dismiss="modal">Close</button>
                                </div>
                            </div>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </div>
</form>