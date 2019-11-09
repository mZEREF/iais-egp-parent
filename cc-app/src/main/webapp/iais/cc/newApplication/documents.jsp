<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");


%>
<webui:setLayout name="iais-internet"/>
<%@ include file="./dashboard.jsp" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto" %>
<form  method="post" id="mainForm" enctype="multipart/form-data"  action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp steps-tab">
                        <%@ include file="./navTabs.jsp" %>
                        <div class="tab-content">
                            <div class="tab-pane active" id="documentsTab" role="tabpanel">
                                <div class="document-info-list">
                                    <ul>
                                        <li>
                                            <p>The maximum file size for each upload is 4MB. </p>
                                        </li>
                                        <li>
                                            <p>Acceptable file formats are PDF, JPG and PNG. </p>
                                        </li>
                                        <li>
                                            <p>All files are mandatory.</p>
                                        </li>
                                    </ul>
                                </div>
                                <div class="document-upload-gp">
                                    <h2>PRIMARY DOCUMENTS</h2>
                                    <div class="document-upload-list">
                                        <h3>Fire Safety Certificate (FSC) from SCDF</h3>
                                        <p><a href="#" target="_blank">Preview</a></p>
                                    </div>
                                    <!--common -->
                                    <c:forEach var="commonDoc" items="${HcsaSvcDocConfigDtoMap.get('common')}">
                                        <div class="document-upload-list">
                                            <h3>${commonDoc.docTitle}</h3>
                                            <div class="file-upload-gp">
                                                <span  ></span>
                                                <span class="hidden delBtn">&nbsp;&nbsp;<button type="button" class="delBtn">Delete</button></span><br/>
                                                <input class="selectedFile"  name = "selectedFile" type="file" style="display: none;" aria-label="selectedFile1"><a class="btn btn-file-upload btn-secondary" >Upload</a>
                                            </div>
                                        </div>
                                    </c:forEach>
                                    <!--prem -->
                                    <c:forEach var="premDoc" items="${HcsaSvcDocConfigDtoMap.get('premises')}">
                                        <div class="document-upload-list">
                                            <h3>${premDoc.docTitle}</h3>
                                            <div class="file-upload-gp">
                                                <span  ></span>
                                                <span class="hidden delBtn">&nbsp;&nbsp;<button type="button" class="delBtn">Delete</button></span><br/>
                                                <input class="selectedFile"  name = "selectedFile" type="file" style="display: none;" aria-label="selectedFile1"><a class="btn btn-file-upload btn-secondary" >Upload</a>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                                <div class="application-tab-footer">
                                    <div class="row">
                                        <div class="col-xs-12 col-sm-6">
                                            <p><a id = "docBack" class="back"><i class="fa fa-angle-left"></i> Back</a></p>
                                        </div>
                                        <div class="col-xs-12 col-sm-6">
                                            <div class="button-group"><a class="btn btn-secondary" id = "docSaveDraft">Save as Draft</a><a class="btn btn-primary next" id="docNext">Next</a></div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>


<script type="text/javascript">
    $(document).ready(function() {
        //Binding method
        $('#docBack').click(function(){
            submit('premises',null,null);
        });
        $('#docSaveDraft').click(function(){
            submit('documents','saveDraft',null);
        });
        $('#docNext').click(function(){
            submit('serviceForms',null,null);
        });
        /*$('#selectedFile').change(function(){
            var file = $('#selectedFile').val();
            $('#showFile').html(getFileName(file))
        });*/
    });
    function getFileName(o) {
        var pos = o.lastIndexOf("\\");
        return o.substring(pos + 1);
    }

    $('.selectedFile').change(function () {
            var file = $(this).val();
            $(this).parent().children('span:eq(0)').html(getFileName(file));
            $(this).parent().children('span:eq(0)').next().removeClass("hidden");

        }
    );

    $('.delBtn').click(function () {
        $(this).parent().children('span:eq(0)').html('');
        $(this).parent().children('span:eq(0)').next().addClass("hidden");
    });


</script>
