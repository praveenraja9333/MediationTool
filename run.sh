#!/bin/sh

basedir=`dirname $(readlink -f $0)`
maxThread=5
jobconfigFile=.txt
mainclass=com.vrp.tool.Application


function help(){
    echo "This script boot the Mediation tool 
          Usage: script can triggered with zero to any of or all below arguments
                Example:1 ./run.sh
                Example:2 ./run.sh -maxThread 10
                Example:3 ./run.sh -maxThread 10 -configFile"

}
#---Main start Here
if [ $@ -gt 0 ]; then
    while $@ -gt 0; do
        case EXPRESSION in
            '-maxThread')
                shift
                maxThread=$1;
                shift
                ;;
            '-configFile')
                shift
                jobconfigFile=$1
                shift
                ;;
            '-h')
                help
                shift
                ;;
            *)
                echo "Wrong arguments types"
                help
                exit 2
                ;;
            esac
    done
fi

JAVA_HOME=`echo $JAVA_HOME`

if [[ "x$JAVA_HOME" == "x" ]]; then
    echo "JAVA_HOME is not set"
fi

VMArgs="-Dmax_SFTPThread=$maxThread -DjobinstalltextFile=$jobconfigFile"
ClassPath=$basedir/libs/*:$basedir/tpplibs/*

$JAVA_HOME/bin/java $VMArgs -cp $ClassPath $mainclass 