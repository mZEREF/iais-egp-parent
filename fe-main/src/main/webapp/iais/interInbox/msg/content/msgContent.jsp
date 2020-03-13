<div class="main-content">
    <div class="container">
        <div class="row">
            <form class="" method="post" id="msgContentForm" action=<%=process.runtime.continueURL()%>>
                <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
                <input type="hidden" name="msg_view_type" value="">
                <div class="tab-gp">
                    <div class="tab-content">
                        <div class="panel panel-info">
                            <h3 class="panel-heading">Message Content</h3>
                            <div class="panel-body">
                                ${IAIS_MSG_CONTENT}
                            </div>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>