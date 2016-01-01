from django.contrib import admin
from models import Order, Cart
# Register your models here.

class OrderAdmin(admin.ModelAdmin):
    list_display = ('user','counter', 'mall', 'created' , 'status', 'paymentMethod', 'transactionId')
    search_fields = ('user','mall', 'transactionId')
    list_filter = ('status','mall','created','paymentMethod')


admin.site.register(Order,OrderAdmin)
admin.site.register(Cart)