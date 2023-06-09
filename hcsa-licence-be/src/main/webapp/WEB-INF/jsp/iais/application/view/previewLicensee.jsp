<c:set var="soloType" value="LICT002" />
<c:set var="subLicenseeDto" value="${AppSubmissionDto.subLicenseeDto}"/>
<c:if test="${showHeadingSign}">
    <c:set var="headingSign" value="${empty coMap.licensee ? 'incompleted' : 'completed'}" />
</c:if>
<div class="panel panel-default">
    <div class="panel-heading ${headingSign}">
        <h4 class="panel-title">
            <a class="collapsed" style="text-decoration: none;" data-toggle="collapse"  href="#previewLicensee${empty documentIndex ? "" : documentIndex}">
                Licensee Details
            </a>
        </h4>
    </div>
    <div id="previewLicensee${empty documentIndex ? "" : documentIndex}" class="panel-collapse collapse <c:if test="${!empty printFlag}">in</c:if>">
        <div class="panel-body">
            <c:if test="${(empty AppSubmissionDto.appEditSelectDto || AppSubmissionDto.appEditSelectDto.licenseeEdit)
            && empty printView && isSingle != 0}">
                <p><div class="text-right app-font-size-16"><a href="#" id="subLicenseeEdit"><em class="fa fa-pencil-square-o"></em>Edit</a></div></p>
            </c:if>
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

                <%@include file="licensee/viewLicenseeCom.jsp"%>
            </div>
        </div>
    </div>
</div>