<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">
            <strong>
                Egg Freezing Only Cycle
            </strong>
        </h4>
    </div>
    <div id="efoDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <h3>
                    <p><label style="font-family:'Arial Negreta', 'Arial Normal', 'Arial';font-weight:700;"><c:out value="${arSuperDataSubmissionDto.patientDto.patientName}"/></label><label style="font-family:'Arial Normal', 'Arial';font-weight:400;"><c:out value="(${arSuperDataSubmissionDto.patientDto.patientIdNO})"/></label></p>
                </h3>
                <iais:row>
                    <iais:field width="5" value="Premises where egg freezing only cycle is performed" mandatory="false"/>
                    <iais:value width="7" cssClass="col-md-7" label="true">
                        <c:out value="${arSuperDataSubmissionDto.efoCycleStageDto.performed}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Date Started" mandatory="false"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:datePicker id="efoDateStarted" name="efoDateStarted" value="${arSuperDataSubmissionDto.efoCycleStageDto.dateStarted}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Patient's Age as of This Treatment" mandatory="false"/>
                    <iais:value width="7" cssClass="col-md-7" label="true">
                        <c:out value="${arSuperDataSubmissionDto.efoCycleStageDto.yearNum} Years and ${arSuperDataSubmissionDto.efoCycleStageDto.monthNum} Months"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Is it Medically Indicated?" mandatory="false"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <c:if test="${arSuperDataSubmissionDto.efoCycleStageDto.indicated ==true }">
                            <label class="form-check-label" >Yes</label></c:if>
                        <c:if test="${arSuperDataSubmissionDto.efoCycleStageDto.indicated ==false }">
                            <label class="form-check-label" >No</label></c:if>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Reasons" mandatory="false"/>
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
$('#reasonSelect').change(function () {

var reason= $('#reasonSelect option:selected').val();
if("MS003"==reason){
$('#cgo').attr("style" ,"display: block");

}else  {
$('#cgo').attr("style" ,"display: none");

}
if("MS008"==reason){
$('#selectHciNameAppeal').attr("style","display: block");

}else {
$('#selectHciNameAppeal').attr("style","display: none");
}
if("MS004"==reason){
$('#licenceYear').attr("style","display: block");
}else {
$('#licenceYear').attr("style","display: none");
}
if("MS007"==reason){
$('#othersReason').attr("style","display: block");
}else {
$('#othersReason').attr("style","display: none");
}

});
</script>