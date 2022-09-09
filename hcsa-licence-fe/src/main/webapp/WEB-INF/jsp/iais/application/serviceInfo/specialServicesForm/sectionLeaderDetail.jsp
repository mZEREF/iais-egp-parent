<div class="personnel-content">
    <input type="hidden" class="indexNo" name="${prepsn}indexNo${index}" value="${sectionLeader.indexNo}"/>
    <iais:row>
        <div class="col-xs-12 col-md-6">
            <p class="bold">${title} <label class="assign-psn-item">${index+1}</label></p>
            <p><span class="error-msg" name="iaisErrorMSg" id="error_${prepsn}personError${index}"></span></p>
        </div>
        <div class="col-xs-12 col-md-6 text-right removeEditDiv <c:if test="${index == 0}">hidden</c:if>">
            <h4 class="text-danger">
                <em class="fa fa-times-circle del-size-36 removeBtns cursorPointer"></em>
            </h4>
        </div>
    </iais:row>

    <iais:row>
        <iais:field width="5" mandatory="true" value="Name" cssClass="col-md-5 control-font-label"/>
        <iais:value width="3" cssClass="col-md-3">
            <iais:select cssClass="salutation" name="${prepsn}salutation${index}" firstOption="Please Select"
                         codeCategory="CATE_ID_SALUTATION" value="${sectionLeader.salutation}" />
        </iais:value>
        <iais:value width="4" cssClass="col-md-4">
            <iais:input cssClass="name" maxLength="66" type="text" name="${prepsn}name${index}" value="${sectionLeader.name}" />
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" mandatory="true" value="Qualification" cssClass="col-md-5 control-font-label"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input cssClass="qualification" maxLength="100" type="text" name="${prepsn}qualification${index}" value="${sectionLeader.qualification}" />
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" mandatory="true" value="Working Experience (in terms of years)" cssClass="col-md-5 control-font-label"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input cssClass="wrkExpYear" maxLength="2" type="text" name="${prepsn}wrkExpYear${index}" value="${sectionLeader.wrkExpYear}" />
        </iais:value>
    </iais:row>
</div>
