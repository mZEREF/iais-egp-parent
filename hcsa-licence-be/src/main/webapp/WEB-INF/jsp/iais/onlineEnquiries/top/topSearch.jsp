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
<script type="text/javascript" src="<%=webrootCom%>js/onlineEnquiries/topSearch.js"></script>
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
                            <h2>Termination of Pregnancy Enquiry</h2>
                        </div>


                        <div class="col-xs-12 col-md-12">
                            <%--                            <iais:row>--%>
                            <%--                                <iais:field width="4" value="SEARCH" />--%>
                            <%--                                <div class="col-md-8">--%>
                            <%--                                </div>--%>
                            <%--                            </iais:row>--%>

                            <%--                            <hr>--%>

                            <iais:row>
                                <iais:field width="4" value="Business Name"/>
                                <iais:value width="4" cssClass="col-md-4">
                                    <iais:select name="centerName" id="centerName" firstOption="Please Select"
                                                 options="arCentreSelectOption"
                                                 cssClass="clearSel" value="${dsEnquiryTopFilterDto.centerName}"/>
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="Submission ID"/>
                                <iais:value width="4" cssClass="col-md-4">
                                    <input type="text" maxlength="100" id="submissionNo" name="submissionNo"
                                           value="${dsEnquiryTopFilterDto.submissionNo}">
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="Name of Patient"/>
                                <iais:value width="4" cssClass="col-md-4">
                                    <input type="text" maxlength="66" id="patientName" name="patientName"
                                           value="${dsEnquiryTopFilterDto.patientName}">
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="Patient ID Type"/>
                                <iais:value width="4" cssClass="col-md-4">
                                    <iais:select name="patientIdType" id="patientIdType" firstOption="Please Select"
                                                 codeCategory="CATE_ID_DS_ID_TYPE_DTV"
                                                 cssClass="clearSel" value="${dsEnquiryTopFilterDto.patientIdType}"/>
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="Patient ID Number"/>
                                <iais:value width="4" cssClass="col-md-4">
                                    <input type="text" maxlength="20" id="patientIdNo" name="patientIdNo"
                                           value="${dsEnquiryTopFilterDto.patientIdNo}">
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="Patient Date Of Birth"/>
                                <iais:value width="4" cssClass="col-md-4">
                                    <iais:datePicker id="birthDateFrom" name="birthDateFrom"
                                                     dateVal="${dsEnquiryTopFilterDto.birthDateFrom}"/>
                                </iais:value>
                                <label class="col-xs-1 col-md-1 control-label">To&nbsp;</label>
                                <iais:value width="3" cssClass="col-md-3">
                                    <iais:datePicker id="birthDateTo" name="birthDateTo"
                                                     dateVal="${dsEnquiryTopFilterDto.birthDateTo}"/>
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="Doctor's Professional Registration Number"/>
                                <iais:value width="4" cssClass="col-md-4">
                                    <input type="text" maxlength="20" id="doctorRegnNo" name="doctorRegnNo"
                                           value="${dsEnquiryTopFilterDto.doctorRegnNo}">
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="Submission Date"/>
                                <iais:value width="4" cssClass="col-md-4">
                                    <iais:datePicker id="submissionDateFrom" name="submissionDateFrom"
                                                     dateVal="${dsEnquiryTopFilterDto.submissionDateFrom}"/>
                                </iais:value>
                                <label class="col-xs-1 col-md-1 control-label">To&nbsp;</label>
                                <iais:value width="3" cssClass="col-md-3">
                                    <iais:datePicker id="submissionDateTo" name="submissionDateTo"
                                                     dateVal="${dsEnquiryTopFilterDto.submissionDateTo}"/>
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="Type of Termination of Pregnancy"/>
                                <iais:value width="4" cssClass="col-md-4">
                                    <iais:select name="topType" id="topType" firstOption="Please Select"
                                                     codeCategory="TOP_TYPE_TERMINATION_PREGNANCY"
                                                     cssClass="clearSel" value="${dsEnquiryTopFilterDto.topType}"/>
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="Gestation Age based on Ultrasound (Weeks)"/>
                                <iais:value width="4" cssClass="col-md-4">
                                    <input type="text" maxlength="2" id="weeksAge" name="weeksAge"
                                               value="${dsEnquiryTopFilterDto.weeksAge}">
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="Gestation Age based on Ultrasound (Days)"/>
                                <iais:value width="4" cssClass="col-md-4">
                                    <input type="text" maxlength="2" id="daysAge" name="daysAge"
                                               value="${dsEnquiryTopFilterDto.daysAge}">
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="Nationality"/>
                                <iais:value width="4" cssClass="col-md-4">
                                    <iais:select name="nationality" id="nationality" firstOption="Please Select" codeCategory="CATE_ID_NATIONALITY"
                                                 value="${dsEnquiryTopFilterDto.nationality}"
                                                 cssClass="nationalitySel"  onchange="toggleOnVal(this, 'NAT0001', '.rStatus')"/>
                                </iais:value>
                            </iais:row>
                            <c:if test="${dsEnquiryTopFilterDto.nationality !='NAT0001'}">
                                <iais:row cssClass="rStatus">
                                    <iais:field width="4" value="Residence Status"/>
                                    <iais:value width="4" cssClass="col-md-4">
                                        <iais:select name="status" id="status" firstOption="Please Select"
                                                     codeCategory="TOP_RESIDENCE_STATUS"
                                                     cssClass="clearSel" value="${dsEnquiryTopFilterDto.status}"/>
                                    </iais:value>
                                </iais:row>
                            </c:if>>
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
                        <iais:pagination param="topParam" result="topResult"/>
                        <div class="table-responsive">
                            <div class="table-gp">
                                <table aria-describedby="" class="table application-group"
                                       style="border-collapse:collapse;">
                                    <thead>
                                    <tr>

                                        <iais:sortableHeader needSort="true"
                                                             style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                             field="CENTER_NAME"
                                                             value="Business Name"/>
                                        <iais:sortableHeader needSort="true"
                                                             style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                             field="SUBMISSION_NO"
                                                             value="Submission ID"/>
                                        <iais:sortableHeader needSort="true"
                                                             style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                             field="PATIENT_NAME"
                                                             value="Patient Name"/>
                                        <iais:sortableHeader needSort="true"
                                                             style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                             field="PATIENT_ID_TYPE"
                                                             value="Patient ID Type"/>
                                        <iais:sortableHeader needSort="true"
                                                             style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                             field="PATIENT_ID_NO"
                                                             value="Patient ID No."/>
                                        <iais:sortableHeader needSort="true"
                                                             style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                             field="PATIENT_BIRTHDAY"
                                                             value="Patient Date of Birth"/>
                                        <iais:sortableHeader needSort="true"
                                                             style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                             field="DOCTOR_REGN_NO"
                                                             value="Doctor's Professional Regn / MCR No."/>
