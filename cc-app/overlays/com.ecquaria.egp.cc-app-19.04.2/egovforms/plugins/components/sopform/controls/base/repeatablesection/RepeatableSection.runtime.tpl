{var id = control.getElementId()}
<div id="${id}" class="repeatable-section control ${control.properties.developerclass}">
	<div class="control-set-font control-font-header repeatable-section-header">
		<div class="rs-pagination"></div>
		<label>${control.properties.title|escape}</label>
	</div>
	<div class="repeatable-section-control-button">
		<input id="${id}--add-row" type="button" value="${control.properties.controlButton|escape}" />
	</div>
</div>
