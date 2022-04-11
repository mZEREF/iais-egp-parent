<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<webui:setLayout name="iais-internet"/>

<div class="container">
    <br>
    <div id="printContent">
        <div class="row">
            <div class="col-xs-12">
                <div class="center-content">
                    <div class="print-gp">
                        <c:forEach var="declItem" items="${selfAssessmentDetail.selfAssessmentConfig}"
                                   varStatus="status">
                            <div class="panel panel-default">
                                <div class="panel-heading" id="headingPremise${status.index}" role="tab">
                                    <h4 class="panel-title"><a id="sub_style_answer${status.index}" role="button"
                                                               data-toggle="collapse"
                                                               href="#config-${status.index}"
                                                               aria-expanded="true"
                                                               aria-controls="config-${status.index}" class="">
                                        <c:choose>
                                            <c:when test="${declItem.common eq true}">
                                                General Regulation
                                            </c:when>
                                            <c:otherwise>
                                                Bsb Regulation
                                            </c:otherwise>
                                        </c:choose>
                                    </a></h4>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                    <c:set var="isPrint" value="${true}"/>
                    <%@include file="../chklst/checkListAnswer.jsp" %>
                </div>
            </div>
        </div>
    </div>
</div>


<script>
    $(function () {
        document.body.innerHTML = document.getElementById('printContent').innerHTML;
        window.print();
    });
</script>