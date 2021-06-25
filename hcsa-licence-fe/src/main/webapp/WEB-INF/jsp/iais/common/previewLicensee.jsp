<style>
    span{
        word-wrap: break-word;
    }
</style>
<c:set var="subLicenseeDto" value="${AppSubmissionDto.subLicenseeDto}"/>
<c:if test="${empty printView}">
    <c:choose>
        <c:when test="${!FirstView}">
            <c:set var="headingSign" value="${empty coMap.licensee ? 'incompleted' : 'completed'}" />
        </c:when>
        <c:when test="${needShowErr}">
            <c:set var="headingSign" value="${not empty svcSecMap.licensee ? 'incompleted' : 'completed'}" />
        </c:when>
    </c:choose>
</c:if>
<div class="panel panel-default">
    <div class="panel-heading ${headingSign}">
        <h4 class="panel-title">
            <a class="collapsed" data-toggle="collapse" data-parent="#accordion" href="#previewLicensee">
                Licensee Details
            </a>
        </h4>
    </div>
    <div id="previewLicensee" class="panel-collapse collapse <c:if test="${!empty printFlag}">in</c:if>">
        <div class="panel-body">
            <c:if test="${(AppSubmissionDto.appEditSelectDto==null || AppSubmissionDto.appEditSelectDto.licenseeEdit) && empty
            printView}">
                <p><div class="text-right app-font-size-16"><a href="#" id="subLicenseeEdit"><em class="fa fa-pencil-square-o"></em>Edit</a></div></p>
            </c:if>
            <div class="panel-main-content form-horizontal">
                <iais:row>
                    <iais:value width="10">
                        <strong class="app-font-size-22 premHeader">Licensee Details</strong>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Licensee Type"/>
                    <iais:value width="7">
                        <iais:code code="${subLicenseeDto.licenseeType}" />
                    </iais:value>
                </iais:row>

                <iais:row cssClass="company-no ${subLicenseeDto.licenseeType == 'LICTSUB001' ? '' : 'hidden'}">
                    <iais:field width="5" value="UEN No."/>
                    <iais:value width="7">
                        <iais:code code="${subLicenseeDto.uenNo}" />
                    </iais:value>
                </iais:row>

                <iais:row cssClass="ind-no ${subLicenseeDto.licenseeType == 'LICTSUB002' ? '' : 'hidden'}">
                    <iais:field width="5" value="ID Type"/>
                    <iais:value width="7">
                        <iais:code code="${subLicenseeDto.idType}" />
                    </iais:value>
                </iais:row>
                <iais:row cssClass="ind-no ${subLicenseeDto.licenseeType == 'LICTSUB002' ? '' : 'hidden'}">
                    <iais:field width="5" value="ID No."/>
                    <iais:value width="7">
                        <c:out value="${subLicenseeDto.idNumber}" />
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field value="Licensee Name" width="5"/>
                    <iais:value width="7">
                        <c:out value="${subLicenseeDto.licenseeName}" />
                    </iais:value>
                </iais:row>

                <%-- Address start --%>
                <iais:row cssClass="postalCodeDiv">
                    <iais:field value="Postal Code " width="5"/>
                    <iais:value width="7">
                        <c:out value="${subLicenseeDto.postalCode}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field value="Address Type" width="5"/>
                    <iais:value width="7">
                        <iais:code code="${subLicenseeDto.addrType}" />
                    </iais:value>
                </iais:row>
                <iais:row cssClass="address">
                    <iais:field value="Block / House No." width="5"/>
                    <iais:value width="7">
                        <c:out value="${subLicenseeDto.blkNo}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field value="Floor / Unit No." width="5"/>
                    <iais:value width="7">
                        <c:out value="${subLicenseeDto.floorNo}" /> - <c:out value="${subLicenseeDto.unitNo}" />
                    </iais:value>
                </iais:row>
                <iais:row cssClass="address">
                    <iais:field value="Street Name" width="5"/>
                    <iais:value width="7">
                        <c:out value="${subLicenseeDto.streetName}" />
                    </iais:value>
                </iais:row>

                <iais:row cssClass="address">
                    <iais:field value="Building Name" width="5"/>
                    <iais:value width="7">
                        <c:out value="${subLicenseeDto.buildingName}" />
                    </iais:value>
                </iais:row>
                <%-- Address end --%>

                <iais:row>
                    <iais:field value="Telephone No." width="5"/>
                    <iais:value width="7">
                        <c:out value="${subLicenseeDto.telephoneNo}" />
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field value="Email Address" width="5"/>
                    <iais:value width="7">
                        <c:out value="${subLicenseeDto.emailAddr}" />
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>