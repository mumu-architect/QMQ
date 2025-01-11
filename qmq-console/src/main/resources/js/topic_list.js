new Vue({
    el: '#app',
    data: {
        topicName:'',
        topicList: [],
        queryTopicReq: {
            topic:''
        },
        tablePage:{
            currentPage:1,
            pageSize:10,
            totalPage:1
        },
        formLabelWidth: '120px'
    },
    mounted() {
        this.queryTopicInPage();
    },

    methods: {
        queryTopicInPage:function () {
            console.log('queryTopicInPage');
            let data = new FormData();
            data.append("page",this.tablePage.currentPage);
            data.append("pageSize",this.tablePage.pageSize);
            if(this.queryTopicReq.topic!='') {
                data.append("topic",this.queryTopicReq.topic);
            }
            let that = this;
            httpPost(queryTopicInPage,data).then(resp=>{
                if(isSuccess(resp)) {
                    that.topicList = resp.data;
                    that.tablePage.totalPage = resp.totalPage;
                    that.tablePage.currentPage = resp.currentPage;
                }
            })
        },
        handlePageChange:function(currentPage) {
            this.tablePage.currentPage = currentPage;
            this.queryTopicInPage();
        }
    }
})