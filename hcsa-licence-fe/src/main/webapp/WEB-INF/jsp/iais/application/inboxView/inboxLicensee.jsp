<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<c:set var="soloType" value="LICT002" />
<c:set var="subLicenseeDto" value="${AppSubmissionDto.subLicenseeDto}"/>

<c:set var="isRFC" value="${'APTY005' == AppSubmissionDto.appType}" />
<c:set var="specialSubLic" value="${subLicenseeDto.licenseeType eq 'LICT002' || subLicenseeDto.licenseeType eq 'LICTSUB002'}" />

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
                        <p class="app-title">Licensee Details</p>
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

                <%@include file="/WEB-INF/jsp/iais/view/licensee/viewLicenseeCom.jsp"%>
            </div>
        </div>
    </div>
</div>