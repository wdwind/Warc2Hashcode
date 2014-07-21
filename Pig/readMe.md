使用这些文件的方法： 把他们拷到同一个目录下面，把所有文件权限改成764,然后在那个目录执行：
./shell.sh
可以在任何位置执行这个shell脚本，但是注意所有文件应该在同一个目录下。


shell.sh的说明
使用者要在前面对week.sh的调用处把2013-09-13改成你想要的日期。这里原来指的是2013年9月13日，那么这两个对week.sh的调用会使得$start表示所在周的第一天，而$terminate表示所在周最后一天。
最后两个pig命令里面input是hadoop文件系统里面输入文件的路径，n是指取最高的n个，这两个参数你们要根据虚拟机的情况给定 begin是指选取晚于begin的图片，end是指取早于end的图片这两个参数由week.sh给定，你们不用动。picture就是这个脚本的文件名


picture的说明
文件picture里面store的那一句"/user/biadmin/processed"是输出文件在hadoop文件系统上面的路径，要改成我们的集群上面的路径，里面按从大到小列出哈希码，文件路径和个数，得到这个文件之后把这个文件拷到本地机器用web application读取显示即可

week.sh
别问我，我也不知道这个文件在干嘛。

其他说明
sampleData/sampleInput/data是输入文件的示例
sampleData/sampleOutput/data是输出文件的示例，里面每一行各个项以tab键分隔(就是'\t')，读文件的时候以'\t'来tokenize就行
