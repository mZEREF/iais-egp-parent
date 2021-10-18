<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %><%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 2/13/2020
  Time: 5:59 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<webui:setLayout name="iais-intranet"/>
<style>
    .column-sort{
        margin-top: 3px;
    }
</style>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<div class="main-content">
    <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
        <input type="hidden" name="prRegNo" value="">
        <input type="hidden" id="prefItemId" name="prefItemId" value="">
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <div class="bg-title"><h2>Preferred Date Period Management</h2></div>
        <span id="error_numberError" name="iaisErrorMsg" class="error-msg"></span>


        <br><br>
        <iais:section title="" id="demoList">
            <iais:row>
                <iais:field value="Service Name"/>
                <iais:value width="18">
                    <iais:select name="svcName" firstOption="Please Select" options="svcNameSelect"
                                 value="${svcName}"></iais:select>
                    <span id="error_svcName" name="iaisErrorMsg" class="error-msg"></span>
                </iais:value>
            </iais:row>

            <iais:row>
                <iais:field value="Block-out Period after Application "/>
                <iais:value width="18">
                    <input type="text" name="periodAfterApp" value="${periodAfterApp}">
                </iais:value>
                <div style="padding-top: 12px;">Weeks</div>
            </iais:row>

            <iais:row>
                <iais:field value="Block-out Period before Expiry"/>
                <iais:value width="18">
                    <input type="text" name="periodBeforeExp" value="${periodBeforeExp}">
                </iais:value>
                <div style="padding-top: 12px;">Weeks</div>
            </iais:row>

            <iais:row>
                <iais:field value="Non-reply Notification Window"/>
                <iais:value width="18">
                    <input type="text" name="nonReplyWindow" value="${nonReplyWindow}">
                </iais:value>
                <div style="padding-top: 12px;">Working days</div>
            </iais:row>


            <div class="application-tab-footer">
                <div class="row">
                    <div class="col-xs-12 col-md-11">
                        <div class="text-right">
                            <a class="btn btn-secondary" id="crud_clear_button" href="#">Clear</a>
                            <a class="btn btn-primary" id="crud_search_button" value="doSearch" href="#">Search</a>
                        </div>
                    </div>
                </div>
            </div>


        </iais:section>


        <br><br>
        <div>
            <div class="tab-pane active" id="tabInbox" role="tabpanel">
                <div class="tab-content">
                    <div class="row">
                        <br><br>
                        <div class="col-xs-12">
                            <div class="components">
                                <h3>
                                    <span>Search Results</span>
                                </h3>
                                <iais:pagination param="prefPeriodSearch" result="prefPeriodResult"/>
                                <div class="table-gp">
                                    <table aria-describedby="" class="table">
                                        <thead>
                                        <tr>
                                            <th scope="col"></th>
                                            <iais:sortableHeader needSort="false" field="index"
                                                                 value="S/N"></iais:sortableHeader>
                                            <iais:sortableHeader needSort="true" field="SVC_NAME"
                                                                 value="Service Name"></iais:sortableHeader>
                                            <iais:sortableHeader needSort="true" field="SVC_TYPE"
                                                                 value="Service Type"></iais:sortableHeader>
                                            <iais:sortableHeader needSort="true" field="PERIOD_AFTER_APP"
                                                                 value="Block-out Period after Application"></iais:sortableHeader>
                                            <iais:sortableHeader needSort="true" field="PERIOD_BEFORE_EXP"
                                                                 value="Block-out Period before Expiry"></iais:sortableHeader>
                                            <iais:sortableHeader needSort="true" field="NON_REPLY_WINDOW"
                                                                 value="Non-reply Notification Window"></iais:sortableHeader>
                                            <iais:sortableHeader needSort="false" field="action"
                                                                 value="Action"></iais:sortableHeader>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <c:choose>
                                            <c:when test="${empty prefPeriodResult.rows}">
                                                <tr>
                                                    <td colspan="6">
                                                        <iais:message key="GENERAL_ACK018" escape="true"></iais:message>
                                                    </td>
                                                </tr>
                                            </c:when>
                                            <c:otherwise>
                                                <c:forEach var="prefItem" items="${prefPeriodResult.rows}"
                                                           varStatus="status">
                                                    <tr>
                                                        <td></td>
                                                        <td>${status.index + 1}</td>

                                                        <td>${prefItem.svcName}</td>

                                                        <td><iais:code code="${prefItem.svcType}"></iais:code></td>
                                                        <td>${prefItem.periodAfterApp}</td>
                                                        <td>${prefItem.periodBeforeExp}</td>
                                                        <td>${prefItem.nonReplyWindow}</td>
                                                        <td>
                                                            <button type="button" class="btn btn-default btn-sm"
                                                                    onclick="preUpdateData('<iais:mask name="prefItemId" value="${prefItem.id}"/>')">Edit
                                                            </button>
                                                        </td>
                                                    </tr>
                                                </c:forEach>


                                            </c:otherwise>
                                        </c:choose>
                                        </tbody>
                                    </table>
                                </div>
                                    <div class="table-footnote">
                                        <div class="row">
                                            <div class="col-xs-6 col-md-8 text-right">
                                                <br><br>


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
</div>
<%@include file="/WEB-INF/jsp/include/validation.jsp" %>
<%@include file="/WEB-INF/jsp/include/utils.jsp" %>

<script>
    function preUpdateData(id) {
        $('#prefItemId').val(id);
        SOP.Crud.cfxSubmit("mainForm", "preUpdateData", id);
    }
</script>