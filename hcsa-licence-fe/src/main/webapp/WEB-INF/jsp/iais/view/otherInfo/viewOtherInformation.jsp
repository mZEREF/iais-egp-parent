<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<div class="amended-service-info-gp">
    <iais:row>
        <label class="app-title">${currStepName}</label>
    </iais:row>
    <div class="amend-preview-info form-horizontal min-row">
        <%@include file="viewOtherInformationTopPerson.jsp"%>
        <%@include file="viewDoucmentation.jsp"%>
        <%@include file="viewAbort.jsp"%>
    </div>
</div>

