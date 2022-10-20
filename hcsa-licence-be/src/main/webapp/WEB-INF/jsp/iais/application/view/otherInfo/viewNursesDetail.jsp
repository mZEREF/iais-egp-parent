<div class="person-detail nurses">
    <input type="hidden" name="npsnType" value="nurses">
    <iais:row>
        <iais:field width="5" value="Name of trained nurses"/>
        <iais:value width="3" cssClass="col-md-7" display="true">
            <c:out value="${person.name}" />
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" value="Qualifications"/>
        <iais:value width="3" cssClass="col-md-7" display="true">
            <c:out value="${person.qualification}" />
        </iais:value>
    </iais:row>
</div>