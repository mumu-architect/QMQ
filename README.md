# QMQ
mq消息队列

#### 01.QMQ核心设计
##### 1.commitLog主题对应mmap映射文件大小为：100*1024*1024=100M,实验为1*1024=1kb
##### 2.主题对应的Queue,mmap映射内存大小
> ###### 2.1 rocketmq,kafka,(5-10mb)存储百万左右消息数据
##### 3.queueList设计，在文件config/qmq-topic.json
> ###### 3.1.fileName：当前队列对应消息文件的名称
> ###### 3.2.id：队列的id号
> ###### 3.2.lastOffset：第一条消息吸入，被正常消费了，过去48小时之后，第一条消息可以被清除，第一条消息大小10byte,清除消息后此值为11
> ###### 3.3.latestOffset：100byte数据进来，此数据值为101
> ###### 3.4.offsetLimit：队列最大存储大小
##### 4.消费队列配置文件设计，在文件config/consumequeue-offset.json
```json
{
	"offsetTable":{
		"topicConsumerGroupDetail":{
			"created_order_topic":{
				"consumerGroupDetailMap":{
					"test-consume-group":{
						"0":"00000000#156",
						"1":"00000000#120",
						"2":"00000000#192"
					}
				}
			},
			"pay_success":{
				"consumerGroupDetailMap":{
					"test-consume-group":{
						"0":"00000000#324",
						"1":"00000000#396",
						"2":"00000000#276"
					}
				}
			},
			"order_cancel_topic":{
				"consumerGroupDetailMap":{
					"user_service_group":{
						"0":"00000000#9792",
						"1":"00000000#0",
						"2":"00000000#0"
					},
					"order_service_group":{
						"0":"00000000#9792",
						"1":"00000000#0",
						"2":"00000000#0"
					}
				}
			}
		}
	}
}
```
##### 5.消费队列配置文件