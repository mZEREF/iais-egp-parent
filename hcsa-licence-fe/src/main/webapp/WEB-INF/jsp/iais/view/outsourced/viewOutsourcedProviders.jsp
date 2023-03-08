<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<style>
    .outstanding {
        width: 100%;
        text-overflow: ellipsis;
        word-break: break-all;
    }
</style>
<div class="amended-service-info-gp form-horizontal min-row">
    <iais:row>
        <div class="col-xs-12">
            <p class="app-title"><c:out value="${currStepName}"/></p>
        </div>
        <div class="amend-preview-info form-horizontal min-row">
            <c:set var="outsourceDto" value="${currentPreviewSvcInfo.appSvcOutsouredDto}"/>
            <%@include file="viewClinicalBoratoryContent.jsp"%>
            <%@include file="viewRadiologicalServiceContent.jsp"%>
        </div>
    </iais:row>
</div>
<script>
    $(document).ready(function (){
        $('textarea.scopeOutsource').attr('disabled','true');
    })
</script>