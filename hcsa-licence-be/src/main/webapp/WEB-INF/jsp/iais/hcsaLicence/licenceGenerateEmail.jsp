<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<script src="<%=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.COMMON_CSS_ROOT%>js/tinymce/tinymce.min.js"></script>
<script src="<%=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.COMMON_CSS_ROOT%>js/initTinyMce.js"></script>
<input type="hidden" id="nextStage" name="nextStage" value="PROCEMAIL"/>
<input type="hidden" id="saveDraftEmail" name="saveDraftEmail" value="">
<input type="hidden" id="perViewEmail" name="perViewEmail" value="">
<input type="hidden" id="template_content_size" name="template_content_size" value="-1">

<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<br/>
<div class="alert alert-info" role="alert">
    <strong>
        <h4>Email to Applicant</h4>
    </strong>
</div>
<br/>
<div class="row">
    <div class="col-xs-12">
        <div class="table-gp">
        <iais:section title="">
            <div >
                <iais:row>
                    <iais:field value="Current Status" required="false"/>
                    <iais:value width="10"><p>${applicationViewDto.currentStatus}</p></iais:value>
                </iais:row>
            </div>
            <div >
                <iais:row>
                    <label class="col-xs-0 col-md-2 control-label col-sm-2">Message Content</label>
                    <div class="col-sm-9">
                <textarea name="mailContent" cols="108" rows="50"
                          id="htmlEditroArea"
                          title="content">${appPremisesUpdateEmailDto.mailContent}</textarea>
                    </div>
                </iais:row>
            </div>
            <br>
            <div>
                <div class="col-sm-12 row">
                    <p><span>These are documents uploaded by an agency officer to support back office processing.</span>
                    </p>
                </div>
            </div>
            <br>
            <div >
                <table aria-describedby="" class="table">
                    <thead>
                    <tr>
                        <th scope="col" width="30%">Document</th>
                        <th scope="col" width="20%">File</th>
                        <th scope="col" width="10%">Size</th>
                        <th scope="col" width="20%">Submitted By</th>
                        <th scope="col" width="10%">Date Submitted</th>
                        <th scope="col" width="10%">Action</th>
                    </tr>
                    </thead>
                    <tbody  id="emailFileListId">
                    <c:choose>
                        <c:when test="${!hasEmailAttaDoc}">
                            <tr>
                                <td colspan="6" align="left">
                                    <iais:message key="GENERAL_ACK018"
                                                  escape="true"/>
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="emailFile" items="${applicationViewDto.appIntranetDocDtoList}"
                                       varStatus="status">
                                <c:if test="${emailFile.appDocType == ApplicationConsts.APP_DOC_TYPE_EMAIL_ATTACHMENT}">
                                    <tr>
                                        <td >
                                            <p>
                                                <c:out value="${emailFile.docDesc}"></c:out>
                                            </p>
                                        </td>
                                        <td >
                                            <p>
                                                <a hidden href="${pageContext.request.contextPath}/file-repo?filerepo=fileRo${status.index}&fileRo${status.index}=<iais:mask name="fileRo${status.index}"  value="${emailFile.fileRepoId}"/>&fileRepoName=${URLEncoder.encode(emailFile.docName, StandardCharsets.UTF_8.toString())}.${emailFile.docType}"
                                                   title="Download" class="downloadFile"><span id="${emailFile.fileRepoId}Down">trueDown</span></a>
                                                <a href="javascript:void(0);" onclick="doVerifyFileGo('${emailFile.fileRepoId}')">
                                                    <c:out value="${emailFile.docName}.${emailFile.docType}"/>
                                                </a>
                                            </p>
                                        </td>
                                        <td >
                                            <p><c:out value="${emailFile.docSize}"/></p>
                                        </td>
                                        <td >
                                            <p><c:out value="${emailFile.submitByName}"/></p>
                                        </td>
                                        <td >
                                            <p>${emailFile.submitDtString}</p>
                                        </td>
                                        <td >
                                            <button type="button" class="btn btn-secondary-del btn-sm"
                                                    onclick="javascript:deleteFile(this,'<iais:mask name="interalFileId"
                                                                                                    value="${emailFile.id}"/>');">
                                                Delete</button>
                                        </td>
                                    </tr>
                                </c:if>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>

                    </tbody>
                </table>
            </div>
        </iais:section>

        </div>
    </div>
