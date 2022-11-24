<c:choose>
    <c:when test="${appSubmissionDto.appType=='APTY009'|| appSubmissionDto.appType=='APTY007'}">

    </c:when>
    <c:when test="${appSubmissionDto.appType == 'APTY005'&&RFC_HCAI_NAME_CHNAGE!='false'&&renew_rfc_show!='Y'}">

    </c:when>
    <c:when test="${isSingle=='N'}">

    </c:when>
    <c:otherwise>
        <div class="panel panel-default svc-content">

            <div class="panel-heading" id="declarations" role="tab">
                <h4 class="panel-title"><a class="svc-pannel-collapse collapsed" role="button" data-toggle="collapse"
                                           href="#collapseDeclarations" aria-expanded="true"
                                           aria-controls="declarations">Declarations
                </a></h4>
            </div>
            <div class=" panel-collapse collapse" id="collapseDeclarations" role="tabpanel"
                 aria-labelledby="headingServiceInfo0">
                <div class="panel-body">
                    <%@include file="declarations.jsp"%>
                </div>
            </div>

        </div>
    </c:otherwise>
</c:choose>
<script>
    $(document).ready(function () {
        $(':input', '#collapseDeclarations').prop('disabled', true);
    });
</script>