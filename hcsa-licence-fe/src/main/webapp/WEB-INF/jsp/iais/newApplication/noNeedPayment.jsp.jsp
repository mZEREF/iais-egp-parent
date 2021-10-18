
<div class="col-xs-12">
  <div class="col-xs-3">
    <c:choose>
      <c:when test="${'APTY004' == AppSubmissionDto.appType}">
        <span name="iaisErrorMsg" id="error_payMethod" class="error-msg"></span>
      </c:when>
      <c:otherwise>
        <span name="iaisErrorMsg" id="error_pay" class="error-msg"></span>
      </c:otherwise>
    </c:choose>
  </div>
</div>

<div class="row">
  <%--    <c:choose>--%>
  <%--        <c:when test="${'APTY004' == AppSubmissionDto.appType}">--%>
  <%--            <div class="col-xs-12 col-sm-6" style="margin-top: 17px;">--%>
  <%--                <a id="BACK" class="back"><em class="fa fa-angle-left"></em> Back</a>--%>
  <%--            </div>--%>
  <%--        </c:when>--%>
  <%--        <c:otherwise>--%>
  <%--            <div class="col-xs-12 col-sm-6">--%>
  <%--            </div>--%>
  <%--        </c:otherwise>--%>
  <%--    </c:choose>--%>
    <div class="col-xs-12 col-md-4">
      <a id="BACK" class="back"><em class="fa fa-angle-left"></em> Back</a>
    </div>
  <div class="col-xs-12 col-md-8">
    <p class="text-right text-center-mobile"><iais:input type="button" id="proceed" cssClass="proceed btn btn-primary" value="Pay"></iais:input></p>
  </div>
</div>
