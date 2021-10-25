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
                        <input type="checkbox" name="isFromPatient" value="true"
                            <c:if test="${oocyteRetrievalStageDto.isFromPatient}"> checked </c:if> >Patient
                        <input type="checkbox" name="isFromPatientTissue" value="true"
                            <c:if test="${oocyteRetrievalStageDto.isFromPatientTissue}"> checked </c:if> >Patient's Ovarian Tissue
                        <input type="checkbox" name="isFromDonor" value="true"
                            <c:if test="${oocyteRetrievalStageDto.isFromDonor}"> checked </c:if> >Directed Donor
                        <input type="checkbox" name="isFromDonorTissue" value="true"
                            <c:if test="${oocyteRetrievalStageDto.isFromDonorTissue}"> checked </c:if> >Directed Donor's Ovarian Tissue
                    </iais:value>
                    <span id="error_oocyteRetrievalFrom" name="iaisErrorMsg" class="error-msg"></span>
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
                        <label name="totalRetrievedNum">${totalRetrievedNum}</label>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Severe Ovarian Hyperstimulation Syndrome" mandatory="true"/>
                    <a class="btn-tooltip styleguide-tooltip" href="javascript:void(0);" data-toggle="tooltip" data-html="true" title="&lt;p&gt;<iais:message key="NEW_ACK028"></iais:message>&lt;/p&gt;">i</a>
                    <iais:value width="3" cssClass="col-md-3">
                        <input type="radio" name="isOvarianSyndrome" value="true"
                            <c:if test="${oocyteRetrievalStageDto.isOvarianSyndrome}"> checked </c:if> >Yes
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4">
                        <input type="radio" name="isOvarianSyndrome" value="false"
                            <c:if test="${not oocyteRetrievalStageDto.isOvarianSyndrome}"> checked </c:if> >No
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>
<script>
    $(document).ready(function () {
        $('input[type="checkbox"]').click(function () {
            if (this.name == "isFromPatient" || this.name == "isFromPatientTissue"){
                if(this.val() == "true"){
                    $("#isFromDonor").attr("disabled",true);
                    $("#isFromDonorTissue").attr("disabled",true);
                }else {
                    $("#isFromDonor").attr("disabled",false);
                    $("#isFromDonorTissue").attr("disabled",false);
                }
            }else if (this.name == "isFromDonor" || this.name == "isFromDonorTissue"){
                if(this.val() == "true"){
                    $("#isFromPatient").attr("disabled",true);
                    $("#isFromPatientTissue").attr("disabled",true);
                }else {
                    $("#isFromPatient").attr("disabled",false);
                    $("#isFromPatientTissue").attr("disabled",false);
                }
            }
        });

        $('input[type="text"]').blur(function () {
            var totalNum = 0;
            for(var i in $('input[type="text"]')){
                totalNum += i.val();
            }
            $('#totalRetrievedNum').val(totalNum);
        });
    });
</script>