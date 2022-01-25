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
    String webrootCom=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.COMMON_CSS_ROOT;
%>
<style>

    thead > tr > th > span {
        line-height: 0px;
    }
</style>
<script type="text/javascript" src="<%=webrootCom%>js/onlineEnquiries/donorSearch.js"></script>
<webui:setLayout name="iais-intranet"/>
<div class="main-content dashboard">
    <form id="mainForm"  method="post" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>

        <div class="col-lg-12 col-xs-12">
            <div class="center-content">
                <div class="intranet-content">
                    <div class="row form-horizontal">
                        <div class="bg-title col-xs-12 col-md-12">
                            <h2>Lab-developed Tests Enquiry</h2>
                        </div>


                        <div class="col-xs-12 col-md-12">

                            <iais:row>
                                <iais:field width="4" value="Name of Laboratory"/>
                                <iais:value width="4" cssClass="col-md-4" >
                                    <input type="text"  id="laboratoryName"  name="laboratoryName" value="${dsLaboratoryDevelopTestEnquiryFilterDto.laboratoryName}" >
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="Name of LDT Test"/>
                                <iais:value width="4" cssClass="col-md-4" >
                                    <input type="text" maxlength="50" id="ldtTestName"  name="ldtTestName" value="${dsLaboratoryDevelopTestEnquiryFilterDto.ldtTestName}" >
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="Date LDT was made or will be made available"/>
                                <iais:value width="4" cssClass="col-md-4">
                                    <iais:datePicker id="ldtDateFrom" name="ldtDateFrom" dateVal="${dsLaboratoryDevelopTestEnquiryFilterDto.ldtDateFrom}"/>
                                </iais:value>
                                <iais:value width="4" cssClass="col-md-4">
                                    <iais:datePicker id="ldtDateTo" name="ldtDateTo" dateVal="${dsLaboratoryDevelopTestEnquiryFilterDto.ldtDateTo}"/>
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="Person responsible for the test"/>
                                <iais:value width="4" cssClass="col-md-4" >
                                    <input type="text" maxlength="66" id="responsePerson"  name="responsePerson" value="${dsLaboratoryDevelopTestEnquiryFilterDto.responsePerson}" >
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="Status of Test" />
                                <iais:value width="4" cssClass="col-md-4">
                                    <div class="form-check">
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
                                <iais:value width="4" cssClass="col-md-4">
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

                    <div class="col-xs-12">
                        <div class="components">

                            <iais:pagination param="ldtParam" result="ldtResult"/>
                            <div class="table-responsive">
                                <div class="table-gp">
                                    <table aria-describedby="" class="table application-group" style="border-collapse:collapse;">
                                        <thead>
                                        <tr >
                                            <th scope="col">S/N</th>
                                            <iais:sortableHeader needSort="true"
                                                                 field="LDT_NAME"
                                                                 value="Name of Laboratory"/>
                                            <iais:sortableHeader needSort="true"
                                                                 field="LDT_TEST_NAME"
                                                                 value="Name of LDT Test"/>
                                            <iais:sortableHeader needSort="false"
                                                                 field="INTENDED_PURPOSE"
                                                                 value="Intended Purpose of Test"/>
                                            <iais:sortableHeader needSort="true"
                                                                 field="LDT_DATE"
                                                                 value="Date LDT was made or will be made available"/>
                                            <iais:sortableHeader needSort="true"
                                                                 field="RESPONSE_PERSON"
                                                                 value="Person responsible for the test"/>
                                            <iais:sortableHeader needSort="true"
                                                                 field="DESIGNATION"
                                                                 value="Designation"/>
                                            <iais:sortableHeader needSort="true"
                                                                 field="TEST_STATUS"
                                                                 value="Status of Test"/>
                                            <iais:sortableHeader needSort="true"
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
                                                            <c:out value="${status.index + 1+ (ldtParam.pageNo - 1) * ldtParam.pageSize}"/>
                                                        </td>
                                                        <td style="vertical-align:middle;">
                                                            <c:out value="${ldt.laboratoryName}"/>
                                                        </td>
                                                        <td style="vertical-align:middle;">
                                                            <c:out value="${ldt.ldtTestName}"/>
                                                        </td>
                                                        <td style="vertical-align:middle;">
                                                            <c:out value="${ldt.intendedPurpose}"/>
                                                        </td>
                                                        <td style="vertical-align:middle;">
                                                            <fmt:formatDate
                                                                    value="${ldt.ldtDate}"
                                                                    pattern="${AppConsts.DEFAULT_DATE_FORMAT}"/>
                                                        </td>
                                                        <td style="vertical-align:middle;">
                                                            <c:out value="${ldt.responsePerson}"/>
                                                        </td>
                                                        <td style="vertical-align:middle;">
                                                            <c:out value="${ldt.designation}"/>
                                                        </td>
                                                        <td style="vertical-align:middle;">
                                                            <c:out value="${ldt.testStatus}"/>
                                                        </td>
                                                        <td style="vertical-align:middle;">
                                                            <c:out value="${ldt.remarks}"/>
                                                        </td>

                                                    </tr>
                                                </c:forEach>
                                            </c:otherwise>
                                        </c:choose>
                                        </tbody>
                                    </table>
                                </div>

                            </div>

                            <iais:action style="text-align:right;">
                                <a class="btn btn-secondary"
                                   href="${pageContext.request.contextPath}/hcsa/enquiry/ar/DonorSample-SearchResults-DownloadS">Download</a>
                            </iais:action>
                        </div>
                    </div>


                </div>
            </div>
        </div>
    </form>
</div>
<%@include file="/WEB-INF/jsp/include/utils.jsp" %>
<script type="text/javascript">


    function doClear() {
        $('input[type="text"]').val("");
        $('input[type="checkbox"]').prop("checked", false);
        $("option:first").prop("selected", 'selected');
        $(".current").text("Please Select");
        $('.date_picker').val("");
        $(".multi-select-button").html("-- Select --");
        $('#cycleStageDisplay').attr("style","display: none");
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
        $("[name='crud_action_type']").val('search');
        $('#mainForm').submit();
    }

    function sortRecords(sortFieldName, sortType) {
        $("[name='crud_action_value']").val(sortFieldName);
        $("[name='crud_action_additional']").val(sortType);
        $("[name='crud_action_type']").val('search');
        $('#mainForm').submit();
    }


    var fullDetailsView = function (submissionNo) {

        showWaiting();
        $("[name='crud_action_value']").val(submissionNo);
        $("[name='crud_action_type']").val('viewInfo');
        $('#mainForm').submit();
    }


</script>