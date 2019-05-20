{var id = control.getElementId()}
<div id="${id}" class="repeatable-section control">
	{var i = 0}
	<div id="${id}**${i}" class="repeatable-section-item">
		<div class="control-set-font control-font-header repeatable-section-header">${control.properties.title|escape}</div>
		<div id="${id}**errorMsg_section_top" class="error_placements"></div>

		<table class="control-grid" {if control.properties.border == false && control.properties.title == ''} style="margin-top: 15px;" {/if}>
		</table>

		<div id="${id}**errorMsg_section_bottom" class="error_placements"></div>
	</div>
	<div class="repeatable-section-control-button">
		<input id="${id}--add-row" type="button" value="${control.properties.controlButton|escape}" />
	</div>
</div>