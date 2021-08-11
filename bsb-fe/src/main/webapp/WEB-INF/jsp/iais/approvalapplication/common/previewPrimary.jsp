<div class="panel panel-default">
    <div class="panel-heading" id="headingOne" role="tab">
        <h4 class="panel-title"><a class="collapsed a-panel-collapse" role="button" data-toggle="collapse" href="#collapseOne${documentIndex}" aria-expanded="true" aria-controls="collapseOne" name="printControlNameForApp">Primary Documents</a></h4>
    </div>
    <div class="panel-collapse collapse <c:if test="${!empty printFlag}">in</c:if>" id="collapseOne${documentIndex}" role="tabpanel" aria-labelledby="headingOne">
        <div class="panel-body">
            <c:if test="${(AppSubmissionDto.appEditSelectDto==null||AppSubmissionDto.appEditSelectDto.docEdit) && empty printView}">
                <p class="mb-0"><div class="text-right app-font-size-16"><a href="#" id="docEdit"><em class="fa fa-pencil-square-o"></em>Edit</a></div></p>
            </c:if>
            <div class="elemClass-1561088919456">
                <div id="control--runtime--34" class="page section control  container-s-1" style="margin: 10px 0px">
                    <div class="control-set-font control-font-header section-header">
                        <label style="font-size: 2.2rem">Uploaded Documents</label>
                    </div>
                    <div class="control col-sm-12">
                        <div class="fileList ">
                            <span class="filename server-site" id="130">
                                xxx.doc(123 KB)
                            </span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
