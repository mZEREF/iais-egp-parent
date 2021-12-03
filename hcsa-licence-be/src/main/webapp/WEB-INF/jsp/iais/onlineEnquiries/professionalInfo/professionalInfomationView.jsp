<%--
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
        <input type="hidden" id="prRegNo" name="prRegNo" value="">
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <div class="bg-title"><h2>Professional information</h2></div>


        <span id="error_checkBoxError" name="iaisErrorMsg" class="error-msg"></span>


        <div class="form-horizontal">
            <table aria-describedby="" width="100%">
                <thead style="display: none">
                <tr>
                    <th scope="col"></th>
                </tr>
                </thead>

            <tr>
                <td>
                    <label class="col-xs-0 col-md-5 control-label">Professional Name</label>
                    <div class="col-sm-7 col-md-5 col-xs-6">
                        <input type="text" name="name" value="${param.name}" maxlength="66">
                        <span id="error_name" name="iaisErrorMsg" class="error-msg"></span>
                    </div>
                </td>

                <td>
                    <label class="col-xs-0 col-md-5 control-label">Professional Regn. No.</label>
                    <div class="col-sm-7 col-md-5 col-xs-6">
                        <input type="text" name="profRegNo" value="${param.profRegNo}" maxlength="20"/>
                        <span id="error_profRegNo" name="iaisErrorMsg" class="error-msg"></span>
                    </div>
                </td>
            </tr>

            <tr>
                <td>
                    <label class="col-xs-0 col-md-5 control-label">HCI Name</label>
                    <div class="col-sm-7 col-md-5 col-xs-6">
                        <input type="text" name="hciName" value="${param.hciName}" maxlength="100"/>
                        <span id="error_hciName" name="iaisErrorMsg" class="error-msg"></span>
                    </div>
                </td>

                <td>
                    <label class="col-xs-0 col-md-5 control-label">HCI Code</label>
                    <div class="col-sm-7 col-md-5 col-xs-6">
                        <input type="text" name="hciCode" value="${param.hciCode}" maxlength="7"/>
                        <span id="error_hciCode" name="iaisErrorMsg" class="error-msg"></span>
                    </div>
                </td>
            </tr>


            <tr>
                <td>
                    <label class="col-xs-0 col-md-5 control-label">HCI Postal Code</label>
                    <div class="col-sm-7 col-md-5 col-xs-6">
                        <input type="text" name="hciPostalcode" value="${param.hciPostalcode}" maxlength="6"/>
                        <span id="error_hciPostalcode" name="iaisErrorMsg" class="error-msg"></span>
                    </div>
                </td>

                <td>
                    <label class="col-xs-0 col-md-5 control-label">HCI Address</label>
                    <div class="col-sm-7 col-md-5 col-xs-6">
                        <input type="text" name="practiceLocation" value="${param.practiceLocation}" maxlength="32"/>
                        <span id="error_practiceLocation" name="iaisErrorMsg" class="error-msg"></span>
                    </div>
                </td>
            </tr>

            <tr>
                <td>
                    <label class="col-xs-0 col-md-5 control-label">Service Name</label>
                    <div class="col-sm-7 col-md-5 col-xs-6">
                        <iais:select name="serviceName" id="serviceName" options="svcNameSelect" needSort="true"
                                     firstOption="Please Select" value="${param.serviceName}"></iais:select>
                        <span id="error_serviceName" name="iaisErrorMsg" class="error-msg"></span>
                    </div>
                </td>

                <td>
                    <label class="col-xs-0 col-md-5 control-label">Designation</label>
                    <div class="col-sm-7 col-md-5 col-xs-6">
                        <iais:select name="designation" id="designation" codeCategory="CATE_ID_DESIGNATION"
                                     firstOption="Please Select" value="${param.designation}"></iais:select>
                        <span id="error_designation" name="iaisErrorMsg" class="error-msg"></span>
                    </div>
                </td>
            </tr>

            <tr>
                <td>
                    <label class="col-xs-0 col-md-5 control-label">Service Personnel Role</label>
                    <div class="col-sm-7 col-md-5 col-xs-6">

                        <iais:select name="role" id="role" codeCategory="CATE_ID_PERSONNEL_PSN_TYPE"
                                     firstOption="Please Select" value="${param.role}"></iais:select>

                        <span id="error_role" name="iaisErrorMsg" class="error-msg"></span>
                    </div>
                </td>

            </tr>

            </table>

            <div class="application-tab-footer">
                <div class="row">
                    <div class="col-xs-12 col-md-11">
                        <div class="text-right">
                            <a class="btn btn-secondary" id="crud_clear_button"  href="#">Clear</a>
                            <a class="btn btn-secondary" onclick="$(this).attr('class', 'btn btn-secondary disabled')" href="${pageContext.request.contextPath}/professional-information-file">Export</a>
                            <a class="btn btn-primary" id="crud_search_button" value="doSearch" href="#">Search</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>


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
                            <iais:pagination param="professionalInfoSearch" result="professionalInfoResult"/>
                            <div class="table-gp">
                                <table aria-describedby="" class="table">
                                    <thead>
                                    <tr>
                                        <th scope="col"></th>
                                        <iais:sortableHeader style="padding-bottom:15px" needSort="false" field="index"
                                                             value="S/N"></iais:sortableHeader>
                                        <iais:sortableHeader needSort="true" field="name"
                                                             value="Professional Name"></iais:sortableHeader>
                                        <iais:sortableHeader needSort="true" field="prof_reg_no"
                                                             value="Professional Regn. No."></iais:sortableHeader>
                                        <iais:sortableHeader needSort="true" field="practice_location"
                                                             value="Practice Location"></iais:sortableHeader>
                                        <iais:sortableHeader needSort="true" field="licence_no"
                                                             value="Active Licence"></iais:sortableHeader>
                                        <iais:sortableHeader needSort="true" field="service_name"
                                                             value="Service"></iais:sortableHeader>
                                        <iais:sortableHeader needSort="true" field="designation"
                                                             value="Designation"></iais:sortableHeader>
                                        <iais:sortableHeader needSort="true" field="psn_type"
                                                             value="Service Personnel Role"></iais:sortableHeader>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:choose>
                                        <c:when test="${empty professionalInfoResult.rows}">
                                            <tr>
                                                <td colspan="6">
                                                    <iais:message key="GENERAL_ACK018" escape="true"></iais:message>
                                                </td>
                                            </tr>
                                        </c:when>
                                        <c:otherwise>
                                            <c:forEach var="info" items="${professionalInfoResult.rows}"
                                                       varStatus="status">
                                                <tr>
                                                    <td></td>
                                                    <td>${(status.index + 1) + (professionalInfoSearch.pageNo - 1) * professionalInfoSearch.pageSize}</td>
                                                    <td>${info.name}</td>

                                                    <td>
                                                    <c:choose>
                                                        <c:when test="${info.profRegNo != null}">
                                                        <a href="#" onclick="viewPfDetails('<iais:mask name="prRegNo"
                                                                                              value="${info.id}|${info.profRegNo}"/>')">${info.profRegNo}</a>
                                                        </c:when>
                                                        <c:otherwise>
                                                            -
                                                        </c:otherwise>
                                                    </c:choose>


                                                    <td>${info.practiceLocation}</td>
                                                    <td>${info.licence}</td>
                                                    <td>${info.serviceName}</td>
                                                    <td><iais:code code="${empty info.designation ? '-' : info.designation}"></iais:code></td>
                                                    <td><iais:code code="${info.role}"></iais:code></td>
                                                </tr>
                                            </c:forEach>


                                        </c:otherwise>
                                    </c:choose>
                                    </tbody>
                                </table>
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


    </div>


    </form>
</div>
<%@include file="/WEB-INF/jsp/include/validation.jsp" %>
<%@include file="/WEB-INF/jsp/include/utils.jsp" %>

<script>
    function viewPfDetails(val) {
        $('#prRegNo').val(val);
        SOP.Crud.cfxSubmit("mainForm", "viewPfDetails");
    }


</script>