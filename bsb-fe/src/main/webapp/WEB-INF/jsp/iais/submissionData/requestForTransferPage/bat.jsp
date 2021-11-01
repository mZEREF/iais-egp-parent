<div class="panel panel-default">
    <div class="panel-heading"><strong>Biological Agent/Toxin</strong></div>
    <div class="row form-horizontal">
        <div class="col-xs-12 col-sm-12" style="padding: 20px 30px 10px 30px; border-radius: 15px;margin: 0 auto">
            <div class = "col-xs-12 col-sm-12">
                <div class="panel-main-content form-horizontal min-row">
                    <div class="form-group">
                        <div class="col-sm-5 control-label">
                            <label for="scheduleType">scheduleType</label>
                            <span class="mandatory otherQualificationSpan">*</span>
                        </div>
                        <div class="col-sm-6 col-md-7">
                            <iais:select name="scheduleType" id="scheduleType"
                                         value=""
                                         codeCategory="CATE_ID_BSB_SCH_TYPE"
                                         firstOption="Please Select"/>
                            <span class="error-msg" name="errorMsg" id="error_scheduleType"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-5 control-label">
                            <label for="BATCode">Biological Agent/Toxin Code</label>
                            <span class="mandatory otherQualificationSpan">*</span>
                        </div>
                        <div class="col-sm-6 col-md-7">
                            <input type="text" name="BATCode" id="BATCode" maxlength="20" value="">
                            <span class="error-msg" name="errorMsg" id="error_BATCode"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-5 control-label">
                            <label for="expectedBatQty">Expected Quantity of Biological Agent</label>
                            <span class="mandatory otherQualificationSpan">*</span>
                        </div>
                        <div class="col-sm-6 col-md-7">
                            <input type="text" name="expectedBatQty" id="expectedBatQty"
                                   maxlength="66" value="">
                            <span class="error-msg" name="errorMsg" id="error_expectedBatQty"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-5 control-label">
                            <label for="receivedQty">Expected Quantity to Receive</label>
                            <span class="mandatory otherQualificationSpan">*</span>
                        </div>
                        <div class="col-sm-6 col-md-7">
                            <input type="number" name="receivedQty" id="receivedQty" value=""
                                   maxlength="11"
                                   οninput="this.value=this.value.replace(/\D*(\d*)(\.?)(\d{0,3})\d*/,'$1$2$3')">
                            <span class="error-msg" name="errorMsg" id="error_receivedQty"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-5 control-label">
                            <label for="measurementUnit">Unit of Measurement</label>
                            <span class="mandatory otherQualificationSpan">*</span>
                        </div>
                        <div class="col-sm-6 col-md-7">
                            <iais:select name="measurementUnit" id="measurementUnit"
                                         value=""
                                         codeCategory="CATE_ID_BSB_DATA_SUBMISSION_UNIT_OF_MEASUREMENT"
                                         firstOption="Please Select"/>
                            <span class="error-msg" name="errorMsg" id="error_measurementUnit"></span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