</div>


<p class="text-right text-center-mobile">

    <iais:action >
        <a style="float:left;padding-top: 1.1%;" class="back" href="/main-web/eservice/INTRANET/MohHcsaBeDashboard?dashProcessBack=1"><em class="fa fa-angle-left"></em> Back</a>
        <button id="emailDocUpload" type="button" style="float:right" class="btn btn-primary" data-toggle="modal" data-target="#uploadDoc">
            Upload Document
        </button>
        <button type="button" onclick="javascript:doOpenEmailView();"  style="float:right" data-panel="main" class=" btn btn-primary">
            Preview
        </button>
        <button class="btn btn-primary next" style="float:right" type="button" id="draftButton" onclick="javascript:doSaveDraftEmail();">Save Draft</button>
        <button name="submitBtn" onclick="chkMailContent()" id="submitButton" style="float:right" type="button" class="btn btn-primary">
            Submit
        </button>
    </iais:action>
</p>
<br/><br/>
<br/><br/>
<iais:confirm msg="LOLEV_ACK056"  popupOrder="saveDraftPop"
              yesBtnDesc="Close" needFungDuoJi="true" needCancel="false"
              callBack="$('#saveDraftPop').modal('hide');"></iais:confirm>
<iais:confirm msg="${confirm_err_msg}" yesBtnDesc="OK" needFungDuoJi="true" needCancel="false" callBack="$('#supportArea').modal('hide');" popupOrder="supportArea" ></iais:confirm>
<script>
    $(document).ready(function () {
        if("Y"=='${doSaveDraftEmail}'){
            $('#saveDraftPop').modal('show');
        }

        if("Y"=='${doValidEmail}'){
            $('#supportArea').modal('show');
        }
    })


    function doSaveDraftEmail(){
        showWaiting();
        $('#saveDraftEmail').val("Y")
        chkMailContent()
        document.getElementById("mainForm").submit();
        $("#submitButton").attr("disabled", true);
        $("#draftButton").attr("disabled", true);
    }

    function chkMailContent() {
        var lists = $('.tox-statusbar__wordcount').text().split(" ");
        if(lists[1] == 'characters'){
            $('.tox-statusbar__wordcount').click();
        }
        var length = $('.tox-statusbar__wordcount').text().split(" ")[0];
        $("#template_content_size").val(length);
    }

    function doOpenEmailView() {
        showWaiting();
        $('#perViewEmail').val("Y")
        chkMailContent()
        document.getElementById("mainForm").submit();
        $("#submitButton").attr("disabled", true);
        $("#draftButton").attr("disabled", true);


    }


    var panelTriggers = document.getElementsByClassName('js-cd-panel-trigger');
    if( panelTriggers.length > 0 ) {
        for(var i = 0; i < panelTriggers.length; i++) {
            (function(i){
                var panelClass = 'js-cd-panel-'+panelTriggers[i].getAttribute('data-panel'),
                    panel = document.getElementsByClassName(panelClass)[0];
                // open panel when clicking on trigger btn
                panelTriggers[i].addEventListener('click', function(event){
                    event.preventDefault();
                    addClass(panel, 'cd-panel--is-visible');
                });
                //close panel when clicking on 'x' or outside the panel
                panel.addEventListener('click', function(event){
                    if( hasClass(event.target, 'js-cd-close') || hasClass(event.target, panelClass)) {
                        event.preventDefault();
                        removeClass(panel, 'cd-panel--is-visible');
                    }
                });
            })(i);
        }
    }

    //class manipulations - needed if classList is not supported
    //https://jaketrent.com/post/addremove-classes-raw-javascript/
    function hasClass(el, className) {
        if (el.classList) return el.classList.contains(className);
        else return !!el.className.match(new RegExp('(\\s|^)' + className + '(\\s|$)'));
    }
    function addClass(el, className) {
        if (el.classList) el.classList.add(className);
        else if (!hasClass(el, className)) el.className += " " + className;
    }
    function removeClass(el, className) {
        if (el.classList) el.classList.remove(className);
        else if (hasClass(el, className)) {
            var reg = new RegExp('(\\s|^)' + className + '(\\s|$)');
            el.className=el.className.replace(reg, ' ');
        }
    }

    function appFlowVehicleShowRadio(recommendation) {

    }
</script>


