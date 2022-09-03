<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<style type="text/css">
    table {
        table-layout: fixed;
        word-break: break-all;
    }
</style>
<div class="panel panel-default">
    <div class="panel-heading" id="headingOne" role="tab">
        <h4 class="panel-title">
            <a class="collapsed" role="button" data-toggle="collapse" href="#collapseServer" aria-expanded="true" aria-controls="collapseOne">
                Service Related Information - ${currentPreviewSvcInfo.serviceName}
            </a>
        </h4>
    </div>
    <div class="panel-collapse collapse " id="collapseServer" role="tabpanel" aria-labelledby="headingOne">
        <div class="panel-body">
            <jsp:include page="/WEB-INF/jsp/iais/view/viewSvcInfo.jsp"/>
        </div>
    </div>
</div>
