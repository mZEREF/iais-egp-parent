<div class="person-detail counsellors">
    <input type="hidden" name="cpsnType" value="counsellors">
    <iais:row>
        <iais:field width="5" value="Name of certified TOP counsellors (Only Doctor/Nurse)"/>
        <iais:value width="3" cssClass="col-md-7" display="true">
            <c:out value="${person.name}" />
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" value="NRIC/FIN No."/>
        <iais:value width="3" cssClass="col-md-7" display="true">
            <c:out value="${person.idNo}" />
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" value="Qualifications"/>
        <iais:value width="3" cssClass="col-md-7" display="true">
            <c:out value="${person.qualification}" />
        </iais:value>
    </iais:row>
</div>