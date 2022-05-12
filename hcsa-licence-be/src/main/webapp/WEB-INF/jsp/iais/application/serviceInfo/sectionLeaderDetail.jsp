<div class="form-horizontal sectionLaderContent">
    <input type="hidden" class="indexNo" name="indexNo${index}" value="${sectionLeader.indexNo}"/>
    <iais:row>
        <iais:value width="6" cssClass="col-md-6">
            <strong>
                <c:out value="${singleName}"/>
                <label class="assign-psn-item"><c:if test="${pageLength > 1}">${index+1}</c:if></label>
            </strong>
        </iais:value>
        <iais:value width="6" cssClass="col-md-6 text-right removeSectionLeaderDiv">
            <c:if test="${index > 0}">
                <h4 class="text-danger">
                    <em class="fa fa-times-circle del-size-36 removeBtn cursorPointer"></em>
                </h4>
            </c:if>
        </iais:value>
    </iais:row>

    <iais:row>
        <div class="col-md-12 col-xs-12 edit-content">
            <c:if test="${'true' == canEdit}">
                <input type="hidden" class="isPartEdit" name="isPartEdit${index}" value="0"/>
                <div class="text-right app-font-size-16">
                    <a class="edit" href="javascript:void(0);">
                        <em class="fa fa-pencil-square-o"></em><span>&nbsp;</span>Edit
                    </a>
                </div>
            </c:if>
        </div>
    </iais:row>

    <iais:row>
        <iais:field width="5" mandatory="true" value="Name" cssClass="col-md-5 control-font-label"/>
        <iais:value width="3" cssClass="col-md-3">
            <iais:select cssClass="salutation" name="salutation${index}" firstOption="Please Select"
                         codeCategory="CATE_ID_SALUTATION" value="${sectionLeader.salutation}" />
        </iais:value>
        <iais:value width="4" cssClass="col-md-4">
            <iais:input cssClass="name" maxLength="66" type="text" name="name${index}" value="${sectionLeader.name}" />
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" mandatory="true" value="Qualification" cssClass="col-md-5 control-font-label"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input cssClass="qualification" maxLength="100" type="text" name="qualification${index}" value="${sectionLeader.qualification}" />
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" mandatory="true" value="Working Experience (in terms of years)" cssClass="col-md-5 control-font-label"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input cssClass="wrkExpYear" maxLength="2" type="text" name="wrkExpYear${index}" value="${sectionLeader.wrkExpYear}" />
        </iais:value>
    </iais:row>

</div>
