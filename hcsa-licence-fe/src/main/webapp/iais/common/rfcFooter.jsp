<div class="row">
    <div class="col-xs-12 col-sm-3 ">
        <a id="Back" class="back" href="#"><em class="fa fa-angle-left"></em> Back</a>
    </div>
    <div class="col-xs-12 col-sm-3  text-right">
        <a class="btn btn-primary next premiseId" id="Save" >Save and Preview</a>
    </div>
    <div class="col-xs-12 col-sm-6">
        <div class="button-group">
            <a class="btn btn-secondary premiseSaveDraft" id="SaveDraft" >Save as Draft</a>
            <a id="Undo" class="" href="#" >Undo All Changes</a>
        </div>
    </div>
</div>

<script>
    $(document).ready(function() {
        $('#Back').click(function () {
            submit('jump','back',null);
        });

        $('#Save').click(function () {
           submit('preview','next',null);
        });

        $('#SaveDraft').click(function () {
            submit('premises','saveDraft',null);
        });

        $('#Undo').click(function () {
            submit('jump','back',null);
        });
    });

</script>




