function currencyWithoutZero(value)
{
	return value == 0 ? null : value;
}
function currencyWithZero(value)
{
	return value == null ? 0 : value;
}
