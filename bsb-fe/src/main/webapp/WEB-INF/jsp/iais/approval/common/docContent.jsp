<div class="document-upload-list">
    <h3>${config.docType}<c:if test="${config.isMandatory}"><span class="mandatory"> *</span></c:if></h3>
    <div class="file-upload-gp">
        <input type="hidden" name="configIndex" value="${configIndex}"/>
        <input type="hidden" name="docIndex" value="${docIndex}"/>
        <span name="${configIndex}ShowId" id="${configIndex}ShowId">
            <c:if test="${docIndex == 1}">
                <c:forEach var="file" items="${primaryDoc1}" varStatus="fileStat">
                    <div>
                        <c:out value="${file.name}"/>
                    </div>
                </c:forEach>
            </c:if>
            <c:if test="${docIndex == 2}">
                <c:forEach var="file" items="${primaryDoc2}" varStatus="fileStat">
                    <div>
                        <c:out value="${file.name}"/>
                    </div>
                </c:forEach>
            </c:if>
            <c:if test="${docIndex == 3}">
                <c:forEach var="file" items="${primaryDoc3}" varStatus="fileStat">
                    <div>
                        <c:out value="${file.name}"/>
                    </div>
                </c:forEach>
            </c:if>
            <c:if test="${docIndex == 4}">
                <c:forEach var="file" items="${primaryDoc4}" varStatus="fileStat">
                    <div>
                        <c:out value="${file.name}"/>
                    </div>
                </c:forEach>
            </c:if>
            <c:if test="${docIndex == 5}">
                <c:forEach var="file" items="${primaryDoc5}" varStatus="fileStat">
                    <div>
                        <c:out value="${file.name}"/>
                    </div>
                </c:forEach>
            </c:if>
            <c:if test="${docIndex == 6}">
                <c:forEach var="file" items="${primaryDoc6}" varStatus="fileStat">
                    <div>
                        <c:out value="${file.name}"/>
                    </div>
                </c:forEach>
            </c:if>
            <c:if test="${docIndex == 7}">
                <c:forEach var="file" items="${primaryDoc7}" varStatus="fileStat">
                    <div>
                        <c:out value="${file.name}"/>
                    </div>
                </c:forEach>
            </c:if>
            <c:if test="${docIndex == 8}">
                <c:forEach var="file" items="${primaryDoc8}" varStatus="fileStat">
                    <div>
                        <c:out value="${file.name}"/>
                    </div>
                </c:forEach>
            </c:if>
        </span>
        <span name="iaisErrorMsg" class="error-msg" id="error_${configIndex}Error"></span>
        <br/>
        <a class="btn file-upload btn-secondary" href="javascript:void(0);">Upload</a>
        <br/>
    </div>
</div>