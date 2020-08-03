<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="C" uri="http://java.sun.com/jsp/jstl/core" %>
<webui:setLayout name="iais-intranet"/>
<%@ page contentType="text/html; charset=UTF-8" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<style>
    .col-md-10 {
        width: 100%;
    }

    .btn.btn-primary {
        font-size: 12px;
    }

</style>
<div class="main-content">
    <form  method="post" id="mainForm" enctype="multipart/form-data"  action=<%=process.runtime.continueURL()%>>
        <span id="error_fileUploadError" name="iaisErrorMsg" class="error-msg"></span>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <br><br>
        <div class="tab-pane active" id="tabInbox" role="tabpanel">
            <c:choose>
                <c:when test="${empty ERR_CONTENT}">
                </c:when>
                <c:otherwise>
                    <table class="table">
                        <thead>
                        <tr>
                            <th>S/N</th>
                            <th>Code Value</th>
                            <th>Status</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="errCodeResultListMap" items="${ERR_RESULT_LIST_MAP}" varStatus="status">
                            <c:forEach var="errCodeResultMap" items="${errCodeResultListMap}">
                                <tr>
                                    <td>${status.index + 1}</td>
                                    <td>${errCodeResultMap.key}</td>
                                    <td>
                                        <c:forEach items="${errCodeResultMap.value}" var="errCodeResultValue">
                                            <p style="color: red">${errCodeResultValue}</p>
                                        </c:forEach>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
        </div>
        <div class="row">
            <div class="col-xs-5 col-md-8">
                <div class="nav">
                    <br><br><br>
                    <div class="text-center-mobile">
                        <div class="button-group"><a href="#" id="doBackUpload"><em class="fa fa-angle-left"></em> Back</a></div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>
<%@include file="/WEB-INF/jsp/include/validation.jsp" %>
<script type="text/javascript">
    $("#doBackUpload").click(function () {
        SOP.Crud.cfxSubmit("mainForm", "doBackUpload");
    })
</script>