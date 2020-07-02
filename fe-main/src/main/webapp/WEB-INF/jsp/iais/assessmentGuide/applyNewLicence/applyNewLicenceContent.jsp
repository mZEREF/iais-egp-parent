<div class="component-gp">
    <div id="svcStep1">
        <%@include file="guideChooseSvcContent.jsp" %>
    </div>
    <br/>
    <div class="row">
        <div class="col-xs-12 col-md-3">
        </div>
        <div class="col-xs-12 col-md-2">
            <div class="text-center-mobile">
                <a class="back" id="Back"><em class="fa fa-angle-left"></em> Back</a>
            </div>
        </div>
        <div class="col-xs-12 col-md-4">
            <div class="text-right text-center-mobile">
                <a class="btn btn-primary next" id="submitService">Continue</a>
            </div>
        </div>
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