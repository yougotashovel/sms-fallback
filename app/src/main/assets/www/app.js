$(document).ready(function() {

   var error = $('.error');
   var wait = $('.wait');
   var success = $('.success');
   var message = {
      error: function(text){
         error.removeClass('hide-message');
         wait.addClass('hide-message');
         success.addClass('hide-success');
      },
      wait: function() {
         wait.removeClass('hide-message');
         error.addClass('hide-message');
      },
      success: function() {
         error.addClass('hide-message');
         wait.addClass('hide-message');
         setTimeout(function(){
            success.removeClass('hide-success');
         }, 200);
      },
      hideSuccess: function() {
         success.addClass('hide-success');
      },
      hide: function(){
         error.addClass('hide-message');
         wait.addClass('hide-message');
      }
   };

   var loader = $('.loading-bg');
   var loading = {
      show: function(){
         loader.removeClass('hide-loading');
      },
      hide: function(){
         loader.addClass('hide-loading');
      }
   };

   var app = {
      reset: function() {
         loading.hide();
         message.hide();
         message.hideSuccess();
      },
      fail: function() {
         app.reset();
         clickCount = 0;
         loading.show();
         setTimeout(function(){
            message.error();
            loading.hide();
         },2000);
      },
      wait: function() {
         app.reset();
         loading.show();
         setTimeout(function(){
            message.wait();
         },1000);
      },
      success: function() {
         loading.hide();
         message.success();

         setTimeout(function(){
            app.reset();
            clickCount = 0;
         },30000);
      }
   };

   function buttonClick(){
//      Android.showToast("balls deep");
      Android.showToast('test');
      app.fail();
   }

   var clickCount = 0;
   $(document).on('click','#button',function(){
        if (clickCount == 0) {
            app.fail();
            clickCount++;
            setTimeout(function(){
               app.reset();
            },5000);
        } else {
            app.wait();
            Android.showToast('test');
            setTimeout(function() {
               var price = JSON.parse(Android.showResp()).body.price;
               $('#price').html(price);
               app.success();
            },4000);
//            clickCount = 0;
        }
   });

   $(document).on('click','#reset',function(){
        setTimeout(function(){
           app.reset();
         //   clickCount = 0;
        }, 1000);
   });

   window.message = message;
   window.loading = loading;
   window.app = app;
});
