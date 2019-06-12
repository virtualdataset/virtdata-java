# CATEGORY premade
## FirstNames

Return a pseudo-randomly sampled first name from the last US census data on first names
occurring more than 100 times. Both male and female names are combined in this function.

- long -> FirstNames() -> String
  - *ex:* `FirstNames()` - *select a random first name based on the chance of seeing it in the census data*
- long -> FirstNames(String: modifier) -> String
  - *ex:* `FirstNames('map')` - *select over the first names by probability as input varies from 1L to Long.MAX_VALUE*

## FullNames

Combines the FirstNames and LastNames functions into one that
simply concatenates them with a space between.
This function is a shorthand equivalent of {@code Template('{} {}', FirstNames(), LastNames())}

- long -> FullNames() -> String

## LastNames

Return a pseudo-randomly sampled last name from the last US census data on last names
occurring more than 100 times.

- long -> LastNames() -> String
  - *ex:* `LastNames()` - *select a random last name based on the chance of seeing it in the census data*
- long -> LastNames(String: modifier) -> String
  - *ex:* `LastNames('map')` - *select over the last names by probability as input varies from 1L to Long.MAX_VALUE*

