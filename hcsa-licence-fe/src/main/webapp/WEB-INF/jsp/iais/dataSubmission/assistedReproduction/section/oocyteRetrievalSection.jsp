<c:set var="oocyteRetrievalStageDto" value="${arSuperDataSubmissionDto.oocyteRetrievalStageDto}"/>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">
            <strong>
                Oocyte Retrieval
            </strong>
        </h4>
    </div>
    <div id="patientDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <iais:row>
                    <iais:field width="5" value="Oocyte(s) was retrieved from?" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <div class="form-check col-xs-12" >
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
                        <div class="form-check col-xs-12" >
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
                        <div class="form-check col-xs-12" >
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
                        <div class="form-check col-xs-12" >
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
                    <iais:field width="5" value="No. Retrieved (Mature)"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:input maxLength="2" type="text" name="matureRetrievedNum" id="matureRetrievedNum" value="${oocyteRetrievalStageDto.matureRetrievedNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="No. Retrieved (Immature)"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:input maxLength="2" type="text" name="immatureRetrievedNum" id="immatureRetrievedNum" value="${oocyteRetrievalStageDto.immatureRetrievedNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="No. Retrieved (Others)"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:input maxLength="2" type="text" name="otherRetrievedNum" id="otherRetrievedNum" value="${oocyteRetrievalStageDto.otherRetrievedNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="No. Retrieved (Total)"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <p id="totalRetrievedNum">${totalRetrievedNum}</p>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <label class="col-xs-5 col-md-4 control-label" >Severe Ovarian Hyperstimulation Syndrome
                        <span class="mandatory">*</span>
                        <a class="btn-tooltip styleguide-tooltip" href="javascript:void(0);" data-toggle="tooltip" data-html="true" title="&lt;p&gt;<iais:message key="DS_ACK001"></iais:message>&lt;/p&gt;">i</a>
                    </label>
                    <iais:value width="3" cssClass="col-md-3">
                        <div class="form-check">
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
                    <iais:value width="4" cssClass="col-md-4">
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
<script>
    $(document).ready(function () {
        $('input[type="checkbox"]').click(function () {
            let setVal = $(this).prop('checked');
            if (this.name == "isFromPatient" || this.name == "isFromPatientTissue"){
                $("#isFromDonor").attr("disabled",setVal);
                $("#isFromDonorTissue").attr("disabled",setVal);
            }else if (this.name == "isFromDonor" || this.name == "isFromDonorTissue"){
                $("#isFromPatient").attr("disabled",setVal);
                $("#isFromPatientTissue").attr("disabled",setVal);
            }
        });

        $('input[type="text"]').change(function () {
            var totalNum = 0;
            $('input[type="text"]').each(function (){
                let val = $(this).val();
                if (val){
                    var intNum = parseInt(val);
                    if (intNum){
                        totalNum += intNum;
                    }
                }
            })
            $('#totalRetrievedNum').html(totalNum);
        });
    });
</script>