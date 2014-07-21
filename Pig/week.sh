#!/bin/bash
n=`date -d "$1" "+%u"`
flag=0
tmp=0
for ((i=$n;i>0;i--))
do
  n1=$(($i-1))
  date +%Y%m%d -d "$1 -$n1 days"
 
done
for ((i=1;i<=$((7-$n));i++))
do
  date +%Y%m%d -d "$1 +$i days"
done
