<%@taglib prefix="common" tagdir="/WEB-INF/tags/common" %>
<%@taglib prefix="iais" uri="http://www.ecq.com/iais" %>

<%--@elvariable id="processType" type="java.lang.String"--%>
<common:dashboard>
    <jsp:attribute name="titleFrag">
        <h1>Application for Approval</h1>
          <c:if test="${processType ne null}">
              <p>You are applying for <strong><iais:code code="${processType}"/></strong></p>
          </c:if>
    </jsp:attribute>
</common:dashboard>