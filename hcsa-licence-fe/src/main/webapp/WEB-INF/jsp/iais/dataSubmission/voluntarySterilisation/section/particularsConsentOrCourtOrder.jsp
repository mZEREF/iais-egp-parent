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
    <%@include file="particulars.jsp" %>
    <iais:row>
        <iais:field width="5" value="Date Court Order Issued" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:datePicker id="" name="" value=""/>
        </iais:value>
    </iais:row>
</div>