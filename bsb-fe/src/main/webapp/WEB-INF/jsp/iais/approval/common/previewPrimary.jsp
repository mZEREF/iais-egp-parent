<div class="panel panel-default">
    <div class="panel-heading" id="headingOne" role="tab">
        <h4 class="panel-title"><a class="collapsed a-panel-collapse" role="button" data-toggle="collapse" href="#collapseOne${documentIndex}" aria-expanded="true" aria-controls="collapseOne" name="printControlNameForApp">Primary Documents</a></h4>
    </div>
    <div class="panel-collapse collapse <c:if test="${!empty printFlag}">in</c:if>" id="collapseOne${documentIndex}" role="tabpanel" aria-labelledby="headingOne">
        <div class="panel-body">
            <%--<c:set var="reloadMap" value="${AppSubmissionDto.multipleGrpPrimaryDoc}"/>--%>
            <c:forEach var="config" items="${primaryDocConfig}" varStatus="configStat">
                <c:choose>
                    <c:when test="${'1' == config.index}">
                        <p><label>Approval/Endorsement: Biosafety Committee</label>
                        <c:forEach var="appGrpPrimaryDocDto" items="${primaryDoc0}" varStatus="docStat">
                            <p>${appGrpPrimaryDocDto.name}</p>
                        </c:forEach></p>
                    </c:when>
                    <c:when test="${'2' == config.index}">
                        <p><label>Risk Assessment</label>
                        <c:forEach var="appGrpPrimaryDocDto" items="${primaryDoc1}" varStatus="docStat">
                            <p>${appGrpPrimaryDocDto.name}</p>
                        </c:forEach></p>
                    </c:when>
                    <c:when test="${'3' == config.index}">
                        <p><label>Standard Operating Procedure (SOP)</label>
                        <c:forEach var="appGrpPrimaryDocDto" items="${primaryDoc2}" varStatus="docStat">
                            <p>${appGrpPrimaryDocDto.name}</p>
                        </c:forEach></p>
                    </c:when>
                    <c:when test="${'4' == config.index}">
                        <p><label>GMAC Endorsement</label>
                        <c:forEach var="appGrpPrimaryDocDto" items="${primaryDoc3}" varStatus="docStat">
                            <p>${appGrpPrimaryDocDto.name}</p>
                        </c:forEach></p>
                    </c:when>
                    <%--<c:when test="${'5' == config.index}">
                        <label>Emergency Response Plan</label>
                        <c:forEach var="appGrpPrimaryDocDto" items="${primaryDoc4}" varStatus="docStat">
                            <p>${appGrpPrimaryDocDto.name}</p>
                        </c:forEach>
                    </c:when>
                    <c:when test="${'6' == config.index}">
                        <label>Approval Document from MOH</label>
                        <c:forEach var="appGrpPrimaryDocDto" items="${primaryDoc5}" varStatus="docStat">
                            <p>${appGrpPrimaryDocDto.name}</p>
                        </c:forEach>
                    </c:when>
                    <c:when test="${'7' == config.index}">
                        <label>Special Approval to Handle</label>
                        <c:forEach var="appGrpPrimaryDocDto" items="${primaryDoc6}" varStatus="docStat">
                            <p>${appGrpPrimaryDocDto.name}</p>
                        </c:forEach>
                    </c:when>--%>
                    <c:when test="${'8' == config.index}">
                        <p><label>Others</label>
                        <c:forEach var="appGrpPrimaryDocDto" items="${primaryDoc4}" varStatus="docStat">
                            <p>${appGrpPrimaryDocDto.name}</p>
                        </c:forEach></p>
                    </c:when>
                </c:choose>
            </c:forEach>
        </div>
    </div>
</div>