<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">
            <strong>
                Patient Details
            </strong>
        </h4>
    </div>
    <div id="patientDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <iais:row>
                    <iais:field width="5" value="ID No." mandatory="true"/>
                    <iais:value width="3" cssClass="col-md-3">
                        <iais:select name="preIdType" firstOption="Please Select" codeCategory="CATE_ID_DS_ID_TYPE"
                                     value="" cssClass="idTypeSel"/>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4">
                        <iais:input maxLength="20" type="text" name="preIdNumber" value="" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Nationality" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-4">
                        <iais:select name="preNationality" firstOption="Please Select" codeCategory="CATE_ID_NATIONALITY"
                                     value="" cssClass="preNationalitySel"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Name of Patient" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-4">
                        <iais:input maxLength="66" type="text" name="patientName" value="" />
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>
