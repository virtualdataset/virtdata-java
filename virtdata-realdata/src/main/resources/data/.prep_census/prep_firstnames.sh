#!/bin/bash
#curl -O https://www2.census.gov/topics/genealogy/1990surnames/dist.all.last
curl -O https://www2.census.gov/topics/genealogy/1990surnames/dist.female.first
curl -O https://www2.census.gov/topics/genealogy/1990surnames/dist.male.first

perl -pi -e '$_=~s/ +/,/g' dist.male.first
perl -pi -e 'if (/^(.+?),(.+)$/) { $_=uc(substr($1,0,1)).lc(substr($1,1)).",".$2."\n"; }' dist.male.first
mv dist.male.first 1990_male_firstnames.csv
zip -9 ../1990_male_firstnames.csv.zip 1990_male_firstnames.csv && rm 1990_male_firstnames.csv

perl -pi -e '$_=~s/ +/,/g' dist.female.first
perl -pi -e 'if (/^(.+?),(.+)$/) { $_=uc(substr($1,0,1)).lc(substr($1,1)).",".$2."\n"; }' dist.female.first
mv dist.female.first 1990_female_firstnames.csv
zip -9 ../1990_female_firstnames.csv.zip 1990_female_firstnames.csv && rm 1990_female_firstnames.csv

