<script type="text/javascript" src="<%=webroot1%>js/dataSubmission/oocyteRectrievalSection.js"></script>
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
                <c:set var="patientDto" value="${arSuperDataSubmissionDto.patientInfoDto.patient}"/>
                <p>
                    <label style="font-family:'Arial Negreta', 'Arial Normal', 'Arial';font-weight:700;font-size: 2.2rem;">
                        <c:out value="${patientDto.name}"/>&nbsp
                    </label>
                    <label style="font-family:'Arial Normal', 'Arial';font-weight:400;">${empty patientDto.idNumber ? "" : "("}
                        <c:out value="${patientDto.idNumber}"/>
                        ${empty patientDto.idNumber ? "" : ")"}
                    </label>
                </p>
                <hr/>
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
                        <div class="form-check col-xs-12" style="padding: 0px;">
                            <input class="form-check-input" type="checkbox"
                                   name="isFromDonor"
                                   value="true"
                                   id="isFromDonor"
                                   <c:if test="${oocyteRetrievalStageDto.isFromDonor}">checked</c:if>
                                   aria-invalid="false">
                            <label class="form-check-label"
                                   for="isFromDonor"><span
                                    class="check-square"></span>Directed Donor</label>
                        </div>
                        <div class="form-check col-xs-12" style="padding: 0px;">
                            <input class="form-check-input" type="checkbox"
                                   name="isFromDonorTissue"
                                   value="true"
                                   id="isFromDonorTissue"
                                   <c:if test="${oocyteRetrievalStageDto.isFromDonorTissue}">checked</c:if>
                                   aria-invalid="false">
                            <label class="form-check-label"
                                   for="isFromDonorTissue"><span
                                    class="check-square"></span>Directed Donor's Ovarian Tissue</label>
                        </div>
                        <span id="error_oocyteRetrievalFrom" name="iaisErrorMsg" class="error-msg"></span>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="No. Retrieved (Mature)" cssClass="col-md-6"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <input type="number" id="matureRetrievedNum" name="matureRetrievedNum"
                               value="${oocyteRetrievalStageDto.matureRetrievedNum}"
                               oninput="if(value.length>2)value=value.slice(0,2)">
                        <span id="error_matureRetrievedNum" name="iaisErrorMsg" class="error-msg"></span>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="No. Retrieved (Immature)" cssClass="col-md-6"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <input type="number" id="immatureRetrievedNum" name="immatureRetrievedNum"
                               value="${oocyteRetrievalStageDto.immatureRetrievedNum}"
                               oninput="if(value.length>2)value=value.slice(0,2)">
                        <span id="error_immatureRetrievedNum" name="iaisErrorMsg" class="error-msg"></span>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="No. Retrieved (Others)" cssClass="col-md-6"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <input type="number" id="otherRetrievedNum" name="otherRetrievedNum"
                               value="${oocyteRetrievalStageDto.otherRetrievedNum}"
                               oninput="if(value.length>2)value=value.slice(0,2)">
                        <span id="error_otherRetrievedNum" name="iaisErrorMsg" class="error-msg"></span>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="No. Retrieved (Total)" cssClass="col-md-6"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <p id="totalRetrievedNum">${totalRetrievedNum}</p>
                    </iais:value>
                </iais:row>
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
            </div>
        </div>
    </div>
</div>