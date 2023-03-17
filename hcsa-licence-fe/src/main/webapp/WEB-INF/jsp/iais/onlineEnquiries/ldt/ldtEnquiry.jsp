<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.AppConsts" %>
<%@page import="com.ecquaria.cloud.moh.iais.common.utils.StringUtil" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<%
    String webrootCom=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.COMMON_CSS_ROOT;
%>
<style>
    .table-info-display {
        margin: 20px 0px 25px 0px;
        background: #efefef;
        padding: 8px;
        border-radius: 8px;
        -moz-border-radius: 8px;
        -webkit-border-radius: 8px;
    }
    thead > tr > th > span {
        line-height: 0px;
    }
    .table-count {
        float: left;
        margin-top: 5px;
    }
    .btn.btn-sm {
        font-size: 16px;
        font-weight: 500;
        padding: 5px 10px;
        text-transform: uppercase;
        border-radius: 30px;
        border: 1px solid grey;
    }
    .column-sort {
        float: left;
        display: block;
        margin: 0 0 9px 0;
        width: 14px;
    }
</style>
<webui:setLayout name="iais-internet"/>
<%@include file="../../common/dashboard.jsp"%>
<form id="mainForm"  method="post" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>

    <div class="container">
        <div class="row">
            <div class="col-xs-12">
                <div class="row form-horizontal">
                    <iais:row>

                    </iais:row>
                    <div class="bg-title col-xs-12 col-md-12">
                        <h2>Lab-developed Tests Enquiry</h2>
                    </div>


                    <div class="col-xs-12 col-md-12">

                        <iais:row>
                            <iais:field width="4" value="Name of Laboratory"/>
                            <iais:value width="4" cssClass="col-md-4" >
                                <input type="text" maxlength="100" id="laboratoryName"  name="laboratoryName" value="<c:out value="${dsLaboratoryDevelopTestEnquiryFilterDto.laboratoryName}"/>" >
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field width="4" value="Name of LDT Test"/>
                            <iais:value width="4" cssClass="col-md-4" >
                                <input type="text" maxlength="50" id="ldtTestName"  name="ldtTestName" value="<c:out value="${dsLaboratoryDevelopTestEnquiryFilterDto.ldtTestName}"/>" >
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field width="4" value="Date LDT was made or will be made available"/>
                            <div class="col-md-4 " style="padding-right: 0;padding-left: 0;">
                                <iais:value width="6" cssClass="col-md-6">
                                    <iais:datePicker id="ldtDateFrom" name="ldtDateFrom" dateVal="${dsLaboratoryDevelopTestEnquiryFilterDto.ldtDateFrom}"/>
                                </iais:value>
                                <label class="col-xs-1 col-md-1 control-label">To&nbsp;</label>
                                <iais:value width="5" cssClass="col-md-5">
                                    <iais:datePicker id="ldtDateTo" name="ldtDateTo" dateVal="${dsLaboratoryDevelopTestEnquiryFilterDto.ldtDateTo}"/>
                                </iais:value>
                            </div>
                        </iais:row>
                        <iais:row>
                            <iais:field width="4" value="Person responsible for the test"/>
                            <iais:value width="4" cssClass="col-md-4" >
                                <input type="text" maxlength="66" id="responsePerson"  name="responsePerson" value="<c:out value="${dsLaboratoryDevelopTestEnquiryFilterDto.responsePerson}"/>" >
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field width="4" value="Status of Test" />
                            <div class="col-md-4 row ">
                                <iais:value width="6" cssClass="col-md-6">
                                    <div class="form-check row ">
                                        <input class="form-check-input"
                                               type="radio"
                                               name="testStatus"
                                               value="1"
                                               id="statusActive"
                                               <c:if test="${ dsLaboratoryDevelopTestEnquiryFilterDto.testStatus =='1' }">checked</c:if>
                                               aria-invalid="false">
                                        <label class="form-check-label"
                                               for="statusActive"><span
                                                class="check-circle"></span>Active</label>
                                    </div>
                                </iais:value>
                                <iais:value width="6" cssClass="col-md-6">
                                    <div class="form-check">
                                        <input class="form-check-input" type="radio"
                                               name="testStatus" value="0" id="statusInactive"
                                               <c:if test="${dsLaboratoryDevelopTestEnquiryFilterDto.testStatus == '0'}">checked</c:if>
                                               aria-invalid="false">
                                        <label class="form-check-label"
                                               for="statusInactive"><span
                                                class="check-circle"></span>Inactive</label>
                                    </div>
                                </iais:value>
                            </div>

                        </iais:row>


                        <div class="col-xs-12 col-md-12">
                            <iais:action style="text-align:right;">
                                <button type="button" class="btn btn-secondary"
                                        onclick="javascript:doClear();">Clear
                                </button>
                                <button type="button" class="btn btn-primary"
                                        onclick="javascript:doSearch();">Search
                                </button>
                            </iais:action>
                        </div>
                    </div>
                </div>
                <br>
                    <div class="components">
                        <h3>
                            <span>Search Results</span>
                        </h3>
                        <iais:pagination param="ldtParam" result="ldtResult"/>
                        <div class="table-responsive">
                            <div class="table-gp">
                                <table aria-describedby="" class="table application-group" style="border-collapse:collapse;">
                                    <thead>
                                    <tr >
                                        <th scope="col" style="display: none"></th>
                                        <iais:sortableHeader needSort="false"  style="white-space: nowrap;padding: 15px 30px 15px 0px;" field="" value="S/N"/>
                                        <iais:sortableHeader needSort="true" style="white-space: nowrap;padding: 15px 30px 15px 0px;"
                                                             field="LDT_NAME"
                                                             value="Name of Laboratory"/>
                                        <iais:sortableHeader needSort="true" style="white-space: nowrap;padding: 15px 30px 15px 0px;"
                                                             field="LDT_TEST_NAME"
                                                             value="Name of LDT Test"/>
                                        <iais:sortableHeader needSort="true" style="white-space: nowrap;padding: 15px 30px 15px 0px;"
                                                             field="INTENDED_PURPOSE"
                                                             value="Intended Purpose of Test"/>
                                        <iais:sortableHeader needSort="true" style="white-space: nowrap;padding: 15px 30px 15px 0px;"
                                                             field="LDT_DATE"
                                                             value="Date LDT was made or will be made available"/>
                                        <iais:sortableHeader needSort="true" style="white-space: nowrap;padding: 15px 30px 15px 0px;"
                                                             field="RESPONSE_PERSON"
                                                             value="Person responsible for the test"/>
                                        <iais:sortableHeader needSort="true" style="white-space: nowrap;padding: 15px 30px 15px 0px;"
                                                             field="DESIGNATION"
                                                             value="Designation"/>
                                        <iais:sortableHeader needSort="true" style="white-space: nowrap;padding: 15px 30px 15px 0px;"
                                                             field="TEST_STATUS"
                                                             value="Status of Test"/>
                                        <iais:sortableHeader needSort="true" style="white-space: nowrap;padding: 15px 30px 15px 0px;"
                                                             field="REMARKS"
                                                             value="Remarks"/>
                                    </tr>
                                    </thead>
                                    <tbody class="form-horizontal">
                                    <c:choose>
                                        <c:when test="${empty ldtResult.rows}">
                                            <tr>
                                                <td colspan="15">
                                                    <iais:message key="GENERAL_ACK018"
                                                                  escape="true"/>
                                                </td>
                                            </tr>
                                        </c:when>
                                        <c:otherwise>
                                            <c:forEach var="ldt"
                                                       items="${ldtResult.rows}"
                                                       varStatus="status">
                                                <tr >
                                                    <td style="vertical-align:middle;">
                                                        <p class="visible-xs visible-sm table-row-title">S/N</p>
                                                        <c:out value="${status.index + 1+ (ldtParam.pageNo - 1) * ldtParam.pageSize}"/>
                                                    </td>
                                                    <td style="vertical-align:middle;">
                                                        <p class="visible-xs visible-sm table-row-title">Name of Laboratory</p>
                                                        <c:out value="${ldt.laboratoryName}"/>
                                                    </td>
                                                    <td style="vertical-align:middle;">
                                                        <p class="visible-xs visible-sm table-row-title">Name of LDT Test</p>
                                                        <c:out value="${ldt.ldtTestName}"/>
                                                    </td>
                                                    <td style="vertical-align:middle;">
                                                        <p class="visible-xs visible-sm table-row-title">Intended Purpose of Test</p>
                                                            ${StringUtil.viewTextHtml(ldt.intendedPurpose)}
                                                    </td>
                                                    <td style="vertical-align:middle;">
                                                        <p class="visible-xs visible-sm table-row-title">Date LDT was made or will be made available</p>
                                                        <fmt:formatDate
                                                                value="${ldt.ldtDate}"
                                                                pattern="${AppConsts.DEFAULT_DATE_FORMAT}"/>
                                                    </td>
                                                    <td style="vertical-align:middle;">
                                                        <p class="visible-xs visible-sm table-row-title">Person responsible for the test</p>
                                                        <c:out value="${ldt.responsePerson}"/>
                                                    </td>
                                                    <td style="vertical-align:middle;">
                                                        <p class="visible-xs visible-sm table-row-title">Designation</p>
                                                        <c:out value="${ldt.designation}"/>
                                                    </td>
                                                    <td style="vertical-align:middle;">
                                                        <p class="visible-xs visible-sm table-row-title">Status of Test</p>
                                                        <c:out value="${ldt.testStatus}"/>
                                                    </td>
                                                    <td style="vertical-align:middle;">
                                                        <p class="visible-xs visible-sm table-row-title">Remarks</p>
                                                            ${StringUtil.viewTextHtml(ldt.remarks)}
                                                    </td>

                                                </tr>
                                            </c:forEach>
                                        </c:otherwise>
                                    </c:choose>
                                    </tbody>
                                </table>
                            </div>

                        </div>
                        <iais:action >
                            <div class="col-xs-12 col-md-2 text-left">
                                <a style="padding-left: 5px;" class="back " href="/main-web/eservice/INTERNET/MohDataSubmissionsInbox">
                                    <em class="fa fa-angle-left">&nbsp;</em> Back
                                </a>
                            </div>
                            <div class="col-xs-12 col-md-10 margin-bottom-10">
                                <div class="text-right">
                                    <a style="text-align:right;" class="btn btn-secondary"
                                       href="${pageContext.request.contextPath}/hcsa/enquiry/ar/LDT-SearchResults-DownloadS">Download</a>
                                </div>
                            </div>
                        </iais:action>
                    </div>
            </div>
        </div>
    </div>
</form>



<%@include file="/WEB-INF/jsp/include/utils.jsp" %>
<script type="text/javascript" src="<%=webrootCom%>js/onlineEnquiries/ldtSearch.js"></script>