<%--                                        <iais:sortableHeader needSort="true"--%>
<%--                                                             style="white-space: nowrap;padding: 15px 25px 15px 0px;"--%>
<%--                                                             field="DOCTOR_NAME"--%>
<%--                                                             value="Doctor's Name"/>--%>
                                        <iais:sortableHeader needSort="true"
                                                             style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                             field="SUBMIT_DT"
                                                             value="Submission Date"/>
                                        <iais:sortableHeader needSort="true"
                                                             style="white-space: nowrap;padding: 15px 25px 15px 0px;"
                                                             field="D_STATUS"
                                                             value="Status"/>
                                    </tr>
                                    </thead>
                                    <tbody class="form-horizontal">
                                    <c:choose>
                                        <c:when test="${empty topResult.rows}">
                                            <tr>
                                                <td colspan="15">
                                                    <iais:message key="GENERAL_ACK018"
                                                                  escape="true"/>
                                                </td>
                                            </tr>
                                        </c:when>
                                        <c:otherwise>
                                            <c:forEach var="top"
                                                       items="${topResult.rows}"
                                                       varStatus="status">
                                                <tr id="advfilter${(status.index + 1) + (topParam.pageNo - 1) * topParam.pageSize}">
                                                    <td style="vertical-align:middle;">
                                                        <p class="visible-xs visible-sm table-row-title">Business Name</p>
                                                        <p style="white-space: nowrap;"><c:out
                                                                value="${top.centerName}"/>
                                                            <c:if test="${top.patientCount > 1}">
                                                                <a href="javascript:void(0);"
                                                                   class="accordion-toggle  collapsed"
                                                                   style="float: right;color: #2199E8"
                                                                   data-toggle="collapse"
                                                                   data-target="#dropdown${(status.index + 1) + (topParam.pageNo - 1) * topParam.pageSize}"
                                                                   onclick="getTopByIdType('${top.patientIdNo}','${top.patientIdType}','${(status.index + 1) + (topParam.pageNo - 1) * topParam.pageSize}')">
                                                                </a>
                                                            </c:if>
                                                        </p>

                                                    </td>
                                                    <td style="vertical-align:middle;">
                                                        <p class="visible-xs visible-sm table-row-title">Submission
                                                            ID</p>
                                                        <a href="#"
                                                           onclick="fullDetailsView('${top.submissionNo}')">${top.submissionNo}</a>
                                                    </td>
                                                    <td style="vertical-align:middle;">
                                                        <p class="visible-xs visible-sm table-row-title">Patient
                                                            Name</p>
                                                        <c:out value="${top.patientName}"/>
                                                    </td>
                                                    <td style="vertical-align:middle;">
                                                        <p class="visible-xs visible-sm table-row-title">Patient ID
                                                            Type</p>
                                                        <iais:code code="${top.patientIdType}"/>
                                                    </td>
                                                    <td style="vertical-align:middle;">
                                                        <p class="visible-xs visible-sm table-row-title">Patient ID
                                                            No.</p>
                                                        <c:out value="${top.patientIdNo}"/>
                                                    </td>
                                                    <td style="vertical-align:middle;">
                                                        <p class="visible-xs visible-sm table-row-title">Patient Date of
                                                            Birth</p>
                                                        <fmt:formatDate
                                                                value="${top.patientBirthday}"
                                                                pattern="${AppConsts.DEFAULT_DATE_FORMAT}"/>
                                                    </td>
                                                    <td style="vertical-align:middle;">
                                                        <p class="visible-xs visible-sm table-row-title">Doctor's Professional Regn / MCR No.</p>
                                                        <c:out value="${top.doctorRegnNo}"/>
                                                    </td>
