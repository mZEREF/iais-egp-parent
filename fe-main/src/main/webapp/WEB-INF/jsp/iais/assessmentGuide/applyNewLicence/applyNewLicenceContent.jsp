<div class="component-gp">
    <div id="svcStep1">
        <%@include file="guideChooseSvcContent.jsp" %>
    </div>
</div>
<script type="text/javascript">

    $(document).ready(function () {
        if ($('#draftsave').val() != null || $('#draftsave').val() != '') {
            $('#saveDraft').modal('show');
        }

        $('#Back').click(function () {
            submit('toInbox', null, 'back');
        });
        $('#submitService').click(function () {
            submit('chooseSvc', null, 'next');
        });
    });

</script>