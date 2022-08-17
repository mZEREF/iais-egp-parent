<div class="personnel-content">
    <iais:row>
        <iais:field width="5" mandatory="true" value="Name" cssClass="col-sm-5"/>
        <iais:value width="7" cssClass="col-sm-5 col-md-7">
            <c:out value="${appSvcPersonnelDto.name}"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" mandatory="true" value="Qualification" cssClass="col-sm-5"/>
        <iais:value width="7" cssClass="col-sm-5 col-md-7">
            <c:out value="${appSvcPersonnelDto.qualification}"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" mandatory="true" value="Relevant working experience(Years)"/>
        <iais:value width="7" cssClass="col-md-7">
            <c:out value="${appSvcPersonnelDto.wrkExpYear}"/>
        </iais:value>
    </iais:row>

</div>


