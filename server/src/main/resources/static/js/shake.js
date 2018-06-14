/**
 * Created by seki on 18/6/13.
 */
if(window.DeviceMotionEvent){//判断设备是否支持运动传感事件。
    window.addEventListener("devicemotion", shake, false);//如果支持，那么就绑定shake方法到事件上
}else{
    alert("本设备不支持摇一摇功能");//如果不支持，提示即可
}

//首先定义一下，全局变量
var lastTime = 0;//此变量用来记录上次摇动的时间
var x = y = z = lastX = lastY = lastZ = 0;//此组变量分别记录对应x、y、z三轴的数值和上次的数值
var shakeSpeed = 1800;//设置阈值
//编写摇一摇方法
function shake(eventDate){
    var acceleration = eventDate.accelerationIncludingGravity;//获取设备加速度信息
    var nowTime = new Date().getTime();//记录当前时间
    //如果这次摇的时间距离上次摇的时间有一定间隔 才执行
    if(nowTime - lastTime > 150){
        var diffTime = nowTime - lastTime;//记录时间段
        lastTime = nowTime;//记录本次摇动时间，为下次计算摇动时间做准备
        x = acceleration.x;//获取x轴数值，x轴为垂直于北轴，向东为正
        y = acceleration.y;//获取y轴数值，y轴向正北为正
        z = acceleration.z;//获取z轴数值，z轴垂直于地面，向上为正
        //计算 公式的意思是 单位时间内运动的路程，即为我们想要的速度
        var speed = Math.abs(x + y + z - lastX - lastY - lastZ) / diffTime * 10000;
        if(speed > shakeSpeed){//如果计算出来的速度超过了阈值，那么就算作用户成功摇一摇
            //这里就是放置如果用户成功的摇一摇，将要触发的事件，例如提示摇到了谁，摇到了多少金币等等
            alert("你个小妹三")
            if (navigator.vibrate) {
                navigator.vibrate(1000);//震动1000毫秒
            } else if (navigator.webkitVibrate) {
                navigator.webkitVibrate(1000);
            }
        }
        lastX = x;//赋值，为下一次计算做准备
        lastY = y;//赋值，为下一次计算做准备
        lastZ = z;//赋值，为下一次计算做准备
    }
}