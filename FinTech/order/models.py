from django.db import models
from django.contrib.auth.models import User
from mall.models import Mall, Item
# Create your models here.

class Order(models.Model):

    RECEIVED = 0
    OLP_PENDING = 1
    PROCESSED = 2
    DISPATCHED = 3
    HANDED_OVER = 4
    CANCELED = 5

    CHOICES = ((RECEIVED, 'RECEIVED'), (OLP_PENDING, 'OLP_PENDING'), (PROCESSED, 'Processed'),
               (DISPATCHED, 'Dispatched'), (HANDED_OVER, 'Handed Over'),
               (CANCELED, 'CANCELED'))
    COD = 0
    ONLINEPAYMENT = 1

    PAYMENTCHOICE = ( (COD,"Cash On Delivery") , (ONLINEPAYMENT,'Online Payment'))
    user = models.ForeignKey(User)
    counter = models.CharField(max_length=20,default = 1)
    mall = models.ForeignKey(Mall)
    created = models.DateTimeField(auto_now_add=True)
    status = models.IntegerField(choices=CHOICES,default=RECEIVED)
    paymentMethod = models.IntegerField(choices=PAYMENTCHOICE,default=COD)
    transactionId = models.CharField(max_length=20,unique=True)

    def __unicode__(self):
        return self.user.username

class Cart(models.Model):
    item = models.ForeignKey(Item)
    quantity = models.IntegerField(default=1)
    order = models.ForeignKey(Order)