<div class="person-detail">
    <iais:row>
        <iais:field width="5" value="Name"/>
        <iais:value width="7" cssClass="col-md-7" display="true">
            <c:out value="${appSvcPersonnelDto.name}"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Qualification"/>
        <iais:value width="7" cssClass="col-md-7" display="true">
            <c:out value="${appSvcPersonnelDto.qualification}"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Relevant working experience(Years)"/>
        <iais:value width="7" cssClass="col-md-7" display="true">
            <c:out value="${appSvcPersonnelDto.wrkExpYear}"/>
        </iais:value>
    </iais:row>

</div>


