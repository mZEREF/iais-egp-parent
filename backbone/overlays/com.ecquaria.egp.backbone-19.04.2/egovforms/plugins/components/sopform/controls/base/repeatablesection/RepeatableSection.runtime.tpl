{var id = control.getElementId()}
<div id="${id}" class="repeatable-section control ${control.properties.developerclass}">
	<div class="control-set-font control-font-header repeatable-section-header">
		<div class="rs-pagination"></div>
		<label>${control.properties.title|escape}</label>
	</div>
	<div class="repeatable-section-control-button">
		<button id="${id}--add-row" type="button" class="btn btn-round-lg btn-green"><i class="fa fa-plus-circle"></i> ${control.properties.controlButton|escape}</button>
	</div>
</div>
