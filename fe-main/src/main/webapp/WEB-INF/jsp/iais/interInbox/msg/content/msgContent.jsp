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
                        <div class="row" style="margin-top: 1.5%">
                            <div class="col-md-12">
                                <div class="col-md-2">
                                    <a onclick="MsgContToMsgPage()"><em class="fa fa-angle-left"></em> Back</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>

<script type="text/javascript">
    function submit(action) {
        $("[name='msg_view_type']").val(action);
        $("#msgContentForm").submit();
    }

    function MsgContToMsgPage() {
        submit("toMsg");
    }
</script>