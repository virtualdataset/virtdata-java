#!/bin/bash

( cat <<"EOSTUB"
#!/bin/bash
MYSELF=`which "$0" 2>/dev/null`
[ $? -gt 0 -a -f "$0" ] && MYSELF="./$0"
java=java
if test -n "$JAVA_HOME"; then
    java="$JAVA_HOME/bin/java"
fi
#printf "java='%s' java_args='%s' MYSELF='%s'\n" "${java}" "${java_args}" "${MYSELF}"
exec "$java" "$JAVA_OPTS" $java_args -jar $MYSELF "$@"
exit 1 
EOSTUB
) >bin/metagen

cat metagen-userlibs/target/metagen-userlibs.jar >>bin/metagen
chmod +x bin/metagen

