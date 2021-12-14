<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MaskUtil" %>
<input type="hidden" id="existFiles--v--${status.index}" name="existFiles--v--${status.index}" value="${MaskUtil.maskValue('file', item.repoIdNewString)}">
<div id="fileUploadInputDiv" style="display: none"></div>
<div class = "row">
    <div class="col-xs-12">
        <div id="PrimaryDocsPanel" role="tabpanel">
            <div class="document-content" style="margin: 0 auto;background-color: #F2F2F2;height: 150px">
                <div class="document-upload-list--v--${status.index}">
                    <c:set var="doc" value="${doSettings.get(item.docType)}"/>
                    <c:set var="newFiles" value="${item.newDocInfos}"/>
                    <h3>${doc.typeDisplay}<c:if test="${doc.mandatory}"> <span class="mandatory otherQualificationSpan">*</span></c:if></h3>
                    <div class="file-upload-gp" style="margin-left: 40px">
                        <c:if test="${newFiles ne null}">
                            <c:forEach var="info" items="${newFiles}">
                                <c:set var="tmpId" value="${MaskUtil.maskValue('file', info.tmpId)}"/>
                                <div id="${tmpId}FileDiv">
                                    <span id="${tmpId}Span">${info.filename}(${String.format("%.1f", info.size/1024.0)}KB)</span>
                                    <button type="button" class="btn btn-secondary btn-sm"
                                            onclick="deleteNewFile('${tmpId}')">Delete
                                    </button>
                                    <button
                                            type="button" class="btn btn-secondary btn-sm"
                                            onclick="reloadNewFile('${tmpId}')">Reload
                                    </button>

                                    <button type="button" class="btn btn-secondary btn-sm"
                                            onclick="downloadFile('new', '${tmpId}')">
                                        Download
                                    </button>
                                    <span data-err-ind="${info.tmpId}" class="error-msg"></span>
                                </div>
                            </c:forEach>
                        </c:if>
                        <a class="btn file-upload btn-secondary" data-upload-file="upload--v--${status.index}" href="javascript:void(0);">Upload</a>
                        <span data-err-ind="upload--v--${status.index}" class="error-msg"></span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
