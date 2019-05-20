<div class="property-container">
{for option in options}
	<input type="checkbox" id="property::${id}::${name}::option::${option_index}"
			class="property-input"
			value="${option[0]|escape}"
			{if selected && selected.indexOf(option[0]) >= 0 } checked="checked" {/if} />
	${option[1]|escape}
	<br />
{/for}
</div>