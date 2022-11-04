<div class="personnel-content">
    <input type="hidden" class="indexNo" name="${prefix}indexNo${index}" value="${sectionLeader.indexNo}"/>
    <input type="hidden" class="isPartEdit" name="${prefix}isPartEdit${index}" value="0"/>
    <iais:row>
        <div class="col-md-12 col-xs-12 edit-content">
            <c:if test="${'true' == canEdit}">
                <input type="hidden" class="isPartEdit" name="${status.index}isPartEdit${index}" value="0"/>
                <div class="text-right app-font-size-16">
                    <a class="edit" href="javascript:void(0);">
                        <em class="fa fa-pencil-square-o"></em><span>&nbsp;</span>Edit
                    </a>
                </div>
            </c:if>
        </div>
    </iais:row>
    <iais:row>
        <div class="col-xs-12 col-md-6">
            <p class="bold">${title} <label class="assign-psn-item"><strong>${index+1}</strong></label></p>
            <p><span class="error-msg" name="iaisErrorMSg" id="error_${prefix}personError${index}"></span></p>
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
            <iais:select cssClass="salutation" name="${prefix}salutation${index}" firstOption="Please Select"
                         codeCategory="CATE_ID_SALUTATION" value="${sectionLeader.salutation}" />
        </iais:value>
        <iais:value width="4" cssClass="col-md-4">
            <iais:input cssClass="name" maxLength="66" type="text" name="${prefix}name${index}" value="${sectionLeader.name}" />
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" mandatory="true" value="Qualification" cssClass="col-md-5 control-font-label"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input cssClass="qualification" maxLength="100" type="text" name="${prefix}qualification${index}" value="${sectionLeader.qualification}" />
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" mandatory="true" value="Working Experience (in terms of years)" cssClass="col-md-5 control-font-label"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input cssClass="wrkExpYear" maxLength="2" type="text" name="${prefix}wrkExpYear${index}" value="${sectionLeader.wrkExpYear}" />
        </iais:value>
    </iais:row>
</div>
