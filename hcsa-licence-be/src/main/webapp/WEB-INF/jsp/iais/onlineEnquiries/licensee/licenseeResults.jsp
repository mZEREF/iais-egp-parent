<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MaskUtil" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.AppConsts" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<%
    String webrootCom = IaisEGPConstant.CSS_ROOT + IaisEGPConstant.COMMON_CSS_ROOT;
%>
<style>

    .form-horizontal p {
        line-height: 23px;
    }

    .hiddenRow {
        padding: 0px 0px !important;
        background-color: #f3f3f3;
    }
</style>
<webui:setLayout name="iais-intranet"/>
<div class="main-content dashboard">
    <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>

        <div class="col-lg-12 col-xs-12">
            <div class="center-content">
                <div class="intranet-content">
                    <div class="row form-horizontal">
                        <div class="bg-title col-xs-12 col-md-12">
                            <h2>Licensee Search</h2>
                        </div>
                        <div class="bg-title col-xs-12 col-md-12">
                            One search filter must be entered to proceed with search.
                        </div>


                        <div class="col-xs-12 col-md-12">


                            <iais:row>
                                <iais:field width="4" value="Licensee Type"/>
                                <iais:value width="4" cssClass="col-md-4">
                                    <iais:select name="licenseeType" id="licenseeType" firstOption="Please Select"
                                                 options="licenseeTypeOption"
                                                 cssClass="clearSel" value="${licenseeEnquiryFilterDto.licenseeType}"/>
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="Licensee ID No."/>
                                <iais:value width="4" cssClass="col-md-4">
                                    <input type="text" maxlength="20" id="licenseeIdNo" name="licenseeIdNo"
                                           value="${licenseeEnquiryFilterDto.licenseeIdNo}">
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="Licensee Name"/>
                                <iais:value width="4" cssClass="col-md-4">
                                    <input type="text" maxlength="20" id="licenseeName" name="licenseeName"
                                           value="${licenseeEnquiryFilterDto.licenseeName}">
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="Organisation Name"/>
                                <iais:value width="4" cssClass="col-md-4">
                                    <input type="text" maxlength="128" id="organisationName" name="organisationName"
                                           value="${licenseeEnquiryFilterDto.organisationName}">
                                </iais:value>
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
                        <iais:pagination param="lisParam" result="licenseeResult"/>
                        <div class="table-gp">
                            <table aria-describedby="" class="table table-responsive"
                                   style="border-collapse:collapse;">
                                <thead>
                                <tr>
                                    <iais:sortableHeader needSort="false"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field=""
                                                         value="S/N"/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="LICENSEE_TYPE"
                                                         value="Licensee Type"/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="ID_NUMBER"
                                                         value="Licensee ID No."/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="LICENSEE_NAME"
                                                         value="Licensee Name"/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="PHONE_NO"
                                                         value="Phone Number"/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="OFFICE_NO"
                                                         value="Office Number"/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="EMAIL_ADDR"
                                                         value="Email Address"/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="ADDRESS"
                                                         value="Licensee's Address"/>
                                    <iais:sortableHeader needSort="true"
                                                         style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                         field="organisation_name"
                                                         value="Organisation Name"/>

                                </tr>
                                </thead>
                                <tbody class="form-horizontal">
                                <c:choose>
                                    <c:when test="${empty licenseeResult.rows}">
                                        <tr>
                                            <td colspan="15">
                                                <iais:message key="GENERAL_ACK018"
                                                              escape="true"/>
                                            </td>
                                        </tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="licensee"
                                                   items="${licenseeResult.rows}"
                                                   varStatus="status">
                                            <tr>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">S/N</p>
                                                    <c:out value="${status.index + 1+ (lisParam.pageNo - 1) * lisParam.pageSize}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Licensee Type</p>
                                                    <iais:code code="${licensee.licenseeType}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Licensee ID No.</p>
                                                    <a href="#"
                                                       onclick="fullDetailsView('${MaskUtil.maskValue('licenseeId', licensee.subLicenseeId)}')">${licensee.licenseeIdNo}</a>
                                                </td>

                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Licensee
                                                        Name</p>
                                                    <c:out value="${licensee.licenseeIdName}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Phone Number</p>
                                                    <c:out value="${licensee.phoneNo}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Office Number</p>
                                                    <c:out value="${licensee.officeNo}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Email Address</p>
                                                    <c:out value="${licensee.emailAddress}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Licensee's Address</p>
                                                    <c:out value="${licensee.licenseeAddress}"/>
                                                </td>
                                                <td style="vertical-align:middle;">
                                                    <p class="visible-xs visible-sm table-row-title">Organisation
                                                        Name</p>
                                                    <c:out value="${licensee.organisationName}"/>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                                </tbody>
                            </table>
                        </div>
                        <iais:action style="text-align:right;">
                            <a class="btn btn-secondary"
                               href="${pageContext.request.contextPath}/hcsa/enquiry/hcsa/Licensee-SearchResults-Download">Download</a>
                        </iais:action>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>
<%@include file="/WEB-INF/jsp/include/utils.jsp" %>
<script>
    function doClear() {
        $('input[type="text"]').val("");
        $('input[type="checkbox"]').prop("checked", false);
        $("select option").prop("selected", false);
        $(".clearSel").children(".current").text("Please Select");

    }


    function jumpToPagechangePage() {
        search();
    }

    function doSearch() {
        $('input[name="pageJumpNoTextchangePage"]').val(1);
        search();
    }

    function search() {
        showWaiting();
        $("[name='crud_action_type']").val('searchLis');
        $('#mainForm').submit();
    }

    function sortRecords(sortFieldName, sortType) {
        $("[name='crud_action_value']").val(sortFieldName);
        $("[name='crud_action_additional']").val(sortType);
        $("[name='crud_action_type']").val('searchLis');
        $('#mainForm').submit();
    }


    var fullDetailsView = function (submissionNo) {

        showWaiting();
        $("[name='crud_action_value']").val(submissionNo);
        $("[name='crud_action_type']").val('searchLic');
        $('#mainForm').submit();
    }
</script>

