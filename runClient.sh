#!/bin/sh

basedir=`dirname $(readlink -f $0)`
maxThread=5
jobconfigFile=.txt
mainclass=com.vrp.tool.client.ClientRMI


function help(){
    echo "This script boot the Mediation tool 
          Usage: script can triggered with zero to any of or all below arguments
                Example:1 ./runClient.sh
                Example:2 ./runClient.sh
                Example:3 ./rinClient.sh"

}
#---Main start Here


JAVA_HOME=`echo $JAVA_HOME`

if [[ "x$JAVA_HOME" == "x" ]]; then
    echo "JAVA_HOME is not set"
fi

ClassPath=$basedir/libs/*:$basedir/tpplibs/*

$JAVA_HOME/bin/java $VMArgs -cp $ClassPath $mainclass 