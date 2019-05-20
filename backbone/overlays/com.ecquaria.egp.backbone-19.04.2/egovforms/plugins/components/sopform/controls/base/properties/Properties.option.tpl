<div class="property-container">
	{for option in options}
		<input type="text" id="property::${id}::${name}::option::${option_index}" class="property-input" value="${option.description|escape}" size="12" />
		<input type="button" class="property-remove-option" value="-" />
	{/for}
	<br />
	<input type="button" class="property-add-option" value="+" />
</div>