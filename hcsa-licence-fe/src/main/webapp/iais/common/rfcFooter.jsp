<div class="row">
    <div class="col-xs-12 col-sm-3 ">
        <a id="RfcBack" class="back" href="#"><em class="fa fa-angle-left"></em> Back</a>
    </div>
    <div class="col-xs-12 col-sm-3  text-right">
        <a class="btn btn-primary next premiseId" id="RfcSave" >Save and Preview</a>
    </div>
    <div class="col-xs-12 col-sm-6">
        <div class="button-group">
            <a class="btn btn-secondary premiseSaveDraft" id="RfcSaveDraft" >Save as Draft</a>
            <a id="RfcUndo" class="" href="#" >Undo All Changes</a>
        </div>
    </div>
</div>

<script>
    $(document).ready(function() {
        $('#RfcBack').click(function () {
            submit('jump','back',null);
        });

        $('#RfcSave').click(function () {
           submit('preview','next',null);
        });

        $('#RfcSaveDraft').click(function () {
            submit('premises','saveDraft',null);
        });

        $('#RfcUndo').click(function () {
            submit('jump','undo',null);
        });
    });

</script>




