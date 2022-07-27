<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<c:if test="${empty printView}">
    <c:choose>
        <c:when test="${!FirstView}">
            <c:set var="headingSign" value="${empty coMap.specialised ? 'incompleted' : 'completed'}" />
        </c:when>
        <c:when test="${needShowErr}">
            <c:set var="headingSign" value="${not empty svcSecMap.specialised ? 'incompleted' : 'completed'}" />
        </c:when>
    </c:choose>
</c:if>
<div class="panel panel-default">
    <div class="panel-heading ${headingSign}">
        <h4 class="panel-title">
            <a class="collapsed" style="text-decoration: none;" data-toggle="collapse"  href="#previewSpecialised${empty documentIndex ? "" : documentIndex}">
                Category/Discipline & Specialised Service/Specified Test
            </a>
        </h4>
    </div>
    <div id="previewLicensee${empty documentIndex ? "" : documentIndex}" class="panel-collapse collapse <c:if test="${!empty printFlag}">in</c:if>">
        <div class="panel-body">
            <c:if test="${(empty AppSubmissionDto.appEditSelectDto || AppSubmissionDto.appEditSelectDto.specialisedEdit)
            && empty printView && (empty isSingle || isSingle == 'Y')}">
                <p><div class="text-right app-font-size-16"><a href="#" id="subLicenseeEdit"><em class="fa fa-pencil-square-o"></em>Edit</a></div></p>
            </c:if>
            <div class="panel-main-content form-horizontal min-row">
                <iais:row>
                    <iais:value width="10">
                        <p class="app-title">Category/Discipline</p>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:value width="10">
                        <p class="app-title">Specialised Service/Specified Test</p>
                    </iais:value>
                </iais:row>

            </div>
        </div>
    </div>
</div>