<div class="form-horizontal sectionLaderContent">
    <input type="hidden" class="slIndexNo" name="slIndexNo${index}" value="${sectionLeader.cgoIndexNo}"/>
    <iais:row>
        <iais:value width="6">
            <strong>
                ${stepName}
                <label class="assign-psn-item"><c:if test="${index > 0}">${index+1}</c:if></label>
            </strong>
        </iais:value>
        <iais:value width="6" cssClass="text-right editDiv">
            <c:if test="${canEdit}">
                <input type="hidden" class="isPartEdit" name="isPartEdit${cdSuffix}" value="1"/>
                <a class="text-right app-font-size-16">
                    <em class="fa fa-pencil-square-o">&nbsp;</em> Edit
                </a>
            </c:if>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" mandatory="true" value="Name" cssClass="control-font-label"/>
        <iais:value width="3">
            <iais:select cssClass="salutation" name="salutation${index}" firstOption="Please Select"
                         codeCategory="CATE_ID_SALUTATION" value="${sectionLeader.salutation}" />
        </iais:value>
        <iais:value width="3">
            <iais:input cssClass="name" maxLength="66" type="text" name="name${index}" value="${sectionLeader.name}" />
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" mandatory="true" value="Qualification" cssClass="control-font-label"/>
        <iais:value width="7">
            <iais:input cssClass="qualification" maxLength="100" type="text" name="qualification${index}" value="${sectionLeader.qualification}" />
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" mandatory="true" value="Working Experience (in terms of years)" cssClass="control-font-label"/>
        <iais:value width="7">
            <iais:input cssClass="wrkExpYear" maxLength="2" type="text" name="wrkExpYear${index}" value="${sectionLeader.wrkExpYear}" />
        </iais:value>
    </iais:row>

</div>
