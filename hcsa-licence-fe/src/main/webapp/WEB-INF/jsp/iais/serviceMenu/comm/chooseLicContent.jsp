<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<style>
    .align-lic-table{
        margin-left: -30px;
    }
</style>
<div class="row">
    <div class="col-xs-12 col-md-3">
    </div>
    <div class="col-xs-12 col-md-6">
        <h3>
            You may choose to align the expiry date of your new licence(s) to any of your existing licences.
        </h3>
        <p><span>If you don't select a licence, MOH will assign an expiry date</span></p>
    </div>
</div>
<div class="row">
    <div class="col-xs-12 col-md-3"></div>
    <div class="col-xs-12 col-md-6">
        <table class="table">
            <thead>
                <tr style="font-size: 16px;">
                    <td><strong>Licence No.</strong></td>
                    <td><strong>Type</strong></td>
                    <td><strong>Premises</strong></td>
                    <td style="width:17%;"><strong>Expires on</strong></td>
                </tr>
            </thead>



            <%--<c:forEach items="${appAlignLic}" var="licInfo" varStatus="status">--%>

            <%--</c:forEach>--%>
            <c:forEach var="lic" items="${licAlignSearchResult.rows}" varStatus="status">
                <c:choose>
                    <c:when test="${'first' == lic.svcName}">
                        <tr>
                            <td colspan="4">
                                <div class="form-check">
                                    <input class="form-check-input" type="radio" name="alignLic" value="-1" aria-invalid="false"
                                    <c:choose>
                                        <c:when test="${chooseFirst && status.first}">
                                           checked="checked"
                                        </c:when>
                                        <c:when test="${!chooseFirst && appSelectSvc.alignLicPremId == lic.premisesId}">
                                           checked="checked"
                                        </c:when>
                                    </c:choose> >
                                    <label class="form-check-label"><span class="check-circle"></span> I do not wish to align to any existing licence</label>
                                </div>
                            </td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td>
                                <input type="hidden" name="premisesId${status.index}" value="<iais:mask name="premisesId${status.index}" value="${lic.premisesId}"/>" />
                                <input type="hidden" name="licenceNo${status.index}" value="<iais:mask name="licenceNo${status.index}" value="${lic.licenceNo}"/>" />
                                <div class="form-check">
                                    <input class="form-check-input" type="radio" name="alignLic" value="${status.index}" aria-invalid="false"
                                    <c:choose>
                                        <c:when test="${chooseFirst && status.first}">
                                               checked="checked"
                                        </c:when>
                                        <c:when test="${!chooseFirst && appSelectSvc.alignLicPremId == lic.premisesId}">
                                               checked="checked"
                                        </c:when>
                                    </c:choose> >
                                    <label class="form-check-label"><span class="check-circle"></span>${lic.licenceNo}</label>
                                </div>
                            </td>
                            <td>
                                <div class="form-check align-lic-table">
                                    <label class="form-check-label">${lic.svcName}</label>
                                </div>
                            </td>
                            <td>
                                <div class="form-check align-lic-table">
                                    <label class="form-check-label">${lic.address}</label>
                                </div>
                            </td>
                            <td>
                                <div class="form-check align-lic-table">
                                    <label class="form-check-label"><fmt:formatDate pattern="dd/MM/yyyy" value="${lic.licExpiryDate}"></fmt:formatDate></label>
                                </div>
                            </td>
                        </tr>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </table>
    </div>
</div>
<div class="row">
    <div class="col-xs-12 col-md-3">

    </div>
    <div class="col-xs-6 col-md-6">
        <iais:pagination param="licAlignSearchParam" result="licAlignSearchResult"/>
    </div>
</div>