<c:forEach var="config" items="${docConfigDtoList}" varStatus="configStat">
    <div class="document-upload-list">
        <h3>${config.docType}<c:if test="${config.isMandatory}"><span class="mandatory"> *</span></c:if></h3>
        <div class="file-upload-gp">
            <%--<input type="hidden" name="configIndex" value="${configIndex}"/>--%>
            <a class="btn file-upload btn-secondary" href="javascript:void(0);">Upload</a>
            <br/>
        </div>
    </div>
</c:forEach>