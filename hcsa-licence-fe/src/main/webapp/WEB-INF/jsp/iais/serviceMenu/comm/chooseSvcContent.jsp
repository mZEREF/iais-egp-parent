<div class="row">
    <div class="col-xs-12 col-md-3">
    </div>
    <div class="col-xs-12 col-md-6">
        <h3>
            Select the service(s) for which you wish to make this licence application
        </h3>
    </div>
</div>
<c:forEach var="errMSg" items="${errList}" >
    <div class="row">
        <div class="col-xs-12 col-md-3">
        </div>
        <div class="col-xs-12 col-md-6">
            <span class="error-msg">${errMSg}</span>
        </div>
    </div>
</c:forEach>
<div class="row">
    <div class="col-xs-12 col-md-3">
    </div>
    <div class="col-xs-12 col-md-6">
        <c:if test="${!empty err}">
            <span class="error-msg">${err}</span>
        </c:if>
    </div>
</div>

<div class="row">
    <div class="col-xs-12 col-md-3">
    </div>
    <div class="col-xs-12 col-md-6">
        <div class="self-assessment-checkbox-gp gradient-light-grey">
            <p class="assessment-title">Base Services</p>
            <div class="form-check-gp">
                <c:forEach var="base" items="${baseService}">
                    <div class="form-check">
                        <input class="form-check-input"
                        <c:if test="${baseServiceChecked != null}">
                        <c:forEach var="checked" items="${baseServiceChecked}">
                               <c:if test="${base.getId().equals(checked)}">checked="checked"</c:if>
                        </c:forEach>
                        </c:if>
                               name="basechk"  type="checkbox" aria-invalid="false"
                               value="${base.getId()}">

                        <label class="form-check-label"><span
                                class="check-square"></span>${base.getSvcName()}</label>
                    </div>
                </c:forEach>
            </div>
            <p class="assessment-title">Specified Services</p>
            <div class="form-check-gp">
                <c:forEach var="specified" items="${specifiedService}">
                    <div class="form-check">
                        <input class="form-check-input"
                        <c:if test="${specifiedServiceChecked != null}">
                        <c:forEach var="specifiedchecked" items="${specifiedServiceChecked}">
                               <c:if test="${specified.getId().equals(specifiedchecked)}">checked="checked"</c:if>
                        </c:forEach>
                        </c:if>
                               name="sepcifiedchk"  type="checkbox"
                               aria-invalid="false" value="${specified.getId()}">
                        <label class="form-check-label" ><span
                                class="check-square"></span>${specified.getSvcName()}</label>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
</div>

