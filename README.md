#### Assumptions:
- null checks and checks for empty string values are omitted, it is assumed that clients won't pass null value or empty strings arguments to Scoreboard methods
- it is assumed that clients will use team names in the same lower or upper case everywhere, e.g. once a match is started for "Brazil - Italy", the score will be updated for "Brazil - Italy" but not for "BRAZIL - italy"  
