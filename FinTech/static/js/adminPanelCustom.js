/* Delivery boy name take in upper case */
$('.letterInUpperCase').keyup(function(){
    this.value = this.value.toUpperCase();
});
/* End Delivery boy name take in upper case */


/* submitting delivery boy */
$('.apply-delivry-boy').on('click',function(){
    
    var index = $(this).data('index');
    //alert('index: ' + index);
    var orderId = $(this).data('orderid');
    var mSelector = 'boySelect-'+index;
    //alert($('#boySelect-'+ index).val());
    
    
    
    
    var deliveryBoyName = $('#boySelect-'+ index).val();
    
    //alert(orderId);
    $.ajax({
            type: "POST",
            url: "/order/del_boy/set/",
            data: {"deliveryBoyName":deliveryBoyName, "orderId":orderId},
            success: function(data, textStatus, jqXHR){
                alert((data[0].status));
            },
            error: function(jqXHR, textStatus, errorThrown)
           {
                    alert("InError: ");
            },
            dataType: "JSON"
    }); 
    
});

/* submitting delivery boy ends*/
