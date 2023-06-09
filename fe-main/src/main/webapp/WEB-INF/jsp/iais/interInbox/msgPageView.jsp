<%@include file="common/commonImport.jsp"%>

<c:set var="tabCode" value="${msgTab == 1 ? 'msg' : ''}"/>
<%@ include file="common/commonDashboard.jsp" %>
<%@ include file="common/mainContent.jsp" %>
<%@ include file="common/commonFile.jsp" %>
<script type="text/javascript">
    $(function () {
        if ('${archiveResult}') {
            $('#isArchivedModal').modal('show');
        }
    });

    function pageAction() {
        if ('${msgPage}' == "msgView") {
            $("[name='msg_page_action']").val("msg_view");
        }else {
            $("[name='msg_page_action']").val("archive_view");
        }
    }
    function submit(action) {
        showWaiting();
        $("[name='msg_action_type']").val(action);
        $("#msgForm").submit();
    }

    function msgToAppPage() {
        submit("msgToApp");
    }

    function msgToLicPage() {
        submit("msgToLic");
    }

    $("#inboxType").change(function () {
        pageAction();
        submit('msgSearch');
    });

    $("#inboxService").change(function () {
        pageAction();
        submit('msgSearch');
    });

    function jumpToPagechangePage() {
        pageAction();
        submit('msgPage');
    }

    function sortRecords(sortFieldName, sortType) {
        pageAction();
        $("[name='crud_action_value']").val(sortFieldName);
        $("[name='crud_action_additional']").val(sortType);
        submit('msgSort');
    }

    function searchBySubject() {
        pageAction();
        submit('msgSearch');
    }

    function toArchiveView() {
        doClearMsg();
        submit('msgToArchive');
    }

    $('#doArchive').click(function () {
        if ($('.msgCheck').is(':checked')){
            $('#doArchiveModal').modal('show')
        }else{
            $('#archiveModal').modal('show');
        }
    });

    $('#confirmArchive').click(function () {
        $('#doArchiveModal').modal('hide');
        setTimeout(function (){
            submit('msgDoArchive');
        }, 1000);

    });

    function toMsgPage() {
        doClearMsg();
        submit('toMsgPage');
    }

    function toMsgView(msgContent,msgId,msgType) {
        pageAction();
        $("[name='crud_action_value']").val(msgContent);
        $("[name='msg_action_id']").val(msgId);
        $("[name='msg_page_type']").val(msgType);
        submit('msgToView');
    }

    function doClearMsg(){
        $("[name='inboxAdvancedSearch']").val("");
        $("#inboxType option:first").prop("selected", 'selected').val("All");
        $("#inboxService option:first").prop("selected", 'selected').val("All");
        $("#inboxType .current").text("Select a type");
        $("#inboxService .current").text("Select a service");
    }

</script>
