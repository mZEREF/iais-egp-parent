<div class="panel panel-default">
    <input type="hidden" name="crud_action_value_ar_stage" id="crud_action_value_ar_stage" value="-2"/>
    <input type="hidden" name="crud_action_value_valiate_donor" id="crud_action_value_valiate_donor" value="-1"/>
    <div class="panel-heading">
        <h4 class="panel-title" data-toggle="collapse" href="#arStageDetails">
            <strong>
                Assisted Reproduction Submission
            </strong>
        </h4>
    </div>
    <div id="arStageDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <c:set var="arCycleStageDto" value="${arSuperDataSubmissionDto.arCycleStageDto}" />
                <c:set var="patientDto" value="${arSuperDataSubmissionDto.patientInfoDto.patient}" />
                <h3>
                    <p><label style="font-family:'Arial Negreta', 'Arial Normal', 'Arial';font-weight:700;"><c:out value="${patientDto.patientName}"/></label><label style="font-family:'Arial Normal', 'Arial';font-weight:400;"><c:out value="${patientDto.patientIdNO}"/></label></p>
                </h3>
                <iais:row>
                    <iais:field width="5" value="Premises where AR is performed" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7" label="true">
                        <c:out value=""/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Date Started" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:datePicker id="startDate" name="startDate" value="${arCycleStageDto.startDate}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Patient's Age as of This Cycle" mandatory="false"/>
                    <iais:value width="7" cssClass="col-md-7" label="true">
                        <c:out value="${arCycleStageDto.cycleAge}"/>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="Main Indication" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:select name="mainIndication"  firstOption="Please Select" codeCategory="AR_MAIN_INDICATION" value="${arCycleStageDto.mainIndication}"  onchange ="toggleOnSelect(this, 'AR_MI_013', 'mainIndicationOtherRow')"/>
                </iais:value>
                </iais:row>

                <iais:row id="mainIndicationOtherRow">
                    <iais:field width="5" value="Main Indication (Others)" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:input maxLength="100" type="text" name="mainIndicationOthers" id="mainIndicationOthers" value="${arCycleStageDto.mainIndicationOthers}" />
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="Other Indication" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:select name="otherIndication" multiSelect="true"  codeCategory="AR_OTHER_INDICATION"  multiValues="${arCycleStageDto.otherIndicationValues}"  onchange ="toggleMultiSelect(this, 'AR_0I_013', 'otherIndicationOthersRow')"/>
                    </iais:value>
                </iais:row>

                <iais:row id="otherIndicationOthersRow">
                    <iais:field width="5" value="Other Indication (Others)" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:input maxLength="100" type="text" name="otherIndicationOthers" id="otherIndicationOthers" value="${arCycleStageDto.otherIndicationOthers}" />
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="In-Vitro Maturation" mandatory="false"/>
                    <iais:value width="3" cssClass="col-md-3">
                        <div class="form-check">
                            <input class="form-check-input"
                                   type="radio"
                                   name="inVitroMaturation"
                                   value="1"
                                   id="inVitroMaturationRadioYes"
                                   <c:if test="${arCycleStageDto.inVitroMaturation}">checked</c:if>
                                   aria-invalid="false">
                            <label class="form-check-label"
                                   for="inVitroMaturationRadioYes"><span
                                    class="check-circle"></span>Yes</label>
                        </div>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4">
                        <div class="form-check">
                            <input class="form-check-input" type="radio"
                                   name="inVitroMaturation"
                                   value="0"
                                   id="inVitroMaturationRadioNo"
                                   <c:if test="${!arCycleStageDto.inVitroMaturation}">checked</c:if>
                                   aria-invalid="false">
                            <label class="form-check-label"
                                   for="inVitroMaturationRadioNo"><span
                                    class="check-circle"></span>No</label>
                        </div>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="Current AR Treatment" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                            <c:forEach items="${currentArTreatments}" var="currentArTreatment">
                                <c:set var="currentArTreatmentCode" value="${currentArTreatment.code}"/>
                                <div class="form-check col-xs-7" >
                                    <input class="form-check-input" type="checkbox"
                                           name="currentArTreatment"
                                           value="${currentArTreatmentCode}"
                                           id="currentArTreatmentCheck${currentArTreatmentCode}"
                                           <c:if test="${arCycleStageDto.currentARTreatment ge currentArTreatmentCode}">checked</c:if>
                                           aria-invalid="false">
                                    <label class="form-check-label"
                                           for="currentArTreatmentCheck${currentArTreatmentCode}"><span
                                            class="check-square"></span>
                                        <c:out value="${currentArTreatment.codeValue}"/></label>
                                </div>
                            </c:forEach>
                    </iais:value>
                    <span id="error_currentArTreatment" name="iaisErrorMsg" class="error-msg"></span>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="No. of Children from Current Marriage" mandatory="false"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:select name="noChildrenCurrentMarriage" options="noChildrenDropDown" firstOption="Please Select" value="${arCycleStageDto.noChildrenCurrentMarriage}"/>
                        </iais:value>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="No. of Children from Previous Marriage" mandatory="false"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:select name="noChildrenPreviousMarriage" options="noChildrenDropDown" firstOption="Please Select" value="${arCycleStageDto.noChildrenPreviousMarriage}"/>
                        </iais:value>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="No. of Children conceived through AR" mandatory="false"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:select name="noChildrenConceivedAR" options="noChildrenDropDown" firstOption="Please Select" value="${arCycleStageDto.noChildrenConceivedAR}"/>
                        </iais:value>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="Total Number of AR cycles previously undergone by patient" mandatory="false"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:select name="totalNumberARCPreviouslyUndergonePatient" options="numberArcPreviouslyDropDown" firstOption="Please Select" value="${arCycleStageDto.totalNumberARCPreviouslyUndergonePatient}"  onchange ="toggleOnSelect(this, '21', 'totalNumberARCOtherRow')"/>
                        </iais:value>
                    </iais:value>
                </iais:row>

                <iais:row id="totalNumberARCOtherRow">
                    <iais:field width="5" value="Number of Cycles undergone Overseas" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:input maxLength="100" type="text" name="numberCyclesUndergoneOverseas" id="numberCyclesUndergoneOverseas" value="${arCycleStageDto.numberCyclesUndergoneOverseas}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Number of Cycles undergone Locally" mandatory="false"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:value width="7" cssClass="col-md-7">
                           <c:out value=""/>
                        </iais:value>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="Enhanced Counselling" mandatory="false"/>
                    <iais:value width="3" cssClass="col-md-3">
                        <div class="form-check">
                            <input class="form-check-input"
                                   type="radio"
                                   name="enhancedCounselling"
                                   value="1"
                                   id="enhancedCounsellingRadioYes"
                                   <c:if test="${arCycleStageDto.enhancedCounselling}">checked</c:if>
                                   aria-invalid="false">
                            <label class="form-check-label"
                                   for="enhancedCounsellingRadioYes"><span
                                    class="check-circle"></span>Yes</label>
                        </div>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4">
                        <div class="form-check">
                            <input class="form-check-input" type="radio"
                                   name="enhancedCounselling"
                                   value="0"
                                   id="enhancedCounsellingRadioNo"
                                   <c:if test="${!arCycleStageDto.enhancedCounselling}">checked</c:if>
                                   aria-invalid="false">
                            <label class="form-check-label"
                                   for="enhancedCounsellingRadioNo"><span
                                    class="check-circle"></span>No</label>
                        </div>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="AR Practitioner" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:select name="practitioner" options="practitionerDropDown" firstOption="Please Select" value="${arCycleStageDto.practitioner}"/>
                        </iais:value>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="Embryologist" mandatory="false"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:select name="embryologist" options="embryologistDropDown" firstOption="Please Select" value="${arCycleStageDto.embryologist}"/>
                        </iais:value>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="Was a donor's Oocyte(s)/Embryo(s)/Sperms used in this cycle?" mandatory="true"/>
                    <iais:value width="3" cssClass="col-md-3">
                        <div class="form-check">
                            <input class="form-check-input"
                                   type="radio"
                                   name="oocyteEmbryoSpermsUsed"
                                   value="1"
                                   id="oocyteEmbryoSpermsUsedRadioYes"
                                   <c:if test="${arCycleStageDto.oocyteEmbryoSpermsUsed}">checked</c:if>
                                   aria-invalid="false">
                            <label class="form-check-label"
                                   for="oocyteEmbryoSpermsUsedRadioYes"><span
                                    class="check-circle"></span>Yes</label>
                        </div>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4">
                        <div class="form-check">
                            <input class="form-check-input" type="radio"
                                   name="oocyteEmbryoSpermsUsed"
                                   value="0"
                                   id="oocyteEmbryoSpermsUsedRadioNo"
                                   <c:if test="${!arCycleStageDto.oocyteEmbryoSpermsUsed}">checked</c:if>
                                   aria-invalid="false">
                            <label class="form-check-label"
                                   for="oocyteEmbryoSpermsUsedRadioNo"><span
                                    class="check-circle"></span>No</label>
                        </div>
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>
<%@include file="donorSection.jsp"%>
<script  type="text/javascript">
  $(document).ready(function (){
     toggleOnSelect("#mainIndication",'AR_MI_013', 'mainIndicationOtherRow');
     toggleOnSelect("#totalNumberARCPreviouslyUndergonePatient",'21', 'totalNumberARCOtherRow');
     toggleOnMultiSelect("#otherIndication",'AR_0I_013', 'otherIndicationOthersRo');
   });

   function validateDonor(index){
       $("#crud_action_value_valiate_donor").val(index);
       submit("page");
   }

   function addDonor(){
       //index ==-1 : add
       $("#crud_action_value_ar_stage").val("-1");
       submit("page");
   }

   function deleteDonor(index){
       if($("#crud_action_value_donor_size").val()>1){
           $("#crud_action_value_ar_stage").val(index);
           submit("page");
       }else {

       }
   }

   function showDonor(index){
       $("#deleteDonor"+index).show();
       $("#source"+index+"Row").hide();
   }
  function hideDonor(index){
      $("#deleteDonor"+index).hide();
      $("#source"+index+"Row").show();
  }

</script>