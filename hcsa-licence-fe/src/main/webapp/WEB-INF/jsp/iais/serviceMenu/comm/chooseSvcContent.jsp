<div class="row">
    <div class="col-xs-12 col-md-3">
    </div>
    <div class="col-xs-12 col-md-6">
        <h3>
            Select the service(s) for which you wish to make this licence application
        </h3>
    </div>
</div>
<c:forEach var="errMSg" items="${errList}" varStatus="stat" >
    <div class="row">
        <div class="col-xs-12 col-md-3">
        </div>
        <div class="col-xs-12 col-md-6">
            <span class="error-msg">${errMSg}</span>
        </div>
    </div>
    <c:if test="${!stat.last}">
        <br/>
    </c:if>
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
            <c:if test="${not empty baseService}">
            <p class="assessment-title"><iais:code code="CDN002"/></p>
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
            </c:if>

            <c:if test="${not empty specifiedService}">
            <p class="assessment-title"><iais:code code="CDN004"/>
                <a class="btn-tooltip styleguide-tooltip" href="javascript:void(0);" data-toggle="tooltip" data-html="true" title="&lt;p&gt;<iais:message  key="NEW_ACK007"></iais:message>&lt;/p&gt;">i</a>
            </p>
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
            </c:if>
        </div>
    </div>
</div>

