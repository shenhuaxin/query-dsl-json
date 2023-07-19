# query-dsl-json

### Comparison

$eq   Matches values that are equal to a specified value.

$gt   Matches values that are greater than a specified value.

$gte   Matches values that are greater than or equal to a specified value.

$in   Matches any of the values specified in an array.

$lt   Matches values that are less than a specified value.

$lte   Matches values that are less than or equal to a specified value.

$ne   Matches all values that are not equal to a specified value.

$nin   Matches none of the values specified in an array.

### Logical

$and Joins query clauses with a logical AND returns all documents that match the conditions of both clauses.
$not  Inverts the effect of a query expression and returns documents that do not match the query expression.
$nor  Joins query clauses with a logical NOR returns all documents that fail to match both clauses.
$or  Joins query clauses with a logical OR returns all documents that match the conditions of either clause.