#!/bin/bash

rm -f evoppi-mysql.sql

sudo mysqldump --no-data --databases evoppi > evoppi-mysql.sql

sed -i 's#USE `evoppi`;#ALTER DATABASE evoppi CHARACTER SET = utf8;\n\nUSE `evoppi`;#g' evoppi-mysql.sql
