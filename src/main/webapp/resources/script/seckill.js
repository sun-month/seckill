// 存放主要交互逻辑js代码
// javaScript 模块化
var seckill = {
		//封装秒杀相关ajax的url
		URL : {
			now : function(){
				return '/seckill/time/now';
			},
			
			exposer : function(seckillId){
				return '/seckill/'+seckillId+'/exposer';
			},

			execution : function(seckillId, md5){
				return '/seckill/' + seckillId + '/' + md5 +'/execution';
			}
			
		},
		
		//时间跑完，暴露接口，执行秒杀操作
		handleSeckill : function(seckillId, node){
			node.hide()
				.html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>');
			$.post('/seckill/'+seckillId+'/exposer', {}, function(result){
				if(result && result['success']){
					var exposer = result['data'];
					if(exposer['exposed']){
						//开启秒杀
						//获取秒杀地址
						var md5 = exposer['md5'];
						var killUrl = '/seckill/' + seckillId + '/' + md5 +'/execution';
						console.log('killUrl : ' + killUrl);
						//绑定一次点击事件
						$("#killBtn").one('click',function(){
							//绑定执行秒杀请求
							//1.先禁用按钮
							$(this).addClass('disabled');
							//2.发送秒杀请求
							$.post(killUrl, {}, function(result){
								if(result && result['success']){
									var killResult = result['data'];
									var stateInfo = killResult['stateInfo'];
									//3.显示秒杀结果
									node.html('<span class="label label-success">'+stateInfo+'</span>');
								}
							});
						});
						node.show();
					}else{
						//未开启秒杀
						var now = exposer['now'];
						var start = exposer['start'];
						var end = exposer['end'];
						seckill.countdown(seckillId, now, start, end);
					}
				}else{
					console.log('result : ' + result);
				}
			});
		},
		
		//及时逻辑
		countdown : function(seckillId, nowTime, startTime, endTime){
			var seckillBox = $('#seckill-box');
			if(nowTime > endTime){//当前时间大于秒杀结束时间
				console.log('>');
				seckillBox.html('秒杀结束');
			}else if(nowTime < startTime){//当前时间小于秒杀开始时间，计时操作
				var killTime = new Date(Number(startTime) + 1000);
				seckillBox.countdown(killTime, function(event){
					var format = event.strftime('秒杀倒计时: %D天 %H时 %M分 %S秒');
					seckillBox.html(format);
				}).on('finish.countdown',function(){
					seckill.handleSeckill(seckillId, seckillBox);
				});
			}else{
				seckill.handleSeckill(seckillId, seckillBox);
			}
		},
		
		//验证手机号
		validatePhone : function(phone){
			if(phone && phone.length == 11 && !isNaN(phone)){
				return true;
			}else{
				return false;
			}
		},
		
		//详情页秒杀逻辑
		detail : {
			//详情页初始化
			init : function(params){
				//手机验证和登录，计时交互
				//规划交互流程
				var killPhone = $.cookie('killPhone');
				//验证和手机号
				if(!seckill.validatePhone(killPhone)){//绑定手机号
					//弹出层控制，不输入正确的手机号码不给退出
					$('#killPhoneModal').modal({
						show : true,//显示弹出层
						backdrop : 'static',//禁止位置关闭
						keyboard : false//关闭键盘事件
					});
					
					$('#killPhoneBtn').click(function(){
						var inputPhone = $('#killPhoneKey').val();
						if(seckill.validatePhone(inputPhone)){
							//把电话埋进cookie里
							$.cookie('killPhone', inputPhone, {expires : 7, path : '/seckill'})
							window.location.reload();
						}else{
							$('#killPhoneMessage').hide().html('<label class="label label-danger">手机号码错误！</label>').show(300);
						}
					});
				}
				//已经登陆
				var seckillId = params['seckillId'];
				var startTime = params['startTime'];
				var endTime = params['endTime'];
				console.log(seckill.URL.now);
				$.get('/seckill/time/now', {}, function(result){
					if(result && result['success']){
						var nowTime = result['data'];
						seckill.countdown(seckillId, nowTime, startTime, endTime);
					}else{
						console.log("result : " + result);
					}
				});
			}
		}
}