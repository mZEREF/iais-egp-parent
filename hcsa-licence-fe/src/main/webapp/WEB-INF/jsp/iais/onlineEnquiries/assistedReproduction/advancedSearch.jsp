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
<script type="text/javascript" src="<%=webrootCom%>js/onlineEnquiries/arAdvancedSearch.js"></script>
<webui:setLayout name="iais-internet"/>
<%@include file="../../common/dashboard.jsp"%>
<form id="mainForm"  method="post" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" name="adv_action_type" id="adv_action_type"/>

    <div class="container">
        <div class="row">
            <div class="col-xs-12">
                <div class="row form-horizontal">

                    <div class="form-group">
                        <div class="col-xs-12 col-md-12">
                            <div class="col-xs-12 col-md-12">
                                <div class="components">
                                    <a class="btn btn-secondary" data-toggle="collapse"
                                       data-target="#searchCondition" aria-expanded="true">Filter</a>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div id="searchCondition" class="collapse" aria-expanded="true" >
                        <%--                            Details of Patient--%>
                        <div class="col-xs-10 col-md-10">
                            <iais:row>
                                <iais:field width="4" value="Details of Patient" />
                            </iais:row>
                            <hr>
                            <iais:row>
                                <iais:field width="4" value="Patient Name"/>
                                <iais:value width="6" cssClass="col-md-6" >
                                    <input type="text"  id="patientName"  name="patientName" value="${assistedReproductionEnquiryFilterDto.patientName}" >
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="Patient ID Type"/>
                                <iais:value width="6" cssClass="col-md-6"  >
                                    <iais:select name="patientIdType" id="patientIdType" firstOption="Please Select" codeCategory="CATE_ID_DS_ID_TYPE"
                                                 value="${assistedReproductionEnquiryFilterDto.patientIdType}" />
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="Patient ID No."/>
                                <iais:value width="6" cssClass="col-md-6"  >
                                    <input type="text" maxlength="20" id="patientIdNumber"  name="patientIdNumber" value="${assistedReproductionEnquiryFilterDto.patientIdNumber}" >
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="Patient's Age as of This Cycle"/>
                                <iais:value width="3" cssClass="col-md-3">
                                    <input type="number" oninput="if(value.length>2)value=value.slice(0,2)" style="margin-bottom: 0px;" id="patientAgeNumberFrom"  name="patientAgeNumberFrom" value="${assistedReproductionEnquiryFilterDto.patientAgeNumberFrom}" >
                                </iais:value>
                                <iais:value width="3" cssClass="col-md-3">
                                    <input type="number" oninput="if(value.length>2)value=value.slice(0,2)" style="margin-bottom: 0px;" id="patientAgeNumberTo"  name="patientAgeNumberTo" value="${assistedReproductionEnquiryFilterDto.patientAgeNumberTo}" >
                                </iais:value>
                            </iais:row>
                        </div>
                        <%--                            Details of Submission--%>
                        <div class="col-xs-10 col-md-10">
                            <iais:row>
                                <iais:field width="4" value="Details of Submission" />
                            </iais:row>
                            <hr>
                            <iais:row>
                                <iais:field width="4" value="Submission ID"/>
                                <iais:value width="6" cssClass="col-md-6"  >
                                    <input type="text"  id="submissionId"  name="submissionId" value="${assistedReproductionEnquiryFilterDto.submissionId}" >
                                </iais:value>
                            </iais:row>


                            <iais:row>
                                <iais:field width="4" value="Submission Date"/>
                                <iais:value width="3" cssClass="col-md-3">
                                    <iais:datePicker id="submissionDateFrom" name="submissionDateFrom" dateVal="${assistedReproductionEnquiryFilterDto.submissionDateFrom}"/>
                                </iais:value>
                                <iais:value width="3" cssClass="col-md-3">
                                    <iais:datePicker id="submissionDateTo" name="submissionDateTo" dateVal="${assistedReproductionEnquiryFilterDto.submissionDateTo}"/>
                                </iais:value>
                            </iais:row>
                        </div>
                        <%--                            Details of Husband--%>
                        <div class="col-xs-10 col-md-10">
                            <iais:row>
                                <iais:field width="4" value="Details of Husband" />
                            </iais:row>
                            <hr>
                            <iais:row>
                                <iais:field width="4" value="Husband Name"/>
                                <iais:value width="6" cssClass="col-md-6"  >
                                    <input type="text"  id="husbandName"  name="husbandName" value="${assistedReproductionEnquiryFilterDto.husbandName}" >
                                </iais:value>
                            </iais:row>

                            <iais:row>
                                <iais:field width="4" value="Husband ID Type"/>
                                <iais:value width="6" cssClass="col-md-6"  >
                                    <iais:select name="husbandIdType" id="husbandIdType" firstOption="Please Select" codeCategory="CATE_ID_DS_ID_TYPE"
                                                 value="${assistedReproductionEnquiryFilterDto.husbandIdType}" />
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="Husband ID No."/>
                                <iais:value width="6" cssClass="col-md-6"  >
                                    <input type="text"  id="husbandIdNumber"  name="husbandIdNumber" value="${assistedReproductionEnquiryFilterDto.husbandIdNumber}" >
                                </iais:value>
                            </iais:row>
                        </div>
                        <%--                            Details of AR Centre--%>
                        <div class="col-xs-10 col-md-10">
                            <iais:row>
                                <iais:field width="4" value="Details of AR Centre" />
                            </iais:row>
                            <hr>
                            <iais:row>
                                <iais:field width="4" value="AR Centre" />
                                <iais:value width="6" cssClass="col-md-6">
                                    <iais:select name="arCentre" id="arCentre" firstOption="Please Select" options="arCentreSelectOption"
                                                 value="${assistedReproductionEnquiryFilterDto.arCentre}"  />
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="Embryologist"/>
                                <iais:value width="6" cssClass="col-md-6"  >
                                    <input type="text"  id="embryologist"  name="embryologist" value="${assistedReproductionEnquiryFilterDto.embryologist}" >
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="AR Practitioner"/>
                                <iais:value width="6" cssClass="col-md-6"  >
                                    <input type="text"  id="arPractitioner"  name="arPractitioner" value="${assistedReproductionEnquiryFilterDto.arPractitioner}" >
                                </iais:value>
                            </iais:row>

                        </div>
                        <%--                            Details of Cycle Stages--%>
                        <div class="col-xs-10 col-md-10">
                            <iais:row>
                                <iais:field width="4" value="Details of Cycle Stages" />
                            </iais:row>
                            <hr>
                            <iais:row>
                                <iais:field width="4" value="Status"/>
                                <iais:value width="6" cssClass="col-md-6"  >
                                    <iais:select name="cycleStagesStatus" id="cycleStagesStatus" firstOption="Please Select" options="cycleStagesStatusOptions"
                                                 value="${assistedReproductionEnquiryFilterDto.cycleStagesStatus}" />
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="Cycle Start Date"/>
                                <iais:value width="3" cssClass="col-md-3">
                                    <iais:datePicker id="cycleStagesDateFrom" name="cycleStagesDateFrom" dateVal="${assistedReproductionEnquiryFilterDto.cycleStagesDateFrom}"/>
                                </iais:value>
                                <iais:value width="3" cssClass="col-md-3">
                                    <iais:datePicker id="cycleStagesDateTo" name="cycleStagesDateTo" dateVal="${assistedReproductionEnquiryFilterDto.cycleStagesDateTo}"/>
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="AR / IUI Cycle"/>
                                <iais:value width="6" cssClass="col-md-6"  >
                                    <iais:select name="arOrIuiCycle" id="arOrIuiCycle" firstOption="Please Select" options="aRorIUICycleOptions"
                                                 value="${assistedReproductionEnquiryFilterDto.arOrIuiCycle}" />
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="IVM" />
                                <iais:value width="3" cssClass="col-md-3">
                                    <div class="form-check">
                                        <input class="form-check-input"
                                               type="radio"
                                               name="IVM"
                                               value="1"
                                               id="IVMYes"
                                               <c:if test="${ assistedReproductionEnquiryFilterDto.IVM =='1' }">checked</c:if>
                                               aria-invalid="false">
                                        <label class="form-check-label"
                                               for="IVMYes"><span
                                                class="check-circle"></span>Yes</label>
                                    </div>
                                </iais:value>
                                <iais:value width="3" cssClass="col-md-3">
                                    <div class="form-check">
                                        <input class="form-check-input" type="radio"
                                               name="IVM" value="0" id="IVMNo"
                                               <c:if test="${assistedReproductionEnquiryFilterDto.IVM == '0'}">checked</c:if>
                                               aria-invalid="false">
                                        <label class="form-check-label"
                                               for="IVMNo"><span
                                                class="check-circle"></span>No</label>
                                    </div>
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="Current AR Treatment" />
                                <div class="col-md-6">
                                    <iais:value width="6" cssClass="col-md-12">
                                        <div class="form-check">
                                            <input class="form-check-input"
                                                   type="checkbox"
                                                   name="freshCycleNatural" id="freshCycleNatural"
                                                   <c:if test="${ assistedReproductionEnquiryFilterDto.freshCycleNatural =='on'  }">checked</c:if>
                                                   aria-invalid="false">
                                            <label class="form-check-label"
                                                   for="freshCycleNatural"><span
                                                    class="check-square"></span><iais:code code="AR_CAT_001"/></label>
                                        </div>
                                    </iais:value>
                                    <iais:value width="6" cssClass="col-md-12">
                                        <div class="form-check">
                                            <input class="form-check-input" type="checkbox"
                                                   name="freshCycleSimulated"  id="freshCycleSimulated"
                                                   <c:if test="${assistedReproductionEnquiryFilterDto.freshCycleSimulated == 'on' }">checked</c:if>
                                                   aria-invalid="false">
                                            <label class="form-check-label"
                                                   for="freshCycleSimulated"><span
                                                    class="check-square"></span><iais:code code="AR_CAT_002"/></label>
                                        </div>
                                    </iais:value>
                                    <iais:value width="6" cssClass="col-md-12">
                                        <div class="form-check">
                                            <input class="form-check-input" type="checkbox"
                                                   name="frozenOocyteCycle"  id="frozenOocyteCycle"
                                                   <c:if test="${assistedReproductionEnquiryFilterDto.frozenOocyteCycle == 'on' }">checked</c:if>
                                                   aria-invalid="false">
                                            <label class="form-check-label"
                                                   for="frozenOocyteCycle"><span
                                                    class="check-square"></span><iais:code code="AR_CAT_003"/></label>
                                        </div>
                                    </iais:value>
                                    <iais:value width="6" cssClass="col-md-12">
                                        <div class="form-check">
                                            <input class="form-check-input" type="checkbox"
                                                   name="frozenEmbryoCycle"  id="frozenEmbryoCycle"
                                                   <c:if test="${assistedReproductionEnquiryFilterDto.frozenEmbryoCycle == 'on' }">checked</c:if>
                                                   aria-invalid="false">
                                            <label class="form-check-label"
                                                   for="frozenEmbryoCycle"><span
                                                    class="check-square"></span><iais:code code="AR_CAT_004"/></label>
                                        </div>
                                    </iais:value>
                                </div>

                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="No. of Fresh cycle(s) previously undergone by patient"/>
                                <iais:value width="3" cssClass="col-md-3">
                                    <input type="number" oninput="if(value.length>2)value=value.slice(0,2)" style="margin-bottom: 0px;"  id="freshCycleNumFrom"  name="freshCycleNumFrom" value="${assistedReproductionEnquiryFilterDto.freshCycleNumFrom}" >
                                </iais:value>
                                <iais:value width="3" cssClass="col-md-3">
                                    <input type="number" oninput="if(value.length>2)value=value.slice(0,2)" style="margin-bottom: 0px;"  id="freshCycleNumTo"  name="freshCycleNumTo" value="${assistedReproductionEnquiryFilterDto.freshCycleNumTo}" >
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="Abandoned Cycle" />
                                <iais:value width="3" cssClass="col-md-3">
                                    <div class="form-check">
                                        <input class="form-check-input"
                                               type="radio"
                                               name="abandonedCycle" id="abandonedCycleYes"
                                               value="1"
                                               <c:if test="${ assistedReproductionEnquiryFilterDto.abandonedCycle =='1' }">checked</c:if>
                                               aria-invalid="false">
                                        <label class="form-check-label"
                                               for="abandonedCycleYes"><span
                                                class="check-circle"></span>Yes</label>
                                    </div>
                                </iais:value>
                                <iais:value width="3" cssClass="col-md-3">
                                    <div class="form-check">
                                        <input class="form-check-input" type="radio"
                                               name="abandonedCycle" value="0" id="abandonedCycleNo"
                                               <c:if test="${assistedReproductionEnquiryFilterDto.abandonedCycle == '0'}">checked</c:if>
                                               aria-invalid="false">
                                        <label class="form-check-label"
                                               for="abandonedCycleNo"><span
                                                class="check-circle"></span>No</label>
                                    </div>
                                </iais:value>
                            </iais:row>


                        </div>
                        <%--                            Details of Donor Sample--%>
                        <div class="col-xs-10 col-md-10">
                            <iais:row>
                                <iais:field width="4" value="Details of Donor Sample" />
                            </iais:row>
                            <hr>
                            <iais:row>
                                <iais:field width="4" value="Donor Gamete (i.e. Oocyte/Embryo/Sperm) Used" />
                                <iais:value width="3" cssClass="col-md-3">
                                    <div class="form-check">
                                        <input class="form-check-input"
                                               type="radio"
                                               name="donorGameteUsed" id="donorGameteUsedYes"
                                               value="1"
                                               <c:if test="${ assistedReproductionEnquiryFilterDto.donorGameteUsed =='1' }">checked</c:if>
                                               aria-invalid="false">
                                        <label class="form-check-label"
                                               for="donorGameteUsedYes"><span
                                                class="check-circle"></span>Yes</label>
                                    </div>
                                </iais:value>
                                <iais:value width="3" cssClass="col-md-3">
                                    <div class="form-check">
                                        <input class="form-check-input" type="radio"
                                               name="donorGameteUsed" value="0" id="donorGameteUsedNo"
                                               <c:if test="${assistedReproductionEnquiryFilterDto.donorGameteUsed == '0'}">checked</c:if>
                                               aria-invalid="false">
                                        <label class="form-check-label"
                                               for="donorGameteUsedNo"><span
                                                class="check-circle"></span>No</label>
                                    </div>
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="Donor's Name"/>
                                <iais:value width="6" cssClass="col-md-6" >
                                    <input type="text"  id="donorName"  name="donorName" value="${assistedReproductionEnquiryFilterDto.donorName}" >
                                </iais:value>
                            </iais:row>

                            <iais:row>
                                <iais:field width="4" value="Donor's ID No."/>
                                <iais:value width="6" cssClass="col-md-6"  >
                                    <input type="text"  id="donorIdNumber"  name="donorIdNumber" value="${assistedReproductionEnquiryFilterDto.donorIdNumber}" >
                                </iais:value>
                            </iais:row>
                        </div>
                        <%--                            Details of Storage--%>
                        <div class="col-xs-10 col-md-10">
                            <iais:row>
                                <iais:field width="4" value="Details of Storage" />
                            </iais:row>
                            <hr>
                            <iais:row>
                                <iais:field width="4" value="No. Removed from Storage"/>
                                <iais:value width="6" cssClass="col-md-6" >
                                    <input type="text"  id="removedFromStorage"  name="removedFromStorage" value="${assistedReproductionEnquiryFilterDto.removedFromStorage}" >
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="Embryos Stored Beyond 10 Years" />
                                <iais:value width="3" cssClass="col-md-3">
                                    <div class="form-check">
                                        <input class="form-check-input"
                                               type="radio"
                                               name="embryosStoredBeyond" id="embryosStoredBeyondTrue"
                                               value="1"
                                               <c:if test="${ assistedReproductionEnquiryFilterDto.embryosStoredBeyond =='1' }">checked</c:if>
                                               aria-invalid="false">
                                        <label class="form-check-label"
                                               for="embryosStoredBeyondTrue"><span
                                                class="check-circle"></span>True</label>
                                    </div>
                                </iais:value>
                                <iais:value width="3" cssClass="col-md-3">
                                    <div class="form-check">
                                        <input class="form-check-input" type="radio"
                                               name="embryosStoredBeyond" value="0" id="embryosStoredBeyondFalse"
                                               <c:if test="${assistedReproductionEnquiryFilterDto.embryosStoredBeyond == '0'}">checked</c:if>
                                               aria-invalid="false">
                                        <label class="form-check-label"
                                               for="embryosStoredBeyondFalse"><span
                                                class="check-circle"></span>False</label>
                                    </div>
                                </iais:value>
                            </iais:row>

                        </div>
                        <%--                            Details of Fertilisation--%>
                        <div class="col-xs-10 col-md-10">
                            <iais:row>
                                <iais:field width="4" value="Details of Fertilisation" />
                            </iais:row>
                            <hr>
                            <iais:row>
                                <iais:field width="4" value="Source of Semen"/>
                                <iais:value width="6" cssClass="col-md-6"  >
                                    <iais:select name="sourceSemen" id="sourceSemen" firstOption="Please Select" options="sourceSemenOptions"
                                                 value="${assistedReproductionEnquiryFilterDto.sourceSemen}" />
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="AR Techniques Used" />
                                <div class="col-md-6">
                                    <iais:value width="6" cssClass="col-md-12">
                                        <div class="form-check">
                                            <input class="form-check-input"
                                                   type="checkbox"
                                                   name="GIFT" id="GIFT"
                                                   <c:if test="${ assistedReproductionEnquiryFilterDto.GIFT =='on'  }">checked</c:if>
                                                   aria-invalid="false">
                                            <label class="form-check-label"
                                                   for="GIFT"><span
                                                    class="check-square"></span><iais:code code="AR_ATU_003"/></label>
                                        </div>
                                    </iais:value>
                                    <iais:value width="6" cssClass="col-md-12">
                                        <div class="form-check">
                                            <input class="form-check-input" type="checkbox"
                                                   name="ICSI" id="ICSI"
                                                   <c:if test="${assistedReproductionEnquiryFilterDto.ICSI == 'on' }">checked</c:if>
                                                   aria-invalid="false">
                                            <label class="form-check-label"
                                                   for="ICSI"><span
                                                    class="check-square"></span><iais:code code="AR_ATU_002"/></label>
                                        </div>
                                    </iais:value>
                                    <iais:value width="6" cssClass="col-md-12">
                                        <div class="form-check">
                                            <input class="form-check-input" type="checkbox"
                                                   name="ZIFT" id="ZIFT"
                                                   <c:if test="${assistedReproductionEnquiryFilterDto.ZIFT == 'on' }">checked</c:if>
                                                   aria-invalid="false">
                                            <label class="form-check-label"
                                                   for="ZIFT"><span
                                                    class="check-square"></span><iais:code code="AR_ATU_004"/></label>
                                        </div>
                                    </iais:value>
                                    <iais:value width="6" cssClass="col-md-12">
                                        <div class="form-check">
                                            <input class="form-check-input" type="checkbox"
                                                   name="IVF" id="IVF"
                                                   <c:if test="${assistedReproductionEnquiryFilterDto.IVF == 'on' }">checked</c:if>
                                                   aria-invalid="false">
                                            <label class="form-check-label"
                                                   for="IVF"><span
                                                    class="check-square"></span><iais:code code="AR_ATU_001"/></label>
                                        </div>
                                    </iais:value>
                                </div>

                            </iais:row>

                        </div>
                        <%--                            Details of Embryo Transfer & Outcome--%>
                        <div class="col-xs-10 col-md-10">
                            <iais:row>
                                <iais:field width="4" value="Details of Embryo Transfer & Outcome" />
                            </iais:row>
                            <hr>
                            <iais:row>
                                <iais:field width="4" value="No. of Embryos Transferred" />
                                <div class="col-md-6">

                                    <iais:value width="2" cssClass="col-md-2">
                                        <div class="form-check">
                                            <input class="form-check-input" type="checkbox"
                                                   name="embryosTransferredNum1"  id="embryosTransferredNum1"
                                                   <c:if test="${assistedReproductionEnquiryFilterDto.embryosTransferredNum1 == 'on' }">checked</c:if>
                                                   aria-invalid="false">
                                            <label class="form-check-label"
                                                   for="embryosTransferredNum1"><span
                                                    class="check-square"></span>1</label>
                                        </div>
                                    </iais:value>
                                    <iais:value width="2" cssClass="col-md-2">
                                        <div class="form-check">
                                            <input class="form-check-input" type="checkbox"
                                                   name="embryosTransferredNum2"  id="embryosTransferredNum2"
                                                   <c:if test="${assistedReproductionEnquiryFilterDto.embryosTransferredNum2 == 0}">checked</c:if>
                                                   aria-invalid="false">
                                            <label class="form-check-label"
                                                   for="embryosTransferredNum2"><span
                                                    class="check-square"></span>2</label>
                                        </div>
                                    </iais:value>
                                    <iais:value width="2" cssClass="col-md-2">
                                        <div class="form-check">
                                            <input class="form-check-input" type="checkbox"
                                                   name="embryosTransferredNum3" id="embryosTransferredNum3"
                                                   <c:if test="${assistedReproductionEnquiryFilterDto.embryosTransferredNum3 == 'on' }">checked</c:if>
                                                   aria-invalid="false">
                                            <label class="form-check-label"
                                                   for="embryosTransferredNum3"><span
                                                    class="check-square"></span>3</label>
                                        </div>
                                    </iais:value>
                                    <iais:value width="2" cssClass="col-md-2">
                                        <div class="form-check">
                                            <input class="form-check-input" type="checkbox"
                                                   name="embryosTransferredNumMax" id="embryosTransferredNumMax"
                                                   <c:if test="${assistedReproductionEnquiryFilterDto.embryosTransferredNumMax == 'on' }">checked</c:if>
                                                   aria-invalid="false">
                                            <label class="form-check-label"
                                                   for="embryosTransferredNumMax"><span
                                                    class="check-square"></span>More than 3</label>
                                        </div>
                                    </iais:value>
                                </div>

                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="Age of Embryos Transferred" />
                                <div class="col-md-6">
                                    <iais:value width="6" cssClass="col-md-6">
                                        <div class="form-check">
                                            <input class="form-check-input"
                                                   type="checkbox"
                                                   name="ageEmbryosNum1" id="ageEmbryosNum1"
                                                   <c:if test="${ assistedReproductionEnquiryFilterDto.ageEmbryosNum1 =='on'  }">checked</c:if>
                                                   aria-invalid="false">
                                            <label class="form-check-label"
                                                   for="ageEmbryosNum1"><span
                                                    class="check-square"></span><iais:code code="AOFET001"/></label>
                                        </div>
                                    </iais:value>
                                    <iais:value width="6" cssClass="col-md-6">
                                        <div class="form-check">
                                            <input class="form-check-input"
                                                   type="checkbox"
                                                   name="ageEmbryosNum4" id="ageEmbryosNum4"
                                                   <c:if test="${ assistedReproductionEnquiryFilterDto.ageEmbryosNum2 =='on'  }">checked</c:if>
                                                   aria-invalid="false">
                                            <label class="form-check-label"
                                                   for="ageEmbryosNum4"><span
                                                    class="check-square"></span><iais:code code="AOFET004"/></label>
                                        </div>
                                    </iais:value>
                                    <iais:value width="6" cssClass="col-md-6">
                                        <div class="form-check">
                                            <input class="form-check-input"
                                                   type="checkbox"
                                                   name="ageEmbryosNum2" id="ageEmbryosNum2"
                                                   <c:if test="${ assistedReproductionEnquiryFilterDto.ageEmbryosNum2 =='on'  }">checked</c:if>
                                                   aria-invalid="false">
                                            <label class="form-check-label"
                                                   for="ageEmbryosNum2"><span
                                                    class="check-square"></span><iais:code code="AOFET002"/></label>
                                        </div>
                                    </iais:value>
                                    <iais:value width="6" cssClass="col-md-6">
                                        <div class="form-check">
                                            <input class="form-check-input"
                                                   type="checkbox"
                                                   name="ageEmbryosNum5" id="ageEmbryosNum5"
                                                   <c:if test="${ assistedReproductionEnquiryFilterDto.ageEmbryosNum5 =='on'  }">checked</c:if>
                                                   aria-invalid="false">
                                            <label class="form-check-label"
                                                   for="ageEmbryosNum5"><span
                                                    class="check-square"></span><iais:code code="AOFET005"/></label>
                                        </div>
                                    </iais:value>
                                    <iais:value width="6" cssClass="col-md-6">
                                        <div class="form-check">
                                            <input class="form-check-input"
                                                   type="checkbox"
                                                   name="ageEmbryosNum3" id="ageEmbryosNum3"
                                                   <c:if test="${ assistedReproductionEnquiryFilterDto.ageEmbryosNum3 =='on'  }">checked</c:if>
                                                   aria-invalid="false">
                                            <label class="form-check-label"
                                                   for="ageEmbryosNum3"><span
                                                    class="check-square"></span><iais:code code="AOFET003"/></label>
                                        </div>
                                    </iais:value>
                                    <iais:value width="6" cssClass="col-md-6">
                                        <div class="form-check">
                                            <input class="form-check-input"
                                                   type="checkbox" id="ageEmbryosNum6"
                                                   name="ageEmbryosNum6"
                                                   <c:if test="${ assistedReproductionEnquiryFilterDto.ageEmbryosNum6 =='on'  }">checked</c:if>
                                                   aria-invalid="false">
                                            <label class="form-check-label"
                                                   for="ageEmbryosNum6"><span
                                                    class="check-square"></span><iais:code code="AOFET006"/></label>
                                        </div>
                                    </iais:value>
                                </div>

                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="Outcome of Embryo Transferred" />
                                <div class="col-md-6">
                                    <iais:value width="6" cssClass="col-md-12">
                                        <div class="form-check">
                                            <input class="form-check-input"
                                                   type="checkbox" id="clinicalPregnancy"
                                                   name="clinicalPregnancy"
                                                   <c:if test="${ assistedReproductionEnquiryFilterDto.clinicalPregnancy =='on'  }">checked</c:if>
                                                   aria-invalid="false">
                                            <label class="form-check-label"
                                                   for="clinicalPregnancy"><span
                                                    class="check-square"></span>Clinical Pregnancy</label>
                                        </div>
                                    </iais:value>
                                    <iais:value width="6" cssClass="col-md-12">
                                        <div class="form-check">
                                            <input class="form-check-input" type="checkbox"
                                                   name="ectopicPregnancy" id="ectopicPregnancy"
                                                   <c:if test="${assistedReproductionEnquiryFilterDto.ectopicPregnancy == 'on' }">checked</c:if>
                                                   aria-invalid="false">
                                            <label class="form-check-label"
                                                   for="ectopicPregnancy"><span
                                                    class="check-square"></span>Ectopic Pregnancy</label>
                                        </div>
                                    </iais:value>
                                    <iais:value width="6" cssClass="col-md-12">
                                        <div class="form-check">
                                            <input class="form-check-input" type="checkbox"
                                                   name="implantationDocumented" id="implantationDocumented"
                                                   <c:if test="${assistedReproductionEnquiryFilterDto.implantationDocumented == 'on' }">checked</c:if>
                                                   aria-invalid="false">
                                            <label class="form-check-label"
                                                   for="implantationDocumented"><span
                                                    class="check-square"></span>Implantation Documented</label>
                                        </div>
                                    </iais:value>
                                    <iais:value width="6" cssClass="col-md-12">
                                        <div class="form-check">
                                            <input class="form-check-input" type="checkbox"
                                                   name="noPregnancy" id="noPregnancy"
                                                   <c:if test="${assistedReproductionEnquiryFilterDto.noPregnancy == 'on' }">checked</c:if>
                                                   aria-invalid="false">
                                            <label class="form-check-label"
                                                   for="noPregnancy"><span
                                                    class="check-square"></span>No Pregnancy</label>
                                        </div>
                                    </iais:value>
                                    <iais:value width="6" cssClass="col-md-12">
                                        <div class="form-check">
                                            <input class="form-check-input" type="checkbox"
                                                   name="unknown" id="unknown"
                                                   <c:if test="${assistedReproductionEnquiryFilterDto.unknown == 'on' }">checked</c:if>
                                                   aria-invalid="false">
                                            <label class="form-check-label"
                                                   for="unknown"><span
                                                    class="check-square"></span>Unknown</label>
                                        </div>
                                    </iais:value>
                                </div>

                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="No. of Live Birth Events (Total)" />
                                <div class="col-md-6">
                                    <iais:value width="2" cssClass="col-md-2">
                                        <div class="form-check">
                                            <input class="form-check-input"
                                                   type="checkbox"
                                                   name="birthEventsTotal0" id="birthEventsTotal0"
                                                   <c:if test="${ assistedReproductionEnquiryFilterDto.birthEventsTotal0 =='on'  }">checked</c:if>
                                                   aria-invalid="false">
                                            <label class="form-check-label"
                                                   for="birthEventsTotal0"><span
                                                    class="check-square"></span>0</label>
                                        </div>
                                    </iais:value>
                                    <iais:value width="2" cssClass="col-md-2">
                                        <div class="form-check">
                                            <input class="form-check-input" type="checkbox"
                                                   name="birthEventsTotal1" id="birthEventsTotal1"
                                                   <c:if test="${assistedReproductionEnquiryFilterDto.birthEventsTotal1 == 'on' }">checked</c:if>
                                                   aria-invalid="false">
                                            <label class="form-check-label"
                                                   for="birthEventsTotal1"><span
                                                    class="check-square"></span>1</label>
                                        </div>
                                    </iais:value>
                                    <iais:value width="2" cssClass="col-md-2">
                                        <div class="form-check">
                                            <input class="form-check-input" type="checkbox"
                                                   name="birthEventsTotal2" id="birthEventsTotal2"
                                                   <c:if test="${assistedReproductionEnquiryFilterDto.birthEventsTotal2 == 'on' }">checked</c:if>
                                                   aria-invalid="false">
                                            <label class="form-check-label"
                                                   for="birthEventsTotal2"><span
                                                    class="check-square"></span>2</label>
                                        </div>
                                    </iais:value>
                                    <iais:value width="2" cssClass="col-md-2">
                                        <div class="form-check">
                                            <input class="form-check-input" type="checkbox"
                                                   name="birthEventsTotal3" id="birthEventsTotal3"
                                                   <c:if test="${assistedReproductionEnquiryFilterDto.birthEventsTotal3 == 'on' }">checked</c:if>
                                                   aria-invalid="false">
                                            <label class="form-check-label"
                                                   for="birthEventsTotal3"><span
                                                    class="check-square"></span>3</label>
                                        </div>
                                    </iais:value>
                                    <iais:value width="2" cssClass="col-md-2">
                                        <div class="form-check">
                                            <input class="form-check-input" type="checkbox"
                                                   name="birthEventsTotalMax" id="birthEventsTotalMax"
                                                   <c:if test="${assistedReproductionEnquiryFilterDto.birthEventsTotalMax == 'on' }">checked</c:if>
                                                   aria-invalid="false">
                                            <label class="form-check-label"
                                                   for="birthEventsTotalMax"><span
                                                    class="check-square"></span>>3</label>
                                        </div>
                                    </iais:value>
                                </div>

                            </iais:row>

                            <iais:row>
                                <iais:field width="4" value="Date of Delivery"/>
                                <iais:value width="3" cssClass="col-md-3">
                                    <iais:datePicker id="deliveryDateFrom" name="deliveryDateFrom" dateVal="${assistedReproductionEnquiryFilterDto.deliveryDateFrom}"/>
                                </iais:value>
                                <iais:value width="3" cssClass="col-md-3">
                                    <iais:datePicker id="deliveryDateTo" name="deliveryDateTo" dateVal="${assistedReproductionEnquiryFilterDto.deliveryDateTo}"/>
                                </iais:value>
                            </iais:row>

                        </div>
                        <%--                            Details of Treatment Subsidies--%>
                        <div class="col-xs-10 col-md-10">
                            <iais:row>
                                <iais:field width="4" value="Details of Treatment Subsidies" />
                            </iais:row>
                            <hr>

                            <iais:row>
                                <iais:field width="4" value="Is patient on ART co-funding?" />

                                <iais:value width="2" cssClass="col-md-2">
                                    <div class="form-check">
                                        <input class="form-check-input"
                                               type="radio"
                                               name="patientART" id="patientARTYes"
                                               value="0"
                                               <c:if test="${ assistedReproductionEnquiryFilterDto.patientART =='0' }">checked</c:if>
                                               aria-invalid="false">
                                        <label class="form-check-label"
                                               for="patientARTYes"><span
                                                class="check-circle"></span>Yes</label>
                                    </div>
                                </iais:value>
                                <iais:value width="2" cssClass="col-md-2">
                                    <div class="form-check">
                                        <input class="form-check-input"
                                               type="radio"
                                               name="patientART" id="patientARTNo"
                                               value="1"
                                               <c:if test="${ assistedReproductionEnquiryFilterDto.patientART =='1' }">checked</c:if>
                                               aria-invalid="false">
                                        <label class="form-check-label"
                                               for="patientARTNo"><span
                                                class="check-circle"></span>No</label>
                                    </div>
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="Is patient on IUI co-funding?" />

                                <iais:value width="2" cssClass="col-md-2">
                                    <div class="form-check">
                                        <input class="form-check-input"
                                               type="radio"
                                               name="patientIUI" id="patientIUIYes"
                                               value="0"
                                               <c:if test="${ assistedReproductionEnquiryFilterDto.patientIUI =='0' }">checked</c:if>
                                               aria-invalid="false">
                                        <label class="form-check-label"
                                               for="patientIUIYes"><span
                                                class="check-circle"></span>Yes</label>
                                    </div>
                                </iais:value>
                                <iais:value width="2" cssClass="col-md-2">
                                    <div class="form-check">
                                        <input class="form-check-input"
                                               type="radio"
                                               name="patientIUI" id="patientIUINo"
                                               value="1"
                                               <c:if test="${ assistedReproductionEnquiryFilterDto.patientIUI =='1' }">checked</c:if>
                                               aria-invalid="false">
                                        <label class="form-check-label"
                                               for="patientIUINo"><span
                                                class="check-circle"></span>No</label>
                                    </div>
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field width="4" value="Is patient on PGT co-funding?" />

                                <iais:value width="2" cssClass="col-md-2">
                                    <div class="form-check">
                                        <input class="form-check-input"
                                               type="radio"
                                               name="patientPGT" id="patientPGTYes"
                                               value="0"
                                               <c:if test="${ assistedReproductionEnquiryFilterDto.patientPGT =='0 '}">checked</c:if>
                                               aria-invalid="false">
                                        <label class="form-check-label"
                                               for="patientPGTYes"><span
                                                class="check-circle"></span>Yes</label>
                                    </div>
                                </iais:value>
                                <iais:value width="2" cssClass="col-md-2">
                                    <div class="form-check">
                                        <input class="form-check-input"
                                               type="radio"
                                               name="patientPGT" id="patientPGTNo"
                                               value="1"
                                               <c:if test="${ assistedReproductionEnquiryFilterDto.patientPGT =='1' }">checked</c:if>
                                               aria-invalid="false">
                                        <label class="form-check-label"
                                               for="patientPGTNo"><span
                                                class="check-circle"></span>No</label>
                                    </div>
                                </iais:value>
                            </iais:row>

                        </div>
                        <%--                            Details of Disposal--%>
                        <div class="col-xs-10 col-md-10">
                            <iais:row>
                                <iais:field width="4" value="Details of Disposal" />
                            </iais:row>
                            <hr>

                            <iais:row>
                                <iais:field width="4" value="Disposal of" />
                                <div class="col-md-8">
                                    <iais:value  cssClass="col-md-12">
                                        <div class="form-check">
                                            <input class="form-check-input"
                                                   type="checkbox"
                                                   name="disposalTypeFreshOocyte" id="disposalTypeFreshOocyte"
                                                   <c:if test="${ assistedReproductionEnquiryFilterDto.disposalTypeFreshOocyte =='on'  }">checked</c:if>
                                                   aria-invalid="false">
                                            <label class="form-check-label"
                                                   for="disposalTypeFreshOocyte"><span
                                                    class="check-square"></span>Fresh Oocyte(s)</label>
                                        </div>
                                    </iais:value>
                                    <iais:value  cssClass="col-md-12">
                                        <div class="form-check">
                                            <input class="form-check-input"
                                                   type="checkbox"
                                                   name="disposalTypeFrozenOocyte" id="disposalTypeFrozenOocyte"
                                                   <c:if test="${ assistedReproductionEnquiryFilterDto.disposalTypeFrozenOocyte =='on'  }">checked</c:if>
                                                   aria-invalid="false">
                                            <label class="form-check-label"
                                                   for="disposalTypeFrozenOocyte"><span
                                                    class="check-square"></span>Frozen Oocyte(s)</label>
                                        </div>
                                    </iais:value>
                                    <iais:value  cssClass="col-md-12">
                                        <div class="form-check">
                                            <input class="form-check-input"
                                                   type="checkbox"
                                                   name="disposalTypeThawedOocyte" id="disposalTypeThawedOocyte"
                                                   <c:if test="${ assistedReproductionEnquiryFilterDto.disposalTypeThawedOocyte =='on'  }">checked</c:if>
                                                   aria-invalid="false">
                                            <label class="form-check-label"
                                                   for="disposalTypeThawedOocyte"><span
                                                    class="check-square"></span>Thawed Oocyte(s)</label>
                                        </div>
                                    </iais:value>
                                    <iais:value  cssClass="col-md-12">
                                        <div class="form-check">
                                            <input class="form-check-input"
                                                   type="checkbox"
                                                   name="disposalTypeFreshEmbryo" id="disposalTypeFreshEmbryo"
                                                   <c:if test="${ assistedReproductionEnquiryFilterDto.disposalTypeFreshEmbryo =='on'  }">checked</c:if>
                                                   aria-invalid="false">
                                            <label class="form-check-label"
                                                   for="disposalTypeFreshEmbryo"><span
                                                    class="check-square"></span>Fresh Embryo(s)</label>
                                        </div>
                                    </iais:value>
                                    <iais:value  cssClass="col-md-12">
                                        <div class="form-check">
                                            <input class="form-check-input"
                                                   type="checkbox"
                                                   name="disposalTypeFrozenEmbryo" id="disposalTypeFrozenEmbryo"
                                                   <c:if test="${ assistedReproductionEnquiryFilterDto.disposalTypeFrozenEmbryo =='on'  }">checked</c:if>
                                                   aria-invalid="false">
                                            <label class="form-check-label"
                                                   for="disposalTypeFrozenEmbryo"><span
                                                    class="check-square"></span>Frozen Embryo(s)</label>
                                        </div>
                                    </iais:value>
                                    <iais:value  cssClass="col-md-12">
                                        <div class="form-check">
                                            <input class="form-check-input"
                                                   type="checkbox"
                                                   name="disposalTypeThawedEmbryo" id="disposalTypeThawedEmbryo"
                                                   <c:if test="${ assistedReproductionEnquiryFilterDto.disposalTypeThawedEmbryo =='on'  }">checked</c:if>
                                                   aria-invalid="false">
                                            <label class="form-check-label"
                                                   for="disposalTypeThawedEmbryo"><span
                                                    class="check-square"></span>Thawed Embryo(s)</label>
                                        </div>
                                    </iais:value>
                                    <iais:value  cssClass="col-md-12">
                                        <div class="form-check">
                                            <input class="form-check-input"
                                                   type="checkbox"
                                                   name="disposalTypeFrozenSperm" id="disposalTypeFrozenSperm"
                                                   <c:if test="${ assistedReproductionEnquiryFilterDto.disposalTypeFrozenSperm =='on'  }">checked</c:if>
                                                   aria-invalid="false">
                                            <label class="form-check-label"
                                                   for="disposalTypeFrozenSperm"><span
                                                    class="check-square"></span>Frozen Sperm</label>
                                        </div>
                                    </iais:value>
                                </div>
                            </iais:row>

                            <iais:row>
                                <iais:field width="4" value="Total No. Disposed Of"/>
                                <iais:value width="6" cssClass="col-md-6"  >
                                    <input type="number" oninput="if(value.length>2)value=value.slice(0,2)" style="margin-bottom: 0px;" id="disposedTotalNumber"  name="disposedTotalNumber" value="${assistedReproductionEnquiryFilterDto.disposedTotalNumber}" >
                                </iais:value>
                            </iais:row>

                            <iais:row>
                                <iais:field width="4" value="Date of Disposal"/>
                                <iais:value width="3" cssClass="col-md-3">
                                    <iais:datePicker id="disposalDateFrom" name="disposalDateFrom" dateVal="${assistedReproductionEnquiryFilterDto.disposalDateFrom}"/>
                                </iais:value>
                                <iais:value width="3" cssClass="col-md-3">
                                    <iais:datePicker id="disposalDateTo" name="disposalDateTo" dateVal="${assistedReproductionEnquiryFilterDto.disposalDateTo}"/>
                                </iais:value>
                            </iais:row>

                        </div>
                        <%--                            Details of Transfer In & Out--%>
                        <div class="col-xs-10 col-md-10">
                            <iais:row>
                                <iais:field width="4" value="Details of Transfer In & Out" />
                            </iais:row>
                            <hr>

                            <iais:row>
                                <iais:field width="4" value="Is this a Transfer In or Out?" />
                                <iais:value width="3" cssClass="col-md-3">
                                    <div class="form-check">
                                        <input class="form-check-input"
                                               type="radio"
                                               name="transferInOrOut" id="transferIn"
                                               value="1"
                                               <c:if test="${ assistedReproductionEnquiryFilterDto.transferInOrOut =='1' }">checked</c:if>
                                               aria-invalid="false">
                                        <label class="form-check-label"
                                               for="transferIn"><span
                                                class="check-circle"></span>Transfer In</label>
                                    </div>
                                </iais:value>
                                <iais:value width="3" cssClass="col-md-3">
                                    <div class="form-check">
                                        <input class="form-check-input" type="radio"
                                               name="transferInOrOut" value="0" id="transferOut"
                                               <c:if test="${assistedReproductionEnquiryFilterDto.transferInOrOut == '0'}">checked</c:if>
                                               aria-invalid="false">
                                        <label class="form-check-label"
                                               for="transferOut"><span
                                                class="check-circle"></span>Transfer Out</label>
                                    </div>
                                </iais:value>
                            </iais:row>

                            <iais:row>
                                <iais:field width="4" value="What was Transferred?" />
                                <div class="col-md-8">
                                    <iais:value  cssClass="col-md-12">
                                        <div class="form-check">
                                            <input class="form-check-input"
                                                   type="checkbox"
                                                   name="transferredOocyte" id="transferredOocyte"
                                                   <c:if test="${ assistedReproductionEnquiryFilterDto.transferredOocyte =='on'  }">checked</c:if>
                                                   aria-invalid="false">
                                            <label class="form-check-label"
                                                   for="transferredOocyte"><span
                                                    class="check-square"></span><iais:code code="AR_WWT_001"/></label>
                                        </div>
                                    </iais:value>
                                    <iais:value  cssClass="col-md-12">
                                        <div class="form-check">
                                            <input class="form-check-input"
                                                   type="checkbox"
                                                   name="transferredEmbryo" id="transferredEmbryo"
                                                   <c:if test="${ assistedReproductionEnquiryFilterDto.transferredEmbryo =='on'  }">checked</c:if>
                                                   aria-invalid="false">
                                            <label class="form-check-label"
                                                   for="transferredEmbryo"><span
                                                    class="check-square"></span><iais:code code="AR_WWT_002"/></label>
                                        </div>
                                    </iais:value>
                                    <iais:value  cssClass="col-md-12">
                                        <div class="form-check">
                                            <input class="form-check-input"
                                                   type="checkbox"
                                                   name="transferredSperm" id="transferredSperm"
                                                   <c:if test="${ assistedReproductionEnquiryFilterDto.transferredSperm =='on' }">checked</c:if>
                                                   aria-invalid="false">
                                            <label class="form-check-label"
                                                   for="transferredSperm"><span
                                                    class="check-square"></span><iais:code code="AR_WWT_003"/></label>
                                        </div>
                                    </iais:value>
                                </div>
                            </iais:row>

                            <iais:row>
                                <iais:field width="4" value="Transferred In From"/>
                                <iais:value width="6" cssClass="col-md-6"  >
                                    <iais:select name="transferredInFrom" id="transferredInFrom" firstOption="Please Select" options="arCentreSelectOption"
                                                 value="${assistedReproductionEnquiryFilterDto.transferredInFrom}"  />
                                </iais:value>
                            </iais:row>

                            <iais:row>
                                <iais:field width="4" value="Transfer Out To"/>
                                <iais:value width="6" cssClass="col-md-6"  >
                                    <iais:select name="transferOutTo" id="transferOutTo" firstOption="Please Select" options="arCentreSelectOption"
                                                 value="${assistedReproductionEnquiryFilterDto.transferOutTo}" />
                                </iais:value>
                            </iais:row>

                            <iais:row>
                                <iais:field width="4" value="Date of Transfer"/>
                                <iais:value width="3" cssClass="col-md-3">
                                    <iais:datePicker id="transferDateFrom" name="transferDateFrom" dateVal="${assistedReproductionEnquiryFilterDto.transferDateFrom}"/>
                                </iais:value>
                                <iais:value width="3" cssClass="col-md-3">
                                    <iais:datePicker id="transferDateTo" name="transferDateTo" dateVal="${assistedReproductionEnquiryFilterDto.transferDateTo}"/>
                                </iais:value>
                            </iais:row>

                        </div>
                        <%--                            Details of PGT--%>
                        <div class="col-xs-10 col-md-10">
                            <iais:row>
                                <iais:field width="4" value="Details of PGT" />
                            </iais:row>
                            <hr>

                            <iais:row>
                                <iais:field width="4" value="PGT" />
                                <iais:value width="3" cssClass="col-md-3">
                                    <div class="form-check">
                                        <input class="form-check-input"
                                               type="radio"
                                               name="PGT" id="PGTYes"
                                               value="1"
                                               <c:if test="${ assistedReproductionEnquiryFilterDto.PGT =='1' }">checked</c:if>
                                               aria-invalid="false">
                                        <label class="form-check-label"
                                               for="PGTYes"><span
                                                class="check-circle"></span>Yes</label>
                                    </div>
                                </iais:value>
                                <iais:value width="3" cssClass="col-md-3">
                                    <div class="form-check">
                                        <input class="form-check-input" type="radio"
                                               name="PGT" value="0" id="PGTNo"
                                               <c:if test="${assistedReproductionEnquiryFilterDto.PGT == '0'}">checked</c:if>
                                               aria-invalid="false">
                                        <label class="form-check-label"
                                               for="PGTNo"><span
                                                class="check-circle"></span>No</label>
                                    </div>
                                </iais:value>
                            </iais:row>

                            <iais:row>
                                <iais:field width="4" value="Outcome of Preimplantation Genetic Testing" />
                                <div class="col-md-8">
                                    <iais:value  cssClass="col-md-12">
                                        <div class="form-check">
                                            <input class="form-check-input"
                                                   type="checkbox"
                                                   name="pgtMCom" id="pgtMCom"
                                                   <c:if test="${ assistedReproductionEnquiryFilterDto.pgtMCom =='on'  }">checked</c:if>
                                                   aria-invalid="false">
                                            <label class="form-check-label"
                                                   for="pgtMCom"><span
                                                    class="check-square"></span><iais:code code="PGTTP001"/></label>
                                        </div>
                                    </iais:value>
                                    <iais:value  cssClass="col-md-12">
                                        <div class="form-check">
                                            <input class="form-check-input"
                                                   type="checkbox"
                                                   name="pgtMRare" id="pgtMRare"
                                                   <c:if test="${ assistedReproductionEnquiryFilterDto.pgtMRare =='on'  }">checked</c:if>
                                                   aria-invalid="false">
                                            <label class="form-check-label"
                                                   for="pgtMRare"><span
                                                    class="check-square"></span><iais:code code="PGTTP002"/></label>
                                        </div>
                                    </iais:value>
                                    <iais:value  cssClass="col-md-12">
                                        <div class="form-check">
                                            <input class="form-check-input"
                                                   type="checkbox"
                                                   name="pgtMEbt" id="pgtMEbt"
                                                   <c:if test="${ assistedReproductionEnquiryFilterDto.pgtMEbt =='on'  }">checked</c:if>
                                                   aria-invalid="false">
                                            <label class="form-check-label"
                                                   for="pgtMEbt"><span
                                                    class="check-square"></span><iais:code code="PGTTP003"/></label>
                                        </div>
                                    </iais:value>
                                    <iais:value  cssClass="col-md-12">
                                        <div class="form-check">
                                            <input class="form-check-input"
                                                   type="checkbox" id="pgtSr"
                                                   name="pgtSr"
                                                   <c:if test="${ assistedReproductionEnquiryFilterDto.pgtSr =='on'  }">checked</c:if>
                                                   aria-invalid="false">
                                            <label class="form-check-label"
                                                   for="pgtSr"><span
                                                    class="check-square"></span><iais:code code="PGTTP004"/></label>
                                        </div>
                                    </iais:value>
                                    <iais:value  cssClass="col-md-12">
                                        <div class="form-check">
                                            <input class="form-check-input"
                                                   type="checkbox"
                                                   name="pgtA" id="pgtA"
                                                   <c:if test="${ assistedReproductionEnquiryFilterDto.pgtA =='on' }">checked</c:if>
                                                   aria-invalid="false">
                                            <label class="form-check-label"
                                                   for="pgtA"><span
                                                    class="check-square"></span><iais:code code="PGTTP005"/></label>
                                        </div>
                                    </iais:value>
                                    <iais:value  cssClass="col-md-12">
                                        <div class="form-check">
                                            <input class="form-check-input"
                                                   type="checkbox"
                                                   name="ptt" id="ptt"
                                                   <c:if test="${ assistedReproductionEnquiryFilterDto.ptt =='on' }">checked</c:if>
                                                   aria-invalid="false">
                                            <label class="form-check-label"
                                                   for="ptt"><span
                                                    class="check-square"></span><iais:code code="PGTTP006"/></label>
                                        </div>

                                    </iais:value>
                                    <iais:value  cssClass="col-md-12">
                                        <div class="form-check">
                                            <input class="form-check-input"
                                                   type="checkbox"
                                                   name="pgtOthers" id="pgtOthers"
                                                   <c:if test="${ assistedReproductionEnquiryFilterDto.pgtOthers =='on' }">checked</c:if>
                                                   aria-invalid="false">
                                            <label class="form-check-label"
                                                   for="pgtOthers"><span
                                                    class="check-square"></span><iais:code code="PGTTP007"/></label>
                                        </div>
                                    </iais:value>
                                </div>
                            </iais:row>

                            <iais:row>
                                <iais:field width="4" value="PGT Performed to Detect Sex-linked Disease" />
                                <iais:value width="3" cssClass="col-md-3">
                                    <div class="form-check">
                                        <input class="form-check-input"
                                               type="radio"
                                               name="pgtDisease" id="pgtDiseaseYes"
                                               value="1"
                                               <c:if test="${ assistedReproductionEnquiryFilterDto.pgtDisease =='1' }">checked</c:if>
                                               aria-invalid="false">
                                        <label class="form-check-label"
                                               for="pgtDiseaseYes"><span
                                                class="check-circle"></span>Yes</label>
                                    </div>
                                </iais:value>
                                <iais:value width="3" cssClass="col-md-3">
                                    <div class="form-check">
                                        <input class="form-check-input" type="radio"
                                               name="pgtDisease" value="0" id="pgtDiseaseNo"
                                               <c:if test="${assistedReproductionEnquiryFilterDto.pgtDisease == '0'}">checked</c:if>
                                               aria-invalid="false">
                                        <label class="form-check-label"
                                               for="pgtDiseaseNo"><span
                                                class="check-circle"></span>No</label>
                                    </div>
                                </iais:value>
                            </iais:row>

                        </div>

                        <div class="col-xs-12 col-md-12">
                            <iais:action style="text-align:right;">
                                <button type="button" class="btn btn-secondary"
                                        onclick="javascript:doClear();">Clear
                                </button>
                                <button type="button" class="btn btn-secondary"
                                        onclick="javascript:doArBack();">Basic Search
                                </button>
                                <button type="button" class="btn btn-primary"
                                        onclick="javascript:doSearch();">Search
                                </button>
                            </iais:action>
                        </div>
                    </div>
                </div>
                <br>

                <div class="col-xs-12 row">
                    <div class="components">

                        <iais:pagination param="patientAdvParam" result="patientAdvResult"/>
                        <div class="table-responsive">
                            <div class="table-gp">
                                <table aria-describedby="" class="table">
                                    <thead>
                                    <tr >
                                        <iais:sortableHeader needSort="true"
                                                             field="BUSINESS_NAME"
                                                             value="AR Centre"/>
                                        <iais:sortableHeader needSort="true"
                                                             field="NAME"
                                                             value="Patient Name"/>
                                        <iais:sortableHeader needSort="false"
                                                             field="ID_TYPE"
                                                             value="Patient ID Type"/>
                                        <iais:sortableHeader needSort="true"
                                                             field="ID_NUMBER"
                                                             value="Patient ID No."/>
                                        <iais:sortableHeader needSort="true"
                                                             field="DATE_OF_BIRTH"
                                                             value="Patient Date of Birth"/>
                                        <iais:sortableHeader needSort="false"
                                                             field="NATIONALITY"
                                                             value="Patient Nationality"/>
                                        <iais:sortableHeader needSort="true"
                                                             field="CREATED_DT"
                                                             value="Cycle Start Date"/>
                                        <iais:sortableHeader needSort="false"
                                                             field=""
                                                             value="Action"/>
                                    </tr>
                                    </thead>
                                    <tbody class="form-horizontal">
                                    <c:choose>
                                        <c:when test="${empty patientAdvResult.rows}">
                                            <tr>
                                                <td colspan="15">
                                                    <iais:message key="GENERAL_ACK018"
                                                                  escape="true"/>
                                                </td>
                                            </tr>
                                        </c:when>
                                        <c:otherwise>
                                            <c:forEach var="patient"
                                                       items="${patientAdvResult.rows}"
                                                       varStatus="status">
                                                <tr>

                                                    <td style="vertical-align:middle;">
                                                        <c:out value="${patient.arCentre}"/>
                                                    </td>
                                                    <td style="vertical-align:middle;">
                                                        <c:out value="${patient.patientName}"/>
                                                    </td>
                                                    <td style="vertical-align:middle;">
                                                        <iais:code code="${patient.patientIdType}"/>
                                                    </td>
                                                    <td style="vertical-align:middle;">
                                                        <c:out value="${patient.patientIdNo}"/>
                                                    </td>
                                                    <td style="vertical-align:middle;">
                                                        <fmt:formatDate
                                                                value="${patient.patientDateBirth}"
                                                                pattern="${AppConsts.DEFAULT_DATE_FORMAT}"/>
                                                    </td>
                                                    <td style="vertical-align:middle;">
                                                        <iais:code code="${patient.patientNationality}"/>
                                                    </td>
                                                    <td style="vertical-align:middle;">
                                                        <fmt:formatDate
                                                                value="${patient.cycleStartDate}"
                                                                pattern="${AppConsts.DEFAULT_DATE_FORMAT}"/>
                                                    </td>
                                                    <td style="vertical-align:middle;">
                                                        <button type="button" onclick="fullDetailsView('${patient.patientCode}')" class="btn btn-default btn-sm">
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

                        <iais:action style="text-align:right;">
                            <a class="btn btn-secondary"
                               href="${pageContext.request.contextPath}/hcsa/enquiry/ar/PatientInfo-Adv-SearchResults-DownloadS">Download</a>
                        </iais:action>
                    </div>
                </div>

            </div>
        </div>
    </div>
</form>
<%@include file="/WEB-INF/jsp/include/utils.jsp" %>
