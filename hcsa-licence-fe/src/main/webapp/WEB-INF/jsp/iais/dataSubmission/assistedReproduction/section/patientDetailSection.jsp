<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">
            <strong>
                Details of Patient
            </strong>
        </h4>
    </div>
    <div id="patientDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <iais:row>
                    <iais:field width="5" value="Name (as per NRIC/Passport)" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:input maxLength="66" type="text" name="name" id="name" value=""/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="ID No." mandatory="true"/>
                    <iais:value width="3" cssClass="col-md-3">
                        <iais:select name="idType" firstOption="Please Select" codeCategory="CATE_ID_ID_TYPE" value=""
                                     cssClass="idTypeSel"/>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4">
                        <iais:input maxLength="9" type="text" name="idNumber" value="" />
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>