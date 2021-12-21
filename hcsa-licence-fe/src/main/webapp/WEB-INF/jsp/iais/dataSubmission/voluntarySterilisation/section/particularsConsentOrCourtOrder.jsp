<iais:row>
    <iais:value width="6" cssClass="col-md-6">
        <%--<strong class="app-font-size-22 premHeader">title</strong>--%>
    </iais:value>
    <iais:value width="6" cssClass="col-md-6 text-right editDiv">
        <c:if test="${canEdit}">
            <input id="isEditHiddenVal" type="hidden" name="isEdit" value="0"/>
            <a id="edit" class="text-right app-font-size-16">
                <em class="fa fa-pencil-square-o">&nbsp;</em> Edit
            </a>
        </c:if>
    </iais:value>
</iais:row>
<div>
        <%@include file="particulars.jsp" %>

</div>
<div>
    <iais:row>
        <iais:field width="5" value="Name Of Person" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="66" type="text" name="appliedPartName" value="" />
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="ID No." mandatory="true"/>
        <iais:value width="3" cssClass="col-md-3">
            <iais:select name="appliedPartIdType" firstOption="Please Select" codeCategory="CATE_ID_DS_ID_TYPE"
                         value="" />
        </iais:value>
        <iais:value width="4" cssClass="col-md-4">
            <iais:input maxLength="15" type="text" name="appliedPartIdNo" value="" />
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Date of Birth" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:datePicker id="birthDate" name="appliedPartBirthday" value=""/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Relationship to Person Who Was Sterilized" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:select name="appliedPartRelationship" firstOption="Please Select" codeCategory=""
                         value=""/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Date Court Order Issued" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:datePicker id="" name="courtOrderIssueDate" value=""/>
        </iais:value>
    </iais:row>
</div>