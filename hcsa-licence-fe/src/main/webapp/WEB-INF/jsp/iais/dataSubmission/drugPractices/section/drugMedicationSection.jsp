<script type="text/javascript" src="<%=webroot1%>js/dataSubmission/dp_drugMedication.js"></script>
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
                <c:set var="drug" value="" />
                <input type="hidden" name="drugMedicationLength" value="1">
                <div class="med" id="indexs">
                <c:forEach items="${drugMedicationDtos}" var="drugMedicationDto" begin="0" end="0" varStatus="idxStatus">
                    <c:set var="index" value="${idxStatus.index}" />
                   <iais:row id="test">
                           <div class="col-sm-6 control-label formtext col-md-8">
                               <div class="cgo-header">
                                   <strong>Medication <label class="assign-psn-item">${index+1}</label></strong>
                               </div>
                           </div>
                       <div class="col-md-4 col-xs-7 text-right">
                           <a class="removeMedications"  onclick="" style="text-decoration:none;" href="javascript:void(0)">
                               <h4 class="text-danger">
                                   <em class="fa fa-times-circle del-size-36 removeBtn cursorPointer"></em>
                               </h4>
                           </a>
                       </div>

                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Batch No" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:input maxLength="3" type="text" name="batchNo${index}" value="${drugMedicationDto.batchNo}"/>
                            <span class="error-msg" name="iaisErrorMsg" id="error_batchNo"${index}></span>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Strength (pg)" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:input maxLength="3" type="text" name="strength${index}" value="${drugMedicationDto.strength}"/>
                            <span class="error-msg" name="iaisErrorMsg" id="error_strength"${index}></span>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Quantity" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:input maxLength="3" type="text" name="quantity${index}" value="${drugMedicationDto.quantity}"/>
                            <span class="error-msg" name="iaisErrorMsg" id="error_quantity"${index}></span>
                        </iais:value>
                    </iais:row>
                <div class="medicationContent">
                    <iais:row>
                        <iais:field width="5" value="Frequency" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:select cssClass="frequency"  name="frequency${index}" firstOption="Please Select" codeCategory="DP_FREQUENCY" value="${drugMedicationDto.frequency}"/>
                            <span class="error-msg" name="iaisErrorMsg" id="error_frequency"${index}></span>
                        </iais:value>
                    </iais:row>
                </div>
               </c:forEach>
                </div>
                <div id ="medicationDiv">

                    <c:forEach items="${drugMedicationDtos}" var="drugMedicationDto" begin="1" varStatus="idxStatus">
                        <c:set var="index" value="${idxStatus.index}" />
                        <div class="med">
                        <iais:row id="test">
                            <div class="col-sm-6 control-label formtext col-md-8">
                                <div class="cgo-header">
                                    <strong>Medication <label class="assign-psn-item">${index+1}</label></strong>
                                </div>
                            </div>
                            <div class="col-md-4 col-xs-7 text-right">
                                <a class="removeMedications"  onclick="" style="text-decoration:none;" href="javascript:void(0)">
                                    <h4 class="text-danger">
                                        <em class="fa fa-times-circle del-size-36 removeBtn cursorPointer"></em>
                                    </h4>
                                </a>
                            </div>

                        </iais:row>
                        <iais:row>
                            <iais:field width="5" value="Batch No" mandatory="true"/>
                            <iais:value width="7" cssClass="col-md-7">
                                <iais:input maxLength="3" type="text" name="batchNo${index}" value="${drugMedicationDto.batchNo}"/>
                                <span class="error-msg" name="iaisErrorMsg" id="error_batchNo"${index}></span>
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field width="5" value="Strength (pg)" mandatory="true"/>
                            <iais:value width="7" cssClass="col-md-7">
                                <iais:input maxLength="3" type="text" name="strength${index}" value="${drugMedicationDto.strength}"/>
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field width="5" value="Quantity" mandatory="true"/>
                            <iais:value width="7" cssClass="col-md-7">
                                <iais:input maxLength="3" type="text" name="quantity${index}" value="${drugMedicationDto.quantity}"/>
                            </iais:value>
                        </iais:row>
                        <div class="medicationContent">
                            <iais:row>
                                <iais:field width="5" value="Frequency" mandatory="true"/>
                                <iais:value width="7" cssClass="col-md-7">
                                    <iais:select cssClass="frequency"  name="frequency${index}" firstOption="Please Select" codeCategory="DP_FREQUENCY" value="${drugMedicationDto.frequency}"/>
                                </iais:value>
                            </iais:row>
                        </div>
                    </div>
                    </c:forEach>

                </div>
                <iais:row >
                    <iais:value width="5" cssClass="col-md-5" display="true">
                        <a class="addMedication" onclick="addMedications()" style="text-decoration:none;">+ Add Medication</a>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Remarks" mandatory="false"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:input maxLength="500" type="text" name="remarks" value="${drugSubmission.remarks}"/>
                    </iais:value>
                </iais:row>

            </div>
        </div>
    </div>
</div>
