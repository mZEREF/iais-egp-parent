<%@ page import="com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto" %>
<%@ page import="com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.OocyteRetrievalStageDto" %>
<script type="text/javascript" src="<%=webroot1%>js/dataSubmission/oocyteRectrievalSection.js"></script>
<%
    ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(request);
    OocyteRetrievalStageDto oocyteRetrievalStageDto = arSuperDataSubmissionDto.getOocyteRetrievalStageDto();
%>
<%--@elvariable id="arSuperDataSubmissionDto" type="com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto"--%>
<c:set var="oocyteRetrievalStageDto" value="${arSuperDataSubmissionDto.oocyteRetrievalStageDto}"/>
<div class="panel panel-default">
    <div class="panel-heading" style="padding-left: 90px;">
        <h4 class="panel-title">
            <strong>
                Oocyte Retrieval
            </strong>
        </h4>
    </div>
    <div id="cycleDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <h3>
                    <label ><c:out value="${arSuperDataSubmissionDto.patientInfoDto.patient.name}"/></label>
                    <span style="font-weight:normal"><c:out value="(${arSuperDataSubmissionDto.patientInfoDto.patient.idNumber})"/>
                    </span>
                </h3>
                <iais:row>
                    <label class="col-xs-6 col-md-6 control-label">Severe Ovarian Hyperstimulation Syndrome
                        <span class="mandatory">*</span>
                        <a class="btn-tooltip styleguide-tooltip" href="javascript:void(0);" data-toggle="tooltip"
                           data-html="true"
                           title="&lt;p&gt;<iais:message key="DS_ACK001"/>&lt;/p&gt;">i</a>
                    </label>
                    <iais:value width="3" cssClass="col-md-3">
                        <div class="form-check" style="padding: 0px;">
                            <input class="form-check-input"
                                   type="radio"
                                   name="isOvarianSyndrome"
                                   value="true"
                                   id="isOvarianSyndromeYes"
                                   <c:if test="${oocyteRetrievalStageDto.isOvarianSyndrome}">checked</c:if>
                                   aria-invalid="false">
                            <label class="form-check-label"
                                   for="isOvarianSyndromeYes"><span
                                    class="check-circle"></span>Yes</label>
                        </div>
                    </iais:value>
                    <iais:value width="3" cssClass="col-md-3">
                        <div class="form-check">
                            <input class="form-check-input"
                                   type="radio"
                                   name="isOvarianSyndrome"
                                   value="false"
                                   id="isOvarianSyndromeNo"
                                   <c:if test="${! oocyteRetrievalStageDto.isOvarianSyndrome}">checked</c:if>
                                   aria-invalid="false">
                            <label class="form-check-label"
                                   for="isOvarianSyndromeNo"><span
                                    class="check-circle"></span>No</label>
                        </div>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="Oocyte(s) was retrieved from?" mandatory="true" cssClass="col-md-6"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <div class="form-check col-xs-12" style="padding: 0px;">
                            <input class="form-check-input" type="checkbox"
                                   name="isFromPatient"
                                   value="true"
                                   id="isFromPatient"
                                   <c:if test="${oocyteRetrievalStageDto.isFromPatient}">checked</c:if>
                                   aria-invalid="false">
                            <label class="form-check-label"
                                   for="isFromPatient"><span
                                    class="check-square"></span>Patient</label>
                        </div>
                        <div class="form-check col-xs-12" style="padding: 0px;">
                            <input class="form-check-input" type="checkbox"
                                   name="isFromPatientTissue"
                                   value="true"
                                   id="isFromPatientTissue"
                                   <c:if test="${oocyteRetrievalStageDto.isFromPatientTissue}">checked</c:if>
                                   aria-invalid="false">
                            <label class="form-check-label"
                                   for="isFromPatientTissue"><span
                                    class="check-square"></span>Patient's Ovarian Tissue</label>
                        </div>
                        <span id="error_oocyteRetrievalFrom" name="iaisErrorMsg" class="error-msg"></span>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="No. Retrieved (Mature)" cssClass="col-md-6"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <iais:input maxLength="2" type="text" name="matureRetrievedNum" id="matureRetrievedNum"
                                    value="${oocyteRetrievalStageDto.matureRetrievedNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="No. Retrieved (Immature)" cssClass="col-md-6"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <iais:input maxLength="2" type="text" name="immatureRetrievedNum" id="immatureRetrievedNum"
                                    value="${oocyteRetrievalStageDto.immatureRetrievedNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="No. Retrieved (Others)" cssClass="col-md-6"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <iais:input maxLength="2" type="text" name="otherRetrievedNum" id="otherRetrievedNum"
                                    value="${oocyteRetrievalStageDto.otherRetrievedNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="No. Retrieved (Total)" cssClass="col-md-6"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <p id="totalRetrievedNum"><%=oocyteRetrievalStageDto.getTotalNum()%></p>
                    </iais:value>
                </iais:row>
                <%@include file="hasDisposalRow.jsp"%>
            </div>
        </div>
    </div>
</div>