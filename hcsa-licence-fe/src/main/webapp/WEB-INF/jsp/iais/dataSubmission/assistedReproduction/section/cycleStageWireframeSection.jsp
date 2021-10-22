<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">
            <strong>
                Assisted Reproduction Submission
            </strong>
        </h4>
    </div>
    <div id="efoDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <c:set var="arCycleStageDto" value="${arSuperDataSubmissionDto.arCycleStageDto}" />
                <c:set var="patientDto" value="${arSuperDataSubmissionDto.patientDto}" />
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
                        <iais:datePicker id="arCycleStageDtoDateStarted" name="arCycleStageDtoDateStarted" value="${arCycleStageDto.dateStarted}"/>
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
                        <iais:select name="mainIndication" options="mainIndicationDrops" firstOption="Please Select" value="${arCycleStageDto.mainIndication}"  onchange ="showMainIndicationOther(this.value)"/>
                </iais:value>
                </iais:row>

                <iais:row id="mainIndicationOtherRow">
                    <iais:field width="5" value="Main Indication (Others)" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:input maxLength="100" type="text" name="mainIndicationOther" id="mainIndicationOther" value="${arCycleStageDto.mainIndicationOther}" />
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="Main Indication" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:select name="mainIndication" options="mainIndicationDrops" firstOption="Please Select" value="${arCycleStageDto.mainIndication}"  onchange ="showMainIndicationOther(this.value)"/>
                    </iais:value>
                </iais:row>

                <iais:row id="mainIndicationOtherRow">
                    <iais:field width="5" value="Main Indication (Others)" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:input maxLength="100" type="text" name="mainIndicationOther" id="mainIndicationOther" value="${arCycleStageDto.mainIndicationOther}" />
                    </iais:value>
                </iais:row>


                <iais:row>
                    <iais:field width="5" value="Is it Medically Indicated?" mandatory="true"/>
                    <iais:value width="3" cssClass="col-md-3">
                        <div class="form-check">
                            <input class="form-check-input"
                                   type="radio"
                                   name="patRadio"
                                   value="yes"
                                   id="radioYes"
                                   <c:if test="${empty arSuperDataSubmissionDto.efoCycleStageDto.indicated || arSuperDataSubmissionDto.efoCycleStageDto.indicated ==true }">checked</c:if>
                                   aria-invalid="false"
                                   disabled>
                            <label class="form-check-label"
                                   for="radioYes"><span
                                    class="check-circle"></span>Yes</label>
                        </div>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4">
                        <div class="form-check">
                            <input class="form-check-input" type="radio"
                                   name="patRadio"
                                   value="no"
                                   id="radioNo"
                                   <c:if test="${arSuperDataSubmissionDto.efoCycleStageDto.indicated == false}">checked</c:if>
                                   aria-invalid="false"
                                   disabled>
                            <label class="form-check-label"
                                   for="radioNo"><span
                                    class="check-circle"></span>No</label>
                        </div>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Reasons" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <select id="reasonSelect" name="reasonSelect" style="margin-left: 2%">
                            <option value="">Please Select</option>
                            <c:forEach items="${selectOptionList}" var="selectOption">
                                <option value="${selectOption.value}" <c:if test="${arSuperDataSubmissionDto.efoCycleStageDto.reason==selectOption.value}">selected="selected"</c:if> >${selectOption.text}</option>
                            </c:forEach>
                        </select>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="" mandatory="false"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <div id="othersReason" style="display: none" >
                            <input type="text" maxlength="20"   name="othersReason" value="${arSuperDataSubmissionDto.efoCycleStageDto.otherReason}" >
                        </div>
                    </iais:value>
                </iais:row>

            </div>
        </div>
    </div>
</div>
<<script  type="text/javascript">
  $(document).ready(function (){
     showMainIndicationOther($("#mainIndication").val());
   });
   function showMainIndicationOther(value){
       if(value == 'AR_MI_001'){
            $("#mainIndicationOtherRow").show();
         }else {
          $("#mainIndicationOtherRow").hide();
          $('#mainIndicationOther').val("");
       }
   }
</script>