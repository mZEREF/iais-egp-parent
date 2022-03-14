<div class="row">
    <div class="col-xs-12 col-md-10">
        <h3>
            Select the service(s) for which you wish to make this licence application
        </h3>
    </div>
</div>
<div class="row">
    <div class="col-xs-12 col-md-7">
        <c:if test="${!empty err}">
            <span class="error-msg">${err}</span>
        </c:if>
    </div>
</div>
<div class="row">
    <div class="col-xs-12 col-md-7">
        <div class="self-assessment-checkbox-gp gradient-light-grey">
            <c:if test="${not empty specifiedService}">
            <div class="form-check-gp" style="width: 50%;float: right">
                <p class="assessment-title"><iais:code code="CDN004"/></p>
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
            </c:if>
            <c:if test="${not empty baseService}">
            <div class="form-check-gp" style="width: 50%;">
            <p class="assessment-title"><iais:code code="CDN002"/></p>
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
            </c:if>
            <div class="text-right text-center-mobile">
                <a class="btn btn-primary next" id="submitService" href="javascript:void(0);">Continue</a>
            </div>
        </div>
    </div>
</div>

