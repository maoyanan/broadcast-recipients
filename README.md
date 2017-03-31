# broadcast-recipients

1,筛选出系统中含有 Application.FLAG_PERSISTENT 标记的应用,并判断是否为系统应用;

2,根据 broadcast 的 action 筛选系统中有哪些应用接收该广播,列出对应的 packageName reciverName 并判断是否为系统应用;
