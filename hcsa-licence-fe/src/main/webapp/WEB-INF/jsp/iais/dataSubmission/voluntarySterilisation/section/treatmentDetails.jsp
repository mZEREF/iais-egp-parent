<div class="form-horizontal licenseeContent">
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
    <iais:row>
        <iais:field width="10" cssClass="col-md-5" value="Name (as per NRIC/Passport)"/>
        <iais:value width="7">
            <c:out value="231242123" />
        </iais:value>
    </iais:row>
</div>