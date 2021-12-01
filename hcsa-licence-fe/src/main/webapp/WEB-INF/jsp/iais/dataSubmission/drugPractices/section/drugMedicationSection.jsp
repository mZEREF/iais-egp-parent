<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">
            <strong>
                Medication Details
            </strong>
        </h4>
    </div>
    <div id="patientDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <c:set var="suffix" value="Med" />
                <c:set var="drug" value="${drugMedication}" />
                <input type="hidden" name="medicationIndex" id="medicationIndex" value="-1"/>
                <c:forEach items="${drugMedication}" var="medication">
                    <c:set var="medicationIndex" value="${medication.medicationIndex}"/>
                    <iais:row >
                        <iais:field width="5" value="Medication 1" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7" label="true">
                            <c:out value="${medication.medicationIndex+1}"/>
                        </iais:value>
                        <iais:value width="1" cssClass="col-md-1">
                            <a  class="deleteMedication" id="deleteMedication${medicationIndex}" onclick="deleteMedication(index)"style="text-decoration:none;">X</a>
                        </iais:value>
                    </iais:row>

                    <iais:row>
                        <iais:field width="5" value="Batch No" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:input maxLength="3" type="text" name="batchNo${medicationIndex}" value="${medication.batchNo}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Strength (pg)" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:input maxLength="3" type="text" name="strength${medicationIndex}" value="${medication.strength}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Quantity" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:input maxLength="3" type="text" name="quantity${medicationIndex}" value="${medication.quantity}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Frequency" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:select cssClass="frequency"  name="frequency${medicationIndex}" firstOption="Please Select" codeCategory="DP_FREQUENCY" value="${medication.frequency}"/>
                        </iais:value>
                    </iais:row>

                    // -1 add , >=0 delete
                    <input type="hidden" name="actionIndex" id="actionIndex" value="-1"/>
                </c:forEach>
                <iais:row >
                    <iais:value width="5" cssClass="col-md-3" display="true">
                        <a class="addMedication"  onclick="addMedication()"style="text-decoration:none;">+ Add Medication</a>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Remarks" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:input maxLength="500" type="text" name="remarks" value="${drugSubmission.remarks}"/>
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>
<script>
    function addMedication(){
        sumbitPage(-1);
    }
    function  deleteMedication(medicationIndex){
        sumbitPage(medicationIndex);
    }

    function sumbitPage(donorAction){
        $("#actionValue").val(donorAction);
        submit("page");
    }
</script>