<div class="person-detail nurses">
    <input type="hidden" name="npsnType" value="nurses">
    <iais:row>
        <iais:field width="6" cssClass="col-md-6" value="Name of trained nurses"/>
        <iais:value width="6" cssClass="col-md-6" display="true">
            <c:out value="${person.name}" />
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="6" cssClass="col-md-6" value="Qualifications"/>
        <iais:value width="6" cssClass="col-md-6" display="true">
            <c:out value="${person.qualification}" />
        </iais:value>
    </iais:row>
</div>