<script type="text/javascript" src="<%=webroot1%>js/dataSubmission/ecSection.js"></script>

<div class="panel panel-default">
    <div class="panel-heading" style="padding-left: 90px;">
        <h4 class="panel-title">
            <strong>
                Embryo Created
            </strong>
        </h4>
    </div>
    <div id="embryoCreatedDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <h3>
                    <label ><c:out value="${arSuperDataSubmissionDto.patientInfoDto.patient.name}"/></label>
                    <span style="font-weight:normal"><c:out value="(${arSuperDataSubmissionDto.patientInfoDto.patient.idNumber})"/>
                    </span>
                </h3>
                <iais:row>
                    <iais:field width="6" value="No. Transferrable embryos created from fresh oocyte(s)" mandatory="true"/>
                    <iais:value width="6" cssClass="col-md-6" >
                        <input type="number" oninput="if(value.length>2)value=value.slice(0,2)" style="margin-bottom: 0px;" id="transEmbrFreshOccNum" name="transEmbrFreshOccNum"  value="${arSuperDataSubmissionDto.embryoCreatedStageDto.transEmbrFreshOccNum}"/>
                        <span class="error-msg" name="iaisErrorMsg" id="error_transEmbrFreshOccNum"></span>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="No. of Poor Quality / Unhealthy / Abnormally / Developed created from fresh oocyte(s)" mandatory="true"/>
                    <iais:value width="6" cssClass="col-md-6" >
                        <input type="number" oninput="if(value.length>2)value=value.slice(0,2)" style="margin-bottom: 0px;" id="poorDevFreshOccNum" name="poorDevFreshOccNum"  value="${arSuperDataSubmissionDto.embryoCreatedStageDto.poorDevFreshOccNum}"/>
                        <span class="error-msg" name="iaisErrorMsg" id="error_poorDevFreshOccNum"></span>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="No. Transferrable embryos created from thawed oocyte(s)" mandatory="true"/>
                    <iais:value width="6" cssClass="col-md-6" >
                        <input type="number" oninput="if(value.length>2)value=value.slice(0,2)" style="margin-bottom: 0px;" id="transEmbrThawOccNum" name="transEmbrThawOccNum"  value="${arSuperDataSubmissionDto.embryoCreatedStageDto.transEmbrThawOccNum}"/>
                        <span class="error-msg" name="iaisErrorMsg" id="error_transEmbrThawOccNum"></span>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="No. of Poor Quality / Unhealthy / Abnormally / Developed created from thawed oocyte(s)" mandatory="true"/>
                    <iais:value width="6" cssClass="col-md-6" >
                        <input type="number" oninput="if(value.length>2)value=value.slice(0,2)" style="margin-bottom: 0px;" id="poorDevThawOccNum" name="poorDevThawOccNum"  value="${arSuperDataSubmissionDto.embryoCreatedStageDto.poorDevThawOccNum}"/>
                        <span class="error-msg" name="iaisErrorMsg" id="error_poorDevThawOccNum"></span>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="Total No. Created" mandatory="false"/>
                    <iais:value width="6" display="true">
                        <div id="totalNum" name="totalNum">${arSuperDataSubmissionDto.embryoCreatedStageDto.totalNum}</div>
                    </iais:value>
                </iais:row>

            </div>
        </div>
    </div>
</div>
