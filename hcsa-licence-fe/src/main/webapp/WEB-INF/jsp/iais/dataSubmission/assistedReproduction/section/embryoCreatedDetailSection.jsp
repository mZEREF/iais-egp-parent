<script type="text/javascript" src="<%=webroot1%>js/dataSubmission/ecSection.js"></script>

<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">
            <strong>
                Embryo Created
            </strong>
        </h4>
    </div>
    <div id="efoDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">

                <iais:row>
                    <iais:field width="6" value="No. Transferrable embryos created from fresh oocyte(s)" mandatory="true"/>
                    <iais:value width="6" cssClass="col-md-6" label="true">
                        <input type="text" maxlength="2" id="transEmbrFreshOccNum" name="transEmbrFreshOccNum" onkeypress="keyNumericPress()" value="${arSuperDataSubmissionDto.embryoCreatedStageDto.transEmbrFreshOccNum}"/>
                        <span class="error-msg" name="iaisErrorMsg" id="error_transEmbrFreshOccNum"></span>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="No. of Poor Quality / Unhealthy / Abnormally / Developed created from fresh oocyte(s)" mandatory="true"/>
                    <iais:value width="6" cssClass="col-md-6" label="true">
                        <input type="text" maxlength="2" id="poorDevFreshOccNum" name="poorDevFreshOccNum" onkeypress="keyNumericPress()" value="${arSuperDataSubmissionDto.embryoCreatedStageDto.poorDevFreshOccNum}"/>
                        <span class="error-msg" name="iaisErrorMsg" id="error_poorDevFreshOccNum"></span>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="No. Transferrable embryos created from thawed oocyte(s)" mandatory="true"/>
                    <iais:value width="6" cssClass="col-md-6" label="true">
                        <input type="text" maxlength="2" id="transEmbrThawOccNum" name="transEmbrThawOccNum" onkeypress="keyNumericPress()" value="${arSuperDataSubmissionDto.embryoCreatedStageDto.transEmbrThawOccNum}"/>
                        <span class="error-msg" name="iaisErrorMsg" id="error_transEmbrThawOccNum"></span>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="No. of Poor Quality / Unhealthy / Abnormally / Developed created from thawed oocyte(s)" mandatory="true"/>
                    <iais:value width="6" cssClass="col-md-6" label="true">
                        <input type="text" maxlength="2" id="poorDevThawOccNum" name="poorDevThawOccNum" onkeypress="keyNumericPress()" value="${arSuperDataSubmissionDto.embryoCreatedStageDto.poorDevThawOccNum}"/>
                        <span class="error-msg" name="iaisErrorMsg" id="error_poorDevThawOccNum"></span>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="Total No. Created" mandatory="false"/>
                    <iais:value width="6" cssClass="col-md-6" label="true">
                        <label id="totalNum" name="totalNum">${arSuperDataSubmissionDto.embryoCreatedStageDto.totalNum}</label>
                    </iais:value>
                </iais:row>

            </div>
        </div>
    </div>
</div>
