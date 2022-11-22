<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>

<div class="amended-service-info-gp">
    <label class="title-font-size">${currStepName}</label>
    <div class="amend-preview-info">
        <div class="form-check-gp">
            <jsp:include page="viewSvcAr.jsp"></jsp:include>
            <jsp:include page="viewSvcNurse.jsp"></jsp:include>
            <jsp:include page="viewSvcEm.jsp"></jsp:include>
<%--            <jsp:include page="viewSvcOthers.jsp"></jsp:include>--%>
            <jsp:include page="viewSvcSpecial.jsp"></jsp:include>
            </div>
        </div>
    </div>