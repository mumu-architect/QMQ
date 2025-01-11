const axiosReq = axios.create({
    timeout: 5000, // 请求超时时间
    withCredentials: true,
    headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
    }
})

function httpPost(url, params) {
    let result = axiosReq.post(url,params).then(function(response){
        return response.data;
    },function(error) {
        //定义一个统一的错误对象返回
        var errorObj = new Object();
        errorObj.code=500;
        errorObj.msg = '亲，系统出小差了';
        return errorObj;
    })
    return result;
}



function isSuccess(resp) {
    return resp.code == 0;
}


