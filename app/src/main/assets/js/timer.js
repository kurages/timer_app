function toHms(t) {
	var hms = "";
	var h = t / 3600 | 0;
	var m = t % 3600 / 60 | 0;
	var s = t % 60;
	if (h != 0) {
		hms = h + "時間" + padZero(m) + "分" + padZero(s) + "秒";
	} else if (m != 0) {
		hms = m + "分" + padZero(s) + "秒";
	} else {
		hms = s + "秒";
	}
	return hms;
	function padZero(v) {
		if (v < 10) {
			return "0" + v;
		} else {
			return v;
		}
	}
}

async function sleep(ms){
    return new Promise(resolve => setTimeout(resolve, ms));
}

async function timer(i){
    while(i){
        i--;
        await sleep(1000);
        document.getElementById("count").innerHTML=toHms(i);
    }
    document.getElementById("count").innerHTML="";
}

async function timer2(){
	console.log("aaaaaaaaa");
	var hour=parseInt(document.form.hour.value)*60*60;
	var min=parseInt(document.form.min.value)*60;
	var sec=parseInt(document.form.sec.value);
	await timer(hour+min+sec);
	try {
		window.javascriptcallinterface.notify("タイマーが終了しました");
	} catch (error) {
		console.log(error.message);
		document.getElementById("count").innerHTML=error.message;
	}
	console.log("ended");
}
