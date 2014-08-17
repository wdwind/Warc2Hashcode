

输入文件格式：
period \t Hash|pHash|height|width|time|url
period和Hash之间用tab分隔，后面的column之间用|分隔

输出文件格式：
count period Hash pHash height width time url
column之间用tab分隔


temp的说明
如果没有pig命令，可以将此文件copy到$PIG_HOME/bin下并执行：
./pig    temp
在虚拟机上，可以执行：
pig    temp
temp中第一行"/user/biadmin/sampleData"是输入文件的路径,要按照我们的集群修改
store的那一句"/user/biadmin/pr"是输出文件在hadoop文件系统上面的路径，要改成我们的集群上面的路径，得到这个文件之后把这个文件拷到本地机器用web application读取显示即可
现在temp里面的程序最终取出每个月最活跃的头两个pattern及相应的图片信息。如果不是top 2而是其他，可以将"top = limit sorted 2;  "那一行的2改成其他数字即可。