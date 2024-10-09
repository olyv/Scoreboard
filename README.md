#### Assumptions:
- null checks and checks for empty string values are omitted, it is assumed that clients won't pass null value or empty strings arguments to Scoreboard methods
- it is assumed that clients will use team names in the same lower or upper case everywhere, e.g. once a match is started for "Brazil - Italy", the score will be updated for "Brazil - Italy" but not for "BRAZIL - italy"  


#### Implementation notes:
- not quite happy with setter/getter for `clock` in `Scoreboard`, `final clock` initialized via constructor would look better but having setter makes testing easier without need of reflection. Probably it would be possible in a different environment but I tried to keep things as simple as possible 