<%--                                                    <td style="vertical-align:middle;">--%>
<%--                                                        <p class="visible-xs visible-sm table-row-title">Doctor's--%>
<%--                                                            Name</p>--%>
<%--                                                        <c:out value="${top.doctorName}"/>--%>
<%--                                                    </td>--%>
                                                    <td style="vertical-align:middle;">
                                                        <p class="visible-xs visible-sm table-row-title">Submission
                                                            Date</p>
                                                        <fmt:formatDate
                                                                value="${top.submitDt}"
                                                                pattern="${AppConsts.DEFAULT_DATE_FORMAT}"/>
                                                    </td>
                                                    <td style="vertical-align:middle;">
                                                        <p class="visible-xs visible-sm table-row-title">Status</p>
                                                        <iais:code code="${top.dstatus}"/>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </c:otherwise>
                                    </c:choose>
                                    </tbody>
                                </table>
                            </div>

                        </div>
<%--                        <iais:action style="text-align:right;">--%>
<%--                            <a class="btn btn-secondary"--%>
<%--                               href="${pageContext.request.contextPath}/hcsa/enquiry/ar/TOP-SearchResults-DownloadS">Download</a>--%>
<%--                        </iais:action>--%>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>
<%@include file="/WEB-INF/jsp/include/utils.jsp" %>
<script>
    function toggleOnVal(sel, val, elem) {
        toggleOnSelects(sel, val, $(sel).closest('.form-group').siblings(elem));
    }
    function toggleOnSelects(sel, val, elem) {
        var $selector = getJqueryNode(sel);
        var $target = getJqueryNode(elem);
        if (isEmpty($selector) || isEmpty($target)){
            return;
        }
        console.log("val - " + val);
        console.log("select val * " + $selector.val())
        if ($selector.val() != val) {
            $target.show();
            $target.removeClass('hidden');
        } else {
            $target.hide();
            $target.addClass('hidden');
            clearFields($target);
        }
        $target.each(function (i, ele) {
            if ('select' == ele.tagName.toLowerCase()) {
                updateSelectTag($(ele));
            }
        });
    }

    function updateSelectTag($sel) {
        if ($sel.is('select[multiple]')) {
            // mutiple select
            $sel.trigger('change.multiselect');
        } else {
            $sel.niceSelect("update");
        }
    }

    function clearFields(targetSelector) {
        var $selector = getJqueryNode(targetSelector);
        if (isEmpty($selector)) {
            return;
        }
        if (!$selector.is(":input")) {
            $selector.find("span[name='iaisErrorMsg']").each(function () {
                $(this).html("");
            });
            $selector = $selector.find(':input[class!="not-clear"]');
        }
        if ($selector.length <= 0) {
            return;
        }
        $selector.each(function() {
            var type = this.type, tag = this.tagName.toLowerCase();
            if (!$(this).hasClass('not-clear')) {
                if (type == 'text' || type == 'password' || type == 'hidden' || tag == 'textarea') {
                    this.value = '';
                } else if (type == 'checkbox') {
                    this.checked = false;
                } else if (type == 'radio') {
                    this.checked = false;
                } else if (tag == 'select') {
                    this.selectedIndex = 0;
                    updateSelectTag($(this));
                }
            }
        });
    }
</script>
