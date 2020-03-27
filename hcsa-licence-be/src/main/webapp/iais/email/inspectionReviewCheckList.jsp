<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp dashboard-tab">

                        <%@ include file="./navTabs.jsp" %>
                        <div class="tab-content">
                            <div class="tab-pane" id="tabInfo" role="tabpanel">
                                <%@include file="/iais/inspectionncList/tabViewApp.jsp"%>
                            </div>
                            <div class="tab-pane" id="tabDocuments" role="tabpanel">
                                <%@include file="/iais/inspectionncList/tabDocuments.jsp"%>
                            </div>
                            <div class="tab-pane active" id="tabCheckList" role="tabpanel">
                                <div class="alert alert-info" role="alert">
                                    <strong>
                                        <h4>Processing Status Update</h4>
                                    </strong>
                                </div>
                                <div class="row">
                                    <div class="col-xs-12">
                                        <h3>Common</h3>
                                        <div class="table-gp">
                                            <c:forEach var ="section" items ="${commonDto.sectionDtoList}">
                                                <br/>
                                                <h4><c:out value="${section.sectionName}"></c:out></h4>
                                                <table class="table">
                                                    <thead>
                                                    <tr>
                                                        <th>No.</th>
                                                        <th>Regulation Clause Number</th>
                                                        <th>Item</th>
                                                        <th>Yes</th>
                                                        <th>No</th>
                                                        <th>N/A</th>
                                                        <th>Remark</th>
                                                        <th>Rectified</th>
                                                    </tr>
                                                    </thead>
                                                    <tbody>
                                                    <c:forEach var = "item" items = "${section.itemDtoList}" varStatus="status">
                                                        <tr>
                                                            <td class="row_no">${(status.index + 1) }</td>
                                                            <td>${item.incqDto.regClauseNo}</td>
                                                            <td>${item.incqDto.checklistItem}</td>
                                                            <td><input name="<c:out value="${item.incqDto.sectionName}"/><c:out value="${item.incqDto.itemId}"/>comrad" id="<c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionName}"/>comitemCheckboxYes" type="radio"  disabled <c:if test="${item.incqDto.chkanswer eq'Yes'}">checked</c:if> value="Yes" /></td>
                                                            <td><input name="<c:out value="${item.incqDto.sectionName}"/><c:out value="${item.incqDto.itemId}"/>comrad" id="<c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionName}"/>comitemCheckboxNo" type="radio" disabled  <c:if test="${item.incqDto.chkanswer eq'No'}">checked</c:if> value="No" /></td>
                                                            <td><input name="<c:out value="${item.incqDto.sectionName}"/><c:out value="${item.incqDto.itemId}"/>comrad" id="<c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionName}"/>comitemCheckboxNa" type="radio" disabled <c:if test="${item.incqDto.chkanswer eq'N/A'}">checked</c:if> value="N/A" /></td>
                                                            <td><input name="<c:out value="${item.incqDto.sectionName}"/><c:out value="${item.incqDto.itemId}"/>comremark" id="<c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionName}"/>comitemCheckboxRemark" disabled type="text" value="<c:out value="${item.incqDto.remark}"/>" /></td>
                                                            <td> <input name="<c:out value="${item.incqDto.sectionName}"/><c:out value="${item.incqDto.itemId}"/>comrec" id="<c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionName}"/>comrec" type="checkbox" <c:if test="${item.incqDto.rectified}">checked</c:if> <c:if test="${item.incqDto.chkanswer != 'No'}">hidden </c:if> value="rec" disabled/></td>
                                                        </tr>
                                                    </c:forEach>
                                                    </tbody>
                                                </table>
                                            </c:forEach>
                                        </div>


                                        <h3>General</h3>
                                        <div class="table-gp">
                                            <c:forEach var ="section" items ="${fillCheckListDto.sectionDtoList}">
                                                <br/>
                                                <h4><c:out value="${section.sectionName}"></c:out></h4>
                                                <table class="table">
                                                    <thead>
                                                    <tr>
                                                        <th>No.</th>
                                                        <th>Regulation Clause Number</th>
                                                        <th>Item</th>
                                                        <th>Yes</th>
                                                        <th>No</th>
                                                        <th>N/A</th>
                                                        <th>Remark</th>
                                                        <th>Rectified</th>
                                                    </tr>
                                                    </thead>
                                                    <tbody>
                                                    <c:forEach var = "item" items = "${section.itemDtoList}" varStatus="status">
                                                        <tr>
                                                            <td class="row_no">${(status.index + 1) }</td>
                                                            <td>${item.incqDto.regClauseNo}</td>
                                                            <td>${item.incqDto.checklistItem}</td>
                                                            <td><input name="<c:out value="${item.incqDto.sectionName}"/><c:out value="${item.incqDto.itemId}"/>rad" id="<c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionName}"/>itemCheckboxYes" type="radio"  disabled <c:if test="${item.incqDto.chkanswer eq'Yes'}">checked</c:if> value="Yes" /></td>
                                                            <td><input name="<c:out value="${item.incqDto.sectionName}"/><c:out value="${item.incqDto.itemId}"/>rad" id="<c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionName}"/>itemCheckboxNo" type="radio" disabled  <c:if test="${item.incqDto.chkanswer eq'No'}">checked</c:if> value="No" /></td>
                                                            <td><input name="<c:out value="${item.incqDto.sectionName}"/><c:out value="${item.incqDto.itemId}"/>rad" id="<c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionName}"/>itemCheckboxNa" type="radio" disabled <c:if test="${item.incqDto.chkanswer eq'N/A'}">checked</c:if> value="N/A" /></td>

                                                            <td><input name="<c:out value="${item.incqDto.sectionName}"/><c:out value="${item.incqDto.itemId}"/>remark" id="<c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionName}"/>itemCheckboxRemark" disabled type="text" value="<c:out value="${item.incqDto.remark}"/>" /></td>
                                                            <td> <input name="<c:out value="${item.incqDto.sectionName}"/><c:out value="${item.incqDto.itemId}"/>rec" id="<c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionName}"/>rec" type="checkbox" <c:if test="${item.incqDto.rectified}">checked</c:if> <c:if test="${item.incqDto.chkanswer != 'No'}">hidden </c:if> value="rec" disabled/></td>
                                                        </tr>
                                                    </c:forEach>
                                                    </tbody>
                                                </table>
                                            </c:forEach>
                                        </div>
                                    </div>
                                </div>
                            </div>


                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
