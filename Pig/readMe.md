如果有pig命令的话（我们现在的虚拟机就有），那么跑下面这个命令：
pig -param input="/user/biadmin/sampleData" -param n=2 -param threshold=20140601 picture
其中input是hadoop文件系统里面输入文件的路径，n是指取最高的n个， threshold是指选取晚于threshold的图片，picture就是这个脚本的文件名
如果没有pig命令的话，需要进到$PIG_HOME/bin执行下面的命令
./pig -param input="/user/biadmin/sampleData" -param n=2 -param threshold=20140601 picture

脚本里面store的那一句"/user/biadmin/processed"是输出文件在hadoop文件系统上面的路径，要改成我们的集群上面的路径，里面按从大到小列出哈希码，文件路径和个数，得到这个文件之后把这个文件拷到本地机器用web application读取显示即可