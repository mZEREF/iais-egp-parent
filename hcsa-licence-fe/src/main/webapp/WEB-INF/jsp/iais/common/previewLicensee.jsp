<style>
    p, span{
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
            <c:if test="${(empty AppSubmissionDto.appEditSelectDto || AppSubmissionDto.appEditSelectDto.licenseeEdit) && empty
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
                    <iais:value width="7" display="true">
                        <iais:code code="${subLicenseeDto.licenseeType}" />
                    </iais:value>
                </iais:row>

                <%@include file="previewLicenseeCom.jsp"%>
            </div>
        </div>
    </div>
</div>