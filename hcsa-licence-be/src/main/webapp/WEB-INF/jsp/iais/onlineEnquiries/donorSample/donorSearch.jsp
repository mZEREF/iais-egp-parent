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
<webui:setLayout name="iais-intranet"/>
<div class="main-content dashboard">
    <form id="mainForm"  method="post" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <input type="hidden" name="enquiryDonarSubNo" id="enquiryDonarSubNo"/>
        <div class="col-lg-12 col-xs-12">
            <div class="center-content">
                <div class="intranet-content">
                    <div class="row form-horizontal">
                        <div class="bg-title col-xs-12 col-md-12">
                            <h2>Assisted Reproduction Enquiry</h2>
                        </div>

                        <div class="col-xs-12 col-md-12">
                            <iais:row>
                                <iais:field width="4" value="SEARCH" />
                                <div class="col-md-8">
                                </div>
                            </iais:row>

                            <hr>

                            <iais:row>
                                <iais:field width="4" value="AR Centre" />
                                <iais:value width="4" cssClass="col-md-4">
                                    <iais:select name="arCentre" id="arCentre" firstOption="Please Select" options="arCentreSelectOption"
                                                 cssClass="clearSel"  value="${arEnquiryDonorSampleFilterDto.arCentre}"  />
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="Donor Sample Code"/>
                                <iais:value width="4" cssClass="col-md-4" >
                                    <input type="text" maxlength="20" id="donorSampleCode"  name="donorSampleCode" value="<c:out value="${arEnquiryDonorSampleFilterDto.donorSampleCode}"/>" >
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="Sample Type"/>
                                <iais:value width="4" cssClass="col-md-4">
                                    <iais:select name="sampleType" id="sampleType" firstOption="Please Select" codeCategory="AR_DONOR_SAMPLE_TYPE"
                                                 cssClass="clearSel"   value="${arEnquiryDonorSampleFilterDto.sampleType}" />
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="Name of Bank / AR Centre where sample is from" />
                                <iais:value width="4" cssClass="col-md-4">
                                    <iais:select name="sampleHciCode" id="sampleHciCode" firstOption="Please Select" options="arCentreSelectOptionFrom"
                                                 cssClass="clearSel"   value="${arEnquiryDonorSampleFilterDto.sampleHciCode}"  />
                                </iais:value>
                                <iais:value width="4" cssClass="col-md-4">
                                    <div id="othersDisplay" <c:if test="${arEnquiryDonorSampleFilterDto.sampleHciCode!='AR_SC_001'}">style="display: none"</c:if> >
                                        <input type="text" maxlength="66" id="othersSampleHciCode"  name="othersSampleHciCode" value="<c:out value="${arEnquiryDonorSampleFilterDto.othersSampleHciCode}"/>" >

                                    </div>
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="Donor ID Type"/>
                                <iais:value width="4" cssClass="col-md-4">
                                    <iais:select name="donorIdType" id="donorIdType" firstOption="Please Select" codeCategory="CATE_ID_DS_ID_TYPE_DTV"
                                                 cssClass="clearSel"  value="${arEnquiryDonorSampleFilterDto.donorIdType}" />
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="Donor ID No."/>
                                <iais:value width="4" cssClass="col-md-4"  >
                                    <input type="text" maxlength="20" id="donorIdNumber"  name="donorIdNumber" value="<c:out value="${arEnquiryDonorSampleFilterDto.donorIdNumber}"/>" >
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="No of Live Birth Events Recorded" />
                                <div class="col-md-8 col-sm-4 row ">
                                    <iais:value width="2" cssClass="col-md-2  row">
                                        <div class="form-check">
                                            <input class="form-check-input"
                                                   type="checkbox"
                                                   name="birthEventsTotal0" id="birthEventsTotal0"
                                                   <c:if test="${ arEnquiryDonorSampleFilterDto.birthEventsTotal0 =='on'  }">checked</c:if>
                                                   aria-invalid="false">
                                            <label class="form-check-label"
                                                   for="birthEventsTotal0"><span
                                                    class="check-square"></span>0</label>
                                        </div>
                                    </iais:value>
                                    <iais:value width="2" cssClass="col-md-2 row">
                                        <div class="form-check">
                                            <input class="form-check-input" type="checkbox"
                                                   name="birthEventsTotal1" id="birthEventsTotal1"
                                                   <c:if test="${arEnquiryDonorSampleFilterDto.birthEventsTotal1 == 'on' }">checked</c:if>
                                                   aria-invalid="false">
                                            <label class="form-check-label"
                                                   for="birthEventsTotal1"><span
                                                    class="check-square"></span>1</label>
                                        </div>
                                    </iais:value>
                                    <iais:value width="2" cssClass="col-md-2 row">
                                        <div class="form-check">
                                            <input class="form-check-input" type="checkbox"
                                                   name="birthEventsTotal2" id="birthEventsTotal2"
                                                   <c:if test="${arEnquiryDonorSampleFilterDto.birthEventsTotal2 == 'on' }">checked</c:if>
                                                   aria-invalid="false">
                                            <label class="form-check-label"
                                                   for="birthEventsTotal2"><span
                                                    class="check-square"></span>2</label>
                                        </div>
                                    </iais:value>
                                    <iais:value width="2" cssClass="col-md-2 row">
                                        <div class="form-check">
                                            <input class="form-check-input" type="checkbox"
                                                   name="birthEventsTotal3" id="birthEventsTotal3"
                                                   <c:if test="${arEnquiryDonorSampleFilterDto.birthEventsTotal3 == 'on' }">checked</c:if>
                                                   aria-invalid="false">
                                            <label class="form-check-label"
                                                   for="birthEventsTotal3"><span
                                                    class="check-square"></span>3</label>
                                        </div>
                                    </iais:value>
                                    <iais:value width="2" cssClass="col-md-2 row">
                                        <div class="form-check">
                                            <input class="form-check-input" type="checkbox"
                                                   name="birthEventsTotalMax" id="birthEventsTotalMax"
                                                   <c:if test="${arEnquiryDonorSampleFilterDto.birthEventsTotalMax == 'on' }">checked</c:if>
                                                   aria-invalid="false">
                                            <label class="form-check-label"
                                                   for="birthEventsTotalMax"><span
                                                    class="check-square"></span>>3</label>
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

                            <iais:pagination param="donorSampleParam" result="donorSampleResult"/>
                            <div class="table-responsive">
                                <div class="table-gp">
                                    <table aria-describedby="" class="table application-group" style="border-collapse:collapse;">
                                        <thead>
                                        <tr >

                                            <iais:sortableHeader needSort="true" style="white-space: nowrap;padding: 15px 30px 15px 0px;"
                                                                 field="DONOR_NAME"
                                                                 value="Donor's Name"/>
                                            <iais:sortableHeader needSort="true" style="white-space: nowrap;padding: 15px 30px 15px 0px;"
                                                                 field="DONOR_SAMPLE_CODE"
                                                                 value="Donor Sample Code / ID No."/>
                                            <iais:sortableHeader needSort="true" style="white-space: nowrap;padding: 15px 30px 15px 0px;"
                                                                 field="SAMPLE_FROM_HCI_CODE"
                                                                 value="Name of Bank / AR Centre where sample is collected"/>
                                            <iais:sortableHeader needSort="true" style="white-space: nowrap;padding: 15px 30px 15px 0px;"
                                                                 field="age_no"
                                                                 value="Total No of Live Birth Events Recorded"/>

                                            <iais:sortableHeader needSort="false" style="white-space: nowrap;padding: 15px 30px 15px 0px;"
                                                                 field=""
                                                                 value="Action"/>
                                        </tr>
                                        </thead>
                                        <tbody class="form-horizontal">
                                        <c:choose>
                                            <c:when test="${empty donorSampleResult.rows}">
                                                <tr>
                                                    <td colspan="15">
                                                        <iais:message key="GENERAL_ACK018"
                                                                      escape="true"/>
                                                    </td>
                                                </tr>
                                            </c:when>
                                            <c:otherwise>
                                                <c:forEach var="donorSample"
                                                           items="${donorSampleResult.rows}"
                                                           varStatus="status">
                                                    <tr >
                                                        <td style="vertical-align:middle;">
                                                            <p class="visible-xs visible-sm table-row-title">Donor's Name</p>
                                                            <c:out value="${donorSample.donorName}"/>
                                                        </td>
                                                        <td style="vertical-align:middle;">
                                                            <p class="visible-xs visible-sm table-row-title">Donor Sample Code / ID No.</p>
                                                            <c:if test="${donorSample.sampleType eq 'DONTY004'
                                                        || donorSample.sampleType eq 'DONTY005'}">
                                                                <c:out value="${donorSample.mdonorSampleCode}"/>
                                                            </c:if>
                                                            <c:if test="${donorSample.sampleType != 'DONTY004'
                                                        && donorSample.sampleType != 'DONTY005'}">
                                                                <c:out value="${donorSample.donorSampleCode}"/>
                                                            </c:if>
                                                        </td>
                                                        <td style="vertical-align:middle;">
                                                            <p class="visible-xs visible-sm table-row-title">Name of Bank / AR Centre where sample is collected</p>
                                                            <c:out value="${donorSample.sampleHciCode}"/>
                                                        </td>
                                                        <td style="vertical-align:middle;">
                                                            <p class="visible-xs visible-sm table-row-title">Total No of Live Birth Events Recorded</p>
                                                            <c:out value="${donorSample.ageNumber}"/>
                                                            <c:if test="${donorSample.ageNumber>=donorResultSize}">
                                                                <a class="btn-tooltip styleguide-tooltip" data-toggle="tooltip" data-html="true" href="javascript:void(0);"
                                                                   title="&lt;p&gt;${donorMessageTip}&lt;/p&gt;"
                                                                   style="z-index: 10"
                                                                   data-original-title="">i</a>
                                                            </c:if>
                                                        </td>
                                                        <td style="vertical-align:middle;">
                                                            <p class="visible-xs visible-sm table-row-title">Action</p>
                                                            <button type="button" onclick="fullDetailsView('<iais:mask name="enquiryDonarSubNo" value="${donorSample.submissionIdNo}"/>','${donorSample.sampleHciCode}')" class="btn btn-default btn-sm">
                                                                View Full Details
                                                            </button>
                                                        </td>



                                                    </tr>
                                                </c:forEach>
                                            </c:otherwise>
                                        </c:choose>
                                        </tbody>
                                    </table>
                                </div>

                            </div>

                        </div>
                </div>
            </div>
        </div>
    </form>
</div>
<%@include file="/WEB-INF/jsp/include/utils.jsp" %>
<script type="text/javascript" src="<%=webrootCom%>js/onlineEnquiries/donorSearch.js"></script>
