<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<c:set var="isMap" value="${person.psnType == ApplicationConsts.PERSONNEL_PSN_TYPE_MAP}"/>
<div class="person-detail">
    <iais:row>
        <iais:field width="5" value="Salutation"/>
        <iais:value width="3" cssClass="col-md-7" display="true">
            <iais:code code="${person.salutation}" />
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" value="Name"/>
        <iais:value width="3" cssClass="col-md-7" display="true">
            <c:out value="${person.name}" />
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" value="ID Type"/>
        <iais:value width="3" cssClass="col-md-7" display="true">
            <iais:code code="${person.idType}" />
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" value="ID No."/>
        <iais:value width="3" cssClass="col-md-7" display="true">
            <c:out value="${person.idNo}" />
        </iais:value>
    </iais:row>

    <c:if test="${person.idType == 'IDTYPE003'}">
        <iais:row>
            <iais:field width="5" value="Nationality"/>
            <iais:value width="3" cssClass="col-md-7" display="true">
                <iais:code code="${person.nationality}" />
            </iais:value>
        </iais:row>
    </c:if>

    <c:if test="${!isMap}">
        <iais:row>
            <iais:field width="5" value="Designation"/>
            <iais:value width="3" cssClass="col-md-7" display="true">
                <iais:code code="${person.designation}" />
            </iais:value>
        </iais:row>
        <c:if test="${'DES999' == person.designation}">
            <iais:row>
                <iais:field width="5" value=""/>
                <iais:value width="3" cssClass="col-md-7" display="true">
                    <c:out value="${person.otherDesignation}" />
                </iais:value>
            </iais:row>
        </c:if>

        <iais:row>
            <iais:field width="5" value="Professional Board"/>
            <iais:value width="3" cssClass="col-md-7" display="true">
                <iais:code code="${person.professionBoard}" />
            </iais:value>
        </iais:row>

        <iais:row>
            <iais:field width="5" value="Professional Type"/>
            <iais:value width="3" cssClass="col-md-7" display="true">
                <iais:code code="${person.professionType}" />
            </iais:value>
        </iais:row>

        <iais:row>
            <iais:field width="5" value="Professional Regn. No."/>
            <iais:value width="3" cssClass="col-md-7" display="true">
                <c:out value="${person.profRegNo}" />
            </iais:value>
        </iais:row>

        <iais:row>
            <iais:field width="5" value="Type of Current Registration"/>
            <iais:value width="3" cssClass="col-md-7" display="true">
                <c:out value="${person.typeOfCurrRegi}" />
            </iais:value>
        </iais:row>

        <iais:row>
            <iais:field width="5" value="Current Registration Date"/>
            <iais:value width="3" cssClass="col-md-7" display="true">
                <c:out value="${person.currRegiDateStr}" />
            </iais:value>
        </iais:row>

        <iais:row>
            <iais:field width="5" value="Practicing Certificate End Date"/>
            <iais:value width="3" cssClass="col-md-7" display="true">
                <c:out value="${person.praCerEndDateStr}" />
            </iais:value>
        </iais:row>

        <iais:row>
            <iais:field width="5" value="Type of Register"/>
            <iais:value width="3" cssClass="col-md-7" display="true">
                <c:out value="${person.typeOfRegister}" />
            </iais:value>
        </iais:row>

        <iais:row>
            <iais:field width="5" value="Specialty"/>
            <iais:value width="7" cssClass="col-md-7 speciality" display="true">
                <c:out value="${person.speciality}" />
            </iais:value>
        </iais:row>

        <iais:row>
            <iais:field width="5" value="Sub-specialty"/>
            <iais:value width="7" cssClass="col-md-7 subSpeciality" display="true">
                <c:out value="${person.subSpeciality}" />
            </iais:value>
        </iais:row>

        <iais:row>
            <iais:field width="5" value="Other Specialities"/>
            <iais:value width="7" cssClass="col-md-7" display="true">
                <c:out value="${person.specialityOther}" />
            </iais:value>
        </iais:row>

        <iais:row>
            <iais:field width="5" value="Date when specialty was obtained"/>
            <iais:value width="7" cssClass="col-md-7" display="true">
                <c:out value="${person.specialtyGetDateStr}" />
            </iais:value>
        </iais:row>

        <iais:row>
            <iais:field width="5" value="Qualification"/>
            <iais:value width="7" cssClass="col-md-7" display="true">
                <c:out value="${person.qualification}" />
            </iais:value>
        </iais:row>

        <iais:row>
            <iais:field width="5" value="Other Qualification"/>
            <iais:value width="7" cssClass="col-md-7" display="true">
                <c:out value="${person.otherQualification}" />
            </iais:value>
        </iais:row>
    </c:if>

    <iais:row cssClass="${officeTelNo == 'officeTelNo'  ? '' : 'hidden'}">
        <iais:field width="5" value="Office Telephone No."/>
        <iais:value width="7" cssClass="col-md-7">
            <c:out value="${person.officeTelNo}" />
        </iais:value>
    </iais:row>

    <c:if test="${keyPerson != 'keyPerson'}">
        <iais:row>
            <iais:field width="5" value="Mobile No."/>
            <iais:value width="7" cssClass="col-md-7" display="true">
                <c:out value="${person.mobileNo}"/>
            </iais:value>
        </iais:row>

        <iais:row>
            <iais:field width="5" value="Email Address"/>
            <iais:value width="7" cssClass="col-md-7" display="true">
                <c:out value="${person.emailAddr}"/>
            </iais:value>
        </iais:row>
    </c:if>
</div>