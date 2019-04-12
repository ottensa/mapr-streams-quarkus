#!/bin/bash
# TODO: check if $NAME is given

if [ -z ${NAME+x} ]; then 
	echo "NAME is unset";
	exit -1
fi

# TODO: check if $CLDBS is given

if [ -z ${CLDBS+x} ]; then 
	echo "CLDBS is unset";
	exit -1
fi

echo "configure"
/opt/mapr/server/configure.sh -N $NAME -c -C $CLDBS -secure


# TODO: check if /deployments/app.jar exists

FILE=/deployments/app.jar  
if [ -f $FILE ]; then
   java -jar /deployments/app.jar
fi