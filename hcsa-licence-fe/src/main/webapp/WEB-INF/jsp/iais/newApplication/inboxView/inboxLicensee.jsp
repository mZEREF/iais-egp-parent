<style>
    span{
        word-wrap: break-word;
    }
</style>
<c:set var="subLicenseeDto" value="${AppSubmissionDto.subLicenseeDto}"/>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">
            <a class="collapsed" data-toggle="collapse" data-parent="#accordion" href="#previewLicensee">
                Licensee Details
            </a>
        </h4>
    </div>
    <div id="previewLicensee" class="panel-collapse collapse <c:if test="${!empty printFlag}">in</c:if>">
        <div class="panel-body">
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

                <%@include file="/WEB-INF/jsp/iais/common/previewLicenseeCom.jsp"%>
            </div>
        </div>
    </div>
</div>