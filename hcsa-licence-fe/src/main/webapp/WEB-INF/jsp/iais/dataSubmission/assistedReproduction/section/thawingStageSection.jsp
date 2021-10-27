<c:set var="thawingStageDto" value="${arSuperDataSubmissionDto.thawingStageDto}"/>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">
            <strong>
                Thawing (Oocytes & Embryos)
            </strong>
        </h4>
    </div>
    <div id="patientDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <iais:row>
                    <iais:field width="5" value="Thawing Oocyte(s) or Embryo(s)" mandatory="true"/>
                    <iais:value width="3" cssClass="col-md-3">
                        <div class="form-check col-xs-12" >
                            <input class="form-check-input" type="checkbox"
                                   name="hasOocyte"
                                   value="true"
                                   id="hasOocyte"
                                   <c:if test="${thawingStageDto.hasOocyte}">checked</c:if>
                                   aria-invalid="false">
                            <label class="form-check-label"
                                   for="hasOocyte"><span
                                    class="check-square"></span>Oocyte(s)</label>
                        </div>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4">
                        <div class="form-check col-xs-12" >
                            <input class="form-check-input" type="checkbox"
                                   name="hasEmbryo"
                                   value="true"
                                   id="hasEmbryo"
                                   <c:if test="${thawingStageDto.hasEmbryo}">checked</c:if>
                                   aria-invalid="false">
                            <label class="form-check-label"
                                   for="hasEmbryo"><span
                                    class="check-square"></span>Embryo(s)</label>
                        </div>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="No. of Oocytes Thawed" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:input maxLength="2" type="text" name="thawedOocytesNum" id="thawedOocytesNum" value="${thawingStageDto.thawedOocytesNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="No. of Oocytes Survived after Thawing (Mature)" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:input maxLength="2" type="text" name="thawedOocytesSurvivedMatureNum" id="thawedOocytesSurvivedMatureNum" value="${thawingStageDto.thawedOocytesSurvivedMatureNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="No. of Oocytes Survived after Thawing (Immature)" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:input maxLength="2" type="text" name="thawedOocytesSurvivedImmatureNum" id="thawedOocytesSurvivedImmatureNum" value="${thawingStageDto.thawedOocytesSurvivedImmatureNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="No. of Oocytes Survived after Thawing (Others)" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:input maxLength="2" type="text" name="thawedOocytesSurvivedOtherNum" id="thawedOocytesSurvivedOtherNum" value="${thawingStageDto.thawedOocytesSurvivedOtherNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="No. of Embryos Thawed" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:input maxLength="2" type="text" name="thawedEmbryosNum" id="thawedEmbryosNum" value="${thawingStageDto.thawedEmbryosNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="No. of Embryos Survived after Thawing" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:input maxLength="2" type="text" name="thawedEmbryosSurvivedNum" id="thawedEmbryosSurvivedNum" value="${thawingStageDto.thawedEmbryosSurvivedNum}"/>
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