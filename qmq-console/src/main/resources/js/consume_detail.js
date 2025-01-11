new Vue({
    el: '#app',
    data: {
        topicName:'',
        topicList: []
    },
    mounted() {
    },
    methods: {
        queryConsumerInfo:function () {
            console.log('queryConsumerInfo');
            let data = new FormData();
            data.append("topic",this.topicName);
            let that = this;
            httpPost(queryConsumerInfo,data).then(resp=>{
                if(isSuccess(resp)) {
                    that.topicList = resp.data;
                }
            })
        }
    }
})