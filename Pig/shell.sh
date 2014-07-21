#!/bin/bash
start=`./week.sh 2013-09-13|head -1`
echo "begin date is $start"
terminate=`./week.sh 2013-09-13|tail -1`
echo "end date is $terminate"
which pig > /dev/null 2>&1
if [ $? -eq 0 ]; then
    echo "The command exist, running it"
    pig -param input="/user/biadmin/sampleData" -param n=2 -param begin=$start -param end=$terminate picture
else
   echo "The command does not exist, running it from bin directoctory"
   cp picture $PIG_HOME/bin
   cd $PIG_HOME/bin
   ./pig -param input="/user/biadmin/sampleData" -param n=2 -param begin=$start -param end=$terminate picture
fi

