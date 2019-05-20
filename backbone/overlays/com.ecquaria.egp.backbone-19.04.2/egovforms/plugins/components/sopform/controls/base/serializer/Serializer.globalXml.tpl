<Global>
	<Name>${name}</Name>
	<Version>
		<Major>${majorVersion}</Major>
		<Minor>${minorVersion}</Minor>
	</Version>
	<Properties>
	{for prop in properties}
		<Property>
			<Name>${prop.name}</Name>
			<Key>${prop.key}</Key>
			<Value>${prop.value}</Value>
			<Description>${prop.description}</Description>
		</Property>
	{/for}
	</Properties>
</Global>
