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
<webui:setLayout name="iais-intranet"/>
<div class="main-content dashboard">
    <form id="mainForm"  method="post" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <input type="hidden" name="adv_action_type" id="adv_action_type"/>

        <div class="col-lg-12 col-xs-12">
            <div class="center-content">
                <div class="intranet-content">
                    <div class="row form-horizontal">
                        <div class="bg-title col-xs-12 col-md-12">
                            <h2>Assisted Reproduction Enquiry</h2>
                        </div>
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
                                    <iais:field width="4" value="Submission ID"/>
                                    <iais:value width="6" cssClass="col-md-6"  >
                                        <input type="text" maxlength="20" id="submissionId"  name="submissionId" value="${assistedReproductionEnquiryFilterDto.submissionId}" >
                                    </iais:value>
                                </iais:row>
                                <iais:row>
                                    <iais:field width="4" value="Acknowledgement No."/>
                                    <iais:value width="6" cssClass="col-md-6"  >
                                        <input type="text" maxlength="20" id="acknowledgementNo"  name="acknowledgementNo" value="${assistedReproductionEnquiryFilterDto.acknowledgementNo}" >
                                    </iais:value>
                                </iais:row>
                                <iais:row>
                                    <iais:field width="4" value="Patient Name"/>
                                    <iais:value width="6" cssClass="col-md-6" >
                                        <input type="text" maxlength="20" id="patientName"  name="patientName" value="${assistedReproductionEnquiryFilterDto.patientName}" >
                                    </iais:value>
                                </iais:row>
                                <iais:row>
                                    <iais:field width="4" value="Patient ID Type"/>
                                    <iais:value width="6" cssClass="col-md-6"  >
                                        <iais:select name="patientIdType" id="patientIdType" firstOption="Please Select" codeCategory="CATE_ID_DS_ID_TYPE"
                                                     value="${assistedReproductionEnquiryFilterDto.patientIdType}" cssClass="idTypeSel" />
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
                                        <input type="text" maxlength="20" id="patientAgeNumberFrom"  name="patientAgeNumberFrom" value="${assistedReproductionEnquiryFilterDto.patientAgeNumberFrom}" >
                                    </iais:value>
                                    <iais:value width="3" cssClass="col-md-3">
                                        <input type="text" maxlength="20" id="patientAgeNumberTo"  name="patientAgeNumberTo" value="${assistedReproductionEnquiryFilterDto.patientAgeNumberTo}" >
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
                                        <input type="text" maxlength="20" id="husbandName"  name="husbandName" value="${assistedReproductionEnquiryFilterDto.husbandName}" >
                                    </iais:value>
                                </iais:row>

                                <iais:row>
                                    <iais:field width="4" value="Husband ID Type"/>
                                    <iais:value width="6" cssClass="col-md-6"  >
                                        <iais:select name="husbandIdType" id="husbandIdType" firstOption="Please Select" codeCategory="CATE_ID_DS_ID_TYPE"
                                                     value="${assistedReproductionEnquiryFilterDto.husbandIdType}" cssClass="idTypeSel" />
                                    </iais:value>
                                </iais:row>
                                <iais:row>
                                    <iais:field width="4" value="Husband ID No."/>
                                    <iais:value width="6" cssClass="col-md-6"  >
                                        <input type="text" maxlength="20" id="husbandIdNumber"  name="husbandIdNumber" value="${assistedReproductionEnquiryFilterDto.husbandIdNumber}" >
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
                                        <select name="arCentre" id="arCentre">
                                            <option value="" <c:if test="${empty assistedReproductionEnquiryFilterDto.arCentre}">selected="selected"</c:if>>Please Select</option>
                                            <c:forEach items="${embryosBiopsiedLocalSelectOption}" var="selectOption">
                                                <option value="${selectOption.value}" <c:if test="${assistedReproductionEnquiryFilterDto.arCentre ==selectOption.value}">selected="selected"</c:if>>${selectOption.text}</option>
                                            </c:forEach>
                                        </select>
                                    </iais:value>
                                </iais:row>
                                <iais:row>
                                    <iais:field width="4" value="Embryologist"/>
                                    <iais:value width="6" cssClass="col-md-6"  >
                                        <input type="text" maxlength="20" id="embryologist"  name="embryologist" value="${assistedReproductionEnquiryFilterDto.embryologist}" >
                                    </iais:value>
                                </iais:row>
                                <iais:row>
                                    <iais:field width="4" value="AR Practitioner"/>
                                    <iais:value width="6" cssClass="col-md-6"  >
                                        <input type="text" maxlength="20" id="arPractitioner"  name="arPractitioner" value="${assistedReproductionEnquiryFilterDto.arPractitioner}" >
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
                                        <iais:select name="cycleStagesStatus" id="cycleStagesStatus" firstOption="Please Select" codeCategory="CATE_ID_DS_ID_TYPE"
                                                     value="${assistedReproductionEnquiryFilterDto.cycleStagesStatus}" cssClass="idTypeSel" />
                                    </iais:value>
                                </iais:row>
                                <iais:row>
                                    <iais:field width="4" value="Cycle Start Date"/>
                                    <iais:value width="3" cssClass="col-md-3">
                                        <iais:datePicker id="cycleStagesDateFrom" name="cycleStagesDateFrom" dateVal="${cycleStagesDateFrom}"/>
                                    </iais:value>
                                    <iais:value width="3" cssClass="col-md-3">
                                        <iais:datePicker id="cycleStagesDateTo" name="cycleStagesDateTo" dateVal="${cycleStagesDateTo}"/>
                                    </iais:value>
                                </iais:row>
                                <iais:row>
                                    <iais:field width="4" value="AR / IUI Cycle"/>
                                    <iais:value width="6" cssClass="col-md-6"  >
                                        <iais:select name="arOrIuiCycle" id="arOrIuiCycle" firstOption="Please Select" codeCategory="CATE_ID_DS_ID_TYPE"
                                                     value="${assistedReproductionEnquiryFilterDto.arOrIuiCycle}" cssClass="idTypeSel" />
                                    </iais:value>
                                </iais:row>
                                <iais:row>
                                    <iais:field width="4" value="Main Indication of AR Cycle"/>
                                    <iais:value width="6" cssClass="col-md-6"  >
                                        <iais:multipleSelect name="indicationArCycle" selectValue="${assistedReproductionEnquiryFilterDto.indicationArCycle}" options="roleSelection"></iais:multipleSelect>
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
                                                        class="check-square"></span>Fresh Cycle (Natural)</label>
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
                                                        class="check-square"></span>Fresh Cycle (Simulated)</label>
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
                                                        class="check-square"></span>Frozen Oocyte Cycle</label>
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
                                                        class="check-square"></span>Frozen Embryo Cycle</label>
                                            </div>
                                        </iais:value>
                                    </div>

                                </iais:row>
                                <iais:row>
                                    <iais:field width="4" value="No. of Fresh cycle(s) previously undergone by patient"/>
                                    <iais:value width="3" cssClass="col-md-3">
                                        <input type="text" maxlength="20" id="freshCycleNumFrom"  name="freshCycleNumFrom" value="${assistedReproductionEnquiryFilterDto.freshCycleNumFrom}" >
                                    </iais:value>
                                    <iais:value width="3" cssClass="col-md-3">
                                        <input type="text" maxlength="20" id="freshCycleNumTo"  name="freshCycleNumTo" value="${assistedReproductionEnquiryFilterDto.freshCycleNumTo}" >
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
                                        <input type="text" maxlength="20" id="donorName"  name="donorName" value="${assistedReproductionEnquiryFilterDto.donorName}" >
                                    </iais:value>
                                </iais:row>
                                <iais:row>
                                    <iais:field width="4" value="Donor's ID Type"/>
                                    <iais:value width="6" cssClass="col-md-6"  >
                                        <iais:select name="donorIdType" id="donorIdType" firstOption="Please Select" codeCategory="CATE_ID_DS_ID_TYPE"
                                                     value="${assistedReproductionEnquiryFilterDto.donorIdType}" cssClass="idTypeSel" />
                                    </iais:value>
                                </iais:row>
                                <iais:row>
                                    <iais:field width="4" value="Donor's ID No."/>
                                    <iais:value width="6" cssClass="col-md-6"  >
                                        <input type="text" maxlength="20" id="donorIdNumber"  name="donorIdNumber" value="${assistedReproductionEnquiryFilterDto.donorIdNumber}" >
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
                                        <input type="text" maxlength="20" id="removedFromStorage"  name="removedFromStorage" value="${assistedReproductionEnquiryFilterDto.removedFromStorage}" >
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
                                        <iais:select name="sourceSemen" id="sourceSemen" firstOption="Please Select" codeCategory="CATE_ID_DS_ID_TYPE"
                                                     value="${assistedReproductionEnquiryFilterDto.sourceSemen}" cssClass="idTypeSel" />
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
                                                        class="check-square"></span>GIFT</label>
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
                                                        class="check-square"></span>ICSI</label>
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
                                                        class="check-square"></span>ZIFT</label>
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
                                                        class="check-square"></span>IVF</label>
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
                                                <input class="form-check-input"
                                                       type="checkbox"
                                                       name="embryosTransferredNum0" id="embryosTransferredNum0"
                                                       <c:if test="${ assistedReproductionEnquiryFilterDto.embryosTransferredNum0 =='on' }">checked</c:if>
                                                       aria-invalid="false">
                                                <label class="form-check-label"
                                                       for="embryosTransferredNum0"><span
                                                        class="check-square"></span>0</label>
                                            </div>
                                        </iais:value>
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
                                                        class="check-square"></span>>3</label>
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
                                                        class="check-square"></span>Day 1</label>
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
                                                        class="check-square"></span>Day 4</label>
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
                                                        class="check-square"></span>Day 2</label>
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
                                                        class="check-square"></span>Day 5</label>
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
                                                        class="check-square"></span>Day 3</label>
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
                                                        class="check-square"></span>Day 6 and above</label>
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
                                        <iais:datePicker id="deliveryDateFrom" name="deliveryDateFrom" dateVal="${deliveryDateFrom}"/>
                                    </iais:value>
                                    <iais:value width="3" cssClass="col-md-3">
                                        <iais:datePicker id="deliveryDateTo" name="deliveryDateTo" dateVal="${deliveryDateTo}"/>
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
                                    <iais:value width="2" cssClass="col-md-2">
                                        <div class="form-check">
                                            <input class="form-check-input"
                                                   type="radio"
                                                   name="patientART" id="patientARTNotApplicable"
                                                   value="2"
                                                   <c:if test="${ assistedReproductionEnquiryFilterDto.patientART =='2' }">checked</c:if>
                                                   aria-invalid="false">
                                            <label class="form-check-label"
                                                   for="patientARTNotApplicable"><span
                                                    class="check-circle"></span>Not Applicable</label>
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
                                    <iais:value width="2" cssClass="col-md-2">
                                        <div class="form-check">
                                            <input class="form-check-input"
                                                   type="radio"
                                                   name="patientIUI" id="patientIUINotApplicable"
                                                   value="2"
                                                   <c:if test="${ assistedReproductionEnquiryFilterDto.patientIUI =='2' }">checked</c:if>
                                                   aria-invalid="false">
                                            <label class="form-check-label"
                                                   for="patientIUINotApplicable"><span
                                                    class="check-circle"></span>Not Applicable</label>
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
                                    <iais:value width="2" cssClass="col-md-2">
                                        <div class="form-check">
                                            <input class="form-check-input"
                                                   type="radio"
                                                   name="patientPGT" id="patientPGTNotApplicable"
                                                   value="2"
                                                   <c:if test="${ assistedReproductionEnquiryFilterDto.patientPGT =='2' }">checked</c:if>
                                                   aria-invalid="false">
                                            <label class="form-check-label"
                                                   for="patientPGTNotApplicable"><span
                                                    class="check-circle"></span>Not Applicable</label>
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
                                    </div>
                                </iais:row>

                                <iais:row>
                                    <iais:field width="4" value="Total No. Disposed Of"/>
                                    <iais:value width="6" cssClass="col-md-6"  >
                                        <input type="text" maxlength="20" id="disposedTotalNumber"  name="disposedTotalNumber" value="${assistedReproductionEnquiryFilterDto.disposedTotalNumber}" >
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
                                                        class="check-square"></span>Oocyte(s)</label>
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
                                                        class="check-square"></span>Embryo(s)</label>
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
                                                        class="check-square"></span>Sperm</label>
                                            </div>
                                        </iais:value>
                                    </div>
                                </iais:row>

                                <iais:row>
                                    <iais:field width="4" value="Transferred In From"/>
                                    <iais:value width="6" cssClass="col-md-6"  >
                                        <iais:select name="transferredInFrom" id="transferredInFrom" firstOption="Please Select" codeCategory="CATE_ID_DS_ID_TYPE"
                                                     value="${assistedReproductionEnquiryFilterDto.transferredInFrom}" cssClass="idTypeSel" />
                                    </iais:value>
                                </iais:row>

                                <iais:row>
                                    <iais:field width="4" value="Transfer Out To"/>
                                    <iais:value width="6" cssClass="col-md-6"  >
                                        <iais:select name="transferOutTo" id="transferOutTo" firstOption="Please Select" codeCategory="CATE_ID_DS_ID_TYPE"
                                                     value="${assistedReproductionEnquiryFilterDto.transferOutTo}" cssClass="idTypeSel" />
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
                                                       name="pgtM" id="pgtM"
                                                       <c:if test="${ assistedReproductionEnquiryFilterDto.pgtM =='on'  }">checked</c:if>
                                                       aria-invalid="false">
                                                <label class="form-check-label"
                                                       for="pgtM"><span
                                                        class="check-square"></span>PGT-M</label>
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
                                                        class="check-square"></span>PGT-SR</label>
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
                                                        class="check-square"></span>PGT-A</label>
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
                                                        class="check-square"></span>PTT</label>
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
                                                        class="check-square"></span>Others</label>
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
                                    <button type="button" class="btn btn-secondary" type="button"
                                            onclick="javascript:doClear();">Clear
                                    </button>
                                    <button type="button" class="btn btn-primary" type="button"
                                            onclick="javascript:doSearch();">Search
                                    </button>
                                </iais:action>
                            </div>
                        </div>
                    </div>
                    <br>

                    <div class="row" id="patientResult">
                        <div class="col-xs-12">
                            <div class="components">

                                <iais:pagination param="SearchParam" result="SearchResult"/>
                                <div class="table-responsive">
                                    <div class="table-gp">
                                        <table aria-describedby="" class="table">
                                            <thead>
                                            <tr >
                                                <iais:sortableHeader needSort="false"
                                                                     field="APPLICATION_NO"
                                                                     value="AR Centre"/>
                                                <iais:sortableHeader needSort="false"
                                                                     field="APP_TYPE"
                                                                     value="Patient Name"/>
                                                <iais:sortableHeader needSort="false"
                                                                     field="LICENCE_NO"
                                                                     value="Patient ID Type"/>
                                                <iais:sortableHeader needSort="false"
                                                                     field="HCI_CODE"
                                                                     value="Patient ID No"/>
                                                <iais:sortableHeader needSort="false"
                                                                     field="HCI_NAME"
                                                                     value="Patient Date of Birth"/>
                                                <iais:sortableHeader needSort="false"
                                                                     field="HCI_ADDRESS"
                                                                     value="Patient Nationality"/>
                                                <iais:sortableHeader needSort="false"
                                                                     field="UEN_NO"
                                                                     value="Cycle Start Date"/>
                                                <iais:sortableHeader needSort="false"
                                                                     field="LICENSEE_NAME"
                                                                     value="Action"/>
                                            </tr>
                                            </thead>
                                            <tbody class="form-horizontal">
                                            <c:choose>
                                                <c:when test="${empty SearchResult.rows}">
                                                    <tr>
                                                        <td colspan="15">
                                                            <iais:message key="GENERAL_ACK018"
                                                                          escape="true"/>
                                                        </td>
                                                    </tr>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:forEach var="pool"
                                                               items="${SearchResult.rows}"
                                                               varStatus="status">
                                                        <tr>

                                                            <td>

                                                            </td>
                                                            <td>

                                                            </td>
                                                            <td>

                                                            </td>
                                                            <td>

                                                            </td>
                                                            <td>

                                                            </td>
                                                            <td>

                                                            </td>
                                                            <td>

                                                            </td>
                                                            <td>

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
                                       href="${pageContext.request.contextPath}/officer-online-enquiries-information-file">Download</a>
                                </iais:action>
                            </div>
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
        $('input[type="radio"]').prop("checked", false);
        $('input[type="checkbox"]').prop("checked", false);
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
        $("[name='adv_action_type']").val('search');
        $('#mainForm').submit();
    }

    function sortRecords(sortFieldName, sortType) {
        $("[name='crud_action_value']").val(sortFieldName);
        $("[name='crud_action_additional']").val(sortType);
        $("[name='adv_action_type']").val('search');
        submit('search');
    }







</script>