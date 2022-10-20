<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<c:set var="soloType" value="LICT002" />
<c:set var="subLicenseeDto" value="${AppSubmissionDto.subLicenseeDto}"/>
<c:if test="${showHeadingSign}">
    <c:set var="headingSign" value="${empty coMap.licensee ? 'incompleted' : 'completed'}" />
</c:if>
<div class="panel panel-default">
    <div class="panel-heading ${headingSign}">
        <h4 class="panel-title">
            <a class="collapsed" style="text-decoration: none;" data-toggle="collapse"  href="#previewLicensee${documentIndex}">
                Licensee Details
            </a>
        </h4>
    </div>
    <div id="previewLicensee${documentIndex}" class="panel-collapse collapse <c:if test="${!empty printFlag}">in</c:if>">
        <div class="panel-body">
            <c:if test="${(empty AppSubmissionDto.appEditSelectDto || AppSubmissionDto.appEditSelectDto.licenseeEdit)
                    && empty printView && (empty isSingle || isSingle == 'Y')}">
                <p><div class="text-right app-font-size-16"><a href="#" id="subLicenseeEdit"><em class="fa fa-pencil-square-o"></em>Edit</a></div></p>
            </c:if>
            <iais:row>
                <div class="app-title">Licensee Details</div>
            </iais:row>
            <div class="panel-main-content form-horizontal min-row">
                <c:if test="${subLicenseeDto.licenseeType ne soloType}">
                    <iais:row>
                        <iais:field width="5" value="Licensee Type"/>
                        <iais:value width="7" display="true">
                            <iais:code code="${subLicenseeDto.licenseeType}" />
                        </iais:value>
                    </iais:row>
                </c:if>

                <%@include file="licensee/viewLicenseeCom.jsp"%>
            </div>
        </div>
    </div>
</div>