<c:set var="soloType" value="LICT002" />
<c:set var="subLicenseeDto" value="${AppSubmissionDto.subLicenseeDto}"/>

<c:set var="isRFC" value="${'APTY005' == AppSubmissionDto.appType}" />
<c:set var="specialSubLic" value="${subLicenseeDto.licenseeType eq 'LICT002' || subLicenseeDto.licenseeType eq 'LICTSUB002'}" />
<c:set var="showClaimFields" value="${isRFC && specialSubLic}" />

<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">
            <a class="collapsed" data-toggle="collapse" href="#previewLicensee">
                Licensee Details
            </a>
        </h4>
    </div>
    <div id="previewLicensee" class="panel-collapse collapse <c:if test="${!empty printFlag}">in</c:if>">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal min-row">
                <iais:row>
                    <iais:value width="10">
                        <strong class="app-font-size-22 premHeader">Licensee Details</strong>
                    </iais:value>
                </iais:row>
                <c:if test="${subLicenseeDto.licenseeType ne soloType}">
                    <iais:row>
                        <iais:field width="5" value="Licensee Type"/>
                        <iais:value width="7" display="true">
                            <iais:code code="${subLicenseeDto.licenseeType}" />
                        </iais:value>
                    </iais:row>
                </c:if>

                <%@include file="/WEB-INF/jsp/iais/common/previewLicenseeCom.jsp"%>
                <c:if test="${showClaimFields}">
                    <iais:row>
                        <iais:field width="5" value="UEN of your Corporate Entity"/>
                        <iais:value width="7" display="true">
                            <c:out value="${subLicenseeDto.claimUenNo}" />
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Name of your Corporate Entity"/>
                        <iais:value width="7" display="true">
                            <c:out value="${subLicenseeDto.claimCompanyName}" />
                        </iais:value>
                    </iais:row>
                </c:if>
            </div>
        </div>
    </div>
</div>